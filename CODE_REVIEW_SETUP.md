# Claude QA Code Review — Getting Started

An AI review that checks your changes against this repository's conventions (`.claude/rules/`)
before they are pushed. **Commits are never blocked.** When you `git push` from a terminal, you are
asked whether to review, and a review with a blocking finding stops the push.

Your machine-wide pre-push hook (the PolinRider malware scanner) still runs on every push, first,
whatever you answer.

```
git commit ──▶ .githooks/pre-commit ──▶ global pre-commit (if any)                           ──▶ commit
git push   ──▶ .githooks/pre-push   ──▶ global pre-push (malware scanner) ──▶ [a/d/n] review ──▶ push
```

| Piece | Where |
|---|---|
| Hook | `.githooks/pre-push` |
| Engine | `.claude/review/review.sh` (`context.py` builds the prompt, `render.py` decides) |
| Agent definition | `.claude/agents/qa-code-reviewer.md` |
| Rules the findings cite | `.claude/rules/*.md` |
| Settings | `.claude/review/config.json` |

---

## 1. Setup (once per clone)

Prerequisites:

```bash
claude --version
```
The Claude Code CLI must be installed **and logged in**. If `claude -p "hi"` does not answer, run
`claude` once and sign in.

```bash
python3 --version
```
Python 3.8 or newer, standard library only.

Then, from the repository root:

```bash
.claude/hooks/install.sh
```

This sets `core.hooksPath=.githooks` **for this repository only**. Your global git config is not
touched, so other repositories behave as before, and the hooks update themselves when you pull.

Check it:

```bash
.claude/hooks/install.sh --status
```

You should see `status: enabled (.githooks/: pre-commit pre-push )`. If it prints a **WARNING**
about global hooks with no `.githooks/` counterpart, those hooks will not run here. Copy
`.githooks/pre-commit` under that hook's name, or tell the maintainers.

Without `python3` or the `claude` CLI, the review is skipped and the push is never blocked.

---

## 2. What gets reviewed

The review runs when you `git push` **from a terminal**. It does **not** run when you open a PR on
GitHub.

It reviews the **combined diff of your branch against `origin/main`** (from the merge-base, the
same as the PR's "Files changed" tab) in **one AI call**, not one review per commit.

---

## 3. The push prompt

```
Claude QA review before push?
  [a] whole branch vs origin/<base> (full PR review)
  [d] only changes since the last review that ran
  [n] skip and push (default)
Choice [a/d/N]:
```

| Key | Reviews | Use it when |
|---|---|---|
| `a` | Whole branch vs `origin/main` | First push of the branch, or a final check before raising the PR |
| `d` | Only commits since the last review that actually ran | Re-checking after fixing review findings |
| `n` (Enter) | Nothing. No AI call | Backup pushes, pushes just to trigger Jenkins |

**Typical flow:** commit as often as you like → push once with `a` → fix any findings → push with
`d` → open the PR.

### Skipping does not lose commits

The last reviewed commit is stored per branch in `.git/claude-review-last`. It moves **only when a
review completes**, whether the push was allowed or blocked. It does not move on `n`, on a skipped
push, or when the AI fails. So commits you skipped are included in your next `d`.

```
origin/main ── A ── B ── C ── D ── E
push after C, choose a  → reviews A+B+C, marker = C
push after D, choose n  → no AI call,    marker = C
push after E, choose d  → reviews D+E,   marker = E
```

If there is no marker, or it is no longer part of your branch's history (for example after a rebase
or amend), `d` falls back to reviewing the whole branch.

---

## 4. Everyday use

### Before you push

The reviewer is not a compiler, and the only CI gate is the compile, so run it yourself:

```bash
mvn clean compile test-compile
```

**A new or renamed test with no row in the test-data sheet never runs**, and never fails. Add the
sheet row before you raise the PR.

To review staged work early, without pushing and without blocking anything:

```bash
.claude/review/review.sh
```

### Push

```bash
git push -u origin <branch>
```

You will see the malware scanner's `No malware IOCs detected`, then the prompt. After `a` or `d`:
`Claude QA review: <branch> — whole branch vs origin/main (N commit(s))`, then the report. A
whole-branch review can take a few minutes; use `d` for small follow-up pushes.

### Read the result

- **`PUSH ALLOWED`**: the push goes ahead. `WARNING` and `INFO` findings are advice; read them.
- **`PUSH BLOCKED`**: nothing was pushed, and your commits are still there locally.

Each finding shows a severity, confidence, location and rule id:

```
▌ ERROR
  src/test/java/com/tiket/test/flight/FlightTest.java:10227
  confidence 0.96
  Rule: SYNC_NO_ARBITRARY_SLEEP  (suppress with: // claude-review-ignore: SYNC_NO_ARBITRARY_SLEEP)
```

### When a push is blocked

**a. Fix it (usual case).** Commit the fix, then push with **`d`**: only the new commits are
reviewed.

**b. Suppress a false positive.** Add both lines next to the code:

```java
// claude-review-ignore: SYNC_NO_ARBITRARY_SLEEP
// Reason: waiting for async DB settle, no observable signal (QAAUT-12345)
```

- A suppression without a `Reason:` line is rejected, and the finding stays.
- `Rule: UNKNOWN_RULE` means the AI returned something that is not a valid rule id. It cannot be
  suppressed by name and cannot block. Fix the code, or tell the maintainers.
- Suppressed findings are counted in the report (`Suppressed findings: N`).

**c. Skip the review for one push:** answer `n`, or use `CLAUDE_REVIEW_SKIP=1 git push`. The
malware scanner still runs.

> ⚠️ Do **not** use `git push --no-verify` to get past the review. It skips the whole pre-push
> hook, **including the malware scanner**.

---

## 5. What blocks a push

- **Blocks:** a `CRITICAL` or `ERROR` finding with confidence of at least `0.85` (`blockOn` and
  `minConfidence` in the config).
- **Never blocks:** `WARNING` and `INFO` findings, a finding below `0.85` (shown as advisory), or
  `UNKNOWN_RULE`.
- **Never blocks when the AI is unavailable:** timeout, quota or token exhaustion, a missing
  `claude` CLI, or an unreadable response all print `AI REVIEW UNAVAILABLE` and let the push through
  (`onAiFailure: fail-open`). The marker does not move, so the next `d` still covers those commits.

---

## 6. When the review is skipped automatically

- **No terminal.** Pushes from an IDE, a git GUI, Claude Code or CI cannot answer the prompt, so the
  review is skipped. Set `CLAUDE_REVIEW_SCOPE` (below) to run one anyway.
- **Nothing to review**: a branch deletion, or a `d` with no new commits.
- The hook is local: it only runs for people who installed it and push with the git CLI. It is a
  convenience, not an enforcement mechanism.

---

## 7. Settings

| What | Where | Default |
|---|---|---|
| Base branch for `a` | `baseBranch` in `config.json`, or env `CLAUDE_REVIEW_BASE` | `main` |
| Answer the prompt non-interactively | env `CLAUDE_REVIEW_SCOPE=a\|d\|n` | unset (ask) |
| Emergency bypass (same effect as `n`) | env `CLAUDE_REVIEW_SKIP=1` | unset |
| Blocking severities and confidence | `blockOn`, `minConfidence` | `CRITICAL`, `ERROR` at `0.85` |
| AI timeout | `timeoutSeconds` | `600`, always enforced; a hung review is killed and the push proceeds (`AI REVIEW UNAVAILABLE`) |
| Behaviour when AI is unavailable | `onAiFailure` | `fail-open` |
| Files reviewed per run | `maxChangedFiles` | `40`; files beyond it are named as **not reviewed** |

Example: review the branch without being prompted.

```bash
CLAUDE_REVIEW_SCOPE=a git push
```

`origin/main` is your local copy of the remote, so run `git fetch` first if it may be stale.

---

## 8. Running it manually

```bash
.claude/review/review.sh --scope all      # whole branch vs origin/main
.claude/review/review.sh --scope delta    # since the last review that ran
.claude/review/review.sh                  # only staged changes
.claude/review/review.sh --dry-run        # build the input, no AI call
.claude/review/review.sh --debug          # keep the prompt and the raw reply
```

`--scope` runs move the marker, exactly as the push prompt does. They report but never block
anything, since nothing is being pushed.

---

## 9. Troubleshooting

| You see | Meaning | Do |
|---|---|---|
| No prompt on push | Hooks not enabled in this checkout, or no terminal | `.claude/hooks/install.sh --status`; from an IDE, use `CLAUDE_REVIEW_SCOPE=a` |
| `No usable last-reviewed commit … reviewing the whole branch` | First `d` on this branch, or history was rewritten | Nothing; this is the fallback |
| `no reviewable files in this change` | Only non-code files changed (reviewed: `.java .properties .json .xml .yml .yaml`) | Nothing |
| `AI REVIEW UNAVAILABLE (…)` | Claude missing, logged out, offline, timed out, or replied badly | Fix Claude, then push again with `d` |
| `WARNING: N findings came back with no confidence score` | The agent's reply was malformed. Those findings cannot block | Read them and fix any that are real |
| `N changed file(s) over the maxChangedFiles limit are NOT reviewed` | A very large branch | Review in smaller pieces with `d` |
| `Push blocked by the machine-wide pre-push hook` | The malware scanner found something. The review did not run | Treat it as real; contact security if it is not obviously your own file |

---

## 10. Turning it off

For this checkout (your global hooks take over again):

```bash
.claude/hooks/install.sh --uninstall
```

For everyone: set `"enabled": false` in `.claude/review/config.json` and commit it. The malware
scanner keeps running either way.

## Maintainers

- Self-tests (no AI calls): `.claude/review/review.sh --test`
- Rule ids live in the `## \`RULE_ID\` — SEVERITY` headers of `.claude/rules/*.md`; the renderer
  reads them to cap severities, and the self-test checks that every rule the agent cites exists.
- Refresh the indexes after structural changes: `.claude/review/review.sh --index`
