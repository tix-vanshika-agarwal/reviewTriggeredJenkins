### Sample Maven Command
mvn test -Dthreads=4 -Dsessions=4 -Dplatform=android -Dtestdata=local -Dtribe=all -Dmodule=all -Dtestname=all -Dtesttype=all -Denv=gk

### MVN Params
* threads -> number of testNG threads
* sessions -> number of Browser Stack sessions
* platform -> android | ios | dweb | amweb | imweb
* testdata -> local | google
* tribe -> all | specific tribe name
  * Selecting all means no filter based on tribe
* module -> all | specific module name
  * Selecting all means no filter based on module
* testname -> all | specific test name
  * Selecting all means no filter based on test
* testtype -> all | sanity | regression
  * Selecting all means no filter based on testtype
* env -> gk | prod | preprod
* coverage -> true | false
  * To be used for Android only, iOS code coverage not supported
* dbUse -> true | false
  * Must only be true if result of test need to go in DB for common reporting util.
* proxyUse -> true | false
  * Must only be true for analytics automation through MITM proxy
* forceDeviceFarmBinaryShutdown -> true | false
  * Must only be true for forcing device farm binary instance to shutdown in the beginning
* deviceFarm -> browserstack | lambdatest | local
  * Select BS or LT, default is local is param not provided or matched

### Google Authentication (Workload Identity Federation)
Google Sheets (`GoogleSheetApi2`) and BigQuery (`BigQueryClient`) no longer use a service account key file (`oauth2.json`).
Both get Application Default Credentials (ADC) and impersonate the service account set in `google.sa.email` (`credential.properties`).

* CI / Jenkins: ADC comes from the WIF credential config set in `GOOGLE_APPLICATION_CREDENTIALS`.
* Local run: log in once with `gcloud auth application-default login`.
  * Your account needs under group `sqa@tiket.com` or `sqa-interns@tiket.com` on the target service account.

### Jenkins shell commands
* Shell 1
```
#!/bin/bash -il
echo "Present Working Directory"
pwd
echo $WORKSPACE

sed -ie 's/google.sheet.api.key=/google.sheet.api.key=SomePrivateSshKey/' src/main/resources/credential.properties
sed -ie 's/google.sheet.id=/google.sheet.id=SomeGoogleSheetID/' src/main/resources/credential.properties
sed -ie 's/testrail.user=/testrail.user=some.user@tiket.com/' src/main/resources/credential.properties
sed -ie 's/testrail.password=/testrail.password=SomePassword/' src/main/resources/credential.properties
sed -ie 's/email=/email=qa.apps@tiket.com/' src/main/resources/email.properties
sed -ie 's/password=/password=SomeanotherPassword/' src/main/resources/email.properties
```

* Shell 2
```
#!/bin/bash -il

echo "mvn version"
mvn --version
echo "java version"
java -version
export JAVA_HOME=/usr/lib/jvm/jdk-17.0.2
echo $JAVA_HOME
echo $PATH
java -version

export VERTICAL_NAME=Flight

# Google Sheets & BigQuery use Workload Identity Federation (no oauth2.json needed).
# Point ADC to the WIF external-account config provided by DevOps:
export GOOGLE_APPLICATION_CREDENTIALS=/path/to/wif-credential-config.json

mvn clean
mvn compile
if [[ -z "${NAGINATOR_BUILD_NUMBER}" ]]; then
  echo "Not retry condition"
  mvn test -Dthreads=$threads -Dsessions=$sessions -Dplatform=$platform -Dtestdata=$testdata -Dtribe=$tribe -Dmodule=$module -Dtestname=$testname -Dtesttype=$testtype -Denv=$env -DdbUse=$dbUse -e
else
  echo "Retrying failed build: ${NAGINATOR_BUILD_NUMBER}"
  FILE=/var/lib/jenkins/jobs/${JOB_NAME}/builds/${NAGINATOR_BUILD_NUMBER}/archive/target/"surefire-reports/testng-failed.xml"
    if test -f "$FILE"; then
        echo "$FILE exists."
        mvn test -Dsurefire.suiteXmlFiles=/$FILE -Dsessions=$sessions -Dplatform=$platform -Dtestdata=$testdata -Dtribe=$tribe -Dmodule=$module -Dtestname=$testname -Dtesttype=$testtype -Denv=$env -DdbUse=$dbUse -e
    else
        echo "$FILE doesn't exists"
        mvn test -Dthreads=$threads -Dsessions=$sessions -Dplatform=$platform -Dtestdata=$testdata -Dtribe=$tribe -Dmodule=$module -Dtestname=$testname -Dtesttype=$testtype -Denv=$env -DdbUse=$dbUse -e
    fi
fi
```
### TestRail case run lookup

Standalone utility that answers "when did this TestRail case last run" from Klov. It does
not run any test, so it needs none of the params above.

Params are passed as `-D` values, the same way the test jobs take `-Dplatform` and
`-Denv`:

```
mvn compile exec:java -Dids=C442697,C2380938 -Denv=preprod -Dlastn=7
```

The `compile` goal is not optional. `exec:java` runs whatever is already in
`target/classes` and never builds it, so on a clean checkout it fails with
`An exception occurred while executing the Java class. com.tiket.tools.caselookup.CaseRunLookup`,
which is a `ClassNotFoundException` wearing a confusing message.

| Param | Meaning |
|-------|---------|
| `-Dids` | TestRail ids (`C123`, `123`) or pasted TestRail URLs, comma separated. Spaces are fine |
| `-Dsuiteid` / `-Dgroupid` | Look up a whole API section instead of, or alongside, ids. `-Dsectionid` is an alias for `-Dgroupid` |
| `-Denv` | `gk`, `preprod` or `prod`. API cases support `gk` and `preprod` only |
| `-Dlastn` | Days back, 1 to 31. Default 7. A larger value is clamped, with a note on stderr |
| `-Dfrom` / `-Dto` | Explicit `YYYY-MM-DD` range instead of `-Dlastn`, 31 days max |
| `-Dplatform` | Override the TestRail platform when the case has none |
| `-Dprojectid` | TestRail project owning the suite. Defaults to `caselookup.testrail.projectId` |
| `-Dklovproject` | Force a Klov project name when auto-resolution picks the wrong one |

Every param also has a `--flag` form (`--ids`, `--last-n`, `--suite-id`, `--klov-project`
and so on) for running it by hand, and a flag beats the matching `-D` value. `-Dids` is
the safer choice in Jenkins, because `-Dexec.args` splits on whitespace.

The tool finds `config.properties` by walking up from the working directory and falls
back to the copy on the classpath, so it does not have to be launched from the repository
root. `-Dcaselookup.configDir=/path/to/resources` overrides the search.

TestRail's vertical names the app Klov projects (`APP Flight PREPROD ANDROID`) but not the
others. Web and panel suites are named after an abbreviated code with a `-Web-Regression`
or `-Panel-Regression` suffix, and the API projects add ` - Client Facing`, so nothing
derives `PANEL AC-Panel-Regression PREPROD` from the vertical `Accommodation`. Those are
listed in `config.properties` as `caselookup.klovAlias.<vertical>`, comma separated, and
`caselookup.klovAlias.api.<vertical>` for the API Klov, whose names carry no prefix or
suffix. Aliases are searched in addition to the derived name, because a case can run under
either; a name matching no project costs one cheap lookup and is then ignored, so a wrong
entry is harmless. When nothing matches, the log lists the projects on the same platform
and environment, which is where the missing name almost always is. If the vertical itself
contains a space, escape it in the key as `caselookup.klovAlias.Ground\ Transport`.

The `api.` entries are not optional. Most verticals also have a bare API project named
exactly after them (`Flight`, `Sphinx`, `Eiffel`) which the exact-name match finds first
and which stopped receiving runs in May 2026, so without the alias an API lookup searches
a dead project and reports "never ran in range" while the live ` - Client Facing` twin
holds thousands. Both are searched, and the newest run wins.

Output lands in `target/case-lookup/` as `case-lookup.csv`, which opens directly in a
spreadsheet, plus `case-lookup.html` for the Extent view. The console also prints the
same table tab separated, for pasting straight into a sheet without downloading a file.

App, web and panel cases map one TestRail id to one method. API cases do not: one method
covers every scenario in a TestRail section, so passing one API id expands to its whole
section and the report groups those scenarios under the method. API runs come from the
`TIKET-API-AUTOMATION` Klov instance (`klov*Api*` keys in `config.properties`), which is
separate from the app and web instances.

A "never ran in range" result and a Klov that would not answer are reported differently
(`Klov unreachable`, `Klov query timed out`), so an empty report is never mistaken for a
case that has genuinely gone stale.

Jenkins freestyle job `TestRail-Case-Run-Lookup`, with `TESTRAIL_IDS`, `ENV`, `LAST_N`,
`DATE_FROM` and `DATE_TO` as build params:

```
mvn -q -e compile exec:java \
  -Dids="${TESTRAIL_IDS}" \
  -Denv="${ENV}" \
  -Dlastn="${LAST_N}" \
  -Dfrom="${DATE_FROM}" \
  -Dto="${DATE_TO}" \
  -Dsuiteid="${SUITE_ID}" \
  -Dgroupid="${GROUP_ID}"
```

Blank params are ignored, so leaving `DATE_FROM` and `DATE_TO` empty falls back to
`LAST_N` days.

Archive `target/case-lookup/**` and publish `case-lookup.html` from the
`target/case-lookup` directory. Both the archive pattern and the publisher have to keep
the directory intact, because the report loads its stylesheet and script from a `spark/`
subfolder next to the HTML. Copying or archiving the `.html` on its own, or flattening
the tree, leaves those assets behind and the report renders as unstyled plain text.

If the job's HTML publisher is pinned to the framework's `extent-reports/extent-report.html`
path, move the whole directory rather than the one file:

```
mkdir -p extent-reports/spark
cp -R target/case-lookup/spark/. extent-reports/spark/
cp target/case-lookup/case-lookup.html extent-reports/extent-report.html
```

### Additional Notes
* Android code coverage emulator UDIDs needs to be synced between Jenkins job params and testdata capabilities

