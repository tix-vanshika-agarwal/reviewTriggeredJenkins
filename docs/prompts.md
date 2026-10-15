# Cursor Prompts — Copy & Paste

Use these so the right skills fire (case-generation, appium-skill). Replace placeholders in `[brackets]`.

## Extra info that helps (optional)

Including these when you have them gives more precise results:

| Prompt type | Extra info that helps | Why |
|-------------|------------------------|-----|
| **New case** | Vertical (Accommodation / Flight / Homes), similar test name to mirror | Picks the right test class and naming style. |
| **Maintenance** | Exact locator name from logs (e.g. HOTEL_PRICE_CARD), full path to page (e.g. SearchResultPageAndroid.java) | Goes straight to the right file and locator. |
| **API** | Domain (e.g. accommodation/hotel), env (preprod/staging) if URL differs | Picks correct package and resource path. |
| **Update existing test** | File path or @test with file, and which platform (Android/iOS/dWeb) | Avoids wrong platform or wrong page. |
| **Change assertion** | Current expected vs new expected, or "line ~X" / snippet | Limits changes to the right assertion. |

---

## New case / Automate case

**Short**
```
Automate this case: [paste scenario / steps / Jira description].
Give test steps and test data in copyable format for the test data sheet (sheet-driven, no hardcoded values).
Platform: [App / Android / iOS / dWeb / mWeb / panel].
```

**Detailed**
```
I need to add a new test case for [e.g. Accommodation / Flight / Homes].

Case: [paste scenario, acceptance criteria, or Jira description]

Please:
1. Propose test steps aligned with our framework (page objects, getParam, step, Assertion).
2. Output test data in copyable format (tab-separated, data rows only, no header) for the test data sheet. Correct column order and params format (key:value, ...). Platform: [App → Android + iOS / dWeb / mWeb / panel] — use the right number of rows per platform.
3. Suggest test method name, Tribe, Module, @Vertical. Use my name for @Author.
```

---

## Maintenance (fix locator / flakiness)

**Locator or failure**
```
This test is failing: [test method name or paste error].
Failed step: [e.g. Verify price in SRP].
Error: [e.g. element HOTEL_PRICE_CARD not found or paste log].

Find where [locator name] is defined and suggest an updated locator (or wait) that matches our framework. File: [e.g. SearchResultPageAndroid.java].
```

**Flakiness / timing**
```
Test [testMethodName] is flaky: [e.g. sometimes HOTEL_CARD is not found after search].
Suggest where to add or adjust waits (explicit wait for element / list) without changing test logic. Use DriverTools wait methods.
```

---

## API automation

**From cURL (and optional response)**
```
I need to add an API test for [e.g. hotel search / create order].

cURL:
[paste cURL]

Please:
1. Convert into our API pattern: API record in src/test/java/com/tiket/api/{domain}/, resources in src/test/resources/api/{domain}/ (_headers.properties, _body.json or _queryParam.properties as needed).
2. If you have a sample response, create a response POJO in src/test/java/com/tiket/pojo/{domain}/ for Gson. If not, I'll paste the response next.
```

**POJO from response only**
```
Response body for [API name]:
[paste JSON]

Generate a response POJO in src/test/java/com/tiket/pojo/{domain}/ with public fields and nested static classes for Gson. Match our existing pojo style.
```

---

## Improvement (refactor / quality / best practices)

**Update existing test (UI change or add/remove steps)**
```
The test [testMethodName] is already automated. I need to update it:

[Choose what applies:]
- UI changed: [describe what changed, e.g. new screen, button moved, different IDs]. Update locators and steps to match the current UI. Keep the same test intent.
- Add steps: [describe new steps to add and where in the flow, e.g. after login, add "dismiss promo popup"]. Reuse existing page methods/locators where possible.
- Remove steps: [describe steps to remove or simplify, e.g. skip optional upsell]. Keep assertions and test data (getParam) unchanged unless needed.

Do not rewrite the whole test; only change what's necessary. Preserve @Author, @Vertical, @Tribe, @Module and test data sheet usage.
```

**Change assertion**
```
The test [testMethodName] is already automated. I need to change only the assertion(s):

[Describe what to change:]
- Expected value: [e.g. new expected text, number, or condition]
- Matcher: [e.g. use containsString instead of is(), or anyOf for multiple valid values]
- Add assertion: [e.g. assert on new element or new step]
- Remove/relax assertion: [e.g. drop or soften an assertion that's too strict]

Do not change steps, locators, or test data; only update the assertion(s). Use Assertion.assertThat and our matchers (anyOf, either, containsString, containsStringIgnoringCase, is).
```

**General improvement**
```
Review [file or test method name] and suggest improvements for:
- Readability and structure
- Aligning with our framework (DriverTools, getParam, page objects, locator priority)
- Waits and stability (explicit waits, avoid flaky xpath)
- Assertions (use anyOf, either, containsString where appropriate)
Do not change behavior; only suggest or apply safe improvements.
```

**Locator / stability**
```
Improve locators and waits in [file or page name]:
- Prefer id / accessibilityId / data-testid over xpath where possible
- Replace fragile xpath (position [1], last(), deep parent/following-sibling) with more stable selectors
- Add or adjust explicit waits where elements load late
List changes and keep existing test behavior.
```

**Test structure**
```
Suggest improvements for test [testMethodName]:
- Use getParam for all test data; no hardcoded values
- Use step() for each logical step
- Use Assertion.assertThat with clear messages
- Correct @Author, @Vertical, @Tribe, @Module
Keep the same test intent.
```

**Page object / DRY**
```
Look at [page class or test file] and suggest:
- Extracting repeated actions into page methods
- Reusing existing locators or page methods instead of duplicating
- Clearer method names and separation of concerns
Do not change public behavior of tests.
```
