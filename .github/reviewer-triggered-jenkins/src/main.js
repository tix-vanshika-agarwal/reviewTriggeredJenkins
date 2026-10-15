'use strict';
/**
 * Entry point called from actions/github-script on every new PR comment.
 *
 *  1. The comment must be on a pull request and start with config.command (e.g. "/run-jenkins"),
 *     followed by a JSON block with the 5 execution parameters. Comments that don't match the
 *     command are ignored entirely.
 *  2. The commenter must have write access (and, by default, must not be the PR author).
 *  3. JSON is validated and mapped to the Jenkins job's parameters.
 *  4. A "Jenkins Automation" check run is created on the PR's exact head SHA (pending).
 *  5. Jenkins is triggered, the queue + build are tracked, and the check is completed
 *     as success / failure based on the real test results -> branch protection gates the merge.
 */
const fs = require('fs');
const path = require('path');
const { parseComment, validate, toJenkinsParams, exampleJson } = require('./command');
const { fromEnv } = require('./jenkins');
const { classify, renderReport } = require('./result');

const RANK = { none: 0, read: 1, triage: 2, write: 3, maintain: 4, admin: 5 };

function loadConfig() {
  const file = process.env.RTJ_CONFIG || path.join(__dirname, '..', 'config.json');
  return JSON.parse(fs.readFileSync(file, 'utf8'));
}

module.exports = async function main({ github, context, core }, deps = {}) {
  const config = deps.config || loadConfig();
  const { owner, repo } = context.repo;
  const { comment, issue, sender } = context.payload;
  if (!comment || !issue || !sender || sender.type === 'Bot') return;
  if (!issue.pull_request) return; // comment is on a plain issue, not a PR

  const parsed = parseComment(comment.body || '', config.command);
  if (!parsed.triggered) return; // not a /run-jenkins comment; nothing to do

  const actor = sender.login;
  const prNumber = issue.number;
  const runUrl = `${context.serverUrl}/${owner}/${repo}/actions/runs/${context.runId}`;
  const say = (body) => github.rest.issues.createComment({ owner, repo, issue_number: prNumber, body });

  // ---- 1. Who commented?
  const { data: perm } = await github.rest.repos.getCollaboratorPermissionLevel({ owner, repo, username: actor });
  const level = RANK[perm.role_name] !== undefined ? perm.role_name : perm.permission;
  const minimum = config.permissions.minimum || 'write';
  if ((RANK[level] || 0) < RANK[minimum]) {
    await say(`@${actor} you need at least \`${minimum}\` access to run Jenkins from a PR (you have \`${level}\`).`);
    return;
  }
  const { data: pr } = await github.rest.pulls.get({ owner, repo, pull_number: prNumber });
  if (pr.state !== 'open') {
    await say(`@${actor} this PR is ${pr.state}; Jenkins can only be run on open PRs.`);
    return;
  }
  if (!config.permissions.allowPrAuthor && pr.user.login === actor) {
    await say(`@${actor} the PR author cannot trigger the gating run — a reviewer has to comment \`${config.command}\`.`);
    return;
  }

  // ---- 2. Parameters
  const howTo = `Comment the execution parameters right after the command, for example:\n\n\`\`\`\n${exampleJson(config)}\n\`\`\``;
  if (!parsed.json.found) {
    await say(`@${actor} ${config.command} needs the execution parameters as a JSON block in the same comment.\n\n${howTo}\n\n${allowedText(config)}`);
    return;
  }
  if (parsed.json.error) {
    await say(`@${actor} the JSON block in your comment is invalid:\n\n- ${parsed.json.error}\n\n${howTo}\n\n${allowedText(config)}`);
    return;
  }
  const v = validate(parsed.json.value, config);
  if (!v.ok) {
    await say(`@${actor} the execution parameters are not valid:\n\n${v.errors.map((e) => `- ${e}`).join('\n')}\n\n${howTo}\n\n${allowedText(config)}`);
    return;
  }
  await runJenkins({ github, context, core, config, owner, repo, prNumber, actor, pr, runUrl, say, v, deps });
};

async function runJenkins({ github, context, core, config, owner, repo, prNumber, actor, pr, runUrl, say, v, deps }) {
  const jenkinsParams = toJenkinsParams(v.input, config);
  const paramsTable = ['| Field | Value | Jenkins param |', '|---|---|---|',
    ...Object.entries(v.input).map(([k, val]) => {
      const def = config.parameters[k];
      return `| \`${k}\` | \`${val}\` | \`${def.jenkinsName}=${jenkinsParams[def.jenkinsName]}\` |`;
    })].join('\n');

  // ---- 3. Pending check on the exact commit
  const sha = pr.head.sha;
  const short = sha.slice(0, 7);
  const { data: check } = await github.rest.checks.create({
    owner, repo, name: config.checkName, head_sha: sha, status: 'queued', details_url: runUrl,
    output: { title: 'Queued in Jenkins', summary: `Requested by @${actor} for \`${short}\`.\n\n${paramsTable}` },
  });
  core.setOutput('check_run_id', String(check.id));
  const setCheck = (fields) => github.rest.checks.update({ owner, repo, check_run_id: check.id, ...fields });

  const statusBody = (state, extra = '') =>
    `**Jenkins automation ${state}** — \`${config.jenkinsJobPath}\` on commit \`${short}\` (requested by @${actor})\n\n${paramsTable}\n\n${extra}[Action logs](${runUrl})`;
  const { data: status } = await say(statusBody('queued'));
  const setStatus = (state, extra) =>
    github.rest.issues.updateComment({ owner, repo, comment_id: status.id, body: statusBody(state, extra) }).catch(() => {});

  // ---- 4. Jenkins
  const t = config.timeouts;
  const intervalMs = t.pollSeconds * 1000;
  let jenkins;
  let buildNo = null;
  let buildUrl = '';
  try {
    jenkins = fromEnv(config, { fetchImpl: deps.fetchImpl, log: (m) => core.info(m) });
    const queueId = await jenkins.trigger(config.jenkinsJobPath, jenkinsParams);
    core.info(`Triggered ${config.jenkinsJobPath}, queue item ${queueId}`);

    const build = await jenkins.waitForQueue(queueId, { timeoutMs: t.queueMinutes * 60000, intervalMs });
    buildNo = build.number;
    buildUrl = build.url || jenkins.buildUrl(config.jenkinsJobPath, buildNo);
    await setCheck({
      status: 'in_progress', details_url: buildUrl,
      output: { title: `Running — Jenkins build #${buildNo}`, summary: `[Jenkins build #${buildNo}](${buildUrl})\n\n${paramsTable}` },
    });
    await setStatus('running', `Jenkins build: [#${buildNo}](${buildUrl})\n\n`);

    const finished = await jenkins.waitForBuild(config.jenkinsJobPath, buildNo, { timeoutMs: t.buildMinutes * 60000, intervalMs });
    const report = await jenkins.getTestReport(config.jenkinsJobPath, buildNo);

    // ---- 5. Gate
    const res = classify(finished, report, config);
    await setCheck({
      status: 'completed', conclusion: res.conclusion, details_url: buildUrl,
      output: { title: res.title, summary: renderReport(res, { buildNo, buildUrl, paramsTable, max: config.maxTestsListed }) },
    });
    const passed = res.conclusion === 'success' || res.conclusion === 'neutral';
    const verdict = passed ? 'merge gate passed' : 'merge is blocked';
    await setStatus('finished', `Jenkins build: [#${buildNo}](${buildUrl}) — **${res.title}** (${verdict})\n\n`);

    const { data: latest } = await github.rest.pulls.get({ owner, repo, pull_number: prNumber });
    const moved = latest.head.sha !== sha
      ? `\n\nNote: new commits were pushed during the run. This result only covers \`${short}\`; comment \`${config.command}\` again to gate the latest commit.`
      : '';
    await say(`@${actor} **${res.title}** — ${verdict}. [Jenkins build #${buildNo}](${buildUrl})${moved}`);
    core.notice(`${res.title} -> check ${res.conclusion}`);
  } catch (err) {
    core.error(err.stack || String(err));
    if (err.timedOut && buildNo && jenkins) await jenkins.stopBuild(config.jenkinsJobPath, buildNo);
    const title = err.timedOut ? 'ERROR: timed out' : 'ERROR: Jenkins run could not be completed';
    await setCheck({
      status: 'completed', conclusion: err.timedOut ? 'timed_out' : 'failure',
      output: { title, summary: `${err.message}\n\n${buildNo ? `[Jenkins build #${buildNo}](${buildUrl})\n\n` : ''}${paramsTable}` },
    }).catch((e) => core.error(`Could not update the check: ${e.message}`));
    await setStatus('errored', `**${title}**: ${err.message}\n\n`);
    await say(`@${actor} ${title}: ${err.message}\n\nThe merge stays blocked. Fix the problem and comment \`${config.command}\` again.`).catch(() => {});
    core.setFailed(err.message);
  }
}

function allowedText(config) {
  const lines = Object.entries(config.parameters)
    .filter(([, d]) => d.values)
    .map(([k, d]) => `- \`${k}\`: ${Object.keys(d.values).join(', ')}`);
  return lines.length ? `Allowed values:\n${lines.join('\n')}` : '';
}
