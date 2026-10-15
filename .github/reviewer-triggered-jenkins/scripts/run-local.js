#!/usr/bin/env node
'use strict';
/**
 * Local smoke test — runs the same Jenkins part the GitHub Action runs, from your laptop.
 *
 *   node .github/reviewer-triggered-jenkins/scripts/run-local.js <file> [--dry-run]
 *
 * <file> holds either the bare JSON, or a whole PR comment (the "/run-jenkins" command
 * followed by a ```json block``` anywhere in it — see examples/pr-comment.md).
 * --dry-run only validates and prints the Jenkins parameters + the equivalent curl.
 *
 * Credentials (environment variables):
 *   JENKINS_USER, JENKINS_API_TOKEN   Jenkins user + API token (recommended)
 *   IAP_ID_TOKEN                      Google IAP ID token, e.g.
 *       gcloud auth print-identity-token --impersonate-service-account=SA_EMAIL \
 *         --audiences=IAP_OAUTH_CLIENT_ID --include-email
 *   JENKINS_COOKIE                    (debug only) browser cookie header; expires in ~1 h
 *   JENKINS_URL                       overrides config.jenkinsUrl
 */
const fs = require('fs');
const path = require('path');
const { parseComment, extractJson, validate, toJenkinsParams } = require('../src/command');
const { fromEnv } = require('../src/jenkins');
const { classify } = require('../src/result');

async function main() {
  const args = process.argv.slice(2);
  const dryRun = args.includes('--dry-run');
  const file = args.find((a) => !a.startsWith('--'));
  if (!file) {
    console.error('Usage: run-local.js <params.json | comment.txt> [--dry-run]');
    process.exit(2);
  }
  const config = JSON.parse(fs.readFileSync(process.env.RTJ_CONFIG || path.join(__dirname, '..', 'config.json'), 'utf8'));
  const text = fs.readFileSync(file, 'utf8');
  const fromComment = parseComment(text, config.command);
  const json = fromComment.triggered ? fromComment.json : extractJson(text);
  if (!json.found) throw new Error('No JSON found in the file.');
  if (json.error) throw new Error(json.error);
  const v = validate(json.value, config);
  if (!v.ok) throw new Error(`Invalid parameters:\n - ${v.errors.join('\n - ')}`);

  const params = toJenkinsParams(v.input, config);
  const base = (process.env.JENKINS_URL || config.jenkinsUrl).replace(/\/+$/, '');
  const jobUrl = `${base}/${config.jenkinsJobPath.split('/').map((p) => `job/${encodeURIComponent(p)}`).join('/')}`;
  console.log('Jenkins parameters:');
  console.table(params);
  console.log('\nEquivalent curl:\n');
  console.log([
    `curl -i -X POST '${jobUrl}/buildWithParameters'`,
    `  -u "$JENKINS_USER:$JENKINS_API_TOKEN"`,
    `  -H "Proxy-Authorization: Bearer $IAP_ID_TOKEN"`,
    ...Object.entries(params).map(([k, val]) => `  --data-urlencode '${k}=${val}'`),
  ].join(' \\\n'));
  if (dryRun) return;

  const t = config.timeouts;
  const jenkins = fromEnv(config, { log: (m) => console.log(`  ${m}`) });
  const queueId = await jenkins.trigger(config.jenkinsJobPath, params);
  console.log(`\nQueued: queue item ${queueId}`);
  const b = await jenkins.waitForQueue(queueId, { timeoutMs: t.queueMinutes * 60000, intervalMs: t.pollSeconds * 1000 });
  console.log(`Started: build #${b.number} ${b.url || ''}`);
  const done = await jenkins.waitForBuild(config.jenkinsJobPath, b.number, { timeoutMs: t.buildMinutes * 60000, intervalMs: t.pollSeconds * 1000 });
  const report = await jenkins.getTestReport(config.jenkinsJobPath, b.number);
  const res = classify(done, report, config);
  console.log(`\n${res.title}\nGitHub check conclusion would be: ${res.conclusion}`);
  if (res.failed.length) console.log('Failed:\n' + res.failed.map((f) => ` - ${f.name}`).join('\n'));
  process.exitCode = res.conclusion === 'success' || res.conclusion === 'neutral' ? 0 : 1;
}

main().catch((e) => {
  console.error(`\n${e.message}`);
  process.exit(1);
});
