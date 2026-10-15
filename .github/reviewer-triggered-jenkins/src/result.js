'use strict';
/**
 * Turns a finished Jenkins build (+ its JUnit/TestNG report) into the gate decision.
 *
 *   outcome:    PASS | FAIL | SKIP | ERROR | ABORTED
 *   conclusion: GitHub check-run conclusion. Only success/neutral/skipped let a
 *               required check pass, so every blocking outcome maps to
 *               failure / cancelled / timed_out.
 */
function classify(build, report, config) {
  const r = config.result || {};
  const buildResult = build.result;
  const counts = report
    ? { pass: report.passCount || 0, fail: report.failCount || 0, skip: report.skipCount || 0 }
    : null;
  if (counts) counts.total = counts.pass + counts.fail + counts.skip;

  const failed = [];
  const skipped = [];
  for (const suite of (report && report.suites) || []) {
    for (const c of suite.cases || []) {
      const name = c.className ? `${c.className}.${c.name}` : c.name;
      if (c.status === 'FAILED' || c.status === 'REGRESSION') failed.push({ name, details: (c.errorDetails || '').trim() });
      else if (c.status === 'SKIPPED') skipped.push({ name });
    }
  }

  const make = (outcome, conclusion, title, notes = []) =>
    ({ outcome, conclusion, title, notes, counts, buildResult, failed, skipped });

  if (buildResult === 'ABORTED') return make('ABORTED', 'cancelled', 'ABORTED: the Jenkins build was aborted');
  if (buildResult === 'NOT_BUILT') return make('ERROR', 'failure', 'ERROR: Jenkins did not run the build (NOT_BUILT)');

  if (!counts) {
    if (r.requireTestReport !== false) {
      return make('ERROR', 'failure', `ERROR: no test report published (Jenkins result ${buildResult})`, [
        'The job did not publish JUnit/TestNG results, so the outcome cannot be verified.',
      ]);
    }
    // Fallback: trust the Jenkins build result only.
    return buildResult === 'SUCCESS'
      ? make('PASS', 'success', 'PASS: Jenkins build succeeded (no test report)', ['Result is based on the Jenkins build status only.'])
      : make('FAIL', 'failure', `FAIL: Jenkins build ${buildResult} (no test report)`);
  }

  if (counts.fail > 0) return make('FAIL', 'failure', `FAIL: ${counts.fail} failed, ${counts.pass} passed, ${counts.skip} skipped`);
  if (counts.total === 0) return make('ERROR', 'failure', 'ERROR: 0 tests executed', ['Check that test_case_name matches an existing test.']);
  if (counts.pass === 0) {
    const pass = r.allSkipped === 'pass';
    return make('SKIP', pass ? 'neutral' : 'failure', `SKIP: all ${counts.skip} test(s) skipped`, [
      pass ? 'Config allows an all-skipped run to pass.' : 'Nothing was actually verified, so the merge stays blocked.',
    ]);
  }
  if (buildResult === 'FAILURE') {
    return make('ERROR', 'failure', 'ERROR: tests passed but the Jenkins build failed', ['A later pipeline stage failed; check the console log.']);
  }
  const notes = [];
  if (counts.skip) notes.push(`${counts.skip} test(s) skipped (listed below).`);
  if (buildResult === 'UNSTABLE') notes.push('Jenkins marked the build UNSTABLE although no test failed.');
  return make('PASS', 'success', `PASS: ${counts.pass} passed${counts.skip ? `, ${counts.skip} skipped` : ''}`, notes);
}

function renderReport(res, { buildNo, buildUrl, paramsTable, max = 50 }) {
  const out = [`### ${res.title}`, '', `Jenkins build [#${buildNo}](${buildUrl}) — result \`${res.buildResult}\``, ''];
  if (res.counts) {
    out.push('| Passed | Failed | Skipped | Total |', '|---|---|---|---|',
      `| ${res.counts.pass} | ${res.counts.fail} | ${res.counts.skip} | ${res.counts.total} |`, '');
  }
  for (const n of res.notes) out.push(`> ${n}`, '');
  if (res.failed.length) {
    out.push(`#### Failed tests (${res.failed.length})`, '');
    for (const t of res.failed.slice(0, max)) {
      out.push(`- \`${t.name}\`${t.details ? ` — ${t.details.replace(/\s+/g, ' ').slice(0, 300)}` : ''}`);
    }
    if (res.failed.length > max) out.push(`- …and ${res.failed.length - max} more`);
    out.push('');
  }
  if (res.skipped.length) {
    out.push(`<details><summary>Skipped tests (${res.skipped.length})</summary>`, '');
    for (const t of res.skipped.slice(0, max)) out.push(`- \`${t.name}\``);
    out.push('', '</details>', '');
  }
  out.push('#### Parameters', '', paramsTable);
  const text = out.join('\n');
  return text.length > 60000 ? `${text.slice(0, 59900)}\n…(truncated)` : text;
}

module.exports = { classify, renderReport };
