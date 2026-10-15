# Reviewer-Triggered Jenkins Execution + PR Merge Gate

A reviewer comments on the PR with the `/run-jenkins` command followed by a JSON block:

````
/run-jenkins
```json
{
  "jenkins_test": {
    "platform": "WEB",
    "environment": "PREPROD",
    "test_case_name": "Flight_testBookingOneWay",
    "gsheet_id": "1ZJu0L5q97Fvsj9bqPlHqyoUfHeElOVUchTp5--agHZc",
    "branch_name": "main"
  }
}
```
````

The comment is the trigger. There's no on/off state to reset like a label — comment
`/run-jenkins` again (with a fresh JSON block) any time you want to re-run.

The bot then:

1. Parses the comment. If `/run-jenkins` is there but the JSON block is missing or invalid, it replies explaining what's wrong.
2. Checks the commenter has write access and isn't the PR author.
3. Validates the JSON.
4. Creates a **Jenkins Automation** check in *queued* state on the PR's exact head commit and posts a status comment.
5. Calls `POST /job/FLG-Web-Regression/buildWithParameters` with the 5 values plus the job's fixed parameters.
6. Follows the Jenkins queue and then the build. The check moves to *in progress* and the comment gets the build link.
7. Reads the JUnit/TestNG report and completes the check as **success** or **failure**. Branch protection then allows or blocks the merge.

### How the JSON maps to Jenkins parameters

| JSON field | Jenkins param | Values |
|---|---|---|
| `platform` | `platform` | `WEB`/`DWEB` → `dweb`, `MWEB` → `mweb` |
| `environment` | `env` | `PREPROD` → `preprod` |
| `test_case_name` | `testname` | as given (also `all`) |
| `gsheet_id` | `gSheetId` | as given |
| `branch_name` | `branch` | as given |
| *(fixed)* | `threads=1 sessions=1 testdata=google tribe=all module=all testtype=all sendEmail=false dbUse=false runFromScheduler=` | from `config.json` |

All of this is set in `config.json`. You don't need to change any code.

## Files

```
.github/workflows/reviewer-triggered-jenkins.yml     workflow (PR comment trigger)
.github/reviewer-triggered-jenkins/
  config.json            job, parameter mapping, allowed values, timeouts, gate rules, command
  src/main.js            GitHub side: permissions, check run, orchestration
  src/command.js         parse the comment + JSON block, validate, map to Jenkins params
  src/jenkins.js         Jenkins API client (API token + Google IAP + crumb)
  src/result.js          Pass / Fail / Skip / Error / Aborted -> check conclusion
  scripts/run-local.js   run the Jenkins part from your laptop
  examples/pr-comment.md   sample PR comment
  test/bot.test.js       tests: node --test .github/reviewer-triggered-jenkins/test/bot.test.js
```

---

## Why the browser curl can't be used as-is

The curl copied from the browser authenticates with **cookies**: `__Host-GCP_IAP_AUTH_TOKEN_*` for Google IAP and `JSESSIONID` for Jenkins, plus a `Jenkins-Crumb` tied to that session. These cookies expire within about an hour and belong to one person's login, so they won't work for an automated bot. **Never commit them or store them as secrets.**

The bot uses two long-lived credentials instead:

| Layer | What the bot sends | Where it comes from |
|---|---|---|
| Google IAP (in front of Jenkins) | `Proxy-Authorization: Bearer <Google ID token>` | A GCP service account that is allowed through IAP |
| Jenkins | `Authorization: Basic <user>:<API token>` | A Jenkins user API token. With an API token, Jenkins doesn't require a crumb. |

## Setup, step by step

### Step 1: Jenkins API token

1. Open https://jenkins-fe-qa-preprod.ggwp.red and click your name (top right). Go to **Security** (or **Configure**), then **API Token**, then **Add new token**, and copy the token.
   For the POC your own user is fine. Later, use a dedicated user such as `qa-github-bot` that has only *Overall/Read*, *Job/Read* and *Job/Build* on `FLG-Web-Regression`.
2. If there is **no API Token section**, Jenkins trusts IAP for login (reverse-proxy auth). In that case skip the API token. The IAP service account from Step 2 becomes the Jenkins user, and a Jenkins admin must give it *Job/Build* and *Job/Read*.

### Step 2: Google IAP service account (ask whoever owns the GCP project for Jenkins)

1. Create a service account, for example `github-jenkins-trigger@<project>.iam.gserviceaccount.com`.
2. Go to **GCP Console → Security → Identity-Aware Proxy**. On the Jenkins backend, add this account with the role **IAP-secured Web App User**.
3. Get the **IAP OAuth client ID** for that backend. It looks like `1234-abc.apps.googleusercontent.com`. This is the token *audience*.
4. Choose how GitHub gets tokens for this account:
   - **POC (fastest):** create a JSON key for the service account and store it as the secret `GCP_SA_KEY`. Grant the account *Service Account OpenID Connect Identity Token Creator* on itself.
   - **Recommended later (no keys):** use Workload Identity Federation for GitHub. Set the variables `GCP_WIF_PROVIDER` and `GCP_SERVICE_ACCOUNT`, and give the pool principal *Workload Identity User* plus *Service Account OpenID Connect Identity Token Creator* on the account.

### Step 3: Test the Jenkins part from your laptop first

```bash
# 1) Check the mapping only. This calls nothing.
node .github/reviewer-triggered-jenkins/scripts/run-local.js .github/reviewer-triggered-jenkins/examples/pr-comment.md --dry-run

# 2) Real run
export JENKINS_USER='<your jenkins user>'
export JENKINS_API_TOKEN='<api token>'
export IAP_ID_TOKEN="$(gcloud auth print-identity-token \
    --impersonate-service-account=github-jenkins-trigger@<project>.iam.gserviceaccount.com \
    --audiences=<IAP_OAUTH_CLIENT_ID> --include-email)"
node .github/reviewer-triggered-jenkins/scripts/run-local.js .github/reviewer-triggered-jenkins/examples/pr-comment.md
```

The script triggers the build, waits for it, and prints the decision the PR check would get, for example `PASS: 1 passed`.

> **Debug only, not for CI:** before the service account exists, you can test with your own browser session. Put the full cookie string from the curl `-b '…'` in `export JENKINS_COOKIE='…'` and leave out the other variables. The script fetches a fresh crumb by itself. The cookie expires in about an hour. Never commit it.

### Step 4: GitHub secrets and variables

Go to **Repo → Settings → Secrets and variables → Actions**.

| Type | Name | Value |
|---|---|---|
| Secret | `JENKINS_USER` | Jenkins user |
| Secret | `JENKINS_API_TOKEN` | API token from Step 1 |
| Secret | `GCP_SA_KEY` | Service account JSON key (POC path) |
| Variable | `IAP_CLIENT_ID` | IAP OAuth client ID from Step 2.3 |
| Variable *(WIF path only)* | `GCP_WIF_PROVIDER`, `GCP_SERVICE_ACCOUNT` | WIF provider resource name and service account email |
| Variable *(optional)* | `JENKINS_URL` | Overrides `jenkinsUrl` in `config.json` |
| Variable *(optional)* | `RUNNER_LABEL` | Label of a self-hosted runner. Only needed if GitHub-hosted runners can't reach the Jenkins host. |

### Step 5: Merge the workflow

`issue_comment` workflows always run from the default branch, so the workflow file must be
merged to `main` before `/run-jenkins` does anything. Use Step 3 to test in the meantime.

### Step 6: Try it on a PR

For a solo test, set `"allowPrAuthor": true` in `config.json`, because by default the PR author can't trigger their own gate. Comment the content of `examples/pr-comment.md` on an open PR. You should see a status comment that goes from queued to running to finished, and the **Jenkins Automation** check on the PR.

### Step 7: Turn on merge gating

Go to **Settings → Rules → Rulesets → New branch ruleset**, target `main`, and enable **Require status checks to pass** with **`Jenkins Automation`**. The check only appears in the picker after it has run once, so do Step 6 first.

Every new push creates a new commit with no check, so the merge is blocked again until a reviewer comments `/run-jenkins` again.

## Gate rules

| Jenkins outcome | Bot result | Check | Merge |
|---|---|---|---|
| All tests passed (some may be skipped) | PASS | success | ✅ |
| One or more tests failed | FAIL | failure | ❌ |
| Every test skipped | SKIP | failure. Set `result.allSkipped: "pass"` to allow it. | ❌ |
| No test report, 0 tests, build FAILURE, NOT_BUILT, or auth/API error | ERROR | failure | ❌ |
| Build aborted | ABORTED | cancelled | ❌ |
| Queue or build timeout | ERROR | timed_out. The bot also stops the Jenkins build. | ❌ |

If the job doesn't publish a JUnit/TestNG report, set `result.requireTestReport` to `false`. The bot then falls back to the Jenkins build status (SUCCESS = pass).

## Troubleshooting

| Message | Fix |
|---|---|
| `rejected by Google IAP` | Wrong or expired `IAP_ID_TOKEN`, wrong audience (`IAP_CLIENT_ID`), or the service account lacks *IAP-secured Web App User*. |
| `redirected to accounts.google.com` | No IAP token was sent. Check that the `IAP_CLIENT_ID` variable is set. |
| `HTTP 401` (not from IAP) | Wrong `JENKINS_USER` or `JENKINS_API_TOKEN` |
| `HTTP 403` | The Jenkins user lacks Job/Build. In cookie mode, the crumb or session is stale. |
| `HTTP 400` | A parameter value isn't one of the job's allowed choices. Check `values` in `config.json`. |
| Nothing happens after commenting | The workflow isn't on the default branch yet, or the comment doesn't start a line with `/run-jenkins` exactly (case-sensitive). |
| Comment posted but no check appears | Check the Actions tab — a crash before the first `say()` call (e.g. a malformed `config.json`) has nothing to report. |
| Run longer than 1 hour fails mid-way with an IAP error | Google ID tokens last 1 hour. Set `IAP_TOKEN_COMMAND` (a command that prints a fresh token) so the bot refreshes it every 45 minutes. |
