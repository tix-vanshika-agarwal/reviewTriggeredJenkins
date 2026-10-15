# TIKET-AUTOMATION

Maven / Java 17 QA automation framework for tiket.com. One codebase drives native Android and iOS
(Appium), desktop and mobile web (Selenium), and the admin panels, from the same tests and page
objects. 2,208 Java files, ~3,904 tests.

## Start here

**`.claude/indexes/repository-index.md`** — the semantic map and the routing table
(*changed file → what to read*). Use it instead of scanning the repository.

| Task | Read |
|---|---|
| Add or change a test | `.claude/indexes/test-index.md` |
| Add or change UI interaction | `.claude/indexes/page-object-index.md` |
| Add an API call | `.claude/indexes/api-index.md` |
| Find an existing helper | `.claude/indexes/utilities-index.md` |
| Add test data | `.claude/indexes/test-data-index.md` |
| Change config or environments | `.claude/indexes/configuration-index.md` |
| Understand the run pipeline | `.claude/indexes/architecture-index.md` |
| Know what is intentional vs drift | `.claude/indexes/known-patterns-index.md` |
| Check whether something is already automated | `.claude/indexes/coverage-index.md` |

**How to write code here** is already documented in `.cursor/skills/appium-skill/` (DriverTools,
CoreFunctions, page objects, locator priority, test structure, API/POJO/resources, DB checking,
Maven commands). Read it; do not re-derive it.

**`.claude/rules/`** holds the same conventions as 62 enforceable rules with ids, used by the
pre-push reviewer.

## The five things that bite

1. **A test with no test-data sheet row never runs.** `AnnotationTransformer` sets
   `invocationCount(0)` when no sheet `Title` matches the method name — the test never fails, so the
   suite looks green. A new or renamed test is not done until its sheet row exists.
2. **`@Vertical` is mandatory.** `BaseTest.beforeMethod` dereferences it with no null check.
   `@Author`, `@Tribe` and `@Module` are required too.
3. **Tests run in parallel** (`surefire parallel=methods`). Per-test state lives in `ThreadLocal`;
   a mutable `static` on a test class is a race.
4. **Layering is strict.** A test calls page objects and `apiPage` facades only — no `driverTools`,
   no `Locator`, no `.hitApi()`. Measured counter-examples in 212 test classes: zero.
5. **`credential.properties` and `email.properties` are committed blank.** Jenkins injects the real
   values. Never commit one.

## Build and run

```bash
mvn clean compile test-compile      # the only automated gate (GitHub Actions, PRs to main)

mvn test -Dthreads=1 -Dsessions=1 -Dplatform=android -Dtestdata=google \
         -Dtribe=all -Dmodule=all -Dtestname=<method> -Dtesttype=all -Denv=preprod -e
```

`platform`: `android` | `ios` | `dweb` | `amweb` | `imweb` | `panel`. `env`: `gk` | `prod` | `preprod`.
All parameters: `README.md`.

## Implement from a specification

```
/implement-spec QAAUT-47088          # Jira
/implement-spec <confluence url>     # Atlassian wiki
/implement-spec <the requirement>    # direct
```

Ingests the spec, checks existing coverage, implements over the existing abstractions, emits the
test-data sheet row, validates, and runs the reviewer. Method: `.claude/agents/spec-implementer.md`.

Two things to know before reading a QAAUT ticket: sub-tasks are thin — the summary is usually just
`[Platform] testMethodName`, with the real context in the parent or a linked product ticket — and a
method name that already exists means the ticket is maintenance, not new automation.

## Validate and review

```bash
.claude/review/validate.sh   # compile gate + staged review
.claude/hooks/install.sh     # once per checkout — local core.hooksPath=.githooks (global hooks still run)
.claude/review/review.sh     # review the staged change by hand
```

Developer setup, step by step: `CODE_REVIEW_SETUP.md`.

`CRITICAL`/`ERROR` findings at confidence ≥ 0.85 block a `git push` (commits are never blocked). There is no lint, formatter or
separate type check here — `mvn test-compile` is the type check. See `.claude/README.md`.

## Conventions in one screen

- Tests: `@Author`/`@Vertical`/`@Tribe`/`@Module` + `@Test`; `sessionThreadLocal.get()`;
  `getParam("<method name>")`; `step("…")` before each action; `Assertion.assertThat(reason, actual,
  matcher)` — never `org.testng.Assert`.
- Pages: `extends BasePage<T>`; `static final Locator` constants; app pages are abstract +
  `…Android`/`…IOS` behind `getInstance(session)`; use the inherited `driverTools`.
- Waits: `driverTools.waitFor…` (22 methods). Never `Thread.sleep` or `sleep(n)`.
- Locators: app `AppiumBy.id` > `accessibilityId` > `className` > xpath; web `By.id` >
  `[data-testid]` > `[aria-label]` > xpath. One line, no `+`. Handle EN and ID text.
- API: test → `apiPage/XxxApi` facade → `api/{domain}/XxxApi` record → `ApiTools`. Paths via
  `uriHelper.withEndpoint(...)`, never a full URL.
- Data: from the sheet params, not from Java literals. Dates relative, not absolute.
