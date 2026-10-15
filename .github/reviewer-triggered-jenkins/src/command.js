'use strict';
/**
 * Parses a PR comment such as:
 *
 *   /run-jenkins
 *   ```json
 *   { "jenkins_test": { "platform": "WEB", "environment": "PREPROD", ... } }
 *   ```
 *
 * and turns it into Jenkins build parameters. The command (config.command, e.g.
 * "/run-jenkins") must start a line; the JSON block can be fenced or bare and may
 * follow on the same line or further down the comment.
 */

const FIELDS = ['platform', 'environment', 'test_case_name', 'gsheet_id', 'branch_name'];

// Format rules for fields that are not restricted to a fixed list of values.
const FORMAT = {
  test_case_name: { re: /^[A-Za-z0-9_.#,\- ]{1,300}$/, hint: 'letters, digits, _ . # , - (max 300 chars), or "all"' },
  gsheet_id: { re: /^[A-Za-z0-9_-]{20,100}$/, hint: 'the ID from the Google Sheet URL' },
  branch_name: {
    re: /^(?![./])(?!.*\.\.)(?!.*\/\/)(?!.*[./]$)(?!.*\.lock$)[A-Za-z0-9._\/-]{1,200}$/,
    hint: 'a valid git branch name, e.g. feature/flight-automation',
  },
};

const escapeRe = (s) => s.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');

/** Lines that start with ">" are quotes of earlier comments; ignore them. */
function stripQuotes(text) {
  return text.split(/\r?\n/).filter((l) => !/^\s*>/.test(l)).join('\n');
}

/**
 * @returns {{triggered:false} | {triggered:true, json:{found:false}|{found:true,value}|{found:true,error}}}
 */
function parseComment(body, command) {
  if (!command) return { triggered: false };
  const text = stripQuotes(body || '');
  const m = new RegExp(`(^|\\n)[ \\t]*${escapeRe(command)}(?=\\s|$)`).exec(text);
  if (!m) return { triggered: false };
  const after = text.slice(m.index + m[0].length);
  return { triggered: true, json: extractJson(after) };
}

function extractJson(text) {
  const fenced = [...text.matchAll(/```[ \t]*(?:json)?[ \t]*\r?\n([\s\S]*?)```/gi)].map((x) => x[1]);
  let candidate = fenced[0];
  if (candidate === undefined) {
    const s = text.indexOf('{');
    const e = text.lastIndexOf('}');
    if (s === -1 || e < s) return { found: false };
    candidate = text.slice(s, e + 1);
  }
  try {
    return { found: true, value: JSON.parse(candidate) };
  } catch (err) {
    return { found: true, error: `Invalid JSON: ${err.message}` };
  }
}

/** @returns {{ok:true, input:object} | {ok:false, errors:string[]}} */
function validate(payload, config) {
  const t = payload && typeof payload === 'object' ? payload.jenkins_test : undefined;
  if (!t || typeof t !== 'object' || Array.isArray(t)) {
    return { ok: false, errors: ['The JSON must have a top-level "jenkins_test" object.'] };
  }
  const errors = [];
  const unknown = Object.keys(t).filter((k) => !FIELDS.includes(k));
  if (unknown.length) errors.push(`Unknown field(s): ${unknown.join(', ')}.`);

  const input = {};
  for (const field of FIELDS) {
    const raw = t[field];
    if (typeof raw !== 'string' || !raw.trim()) {
      errors.push(`"${field}" is required and must be a non-empty string.`);
      continue;
    }
    const value = raw.trim();
    const values = config.parameters[field] && config.parameters[field].values;
    if (values) {
      const key = Object.keys(values).find((k) => k.toUpperCase() === value.toUpperCase());
      if (!key) {
        errors.push(`"${field}" must be one of: ${Object.keys(values).join(', ')}.`);
        continue;
      }
      input[field] = key;
      continue;
    }
    const rule = FORMAT[field];
    if (rule && !rule.re.test(value)) {
      errors.push(`"${field}" is invalid (${rule.hint}).`);
      continue;
    }
    if (field === 'gsheet_id' && /^YOUR_/i.test(value)) {
      errors.push('"gsheet_id" still has the placeholder value.');
      continue;
    }
    input[field] = value;
  }
  return errors.length ? { ok: false, errors } : { ok: true, input };
}

/** Fixed job parameters + the reviewer's 5 values, using Jenkins' parameter names. */
function toJenkinsParams(input, config) {
  const params = { ...config.fixedParameters };
  for (const field of FIELDS) {
    const def = config.parameters[field];
    params[def.jenkinsName] = def.values ? def.values[input[field]] : input[field];
  }
  return params;
}

function exampleJson(config) {
  const first = (f, fallback) => Object.keys(config.parameters[f].values || {})[0] || fallback;
  const example = {
    jenkins_test: {
      platform: first('platform', 'WEB'),
      environment: first('environment', 'PREPROD'),
      test_case_name: 'Flight_testBookingOneWay',
      gsheet_id: 'YOUR_GOOGLE_SHEET_ID',
      branch_name: 'main',
    },
  };
  return `${config.command}\n\`\`\`json\n${JSON.stringify(example, null, 2)}\n\`\`\``;
}

module.exports = { FIELDS, parseComment, extractJson, validate, toJenkinsParams, exampleJson };
