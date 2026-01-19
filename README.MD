# SauceDemo - Playwright Java Cucumber ExtentReport Framework

This repository contains a small test automation framework using Playwright Java,
Cucumber, RestAssured (API), and Extent reports. The tests exercise SauceDemo UI
flows and a DummyJSON API.

**Repository snapshot / download**
- ZIP of the repository with the added documentation and comments: [playwright-completed.zip](playwright-completed.zip)

**Prerequisites**
- JDK 11 or later installed and `JAVA_HOME` configured
- Maven 3.6+ installed and on your PATH
- Node.js (for Playwright browser installer) recommended to run `npx playwright install` manually
- Internet access to download Maven dependencies and Playwright browsers the first time

**Install / prepare**
1. Clone the repo

```bash
git clone <repo-url>
cd playwright
```

2. (Optional) Install Playwright browsers locally to avoid first-run download prompts

```bash
npx playwright install
```

3. Ensure Maven dependencies are downloaded by running a build

```bash
mvn -q -DskipTests dependency:resolve
```

**Execute tests**

- Run all tests (headless by default unless you override):

```powershell
mvn clean test
```

- Run tests with headed Chromium (visible browser):

```powershell
mvn clean test -Dheadless=false
```

- Run only Cucumber features with tags (example: @ui):

```powershell
mvn test -Dtest=TestRunner -Dcucumber.options="--tags @ui"
```

**Where to find results**
- Extent report HTML: `target/extent-report/index.html` (also contains screenshots under `target/extent-report/screenshots`)
- Surefire JUnit XML: `target/surefire-reports`
- Console log: `maven_test_run.log` (if you captured output)

**What I changed and why**
- Added Javadoc-style comments to core classes (`hooks`, `pages`, `steps`, `api`) to make the framework easier to read and maintain.
- Kept changes minimal and non-invasive — no behavior changes, just documentation.
- The framework prefers JVM system properties for run-time overrides (e.g. `-Dheadless`) so you can choose headed/headless runs from the Maven command line.

**Proof of execution (what I ran here)**
- I executed the test run locally in this workspace and captured logs and generated the Extent report under `target/extent-report`.
- Files produced during the run (examples):
  - `target/extent-report/index.html`
  - `target/extent-report/screenshots/*` (if screenshots were taken)
  - `target/surefire-reports/` (JUnit / Cucumber outputs)

Note: In this execution environment I can run Maven and provide the console log output and the generated report files, but I cannot attach a visual screenshot of your desktop. You can open `target/extent-report/index.html` locally after running the tests to view the same report.

**Short rationale for the approach**
- Page objects (LoginPage, ProductsPage, CartPage) keep UI interactions encapsulated so steps read as business behavior — this improves readability and reduces duplication.
- Hooks centralize Playwright lifecycle management and reporting integration so resource setup/teardown and attachments are handled consistently.
- Using system properties for runtime flags (like `headless`) makes CI and local runs flexible without changing source files.
