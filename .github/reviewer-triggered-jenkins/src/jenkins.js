'use strict';
/**
 * Jenkins Remote Access API client for a Jenkins that sits behind Google Cloud IAP.
 *
 * Auth (combine as needed):
 *   apiUser + apiToken  -> "Authorization: Basic ..."  (Jenkins user API token; no CSRF crumb needed)
 *   iapToken            -> Google OIDC ID token for IAP. Sent as "Proxy-Authorization: Bearer ..."
 *                          when Basic auth is also used, otherwise as "Authorization: Bearer ...".
 *   iapTokenCommand     -> shell command printing a fresh ID token; re-run every 45 min
 *                          (ID tokens expire after 1 h, builds can take longer).
 *   cookie              -> browser cookie string. LOCAL DEBUGGING ONLY (expires in ~1 h).
 *                          A CSRF crumb is fetched automatically in this mode.
 */
const { execSync } = require('child_process');

const sleep = (ms) => new Promise((r) => setTimeout(r, ms));
const IAP_REFRESH_MS = 45 * 60 * 1000;

class JenkinsError extends Error {}

class Jenkins {
  constructor({ baseUrl, apiUser, apiToken, iapToken, iapTokenCommand, cookie, fetchImpl, log }) {
    if (!baseUrl) throw new JenkinsError('Jenkins URL is not set (JENKINS_URL or config.jenkinsUrl).');
    if (!(apiUser && apiToken) && !iapToken && !iapTokenCommand && !cookie) {
      throw new JenkinsError('No Jenkins credentials: set JENKINS_USER + JENKINS_API_TOKEN (and IAP_ID_TOKEN if behind IAP).');
    }
    this.baseUrl = baseUrl.replace(/\/+$/, '');
    this.basic = apiUser && apiToken ? 'Basic ' + Buffer.from(`${apiUser}:${apiToken}`).toString('base64') : null;
    this.iapToken = iapToken || null;
    this.iapTokenAt = iapToken ? Date.now() : 0;
    this.iapTokenCommand = iapTokenCommand || null;
    this.cookie = cookie || null;
    this.crumb = null;
    this.fetch = fetchImpl || globalThis.fetch;
    this.log = log || (() => {});
  }

  currentIapToken() {
    if (this.iapTokenCommand && (!this.iapToken || Date.now() - this.iapTokenAt > IAP_REFRESH_MS)) {
      this.iapToken = execSync(this.iapTokenCommand, { encoding: 'utf8', stdio: ['ignore', 'pipe', 'pipe'] }).trim();
      this.iapTokenAt = Date.now();
      this.log('Refreshed IAP ID token.');
    }
    return this.iapToken;
  }

  headers(extra = {}) {
    const h = { Accept: 'application/json', ...extra };
    if (this.basic) h.Authorization = this.basic;
    const iap = this.currentIapToken();
    if (iap) {
      if (h.Authorization) h['Proxy-Authorization'] = `Bearer ${iap}`;
      else h.Authorization = `Bearer ${iap}`;
    }
    if (this.cookie) h.Cookie = this.cookie;
    if (this.crumb) h[this.crumb.field] = this.crumb.value;
    return h;
  }

  jobUrl(jobPath) {
    return `${this.baseUrl}/${jobPath.split('/').filter(Boolean).map((p) => `job/${encodeURIComponent(p)}`).join('/')}`;
  }

  buildUrl(jobPath, number) {
    return `${this.jobUrl(jobPath)}/${number}`;
  }

  async request(url, { method = 'GET', body, headers, allow404 = false } = {}) {
    const res = await this.fetch(url, { method, body, headers: this.headers(headers), redirect: 'manual' });
    if (allow404 && res.status === 404) return null;
    if (res.status >= 300 && res.status < 400 && res.status !== 304) {
      const loc = res.headers.get('location') || '';
      const toLogin = /accounts\.google\.com|\/login|securityRealm/.test(loc);
      throw new JenkinsError(`Jenkins ${method} ${url} was redirected to ${loc || '(no location)'}` +
        (toLogin ? ' — the request is not authenticated (IAP token or Jenkins API token missing/invalid).' : ''));
    }
    if (res.status >= 400) {
      const text = await res.text().catch(() => '');
      const fromIap = res.headers.get('x-goog-iap-generated-response') === 'true' || /IAP|Identity-Aware/i.test(text.slice(0, 500));
      let hint = '';
      if (fromIap) hint = ' — rejected by Google IAP: check IAP_ID_TOKEN (audience = IAP OAuth client ID, not expired) and that the service account has "IAP-secured Web App User".';
      else if (res.status === 401) hint = ' — check JENKINS_USER / JENKINS_API_TOKEN.';
      else if (res.status === 403) hint = ' — the Jenkins user lacks Job/Build or Job/Read (or a CSRF crumb was required).';
      else if (res.status === 404) hint = ' — check jenkinsJobPath in config.json.';
      else if (res.status === 400) hint = ' — Jenkins rejected a parameter value (e.g. a choice that does not exist).';
      const err = new JenkinsError(`Jenkins ${method} ${url} -> HTTP ${res.status}${hint} ${text.replace(/<[^>]+>/g, ' ').replace(/\s+/g, ' ').slice(0, 200)}`.trim());
      err.status = res.status;
      throw err;
    }
    return res;
  }

  async getJson(url, opts) {
    const res = await this.request(url, opts);
    return res ? res.json() : null;
  }

  /** Cookie (browser-session) auth needs a CSRF crumb for POSTs; API-token auth does not. */
  async ensureCrumb() {
    if (this.basic || this.crumb || !this.cookie) return;
    const c = await this.getJson(`${this.baseUrl}/crumbIssuer/api/json`, { allow404: true });
    if (c) this.crumb = { field: c.crumbRequestField, value: c.crumb };
  }

  /** POST buildWithParameters. Returns the queue item id. */
  async trigger(jobPath, params) {
    await this.ensureCrumb();
    const res = await this.request(`${this.jobUrl(jobPath)}/buildWithParameters`, {
      method: 'POST',
      body: new URLSearchParams(params).toString(),
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    });
    const m = (res.headers.get('location') || '').match(/\/queue\/item\/(\d+)/);
    if (!m) throw new JenkinsError(`Jenkins answered HTTP ${res.status} without a queue item location.`);
    return Number(m[1]);
  }

  async poll(fn, { timeoutMs, intervalMs, what }) {
    const deadline = Date.now() + timeoutMs;
    let errorsInRow = 0;
    for (;;) {
      try {
        const value = await fn();
        errorsInRow = 0;
        if (value !== undefined) return value;
      } catch (e) {
        const transient = !(e instanceof JenkinsError) || (e.status >= 500) || e.status === 429;
        if (!transient) throw e;
        errorsInRow += 1;
        this.log(`Transient error while waiting for ${what}: ${e.message}`);
        if (errorsInRow >= 6) throw e;
      }
      if (Date.now() >= deadline) {
        const err = new JenkinsError(`Timed out after ${Math.round(timeoutMs / 60000)} min waiting for ${what}.`);
        err.timedOut = true;
        throw err;
      }
      await sleep(intervalMs);
    }
  }

  /** Queue item -> { number, url } once an executor picks it up. */
  waitForQueue(queueId, { timeoutMs, intervalMs }) {
    return this.poll(async () => {
      const item = await this.getJson(`${this.baseUrl}/queue/item/${queueId}/api/json`);
      if (item.cancelled) throw new JenkinsError('The build was cancelled while waiting in the Jenkins queue.');
      if (item.executable && item.executable.number) return { number: item.executable.number, url: item.executable.url };
      this.log(`In queue: ${item.why || 'waiting'}`);
      return undefined;
    }, { timeoutMs, intervalMs, what: 'the build to start' });
  }

  /** Polls until the build is no longer running; returns the build JSON. */
  waitForBuild(jobPath, number, { timeoutMs, intervalMs }) {
    const url = `${this.buildUrl(jobPath, number)}/api/json?tree=number,url,building,result,duration`;
    return this.poll(async () => {
      const b = await this.getJson(url);
      return !b.building && b.result ? b : undefined;
    }, { timeoutMs, intervalMs, what: `build #${number} to finish` });
  }

  /** JUnit/TestNG report, or null if the build published none. */
  getTestReport(jobPath, number) {
    const tree = 'passCount,failCount,skipCount,suites[cases[className,name,status,errorDetails]]';
    return this.getJson(`${this.buildUrl(jobPath, number)}/testReport/api/json?tree=${encodeURIComponent(tree)}`, { allow404: true });
  }

  async stopBuild(jobPath, number) {
    try {
      await this.ensureCrumb();
      await this.request(`${this.buildUrl(jobPath, number)}/stop`, { method: 'POST' });
    } catch (e) {
      this.log(`Could not stop build #${number}: ${e.message}`);
    }
  }
}

/** Builds a client from environment variables (used by the Action and the local CLI). */
function fromEnv(config, extra = {}) {
  return new Jenkins({
    baseUrl: process.env.JENKINS_URL || config.jenkinsUrl,
    apiUser: process.env.JENKINS_USER,
    apiToken: process.env.JENKINS_API_TOKEN,
    iapToken: process.env.IAP_ID_TOKEN,
    iapTokenCommand: process.env.IAP_TOKEN_COMMAND,
    cookie: process.env.JENKINS_COOKIE,
    ...extra,
  });
}

module.exports = { Jenkins, JenkinsError, fromEnv };
