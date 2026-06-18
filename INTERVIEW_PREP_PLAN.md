# Java + Selenium Interview Prep Plan — Service Virtualization → Automation Test Lead

> A stage-wise, self-paced preparation roadmap to refresh **Core Java + Selenium**, rebuild
> **framework/automation depth**, and layer in the **leadership, strategy, and stakeholder** skills
> that distinguish a **Test Lead / Automation Lead** from an SDET. Built specifically for someone
> with a strong **Service Virtualization (SV)** background pivoting into automation leadership.
>
> **How to use:** Work the stages in order — each builds on the previous. Tick the checklists, do
> the practice questions out loud (interview muscle), and finish each stage with its "Prove it"
> deliverable. The default cadence is **6 weeks**; compress to 3 by doubling daily hours, or stretch
> to 10 for evenings-only. A day-before quick-revision sheet is at the end.
>
> Companion docs: [`INTERVIEW_QUESTIONS.md`](INTERVIEW_QUESTIONS.md) (framework-level Playwright Q&A),
> [`PLAYWRIGHT_GUIDE.md`](PLAYWRIGHT_GUIDE.md) (Playwright tutorial), [`FRAMEWORK_GUIDE.md`](FRAMEWORK_GUIDE.md).

---

## Table of Contents

1. [Your Positioning: SV → Automation Test Lead](#1-your-positioning-sv--automation-test-lead)
2. [The 6-Week Plan at a Glance](#2-the-6-week-plan-at-a-glance)
3. [Stage 0 — Setup & Baseline (Day 0)](#stage-0--setup--baseline-day-0)
4. [Stage 1 — Core Java Refresh](#stage-1--core-java-refresh)
5. [Stage 2 — Java 8+ & Coding Fluency](#stage-2--java-8-coding-fluency)
6. [Stage 3 — Selenium WebDriver Deep Dive](#stage-3--selenium-webdriver-deep-dive)
7. [Stage 4 — TestNG / JUnit & Assertions](#stage-4--testng--junit--assertions)
8. [Stage 5 — Framework Design & Architecture](#stage-5--framework-design--architecture)
9. [Stage 6 — API Automation + Leveraging Your SV Edge](#stage-6--api-automation--leveraging-your-sv-edge)
10. [Stage 7 — Build, CI/CD, Git, Reporting](#stage-7--build-cicd-git-reporting)
11. [Stage 8 — BDD / Cucumber](#stage-8--bdd--cucumber)
12. [Stage 9 — Test Lead: Strategy, Process & People](#stage-9--test-lead-strategy-process--people)
13. [Stage 10 — Behavioral & Leadership (STAR)](#stage-10--behavioral--leadership-star)
14. [Stage 11 — Mock Interviews & Final Polish](#stage-11--mock-interviews--final-polish)
15. [Master Topic Checklist](#15-master-topic-checklist)
16. [Coding Practice Set](#16-coding-practice-set)
17. [Day-Before Quick-Revision Sheet](#17-day-before-quick-revision-sheet)
18. [Resources](#18-resources)

---

## 1. Your Positioning: SV → Automation Test Lead

You're not a fresher relearning testing — you're an experienced engineer **repositioning**. The
interview narrative matters as much as the technical recall. Lead with this framing:

**Your story (one paragraph to memorize and adapt):**
> "I've spent my career ensuring teams can test reliably even when downstream systems aren't
> available — building and managing virtual services so functional, performance, and integration
> testing never block on third-party or unstable dependencies. That gave me deep grounding in
> APIs, protocols, test data, and environment strategy. I'm now consolidating that with hands-on
> Java/Selenium automation and framework ownership, and I want to lead automation — setting test
> strategy, building the framework, mentoring engineers, and owning quality end-to-end."

**Why your SV background is a leadership asset (use these in answers):**

| SV skill | How it translates to Automation Lead value |
|---|---|
| Building virtual services (CA DevTest/Parasoft Virtualize/WireMock) | You understand **test environment & dependency strategy** — a top reason automation suites are flaky. |
| Request/response modelling, protocols (HTTP, SOAP, JMS, MQ) | Strong **API testing** foundation → RestAssured/Playwright API is a small step. |
| Stateful virtualization, data-driven responses | Maps directly to **test data management** and **mocking/stubbing** in automation. |
| Decoupling teams from unstable dependencies | A **strategy/architecture** mindset — exactly what leads do. |
| Performance/shift-left enablement | Lets you speak to **CI/CD, early testing, and quality gates**. |

**Gaps to close honestly (and what this plan does about it):** hands-on Java fluency (Stages 1–2),
Selenium internals & framework design (Stages 3–5), and the leadership/process vocabulary of a lead
(Stages 9–10). Name these proactively in interviews — owning a gap reads as senior; hiding it reads
as junior.

**Roles you're targeting & what each weights:**
- **Automation Lead / SDET Lead** → 50% hands-on Java+Selenium+framework, 30% design/CI, 20% leadership.
- **Test Lead / QA Lead** → 30% technical, 40% strategy/process/metrics, 30% people/stakeholders.
- **Test Architect** → heavy design/tooling, lighter people management.

Find out which flavour you're interviewing for and shift emphasis accordingly.

---

## 2. The 6-Week Plan at a Glance

| Week | Stages | Theme | "Prove it" deliverable |
|---|---|---|---|
| **1** | 0, 1 | Core Java foundations | A small Java program using OOP + collections + exceptions, explained aloud |
| **2** | 2, 3 | Java 8 streams + Selenium core | 5 Selenium scripts (waits, frames, windows, actions, dropdowns) |
| **3** | 3, 4 | Selenium advanced + TestNG | A TestNG suite with data providers, groups, parallel run |
| **4** | 5, 6 | Framework design + API/SV | A POM + data-driven framework skeleton + 1 RestAssured test |
| **5** | 7, 8, 9 | CI/CD + BDD + Lead strategy | Jenkins/GitHub Actions pipeline running the suite; a 1-page test strategy |
| **6** | 10, 11 | Behavioral + mock interviews | 3 recorded mock interviews; STAR story bank of 8 stories |

**Daily rhythm (≈2.5–3 hrs):** 60 min concept refresh → 60 min hands-on coding → 30 min answer
practice (say answers out loud) → 15 min log what you learned. **Spaced repetition:** re-skim the
prior stage's checklist every Monday.

---

## Stage 0 — Setup & Baseline (Day 0)

**Goal:** Remove all friction so you code, not configure.

- [ ] Install **JDK 17+**, verify `java -version`, `javac -version`.
- [ ] Install **IntelliJ IDEA** (Community is fine) — learn run/debug, breakpoints, evaluate-expression.
- [ ] Install **Maven**; understand `pom.xml`, `mvn clean test`.
- [ ] Create a scratch repo `interview-practice` with Selenium + TestNG + RestAssured deps.
- [ ] Bookmark this plan + the two companion docs in this repo.
- [ ] **Baseline self-test (30 min, honest):** Without notes, write (a) a class with constructor +
      2 overloaded methods, (b) iterate a `HashMap`, (c) a simple Selenium login script. Score each
      0–3. Your lowest scores tell you where to spend extra time.

**Prove it:** Repo compiles and runs one passing dummy test.

---

## Stage 1 — Core Java Refresh

**Goal:** Be able to *write and explain* idiomatic Java under interview pressure.

### Topics
- **OOP pillars:** Encapsulation, Inheritance, Polymorphism (compile-time/overloading vs runtime/overriding), Abstraction.
- **Class design:** constructors, `this`/`super`, access modifiers, `static` vs instance, `final`.
- **Abstract class vs interface** (and Java 8 default/static methods in interfaces).
- **Strings:** immutability, `String` vs `StringBuilder` vs `StringBuffer`, string pool, `==` vs `.equals()`.
- **Collections framework:** `List`/`Set`/`Map` (`ArrayList`, `LinkedList`, `HashSet`, `TreeSet`, `HashMap`, `LinkedHashMap`, `TreeMap`), when to use which, `Iterator`.
- **Exceptions:** checked vs unchecked, `try/catch/finally`, `try-with-resources`, custom exceptions, `throw` vs `throws`.
- **Generics, enums, `equals()`/`hashCode()` contract, autoboxing.**
- **Wrapper classes, `var`, varargs.**

### Must-be-able-to-answer
1. Difference between **abstraction and encapsulation** (and a real example from your test framework).
2. **Overloading vs overriding** — rules, and what `@Override` does.
3. Why is **`String` immutable** and why does that matter for security/caching?
4. **`ArrayList` vs `LinkedList`** — internal structure, when each wins.
5. **`HashMap` internals** — bucketing, hashCode/equals, collision handling, load factor (Java 8 treeification).
6. **`HashSet` vs `TreeSet` vs `LinkedHashSet`.**
7. Checked vs unchecked exception — give one of each; can `finally` override a return?
8. **`equals()`/`hashCode()` contract** — what breaks if you override one but not the other?
9. `final` vs `finally` vs `finalize`.
10. What is the **`static` keyword** — variables, methods, blocks, when used in test frameworks (e.g. WebDriver factory).

### Coding drills
- Reverse a string without library reverse.
- Count character frequency using a `HashMap`.
- Find duplicates in a list; remove them.
- Swap two numbers without a temp variable.
- Implement a simple `Employee` class with proper `equals`/`hashCode`/`toString`.

**Prove it:** Explain HashMap internals aloud in <2 minutes, and code the char-frequency problem unaided.

---

## Stage 2 — Java 8+ & Coding Fluency

**Goal:** Modern Java that interviewers now expect, plus problem-solving speed.

### Topics
- **Lambda expressions** & functional interfaces (`Predicate`, `Function`, `Consumer`, `Supplier`, `BiFunction`, `Comparator`).
- **Streams API:** `filter`, `map`, `flatMap`, `collect`, `reduce`, `sorted`, `distinct`, `count`, `groupingBy`, `Collectors`.
- **`Optional`** — purpose, `orElse`/`orElseGet`/`ifPresent`, avoiding null.
- **Method references** (`Class::method`), **default & static interface methods**.
- **`forEach`, `Stream.of`, `IntStream`.**
- (Awareness) records, `switch` expressions, `var`, text blocks (used in this repo's Playwright tests).

### Must-be-able-to-answer
1. What is a **functional interface**? Name the built-in ones and a use for each.
2. **Intermediate vs terminal** stream operations; is a stream lazy?
3. Use streams to: filter even numbers, get distinct sorted, group strings by length.
4. `map` vs `flatMap`.
5. Why `Optional` — and why `Optional.get()` is a code smell.
6. `Collectors.groupingBy` / `Collectors.toMap` example.

### Coding drills (do these with streams *and* plain loops)
- From a list of names, return those starting with "A", uppercased, sorted.
- Given `List<Integer>`, sum of squares of even numbers.
- Find the first non-repeating character in a string.
- Group a list of words by their first letter.
- Second-highest number in an array.

**Prove it:** Solve any of the drills in under 5 minutes, then refactor loop→stream live.

---

## Stage 3 — Selenium WebDriver Deep Dive

**Goal:** Know Selenium cold — architecture, every interaction type, and the *why* behind waits.

### Topics
- **Selenium architecture:** client bindings → JSON Wire / **W3C protocol** → browser driver → browser. What changed in **Selenium 4** (W3C standard, relative locators, CDP, `WebDriverManager`/Selenium Manager auto-driver).
- **WebDriver / WebElement** interfaces; `driver` instantiation.
- **Locators:** id, name, className, tagName, linkText, partialLinkText, **CSS selectors**, **XPath** (absolute vs relative, axes, `contains()`, `text()`, `following`, `ancestor`). When CSS beats XPath.
- **Selenium 4 relative locators:** `above`, `below`, `toLeftOf`, `toRightOf`, `near`.
- **Waits:** implicit vs explicit (`WebDriverWait` + `ExpectedConditions`) vs **Fluent wait**; why mixing implicit + explicit is an anti-pattern. `Thread.sleep` and why to avoid it.
- **Handling:** dropdowns (`Select`), **alerts** (`switchTo().alert()`), **frames/iframes**, **multiple windows/tabs** (`getWindowHandles`), **Actions** class (hover, drag-drop, right-click, double-click, keyboard).
- **JavascriptExecutor** (scroll, click, value injection — and when it's a smell).
- **Screenshots** (`TakesScreenshot`), **navigation** (`get`/`navigate`), cookies, `getText`/`getAttribute`.
- **StaleElementReferenceException, NoSuchElementException, TimeoutException, ElementClickInterceptedException** — causes & fixes.
- **Headless** execution, browser options/capabilities (`ChromeOptions`).

### Must-be-able-to-answer
1. Draw/describe **Selenium 4 architecture**; what is the **W3C protocol** and why did it replace JSON Wire?
2. **Implicit vs explicit vs fluent wait** — code each; why never mix implicit + explicit?
3. Write an **XPath** for "the price cell in the row whose first column says NIFTY."
4. **CSS vs XPath** — speed, readability, limitations (XPath can traverse up; CSS can't).
5. How do you handle a **dynamic element** whose id changes each load?
6. **Switch to an iframe** and back; handle a **new tab** opened on click.
7. What causes a **StaleElementReferenceException** and how do you fix it robustly?
8. How do you click an element that "isn't clickable / is intercepted"? (scroll into view, wait, JS click as last resort)
9. Difference between `driver.close()` and `driver.quit()`.
10. `findElement` vs `findElements` (and what each does when nothing matches).
11. How does **Selenium Manager** (Selenium 4.6+) remove the need for `WebDriverManager`?
12. How do you run **headless** and why for CI?

### Hands-on scripts to write (one each)
- Login form with explicit waits.
- Select from a native dropdown + a custom (div-based) dropdown.
- Handle a JS alert (accept/dismiss/sendKeys).
- Switch into an iframe, act, switch out.
- Open a link in a new tab, switch, assert title, close, switch back.
- Mouse hover menu → click submenu (Actions).
- Capture a screenshot on a step.
- Scroll to footer with JavascriptExecutor; infinite-scroll page.

**Prove it:** Explain why mixing implicit+explicit waits is dangerous, and write a robust explicit wait from memory.

---

## Stage 4 — TestNG / JUnit & Assertions

**Goal:** Own the test-runner layer interviewers probe hard.

### Topics (TestNG-first; know JUnit 5 differences)
- **Annotations & order:** `@BeforeSuite/@BeforeTest/@BeforeClass/@BeforeMethod` → `@Test` → the `@After*` mirror. Execution order across the hierarchy.
- **`@Test` attributes:** `priority`, `groups`, `dependsOnMethods`, `enabled`, `invocationCount`, `timeOut`, `expectedExceptions`, `dataProvider`.
- **Assertions:** hard (`Assert`) vs **soft** (`SoftAssert`) — when each; `assertEquals/assertTrue/assertNotNull`.
- **Data-driven:** `@DataProvider` (returns `Object[][]`), parameterising from Excel/CSV/JSON.
- **`@Parameters`** + `testng.xml` for config-driven inputs.
- **Grouping & suites:** `testng.xml`, `<groups>`, `<include>/<exclude>`, suite ordering.
- **Parallel execution:** `parallel="methods|tests|classes"`, `thread-count`, thread-safety with `ThreadLocal<WebDriver>`.
- **Listeners:** `ITestListener`, `IRetryAnalyzer` (re-running flaky tests), `ISuiteListener`.
- **Dependencies & skip:** `dependsOnMethods`, `SkipException`.
- JUnit 5 equivalents (`@BeforeEach`, `@BeforeAll`, `@ParameterizedTest`, `Assertions`).

### Must-be-able-to-answer
1. **TestNG annotation execution order** — recite it.
2. **Hard vs soft assertion** — give a scenario for each (soft: validate 5 fields on a form in one run).
3. How does **`@DataProvider`** work; how do you feed it from Excel?
4. How do you **run tests in parallel** and keep WebDriver thread-safe? (`ThreadLocal`)
5. How do you **retry failed tests** automatically? (`IRetryAnalyzer`)
6. **`priority` vs `dependsOnMethods`** — difference.
7. How do you group smoke vs regression and run only smoke in CI?
8. TestNG vs JUnit — when would you pick which?

**Prove it:** Build a 6-test TestNG class with a data provider, two groups, and a retry analyzer; run smoke-only via `testng.xml`.

---

## Stage 5 — Framework Design & Architecture

**Goal:** Talk about frameworks like an architect — this is where leads are made or broken.

### Topics
- **Page Object Model (POM)** — why; separating locators/actions (pages) from assertions (tests).
- **PageFactory** (`@FindBy`, `initElements`) — and why many teams now prefer plain `By` locators.
- **Framework types:** Linear → Modular → **Data-Driven** → Keyword-Driven → **Hybrid** → **BDD**. Pros/cons of each.
- **Layered design:** base/driver factory, config, page objects, tests, utils, reporting, test data.
- **Driver management:** factory pattern, `ThreadLocal<WebDriver>` for parallel safety.
- **Config management:** properties files, system properties (`-Dbrowser=`), environment switching (dev/qa/prod) — *you've done this for SV environments, draw the parallel*.
- **Test data management:** external files (Excel/CSV/JSON/DB), data builders, **and virtualization/mocking** (your edge).
- **Utilities & reusability:** waits wrapper, screenshot helper, JSON/Excel readers (Apache POI), logging.
- **Reporting:** Extent Reports, Allure (this repo uses Allure — study it), TestNG default reports, screenshots on failure.
- **Logging:** Log4j2 / SLF4J + Logback; meaningful log levels.
- **Exception handling & reliability:** retries, soft-fail for external deps, explicit-wait wrappers.
- **Design patterns in automation:** Singleton (driver/config), Factory (driver/page), **Page Object**, Builder (test data), Strategy (browser selection), Fluent interfaces.
- **SOLID principles** applied to a test framework.
- **Maven project structure** (`src/test/java`, resources), dependency management.

### Must-be-able-to-answer
1. **Explain POM** and what problem it solves. POM vs PageFactory.
2. **Design a Selenium framework from scratch** — walk through the layers, folder structure, and key classes (rehearse a 5-minute whiteboard version).
3. How do you make the framework **support multiple browsers and environments**?
4. How do you achieve **thread-safe parallel execution**? (`ThreadLocal` driver)
5. Which **design patterns** have you used and where?
6. How do you manage **test data**? (and mention SV/mocking for unstable deps — your differentiator)
7. How do you keep tests **independent and atomic**?
8. How do you handle **flaky tests** at framework level? (waits strategy, retry analyzer, isolation, root-causing)
9. How would you decide **what to automate** (ROI, risk-based, test pyramid)?
10. Explain the **test automation pyramid** and where UI/API/unit tests sit. Why UI-heavy suites rot.

### Talking points to memorize (lead-level)
- *"I favour an API-first / pyramid approach — push validation down to API and unit layers where it's
  fast and stable, reserve UI automation for true end-to-end journeys. My SV experience makes the API
  and dependency layer second nature."*
- *"Flakiness is usually environment or synchronization, not the test. I attack it with explicit-wait
  wrappers, isolated test data, dependency virtualization, and a retry-with-root-cause policy — not by
  blindly re-running."*

**Prove it:** Build a minimal POM framework skeleton (BasePage, BaseTest, DriverFactory with ThreadLocal, one page, one data-driven test, config properties, Extent/Allure report). Be ready to whiteboard it in 5 minutes.

---

## Stage 6 — API Automation + Leveraging Your SV Edge

**Goal:** Turn your strongest existing skill into interview gold.

### Topics
- **HTTP fundamentals:** methods (GET/POST/PUT/PATCH/DELETE), status codes (2xx/3xx/4xx/5xx), headers, query vs path vs body params, content types, auth (Basic, Bearer/JWT, OAuth2, API key).
- **REST vs SOAP**, idempotency, statelessness.
- **RestAssured (Java):** `given().when().then()`, request/response specs, body/header/status assertions, **JSON path / `jsonPath()`**, schema validation, serialization/deserialization (POJO ↔ JSON with Jackson/Gson).
- **API test design:** positive/negative, boundary, contract testing, data-driven API tests.
- **Service Virtualization (your home turf):** when to virtualize vs use a real service, stateful vs stateless virtual services, request matching, response templating, **WireMock** (open-source) basics to show breadth beyond commercial tools (CA DevTest / Parasoft Virtualize).
- **Mocking vs stubbing vs virtualization** — be crisp on the distinctions.
- Awareness: Postman/Newman, Karate, Pact (consumer-driven contract testing).

### Must-be-able-to-answer
1. Write a **RestAssured** test: POST a login, assert 200 + extract a token, use it in the next call.
2. Validate a **nested JSON** field with `jsonPath`.
3. **REST vs SOAP**; when SOAP still appears (enterprise/legacy/banking — likely in your SV past).
4. How do you **test an API whose downstream dependency isn't ready**? → *virtualize it* (your SV story).
5. **Mock vs stub vs service virtualization** — define each.
6. **Stateful vs stateless** virtual service — give an example (e.g. an account balance that changes after a debit).
7. How does **contract testing (Pact)** prevent integration breakage in microservices?
8. How do you integrate **API tests into CI** as a fast quality gate before UI tests?

### Your differentiator pitch
> *"On my last programme we couldn't get reliable access to a payments gateway and a partner KYC
> service. I built virtual services that modelled their request/response behaviour — including
> error and latency scenarios — so the team ran full regression independently and shifted testing
> left. That's the same instinct I bring to automation: make tests fast, deterministic, and
> independent of fragile environments."*

**Prove it:** A RestAssured test that chains auth → action → assert, plus a 2-minute explanation of when you'd virtualize vs hit the real service.

---

## Stage 7 — Build, CI/CD, Git, Reporting

**Goal:** Show you can operationalize automation, not just write scripts.

### Topics
- **Maven:** lifecycle (`validate→compile→test→package→install→deploy`), `pom.xml`, dependencies, Surefire plugin, profiles, running suites. (Gradle awareness.)
- **Git:** clone/branch/commit/merge/rebase, pull requests, merge conflicts, `.gitignore`, branching strategies (GitFlow/trunk-based), code review etiquette.
- **CI/CD:** Jenkins (jobs, pipelines, `Jenkinsfile`, parameterized builds, scheduling, publishing reports) **and** GitHub Actions (workflows, triggers, matrix — see `.github/workflows/` in this repo). Concept of quality gates.
- **Running automation in CI:** headless, parallel, browsers in Docker/Selenium Grid, artifacts (reports, screenshots, logs).
- **Selenium Grid / cloud:** Grid 4 hub-node, **Selenium Grid in Docker**, cloud vendors (BrowserStack/Sauce Labs/LambdaTest) — cross-browser at scale.
- **Reporting integration:** publishing Allure/Extent in the pipeline; trend history.
- **Dockerization** (awareness): containerizing the test suite.

### Must-be-able-to-answer
1. Walk through the **Maven build lifecycle**; what does Surefire do?
2. How do you **trigger your suite in Jenkins** nightly and on every PR? Parameterize browser/env.
3. **Jenkins vs GitHub Actions** — when each; what's a pipeline-as-code benefit?
4. How do you run **cross-browser tests in parallel** in CI? (Grid/cloud + ThreadLocal)
5. How do you **publish and trend reports** in CI?
6. What's your **branching strategy** and PR review process as a lead?
7. How do automation suites act as a **quality gate** (fail the build, block merge)?
8. (From this repo) Explain a GitHub Actions workflow: triggers, jobs, matrix, artifacts.

**Prove it:** A GitHub Actions (or Jenkins) pipeline that runs your TestNG suite headless and publishes a report. Reference this repo's `.github/workflows/playwright-tests.yml` as a model.

---

## Stage 8 — BDD / Cucumber

**Goal:** Many lead roles expect BDD fluency for business-readable automation.

### Topics
- **Gherkin:** `Feature`, `Scenario`, `Scenario Outline` + `Examples`, `Given/When/Then/And/But`, `Background`, tags.
- **Cucumber-JVM:** step definitions, glue, `@CucumberOptions`/runner, hooks (`@Before`/`@After`), data tables, parameter types.
- **BDD process:** Three Amigos, living documentation, when BDD adds value vs adds overhead.
- Integration with Selenium + POM; dependency injection (PicoContainer) for sharing state.

### Must-be-able-to-answer
1. What is **BDD** and what problem does it solve (collaboration, shared understanding)?
2. **Scenario vs Scenario Outline**; how do `Examples` drive data?
3. How do **step definitions** map to Gherkin; what is "glue"?
4. How do you **share state** between steps? (DI / scenario context)
5. When is BDD **overkill**? (be honest — a lead knows when not to use a tool)
6. How do you keep step definitions **reusable** and avoid a step explosion?

**Prove it:** One feature file (with a Scenario Outline) wired to step defs driving a Selenium flow.

---

## Stage 9 — Test Lead: Strategy, Process & People

**Goal:** The 30–40% that decides lead interviews. Speak the language of strategy and delivery.

### A. Test Strategy & Planning
- **Test strategy vs test plan** (strategy = org/programme approach; plan = project-specific).
- **Test approach for a new project:** scope, risk-based testing, automation candidates, environments, data, entry/exit criteria, tooling.
- **Automation strategy:** what to automate (ROI, frequency, stability, risk), the **test pyramid**, framework choice, in-sprint automation.
- **Estimation:** test estimation techniques (work breakdown, three-point/PERT, function points, historical velocity), estimating automation effort.
- **Risk-based testing:** identifying, prioritising, and mitigating quality risks.
- **Shift-left & shift-right**, in-sprint testing, continuous testing.

### B. Process & Metrics
- **SDLC & STLC** phases; **Agile/Scrum/Kanban**, SAFe awareness; role of QA in each ceremony.
- **Defect management:** lifecycle, severity vs priority, triage, RCA, defect leakage/density.
- **Metrics & KPIs:** test coverage, pass/fail %, **automation coverage**, defect leakage, defect density, MTTR, escaped defects, flaky-test rate, requirements traceability (RTM). *Which metrics matter and which are vanity.*
- **Quality gates & Definition of Done.**
- **Test management tools:** Jira/Xray/Zephyr, TestRail, ALM.

### C. People & Stakeholders
- **Team leadership:** mentoring, upskilling juniors, code reviews, pairing, distributing work, onboarding.
- **Stakeholder management:** reporting quality status to non-technical stakeholders, managing expectations, saying "no" with data, go/no-go decisions.
- **Conflict & prioritisation:** competing deadlines, scope cuts, quality vs speed trade-offs.
- **Process improvement:** retrospectives, reducing cycle time, driving automation adoption.
- **Vendor/tool selection:** evaluating tools (incl. SV/automation), POCs, build-vs-buy.

### Must-be-able-to-answer (lead-level — answer with structure + examples)
1. **How would you define a test/automation strategy** for a new product?
2. **What/when/how do you decide to automate** something? (ROI + pyramid + risk)
3. How do you **estimate** an automation effort for a release?
4. Which **QA metrics** do you report to leadership and why? Which do you distrust?
5. **Severity vs priority** — give a high-severity/low-priority and a low-severity/high-priority example.
6. How do you **handle a release with open defects** and a fixed go-live? (risk-based go/no-go)
7. How do you **mentor a struggling automation engineer**?
8. How do you **convince a sceptical PM/dev team to invest in automation** (or SV)?
9. How do you **reduce flaky tests across a team's suite** (process + tech)?
10. How do you **introduce automation/SV into a team that has none**? (your real differentiator)
11. How do you **handle conflict** between dev and QA on quality bar?
12. How do you keep automation **maintainable** as the product scales?

**Prove it:** Write a 1-page **Test Strategy** for a sample app (scope, risks, automation approach, pyramid, environments, data/virtualization, CI, metrics, entry/exit). Reuse the EaseMyTrade app in this repo as the example system.

---

## Stage 10 — Behavioral & Leadership (STAR)

**Goal:** Convert experience into crisp, structured stories.

### Method: **STAR** — Situation, Task, Action, Result (quantify the result).

### Build a story bank (write 8, memorize the arcs, never recite robotically)
Map at least one strong story to each theme — lean on your SV/programme experience:
1. **Led an initiative** (introduced SV/automation, built a framework).
2. **Solved a hard technical problem** (flaky environment, unstable dependency → virtualized it).
3. **Improved a process / metric** (cut regression time, reduced defect leakage — quantify).
4. **Mentored / grew someone.**
5. **Handled conflict / pushback** (convinced a team to adopt a practice).
6. **Dealt with a tight deadline / pressure** (risk-based prioritisation).
7. **A failure / mistake and what you learned** (own it, show growth).
8. **Influenced without authority** (cross-team, stakeholders).

### Common behavioral questions
- "Tell me about yourself" → use the positioning paragraph from Stage 1.
- "Why are you moving from SV into automation leadership?"
- "Describe a time you improved quality/efficiency — with numbers."
- "Tell me about a conflict with a developer/manager."
- "A time you had to make a quality call under pressure."
- "How do you prioritise when everything is urgent?"
- "Your biggest professional failure."
- "Where do you see yourself / why this role?"
- "How do you keep the team motivated and learning?"

### Tips
- **Quantify** every Result ("cut regression from 3 days to 4 hours", "reduced env-related blockers by 60%").
- Use **"I"** for your contribution, **"we"** for team context — be clear which.
- Keep each story to **~90 seconds**; pause for follow-ups.
- For "weakness/failure": pick a real one with a genuine lesson, not a humblebrag.

**Prove it:** 8 written STAR stories, each ≤120 words, rehearsed aloud.

---

## Stage 11 — Mock Interviews & Final Polish

**Goal:** Simulate pressure; fix delivery, not just knowledge.

- [ ] **Mock 1 — Core Java + coding** (60 min): screen-share, code 3 problems aloud, explain HashMap/streams.
- [ ] **Mock 2 — Selenium + framework design** (60 min): whiteboard a framework, answer waits/locators/parallel, debug a flaky-test scenario.
- [ ] **Mock 3 — Lead/behavioral** (45 min): strategy, metrics, people, STAR stories.
- [ ] **Record** each, watch back, note filler words, gaps, where you rambled.
- [ ] Prepare **5 smart questions to ask the interviewer** (team maturity, automation coverage, CI setup, biggest quality pain, how success is measured for this role).
- [ ] Re-read the **company/JD**; map each JD bullet to a story or skill.
- [ ] Tidy your **GitHub** (this repo is a great portfolio piece — the framework + docs show range).

**Self-rating gate (be honest, 1–5 each; aim ≥4):** Core Java · Java 8 streams · Selenium core ·
Waits/sync · TestNG · Framework design · API/RestAssured · SV articulation · CI/CD · BDD ·
Test strategy · Metrics · People/leadership · STAR delivery. Anything <4 → one more focused day.

---

## 15. Master Topic Checklist

**Core Java** — [ ] OOP 4 pillars · [ ] overloading/overriding · [ ] abstract vs interface ·
[ ] String immutability & pool · [ ] StringBuilder/Buffer · [ ] Collections (List/Set/Map) ·
[ ] HashMap internals · [ ] equals/hashCode · [ ] exceptions checked/unchecked · [ ] try-with-resources ·
[ ] generics · [ ] enums · [ ] static/final · [ ] access modifiers · [ ] wrapper/autoboxing.

**Java 8+** — [ ] lambdas · [ ] functional interfaces · [ ] streams (filter/map/collect/reduce/groupingBy) ·
[ ] flatMap · [ ] Optional · [ ] method references · [ ] default methods.

**Selenium** — [ ] architecture/W3C · [ ] Selenium 4 changes · [ ] all locators · [ ] CSS vs XPath ·
[ ] relative locators · [ ] implicit/explicit/fluent waits · [ ] dropdowns · [ ] alerts · [ ] frames ·
[ ] windows/tabs · [ ] Actions · [ ] JSExecutor · [ ] screenshots · [ ] exceptions (stale/intercepted) ·
[ ] headless · [ ] options/capabilities · [ ] Grid.

**TestNG** — [ ] annotation order · [ ] @Test attrs · [ ] hard/soft assert · [ ] DataProvider ·
[ ] parameters/testng.xml · [ ] groups · [ ] parallel + ThreadLocal · [ ] listeners/retry · [ ] dependsOn.

**Framework** — [ ] POM · [ ] PageFactory · [ ] framework types · [ ] driver factory/ThreadLocal ·
[ ] config mgmt · [ ] test data mgmt · [ ] reporting (Allure/Extent) · [ ] logging · [ ] design patterns ·
[ ] SOLID · [ ] pyramid · [ ] flaky-test strategy · [ ] Maven structure.

**API/SV** — [ ] HTTP methods/codes · [ ] auth types · [ ] REST vs SOAP · [ ] RestAssured given/when/then ·
[ ] jsonPath · [ ] POJO ser/deser · [ ] schema validation · [ ] mock vs stub vs virtualization ·
[ ] stateful vs stateless VS · [ ] WireMock · [ ] contract testing.

**CI/CD/Git** — [ ] Maven lifecycle · [ ] Git workflow/branching · [ ] Jenkins pipeline ·
[ ] GitHub Actions · [ ] headless/parallel in CI · [ ] Grid/cloud · [ ] report publishing · [ ] Docker awareness.

**BDD** — [ ] Gherkin · [ ] Scenario Outline · [ ] step defs/glue · [ ] hooks · [ ] state sharing · [ ] when not to use.

**Lead** — [ ] strategy vs plan · [ ] what-to-automate/ROI · [ ] estimation · [ ] risk-based testing ·
[ ] SDLC/STLC/Agile · [ ] defect lifecycle · [ ] severity vs priority · [ ] metrics/KPIs · [ ] RTM ·
[ ] test mgmt tools · [ ] mentoring · [ ] stakeholder mgmt · [ ] conflict · [ ] process improvement · [ ] tool selection.

**Behavioral** — [ ] positioning pitch · [ ] 8 STAR stories · [ ] failure story · [ ] questions to ask.

---

## 16. Coding Practice Set

Do each **on paper or a plain editor first** (no autocomplete), then verify in the IDE. Be ready to
explain time/space complexity.

**Strings**
1. Reverse a string / check palindrome.
2. First non-repeating character.
3. Count word/character frequency (HashMap).
4. Check if two strings are anagrams.
5. Remove duplicate characters preserving order.

**Arrays/Collections**
6. Find duplicates in an array; remove duplicates from a List.
7. Second-largest element (no sort).
8. Find pairs summing to a target.
9. Sort a `Map` by value.
10. Merge two sorted lists.

**Numbers/Logic**
11. Fibonacci (iterative + recursive); factorial.
12. Prime check / primes up to N.
13. Swap without temp; reverse an integer.
14. FizzBuzz (loop + stream).

**Java 8 streams**
15. Filter + map + collect pipelines (names, numbers).
16. `groupingBy` length / first letter.
17. Sum/average/max with streams.
18. Count occurrences with `Collectors.groupingBy(..., counting())`.

**Selenium mini-scenarios (write the script + explain the wait choice)**
19. Robust login with explicit wait.
20. Data-driven login from a `@DataProvider`.
21. Handle dynamic table: read all rows, assert a value.
22. Switch tabs, iframes, handle an alert.
23. Build a 3-page POM flow with a BaseTest + DriverFactory (ThreadLocal).

---

## 17. Day-Before Quick-Revision Sheet

**Java one-liners**
- OOP: Encapsulation (hide state), Inheritance (reuse), Polymorphism (one interface/many forms), Abstraction (hide complexity).
- Overloading = compile-time (same name, diff params); Overriding = runtime (subclass redefines).
- `String` immutable → thread-safe, poolable, secure. Use `StringBuilder` for heavy concatenation.
- `HashMap`: array of buckets, key `hashCode`→bucket, `equals` resolves collisions; treeifies long chains (Java 8); not thread-safe (use `ConcurrentHashMap`).
- Stream ops: intermediate (lazy: `filter/map/sorted`) → terminal (eager: `collect/forEach/reduce/count`).

**Selenium one-liners**
- Architecture: bindings → **W3C protocol** → driver → browser. Selenium 4 = W3C native, relative locators, CDP, Selenium Manager.
- Implicit wait = global, applies to all `findElement`; explicit = per-condition (`WebDriverWait`+`ExpectedConditions`); fluent = explicit + polling + ignored exceptions. **Never mix implicit + explicit.**
- Stale element = DOM re-rendered; re-locate. Intercepted click = overlay/not-in-view; scroll/wait, JS click as last resort.
- `close()` = current window; `quit()` = whole session.
- Prefer **CSS** for speed/readability; **XPath** when you must traverse up or match by text.

**TestNG**
- Order: BeforeSuite→BeforeTest→BeforeClass→BeforeMethod→Test→AfterMethod→AfterClass→AfterTest→AfterSuite.
- Soft assert = collect all failures, `assertAll()` at end. Parallel + `ThreadLocal<WebDriver>` for safety. `IRetryAnalyzer` for flaky retries.

**Framework / Lead**
- POM = locators+actions in pages, assertions in tests → maintainable, reusable.
- Pyramid: many unit, fewer API, fewest UI. Automate by ROI + risk + frequency + stability.
- Severity = impact; Priority = urgency to fix.
- Strategy = how the org tests; Plan = project specifics.
- Flakiness: fix sync (explicit waits), isolate data, virtualize deps, retry-with-RCA — don't blind-rerun.

**Your pitch:** SV background → deep API/dependency/environment mastery → now owning automation
strategy, framework, CI, and team. Lead with the test pyramid and "fast, deterministic, independent
tests."

**Logistics:** test your camera/mic, 5 questions ready, JD mapped to stories, water, calm. You've got this. 💪

---

## 18. Resources

- **Selenium docs:** <https://www.selenium.dev/documentation/>
- **TestNG:** <https://testng.org/doc/>
- **RestAssured:** <https://rest-assured.io/>
- **WireMock (open-source SV):** <https://wiremock.org/>
- **Cucumber JVM:** <https://cucumber.io/docs/cucumber/>
- **Allure:** <https://allurereport.org/docs/> (and this repo's `FRAMEWORK_GUIDE.md`)
- **Java practice:** LeetCode (Easy/Medium), HackerRank Java track.
- **This repo** as a live portfolio reference: `FRAMEWORK_GUIDE.md`, `INTERVIEW_QUESTIONS.md`,
  `PLAYWRIGHT_GUIDE.md`, and the `.github/workflows/` CI examples.

> Selenium and Playwright differ in API, but the *concepts* transfer 1:1 — locators, waits, POM,
> parallel execution, CI, reporting. Use this repo's Playwright framework as concrete proof you can
> design and ship automation; map every concept back to Selenium when the JD asks for it.
