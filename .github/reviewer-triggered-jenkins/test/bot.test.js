'use strict';
// Run:  node --test .github/reviewer-triggered-jenkins/test/bot.test.js
const test = require('node:test');
const assert = require('node:assert/strict');
const http = require('node:http');
const { parseComment, validate, toJenkinsParams } = require('../src/command');
const { classify } = require('../src/result');
const main = require('../src/main');
const base = require('../config.json');

const config = { ...base, command: '/run-jenkins', timeouts: { queueMinutes: 0.05, buildMinutes: 0.05, pollSeconds: 0.01 } };
const SHEET = '1ZJu0L5q97Fvsj9bqPlHqyoUfHeElOVUchTp5--agHZc';
const params = { jenkins_test: { platform: 'WEB', environment: 'PREPROD', test_case_name: 'Flight_testBookingOneWay', gsheet_id: SHEET, branch_name: 'main' } };
const comment = (o = params) => `/run-jenkins\n\`\`\`json\n${JSON.stringify(o, null, 2)}\n\`\`\``;

// ---------------------------------------------------------------- command parsing
test('command must be at the start of a line', () => {
  assert.equal(parseComment('/run-jenkins', '/run-jenkins').triggered, true);
  assert.equal(parseComment('LGTM\n/run-jenkins', '/run-jenkins').triggered, true);
  assert.equal(parseComment('please do /run-jenkins', '/run-jenkins').triggered, false);
  assert.equal(parseComment('/run-jenkinsfoo', '/run-jenkins').triggered, false);
  assert.equal(parseComment('> /run-jenkins (quoted)', '/run-jenkins').triggered, false);
});

test('JSON after the command: fenced, bare, missing, broken', () => {
  assert.deepEqual(parseComment(comment(), '/run-jenkins').json.value, params);
  assert.deepEqual(parseComment(`/run-jenkins ${JSON.stringify(params)}`, '/run-jenkins').json.value, params);
  assert.equal(parseComment('/run-jenkins', '/run-jenkins').json.found, false);
  assert.match(parseComment('/run-jenkins\n```json\n{"jenkins_test": }\n```', '/run-jenkins').json.error, /Invalid JSON/);
});

test('parseComment with no command configured never triggers', () => {
  assert.equal(parseComment('/run-jenkins', undefined).triggered, false);
  assert.equal(parseComment('anything', '').triggered, false);
});

test('maps the 5 fields + fixed params to FLG-Web-Regression parameters', () => {
  const v = validate(params, config);
  assert.ok(v.ok, v.errors && v.errors.join());
  assert.deepEqual(toJenkinsParams(v.input, config), {
    threads: '1', sessions: '1', testdata: 'google', tribe: 'all', module: 'all', testtype: 'all',
    sendEmail: 'false', dbUse: 'false', runFromScheduler: '',
    platform: 'dweb', env: 'preprod', testname: 'Flight_testBookingOneWay', gSheetId: SHEET, branch: 'main',
  });
  // case-insensitive input values
  assert.equal(toJenkinsParams(validate({ jenkins_test: { ...params.jenkins_test, platform: 'mweb', environment: 'preprod' } }, config).input, config).platform, 'mweb');
});

test('rejects bad input', () => {
  const v = validate({ jenkins_test: { ...params.jenkins_test, environment: 'PROD', gsheet_id: 'YOUR_GOOGLE_SHEET_ID', branch_name: 'a/../b', extra: 'x' } }, config);
  assert.equal(v.ok, false);
  assert.equal(v.errors.length, 4);
  assert.equal(validate({ jenkins_test: { ...params.jenkins_test, test_case_name: '$(rm -rf /)' } }, config).ok, false);
  assert.equal(validate({}, config).ok, false);
});

// ---------------------------------------------------------------- result classification
const rep = (pass, fail, skip, cases = []) => ({ passCount: pass, failCount: fail, skipCount: skip, suites: [{ cases }] });
test('Pass / Fail / Skip / Error / Aborted', () => {
  assert.equal(classify({ result: 'SUCCESS' }, rep(3, 0, 0), config).conclusion, 'success');
  assert.equal(classify({ result: 'SUCCESS' }, rep(3, 0, 1), config).outcome, 'PASS');
  assert.equal(classify({ result: 'UNSTABLE' }, rep(2, 1, 0), config).outcome, 'FAIL');
  assert.equal(classify({ result: 'SUCCESS' }, rep(0, 0, 5), config).conclusion, 'failure');
  assert.equal(classify({ result: 'SUCCESS' }, rep(0, 0, 5), { ...config, result: { allSkipped: 'pass' } }).conclusion, 'neutral');
  assert.equal(classify({ result: 'SUCCESS' }, null, config).outcome, 'ERROR');
  assert.equal(classify({ result: 'SUCCESS' }, null, { ...config, result: { requireTestReport: false } }).conclusion, 'success');
  assert.equal(classify({ result: 'FAILURE' }, null, { ...config, result: { requireTestReport: false } }).conclusion, 'failure');
  assert.equal(classify({ result: 'SUCCESS' }, rep(0, 0, 0), config).outcome, 'ERROR');
  assert.equal(classify({ result: 'FAILURE' }, rep(4, 0, 0), config).outcome, 'ERROR');
  assert.equal(classify({ result: 'ABORTED' }, null, config).conclusion, 'cancelled');
});

// ---------------------------------------------------------------- mock Jenkins behind a mock IAP
const BASIC = 'Basic ' + Buffer.from('qa-bot:apitoken').toString('base64');
function mockJenkins({ result = 'SUCCESS', report = rep(2, 0, 1), iap = 'iap-token', requireCrumbForCookie = true } = {}) {
  let polls = 0;
  const server = http.createServer((req, res) => {
    // --- IAP layer
    const bearer = req.headers['proxy-authorization'] || (req.headers.authorization || '').startsWith('Bearer') && req.headers.authorization;
    const cookieOk = (req.headers.cookie || '').includes('GCP_IAP');
    if (iap && bearer !== `Bearer ${iap}` && !cookieOk) {
      res.writeHead(401, { 'x-goog-iap-generated-response': 'true' }); return res.end('Invalid IAP credentials');
    }
    // --- Jenkins layer
    const jenkinsAuthed = req.headers.authorization === BASIC || cookieOk;
    if (!jenkinsAuthed) { res.writeHead(403); return res.end('Authentication required'); }
    let body = '';
    req.on('data', (d) => (body += d));
    req.on('end', () => {
      const json = (o) => { res.writeHead(200, { 'Content-Type': 'application/json' }); res.end(JSON.stringify(o)); };
      if (req.url === '/crumbIssuer/api/json') return json({ crumbRequestField: 'Jenkins-Crumb', crumb: 'abc' });
      if (req.method === 'POST' && req.url === '/job/FLG-Web-Regression/buildWithParameters') {
        if (cookieOk && requireCrumbForCookie && req.headers['jenkins-crumb'] !== 'abc') { res.writeHead(403); return res.end('No valid crumb'); }
        server.params = Object.fromEntries(new URLSearchParams(body));
        server.headers = req.headers;
        res.writeHead(201, { Location: 'https://jenkins-fe-qa-preprod.ggwp.red/queue/item/7/' }); return res.end();
      }
      if (req.url === '/queue/item/7/api/json') {
        return json(++polls < 2 ? { why: 'Waiting for next available executor' } : { executable: { number: 31, url: 'https://jenkins-fe-qa-preprod.ggwp.red/job/FLG-Web-Regression/31/' } });
      }
      if (req.url.startsWith('/job/FLG-Web-Regression/31/api/json')) {
        return json(++polls < 4 ? { building: true, result: null } : { building: false, result, number: 31 });
      }
      if (req.url.startsWith('/job/FLG-Web-Regression/31/testReport/api/json')) {
        if (!report) { res.writeHead(404); return res.end(); }
        return json(report);
      }
      res.writeHead(404); res.end();
    });
  });
  return new Promise((r) => server.listen(0, '127.0.0.1', () => r(server)));
}

function mockGithub({ permission = 'write', author = 'pr-author' } = {}) {
  const log = { comments: [], checks: [], updates: [], outputs: {} };
  const github = {
    rest: {
      issues: {
        createComment: async ({ body }) => { log.comments.push(body); return { data: { id: log.comments.length } }; },
        updateComment: async () => ({ data: {} }),
      },
      repos: { getCollaboratorPermissionLevel: async () => ({ data: { permission, role_name: permission } }) },
      pulls: { get: async () => ({ data: { state: 'open', user: { login: author }, head: { sha: '0123456789abcdef' } } }) },
      checks: {
        create: async (a) => { log.checks.push(a); return { data: { id: 99 } }; },
        update: async (a) => { log.updates.push(a); return { data: {} }; },
      },
    },
  };
  const core = { info() {}, error() {}, notice() {}, setOutput: (k, v) => (log.outputs[k] = v), setFailed: (m) => (log.failed = m) };
  return { github, core, log };
}

const ctx = (body, login = 'reviewer') => ({
  repo: { owner: 'tiket', repo: 'app' }, serverUrl: 'https://github.com', runId: 1,
  payload: {
    comment: { body },
    issue: { number: 12, pull_request: {} },
    sender: { login, type: 'User' },
  },
});

async function withEnv(server, env, fn) {
  const keys = ['JENKINS_URL', 'JENKINS_USER', 'JENKINS_API_TOKEN', 'IAP_ID_TOKEN', 'JENKINS_COOKIE', 'IAP_TOKEN_COMMAND'];
  const saved = Object.fromEntries(keys.map((k) => [k, process.env[k]]));
  keys.forEach((k) => delete process.env[k]);
  Object.assign(process.env, { JENKINS_URL: `http://127.0.0.1:${server.address().port}` }, env);
  try { await fn(); } finally {
    keys.forEach((k) => (saved[k] === undefined ? delete process.env[k] : (process.env[k] = saved[k])));
    server.close();
  }
}
const API_ENV = { JENKINS_USER: 'qa-bot', JENKINS_API_TOKEN: 'apitoken', IAP_ID_TOKEN: 'iap-token' };

test('e2e: comment without the command is ignored entirely', async () => {
  const { github, core, log } = mockGithub();
  await main({ github, context: ctx('just a regular comment'), core }, { config });
  assert.equal(log.comments.length, 0);
  assert.equal(log.checks.length, 0);
});

test('e2e: comment on a plain issue (not a PR) is ignored', async () => {
  const { github, core, log } = mockGithub();
  const context = ctx(comment());
  context.payload.issue.pull_request = undefined;
  await main({ github, context, core }, { config });
  assert.equal(log.comments.length, 0);
});

test('e2e: /run-jenkins with no JSON -> bot asks for parameters, no check', async () => {
  const { github, core, log } = mockGithub();
  await main({ github, context: ctx('/run-jenkins'), core }, { config });
  assert.match(log.comments[0], /needs the execution parameters as a JSON block/);
  assert.equal(log.checks.length, 0);
});

test('e2e: PASS — API token + IAP via Proxy-Authorization, check success', async () => {
  const server = await mockJenkins();
  const { github, core, log } = mockGithub();
  await withEnv(server, API_ENV, async () => {
    await main({ github, context: ctx(comment()), core }, { config });
  });
  assert.equal(log.failed, undefined, log.failed);
  assert.equal(server.headers['proxy-authorization'], 'Bearer iap-token');
  assert.equal(server.headers.authorization, BASIC);
  assert.equal(server.params.platform, 'dweb');
  assert.equal(server.params.env, 'preprod');
  assert.equal(server.params.testname, 'Flight_testBookingOneWay');
  assert.equal(server.params.gSheetId, SHEET);
  assert.equal(server.params.branch, 'main');
  assert.equal(server.params.threads, '1');
  assert.equal(log.checks[0].head_sha, '0123456789abcdef');
  assert.equal(log.outputs.check_run_id, '99');
  assert.equal(log.updates.at(-1).conclusion, 'success');
  assert.match(log.comments.at(-1), /PASS: 2 passed, 1 skipped.*merge gate passed/);
});

test('e2e: FAIL — failing test blocks merge and is listed', async () => {
  const server = await mockJenkins({ result: 'UNSTABLE', report: rep(0, 1, 0, [{ className: 'Flight', name: 'testBookingOneWay', status: 'FAILED', errorDetails: 'element not found' }]) });
  const { github, core, log } = mockGithub();
  await withEnv(server, API_ENV, () => main({ github, context: ctx(comment()), core }, { config }));
  const last = log.updates.at(-1);
  assert.equal(last.conclusion, 'failure');
  assert.match(last.output.summary, /`Flight\.testBookingOneWay` — element not found/);
  assert.match(log.comments.at(-1), /merge is blocked/);
});

test('e2e: SKIP — all skipped blocks merge by default', async () => {
  const server = await mockJenkins({ report: rep(0, 0, 3) });
  const { github, core, log } = mockGithub();
  await withEnv(server, API_ENV, () => main({ github, context: ctx(comment()), core }, { config }));
  assert.equal(log.updates.at(-1).conclusion, 'failure');
  assert.match(log.updates.at(-1).output.title, /^SKIP/);
});

test('e2e: ERROR — expired IAP token gives a clear IAP hint', async () => {
  const server = await mockJenkins();
  const { github, core, log } = mockGithub();
  await withEnv(server, { ...API_ENV, IAP_ID_TOKEN: 'expired' }, () => main({ github, context: ctx(comment()), core }, { config }));
  const last = log.updates.at(-1);
  assert.equal(last.conclusion, 'failure');
  assert.match(last.output.summary, /rejected by Google IAP/);
  assert.ok(log.failed);
});

test('e2e: cookie mode (local debug) fetches and sends the CSRF crumb', async () => {
  const server = await mockJenkins();
  const { github, core, log } = mockGithub();
  await withEnv(server, { JENKINS_COOKIE: 'GCP_IAP_UID=1; JSESSIONID=x' }, () => main({ github, context: ctx(comment()), core }, { config }));
  assert.equal(server.headers['jenkins-crumb'], 'abc');
  assert.equal(log.updates.at(-1).conclusion, 'success');
});

test('e2e: read-only user and PR author are refused', async () => {
  let m = mockGithub({ permission: 'read' });
  await main({ github: m.github, context: ctx(comment()), core: m.core }, { config });
  assert.match(m.log.comments[0], /at least `write`/);
  m = mockGithub({ author: 'reviewer' });
  const noAuthor = { ...config, permissions: { ...config.permissions, allowPrAuthor: false } };
  await main({ github: m.github, context: ctx(comment()), core: m.core }, { config: noAuthor });
  assert.match(m.log.comments[0], /PR author cannot trigger/);
  assert.equal(m.log.checks.length, 0);
});

test('e2e: invalid values -> reply with errors, no Jenkins call', async () => {
  const { github, core, log } = mockGithub();
  await main({ github, context: ctx(comment({ jenkins_test: { ...params.jenkins_test, environment: 'PROD' } })), core }, { config });
  assert.match(log.comments[0], /"environment" must be one of: PREPROD/);
  assert.equal(log.checks.length, 0);
});
