# Automation QA — Interview Questions & Detailed Answers

> A complete, **answer-included** interview Q&A bank for an **Automation QA / Test Lead** profile,
> covering every topic in [`INTERVIEW_PREP_PLAN.md`](INTERVIEW_PREP_PLAN.md): **Core Java, Java 8+,
> Selenium, Playwright, TestNG, Framework Design, API Automation + Service Virtualization, CI/CD &
> Git, BDD, and Test-Lead strategy/process/people** — plus a deep-dive on this repo's real
> Playwright framework.
>
> Every question has a **model answer with a short description** so you can study the *reasoning*,
> not just memorize lines. Read the answer, then say it back in your own words — that's the rep that
> sticks. Answers are written at **lead level**: correct, concise, and with the "why" an interviewer
> is probing for.
>
> Companion docs: [`INTERVIEW_PREP_PLAN.md`](INTERVIEW_PREP_PLAN.md) (the study roadmap),
> [`PLAYWRIGHT_GUIDE.md`](PLAYWRIGHT_GUIDE.md) (Playwright tutorial/cheatsheet),
> [`FRAMEWORK_GUIDE.md`](FRAMEWORK_GUIDE.md) (full framework reference), [`README.md`](README.md).

---

## How to Use This Document

- **Part 1** is topic-wise interview Q&A with answers — maps 1:1 to the prep-plan stages. Use it to
  *learn and rehearse*.
- **Part 2** is a deep-dive on the **EaseMyTrade Playwright framework** in this repo — use it as a
  concrete portfolio talking point ("here's a real framework I can walk you through").
- Symbols in answers: 💡 = the key insight an interviewer wants; ⚠️ = a common trap/anti-pattern;
  🗣️ = a ready-to-say one-liner.

---

## Table of Contents

### Part 1 — Topic-Wise Interview Q&A (with model answers)
- [A. Core Java](#a-core-java)
- [B. Java 8+ & Coding Fluency](#b-java-8--coding-fluency)
- [C. Selenium WebDriver](#c-selenium-webdriver)
- [D. Playwright (Concepts + Selenium Comparison)](#d-playwright-concepts--selenium-comparison)
- [E. TestNG / JUnit](#e-testng--junit)
- [F. Framework Design & Architecture](#f-framework-design--architecture)
- [G. API Automation + Service Virtualization](#g-api-automation--service-virtualization)
- [H. CI/CD, Git, Build & Reporting](#h-cicd-git-build--reporting)
- [I. BDD / Cucumber](#i-bdd--cucumber)
- [J. Test Lead — Strategy, Process & People](#j-test-lead--strategy-process--people)
- [K. Behavioral & Leadership (STAR)](#k-behavioral--leadership-star)

### Part 2 — EaseMyTrade Playwright Framework Deep-Dive
1. [Stack & Tooling](#1-stack--tooling)
2. [Architecture & Design](#2-architecture--design)
3. [The Page Object Model Layer](#3-the-page-object-model-layer)
4. [Configuration & Environment](#4-configuration--environment)
5. [Test Lifecycle — How a Test Boots and Runs (the Flow)](#5-test-lifecycle--how-a-test-boots-and-runs-the-flow)
6. [Playwright Mechanics in This Framework](#6-playwright-mechanics-in-this-framework)
7. [Authentication & Protected Pages](#7-authentication--protected-pages)
8. [Reliability — Retries, Waits, Soft-Fail & Skips](#8-reliability--retries-waits-soft-fail--skips)
9. [API-Level Testing](#9-api-level-testing)
10. [Signal Logic — Business Validation](#10-signal-logic--business-validation)
11. [Data-Driven Testing](#11-data-driven-testing)
12. [Reporting — Allure](#12-reporting--allure)
13. [Logging](#13-logging)
14. [CI/CD — GitHub Actions](#14-cicd--github-actions)
15. [Scenario / "What Would You Do" Questions](#15-scenario--what-would-you-do-questions)

---

# Part 1 — Topic-Wise Interview Q&A

## A. Core Java

**A1. Explain the four pillars of OOP with a testing example.**
- **Encapsulation** — bundling data with the methods that act on it and hiding internal state behind
  accessors. *Example:* a `LoginPage` page object hides its locators (`private By usernameField`) and
  exposes behavior (`login(user, pass)`).
- **Inheritance** — a subclass reuses/extends a parent. *Example:* every page extends `BasePage`;
  every test extends `BaseTest`.
- **Polymorphism** — one interface, many implementations. *Compile-time* = method overloading;
  *runtime* = overriding (`WebDriver driver = new ChromeDriver()` — the `driver.get()` call is
  resolved at runtime).
- **Abstraction** — exposing *what* something does, hiding *how*. *Example:* `WebDriver` is an
  interface; tests code to it, not to `ChromeDriver` internals.
- 💡 Interviewers want a concrete framework example, not textbook definitions.

**A2. Difference between abstraction and encapsulation?**
Abstraction is about **design** — hiding complexity behind a simple interface (the *what*).
Encapsulation is about **implementation** — hiding data and restricting access via modifiers (the
*how*). Abstraction is achieved with interfaces/abstract classes; encapsulation with `private`
fields + getters/setters. 🗣️ *"Abstraction hides complexity; encapsulation hides data."*

**A3. Overloading vs overriding.**
- **Overloading** (compile-time polymorphism): same method name, **different parameter list**, same
  class (or inherited). Return type alone can't distinguish overloads.
- **Overriding** (runtime polymorphism): subclass provides a **new implementation** of a parent
  method with the **same signature**. `@Override` enforces the contract at compile time.
- ⚠️ Trap: overriding requires same signature *and* covariant-or-same return type; overloading is
  resolved by the compiler from the argument types.

**A4. Abstract class vs interface (Java 8+).**
| | Abstract class | Interface |
|---|---|---|
| Instantiable | No | No |
| Methods | abstract + concrete | abstract + `default` + `static` (Java 8), `private` (Java 9) |
| Fields | instance fields allowed | only `public static final` constants |
| Inheritance | single (`extends`) | multiple (`implements`) |
| Use when | shared state + base behavior (e.g. `BasePage`) | a capability/contract (e.g. `WebDriver`) |
💡 Since Java 8, interfaces can have `default` methods — used heavily for backward-compatible API
evolution.

**A5. Why is `String` immutable, and why does it matter?**
A `String`'s value can't change after creation. Benefits: **thread-safety** (shareable without
locks), **string-pool reuse** (memory efficiency), **safe as HashMap keys** (hashcode is stable),
and **security** (a path/connection string can't be mutated after a security check). ⚠️ Heavy
concatenation in a loop creates many objects — use `StringBuilder` (or `StringBuffer` if you need
thread-safety).

**A6. `String` vs `StringBuilder` vs `StringBuffer`.**
`String` = immutable. `StringBuilder` = mutable, **not** thread-safe, **fast** — default for local
string building. `StringBuffer` = mutable, **synchronized**/thread-safe, slightly slower — only when
shared across threads.

**A7. `==` vs `.equals()`.**
`==` compares **references** (same object in memory); `.equals()` compares **logical equality** (as
overridden). For `String`, `==` may be `true` for pooled literals but `false` for `new String(...)`;
always use `.equals()` for content comparison. 🗣️ *"`==` is identity, `equals()` is equality."*

**A8. Explain the Collections framework — List vs Set vs Map.**
- **List** — ordered, allows duplicates, index access. `ArrayList` (array-backed, fast random
  access), `LinkedList` (node-backed, fast insert/delete).
- **Set** — no duplicates. `HashSet` (unordered, O(1)), `LinkedHashSet` (insertion order),
  `TreeSet` (sorted, O(log n)).
- **Map** — key→value. `HashMap` (unordered), `LinkedHashMap` (insertion order), `TreeMap` (sorted
  by key), `ConcurrentHashMap` (thread-safe).
- 💡 Pick by need: random access → `ArrayList`; uniqueness → `HashSet`; lookups → `HashMap`; sorted
  → `Tree*`.

**A9. ArrayList vs LinkedList — internals and when each wins.**
`ArrayList` is backed by a dynamic array: O(1) random access, but insert/delete in the middle is
O(n) (shifting) and it resizes (~1.5×) when full. `LinkedList` is a doubly-linked list: O(1)
insert/delete at ends/known nodes, but O(n) access by index. **Use `ArrayList`** for read-heavy
workloads (the common case); `LinkedList` only for frequent head/tail mutations (queue/deque).

**A10. How does HashMap work internally?**
A `HashMap` is an **array of buckets**. On `put(k,v)`: compute `k.hashCode()`, spread it, map to a
bucket index. Collisions (same bucket) form a **linked list**; since Java 8, a bucket with >8
entries **treeifies** into a red-black tree (O(log n) instead of O(n)). `equals()` resolves which
entry within a bucket. Default capacity 16, **load factor 0.75** → resizes (doubles + rehashes) when
75% full. ⚠️ A bad/constant `hashCode()` degrades it to O(n). Not thread-safe → use
`ConcurrentHashMap`.

**A11. The equals()/hashCode() contract — what breaks if you violate it?**
Rule: if `a.equals(b)` then `a.hashCode() == b.hashCode()` (the reverse need not hold). If you
override `equals()` but not `hashCode()`, two "equal" objects may land in different buckets, so a
`HashMap`/`HashSet` lookup **fails to find** an object you just stored. Always override both
together.

**A12. Checked vs unchecked exceptions.**
- **Checked** (extend `Exception`): must be declared or caught; for recoverable conditions
  (`IOException`, `SQLException`).
- **Unchecked** (extend `RuntimeException`): not enforced by the compiler; for programming errors
  (`NullPointerException`, `IllegalArgumentException`).
- **Error** (`OutOfMemoryError`): JVM-level, don't catch.
- 💡 Selenium's `NoSuchElementException`, `TimeoutException`, `StaleElementReferenceException` are
  all **unchecked** (`RuntimeException`s).

**A13. try / catch / finally / try-with-resources — and can finally override a return?**
`finally` always runs (even after a `return`/exception) — used for cleanup. ⚠️ A `return` *inside*
`finally` will override the `try`'s return (an anti-pattern — avoid it). **try-with-resources**
(`try (var r = ...)`) auto-closes anything implementing `AutoCloseable` (drivers, streams, API
contexts), removing manual `finally` close blocks.

**A14. `final` vs `finally` vs `finalize`.**
`final` = a modifier (constant variable, non-overridable method, non-extendable class). `finally` =
the always-executed block. `finalize()` = a deprecated `Object` method the GC *used* to call before
reclaiming an object — don't rely on it.

**A15. What is `static`, and where is it used in frameworks?**
`static` members belong to the **class**, not an instance — shared across all instances, accessible
without an object. Used for: utility methods (`SignalParser.parsePrice`), constants
(`TestConfig.BASE_URL`), a shared `WebDriver`/`Playwright` instance, and `static` blocks for one-time
init. ⚠️ A `static` mutable WebDriver breaks parallel tests — use `ThreadLocal`.

**A16. Generics — what problem do they solve?**
Compile-time **type safety** and elimination of casts. `List<String>` guarantees only `String`s go
in and come out, catching type errors at compile time instead of runtime `ClassCastException`s.

**A17. Access modifiers.**
`private` (same class) → default/package-private (same package) → `protected` (package + subclasses)
→ `public` (everywhere). 💡 In page objects, locators are `private`, actions are `public`.

**A18. Marker, functional, and SAM interfaces.**
A **marker** interface has no methods (`Serializable`) — metadata only. A **functional** (SAM =
Single Abstract Method) interface has exactly one abstract method (`Runnable`, `Comparator`) and can
be a lambda target — the basis of Java 8 functional programming.

---

## B. Java 8+ & Coding Fluency

**B1. What is a functional interface? Name the built-in ones.**
An interface with a single abstract method, usable as a lambda target (annotated
`@FunctionalInterface`). Built-ins in `java.util.function`: **`Predicate<T>`** (`T→boolean`),
**`Function<T,R>`** (`T→R`), **`Consumer<T>`** (`T→void`), **`Supplier<T>`** (`()→T`),
**`BiFunction<T,U,R>`**, **`UnaryOperator<T>`**, and `Comparator<T>`.

**B2. Lambda expressions — what and why.**
A concise way to implement a functional interface inline: `(a, b) -> a + b`. They reduce anonymous
class boilerplate and enable the Streams API. 🗣️ *"A lambda is an anonymous implementation of a SAM
interface."*

**B3. Streams — intermediate vs terminal, and laziness.**
A **Stream** is a pipeline over a data source. **Intermediate** ops (`filter`, `map`, `sorted`,
`distinct`, `limit`) return a new stream and are **lazy** — nothing executes until a **terminal** op
(`collect`, `forEach`, `reduce`, `count`, `anyMatch`) is invoked. Laziness allows fusion and
short-circuiting. ⚠️ A stream is **single-use** — reusing it throws `IllegalStateException`.

**B4. `map` vs `flatMap`.**
`map` transforms each element 1:1 (`Stream<T>` → `Stream<R>`). `flatMap` transforms each element
into a **stream** and **flattens** them into one (`Stream<List<T>>` → `Stream<T>`). Use `flatMap` to
flatten nested collections.

**B5. Common stream examples (be ready to write live).**
```java
// filter even, square, collect
nums.stream().filter(n -> n % 2 == 0).map(n -> n * n).collect(Collectors.toList());
// names starting with A, uppercased, sorted
names.stream().filter(s -> s.startsWith("A")).map(String::toUpperCase).sorted().toList();
// group words by length
words.stream().collect(Collectors.groupingBy(String::length));
// count occurrences
words.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));
// first non-repeating char
s.chars().mapToObj(c -> (char) c)
 .collect(Collectors.groupingBy(c -> c, LinkedHashMap::new, Collectors.counting()))
 .entrySet().stream().filter(e -> e.getValue() == 1).map(Map.Entry::getKey).findFirst();
```

**B6. Why `Optional`, and why is `Optional.get()` a smell?**
`Optional<T>` is a container that explicitly models "value or absent", avoiding `NullPointerException`
and making nullability part of the API. Use `orElse`, `orElseGet`, `ifPresent`, `map`. ⚠️
`Optional.get()` without `isPresent()` throws `NoSuchElementException` — defeats the purpose.

**B7. Method references.**
Shorthand for a lambda that just calls a method: `String::toUpperCase` (instance method of type),
`System.out::println` (instance method of object), `Integer::parseInt` (static),
`ArrayList::new` (constructor).

**B8. `Collectors.groupingBy` / `toMap`.**
`groupingBy` partitions a stream into a `Map<K, List<V>>` by a classifier; a downstream collector
(`counting()`, `mapping(...)`) refines the values. `toMap(keyFn, valueFn)` builds a map directly —
⚠️ throws on duplicate keys unless you supply a merge function.

**B9. Other modern features you should mention.**
`var` (local type inference), **records** (immutable data carriers — used in this repo's tests),
**switch expressions** (`case x ->`), **text blocks** (`"""..."""`), and **`Stream.toList()`** (Java
16+).

---

## C. Selenium WebDriver

**C1. Explain Selenium's architecture (and the Selenium 4 changes).**
Your test calls the **Selenium client library** (Java bindings) → which sends commands over the
**W3C WebDriver protocol** (HTTP/JSON) → to the **browser driver** (chromedriver, geckodriver) →
which controls the **browser**. **Selenium 4** dropped the legacy JSON Wire protocol for the **W3C
standard** (more stable cross-browser), added **relative locators**, **CDP** access, and **Selenium
Manager** (auto-downloads the right driver — no more manual driver setup or WebDriverManager).

**C2. WebDriver vs WebElement.**
`WebDriver` is the interface controlling the browser (navigate, find, manage windows). `WebElement`
represents a single DOM element you interact with (`click`, `sendKeys`, `getText`). ⚠️ A
`WebElement` reference can go **stale** if the DOM re-renders.

**C3. List the locator strategies; CSS vs XPath.**
`id`, `name`, `className`, `tagName`, `linkText`, `partialLinkText`, **`cssSelector`**, **`xpath`**.
**CSS** is faster and more readable but can only go **down/sideways** the DOM. **XPath** is more
powerful — it can traverse **up** (`ancestor`, `parent`), match by **text** (`text()`, `contains()`),
and use axes — but is slower and more brittle. 💡 Prefer id/CSS; use XPath only when you must
traverse up or match by visible text.

**C4. Write an XPath for "the price cell in the row whose first column is NIFTY".**
```
//tr[td[1][normalize-space()='NIFTY']]/td[@class='price']
```
Shows axis/predicate fluency — a common live-coding ask.

**C5. Implicit vs explicit vs fluent wait — and the cardinal sin.**
- **Implicit**: a global poll applied to every `findElement` (`driver.manage().timeouts()
  .implicitlyWait(...)`).
- **Explicit**: wait for a **specific condition** on a specific element
  (`new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(el))`).
- **Fluent**: explicit wait with custom **polling interval** and **ignored exceptions**.
- ⚠️ **Never mix implicit and explicit waits** — their timeouts compound unpredictably, causing long,
  flaky waits. Pick explicit waits and disable implicit.

**C6. Why is `Thread.sleep()` an anti-pattern?**
It's a **fixed, unconditional** wait — too short → flaky, too long → slow. It waits even when the
element is already ready. Always wait on a **condition** (explicit wait), not the clock.

**C7. How do you handle a dynamic element whose id changes each load?**
Locate by a **stable attribute** instead: a partial match (`css=[id^='user_']`, `contains()` in
XPath), a `data-*`/`aria` attribute, visible text, or relative position to a stable anchor. Avoid
absolute XPath.

**C8. Handle dropdowns, alerts, frames, and windows.**
- **Native `<select>`**: `new Select(el).selectByVisibleText("...")`. Custom (div) dropdowns: click
  to open, then click the option.
- **Alert**: `driver.switchTo().alert().accept()/dismiss()/sendKeys()`.
- **iframe**: `driver.switchTo().frame(...)`, act, then `defaultContent()` to switch back.
- **Windows/tabs**: capture `getWindowHandles()`, `switchTo().window(handle)`.

**C9. StaleElementReferenceException — cause and robust fix.**
The element reference points to a DOM node that's been **removed/re-rendered** (AJAX update,
navigation). Fix: **re-locate** the element right before use (don't cache `WebElement`s across
actions), or wrap in an explicit wait / retry. Page Object methods that re-find on each call avoid
this.

**C10. ElementClickInterceptedException — how do you fix it?**
Another element (overlay, sticky header, modal) is on top of your target. Fix in order: wait for the
overlay to disappear, `scrollIntoView`, then as a **last resort** a JS click
(`((JavascriptExecutor)driver).executeScript("arguments[0].click();", el)`). ⚠️ JS click bypasses
real user-event checks — use sparingly.

**C11. `findElement` vs `findElements`.**
`findElement` returns the first match or **throws `NoSuchElementException`** if none.
`findElements` returns a **List** (empty if none, never throws) — use it to check existence/count.

**C12. `driver.close()` vs `driver.quit()`.**
`close()` closes the **current** window/tab. `quit()` closes **all** windows and **ends the
WebDriver session** (kills the driver process). Always `quit()` in teardown to avoid leaked
processes.

**C13. Selenium 4 relative locators.**
`above`, `below`, `toLeftOf`, `toRightOf`, `near` — locate by visual position relative to another
element: `driver.findElement(with(By.tagName("input")).below(label));`. Useful for forms without
stable selectors.

**C14. JavascriptExecutor — when is it justified vs a smell?**
Justified: scrolling, reading computed values, triggering events the API can't. ⚠️ Smell: using JS
click/`sendKeys` to bypass actionability — it hides real bugs (an element a user couldn't actually
click). Prefer native interactions; reach for JS only when there's no alternative.

**C15. How do you run headless, and why in CI?**
`ChromeOptions options = new ChromeOptions(); options.addArguments("--headless=new");` — no GUI →
faster, less resource use, and works on headless CI agents/containers. Trade-off: occasionally
masks rendering issues, so run a headed/cross-browser pass periodically.

**C16. The Actions class.**
For complex user gestures: hover (`moveToElement`), drag-and-drop (`dragAndDrop`), right-click
(`contextClick`), double-click, click-and-hold, and keyboard chords (`keyDown(Keys.CONTROL)`). Build
a sequence and call `.perform()`.

**C17. How do you capture a screenshot?**
`((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE)` — wire it into the test teardown on
failure (this repo auto-attaches screenshots to the report on every failure).

**C18. What is Selenium Grid?**
A hub-node architecture (Grid 4) for **distributed/parallel** cross-browser execution — run tests
against many browser/OS combos remotely. Commonly run in Docker or via cloud (BrowserStack/Sauce/
LambdaTest) for scale without maintaining a grid.

---

## D. Playwright (Concepts + Selenium Comparison)

**D1. What makes Playwright's locators "never stale"?**
A Playwright `Locator` is **lazy** — it stores *how to find* an element, not a snapshot. The DOM is
queried (and auto-waited) at **action time**, so a re-rendered DOM is simply re-resolved. There's no
cached node to go stale (unlike Selenium's `WebElement`). 💡 This single design choice removes a
whole class of flakiness.

**D2. Explain auto-waiting / actionability.**
Before any action (`click`, `fill`, `innerText`), Playwright automatically waits for the element to
be **attached, visible, stable, enabled, and receiving events** — up to the timeout. You rarely
write explicit waits. 🗣️ *"Playwright waits for actionability by default; Selenium makes you wait
explicitly."*

**D3. Browser vs BrowserContext vs Page.**
`Browser` = a launched engine (expensive, launched once). `BrowserContext` = an **isolated session**
(own cookies/storage — like incognito), cheap, **one per test** for isolation. `Page` = a tab within
a context. This repo uses **one browser per suite, one context per test**.

**D4. Web-first assertions — how do they differ from a normal assert?**
`assertThat(locator).isVisible()` (from `PlaywrightAssertions`) **auto-retries** against the live DOM
until the condition is true or it times out — no manual wait loop. A plain AssertJ/JUnit assert
evaluates **once**. Use web-first for element/page state; AssertJ for already-extracted values.

**D5. How do you mock an API in Playwright, and why is that powerful?**
```java
page.route("**/api/live-market", route -> route.fulfill(new Route.FulfillOptions()
    .setStatus(200).setContentType("application/json").setBody("{...}")));
```
You intercept the request and return a **controlled response** — letting you force states your live
data can't (a "Trade Signal", an error, latency) deterministically. 💡 This is a natural extension
of a Service-Virtualization mindset — same instinct, in-process.

**D6. What is APIRequestContext?**
A built-in HTTP client (`playwright.request().newContext()`) for hitting endpoints **without a
browser tab** — `get`/`post` with `RequestOptions`, read status/body, and share the browser session.
This repo validates signal logic directly against `/data/market.json` with it. Selenium has no
equivalent — you'd add RestAssured.

**D7. How do you handle authentication efficiently?**
Don't log in via UI every test. Either **API-login then inject the cookie** into each context, or
save **`storageState`** once and reuse it (`newContext(setStorageStatePath(...))`). This repo
API-logs-in at suite start and adds the session cookie per context.

**D8. What does the Trace Viewer give you?**
A post-mortem recording: **DOM snapshots before/after each action**, network, console, and a step
timeline. `context.tracing().start(...)` → run → `stop(path)` → `npx playwright show-trace
trace.zip`. The single best tool for debugging a CI-only failure.

**D9. Playwright vs Selenium — give the real differences.**
| Aspect | Selenium | Playwright |
|---|---|---|
| Element ref | `WebElement`, can go **stale** | lazy `Locator`, never stale |
| Waiting | mostly explicit | **auto-wait** + retrying assertions |
| Drivers | driver binaries (auto in 4.6+) | bundled engines (`playwright install`) |
| Isolation | new driver/session per test | cheap `BrowserContext` |
| API testing | needs RestAssured | built-in `APIRequestContext` |
| Network mock | hard (proxy/CDP) | first-class `route().fulfill()` |
| Debug | logs | **Trace Viewer**, codegen, inspector |
| Ecosystem | huge, mature, legacy reach | newer, fast, modern |
🗣️ *"Selenium is the mature standard; Playwright is faster and less flaky out of the box. Concepts
map 1:1, so I move between them easily."*

**D10. When would you still choose Selenium?**
Legacy browser/IE support, an existing large Selenium suite/team skillset, Grid infrastructure
already in place, or org standardization. Tool choice is context-driven, not dogmatic.

**D11. How do you run cross-browser in parallel with Playwright?**
One API drives Chromium/Firefox/WebKit; launch by config (`-Dbrowser=`), give each test its own
context, and run a **matrix** in CI (see this repo's `.github/workflows/`). Contexts are cheap, so
parallelism is natural.

**D12. How does Playwright reduce flakiness compared to a typical Selenium suite?**
Lazy locators (no stale refs) + auto-wait (no race on readiness) + web-first retrying assertions
(no single-shot checks) + isolated contexts (no test pollution) + built-in tracing (fast root-cause).
Most Selenium flakiness is sync/staleness — Playwright designs it out.

---

## E. TestNG / JUnit

**E1. Recite the TestNG annotation execution order.**
`@BeforeSuite` → `@BeforeTest` → `@BeforeClass` → `@BeforeMethod` → **`@Test`** → `@AfterMethod` →
`@AfterClass` → `@AfterTest` → `@AfterSuite`. The `@Before*` widen-to-narrow, the `@After*` mirror in
reverse. `@BeforeMethod`/`@AfterMethod` run around **every** test method.

**E2. `@Test` attributes worth knowing.**
`priority` (order), `groups` (smoke/regression), `dependsOnMethods`, `enabled` (skip),
`invocationCount` (repeat), `timeOut`, `expectedExceptions`, `dataProvider`, `description`.

**E3. Hard vs soft assertions — when each.**
A **hard** assertion (`Assert.assertEquals`) stops the test on first failure. A **soft** assertion
(`SoftAssert`) **collects** failures and reports them all at `assertAll()`. 💡 Use soft when
validating multiple independent fields on one screen (validate all 5 form errors in one run instead
of fixing-rerunning five times).

**E4. How does `@DataProvider` work, and how do you feed it from Excel?**
A `@DataProvider` method returns `Object[][]`; a `@Test(dataProvider="...")` runs **once per row**.
For Excel, read rows with **Apache POI** into the `Object[][]`. 💡 Each row is a separately-reported
result — one bad input doesn't hide the others (vs a `for` loop that fails fast).

**E5. `@Parameters` vs `@DataProvider`.**
`@Parameters` injects values from **`testng.xml`** (good for static config like browser/env).
`@DataProvider` supplies **dynamic, multi-row** data from code/files. Use `@Parameters` for config,
`@DataProvider` for data-driven cases.

**E6. How do you run tests in parallel and keep WebDriver thread-safe?**
In `testng.xml`: `parallel="methods|classes|tests" thread-count="N"`. ⚠️ A shared `static` driver
breaks under parallelism — store the driver in a **`ThreadLocal<WebDriver>`** so each thread gets its
own instance. (Playwright solves this with per-test contexts.)

**E7. How do you auto-retry flaky tests?**
Implement **`IRetryAnalyzer`** (`retry()` returns true up to N times) and attach it via
`@Test(retryAnalyzer=...)` or an `IAnnotationTransformer` listener. ⚠️ Retries hide real flakiness —
pair with root-cause analysis, don't use them as a permanent crutch.

**E8. `priority` vs `dependsOnMethods`.**
`priority` only **orders** independent tests. `dependsOnMethods` creates a **dependency**: if the
prerequisite fails/skips, the dependent test is **skipped** (not failed). Use dependencies for true
prerequisites (login before checkout), priority for ordering.

**E9. TestNG listeners.**
`ITestListener` (hooks on test start/success/failure — log, screenshot), `ISuiteListener`,
`IRetryAnalyzer`, `IAnnotationTransformer` (modify annotations at runtime). The basis of reporting
and screenshot-on-failure plumbing.

**E10. How do you organize smoke vs regression and run only smoke in CI?**
Tag tests with `groups = {"smoke"}` / `{"regression"}`; in `testng.xml` `<groups><run><include
name="smoke"/></run></groups>`, or `mvn test -Dgroups=smoke`. CI runs smoke on every PR, full
regression nightly.

**E11. TestNG vs JUnit 5 — when each.**
TestNG has richer suite control (`testng.xml`), `@DataProvider`, groups, and built-in parallelism —
strong for **end-to-end/UI** suites. JUnit 5 is the **unit-testing** default in the Java ecosystem
(Spring, etc.), with `@ParameterizedTest` and extensions. Map: `@BeforeMethod`↔`@BeforeEach`,
`@BeforeClass`↔`@BeforeAll`.

---

## F. Framework Design & Architecture

**F1. Explain the Page Object Model and the problem it solves.**
POM puts each page's **locators and actions** in a dedicated class; **tests hold only assertions and
flow**. Benefit: when the UI changes, you fix **one** page object, not dozens of tests — eliminating
duplication and centralizing maintenance. 🗣️ *"Locators and actions in pages, assertions in tests."*

**F2. POM vs PageFactory.**
PageFactory is an older POM helper using `@FindBy` annotations + `PageFactory.initElements(...)` for
lazy proxies. Many teams now prefer **plain `By`/`Locator`** definitions because PageFactory adds
little, can mask staleness, and is less flexible. Know both; recommend plain locators.

**F3. Walk me through designing a framework from scratch (5-min whiteboard).**
Layers: **(1) Driver/Browser factory** (`ThreadLocal` for parallel safety, browser switch) → **(2)
Config** (properties + system props for env/browser) → **(3) Base classes** (`BaseTest` lifecycle,
`BasePage` shared helpers) → **(4) Page objects** → **(5) Tests** (TestNG + assertions) → **(6)
Utilities** (waits wrapper, data readers, screenshot/attachment helpers) → **(7) Test data** (Excel/
JSON/DB + mocking/virtualization) → **(8) Reporting** (Allure/Extent) + **logging** (Log4j2/SLF4J) →
**(9) CI** (Maven + Jenkins/GitHub Actions) → **(10) Reliability** (explicit-wait wrappers, retry,
soft-fail for external deps). 💡 Mention the **test pyramid** to show you'd push checks to API/unit
where possible.

**F4. Framework types — and trade-offs.**
Linear (record-replay, unmaintainable) → Modular (functions per module) → **Data-Driven** (logic +
external data) → Keyword-Driven (actions as keywords, non-coders author) → **Hybrid** (data +
keyword + POM, most common) → **BDD** (business-readable Gherkin). Pick by team skill and need.

**F5. How do you support multiple browsers and environments?**
Browser via a **factory + system property** (`-Dbrowser=firefox` → `switch`). Environment via
**config files per env** (dev/qa/prod) selected by `-Denv=qa`, feeding base URLs/credentials. 💡
Your SV background maps here — you've managed environment/dependency strategy before.

**F6. How do you achieve thread-safe parallel execution?**
`ThreadLocal<WebDriver>` (each thread its own driver), no shared mutable state in page objects,
independent/atomic tests, and isolated test data per thread. Playwright uses per-test `BrowserContext`
instead.

**F7. Which design patterns appear in automation?**
**Page Object** (pages), **Factory** (driver/page creation), **Singleton** (driver/config holder),
**Builder** (test-data/request builders), **Strategy** (browser/wait selection), **Fluent interface**
(chained page actions), **Decorator/Wrapper** (logging wrappers). Be ready with where you used each.

**F8. How do you apply SOLID to a test framework?**
**S** — a page object does one page; utilities do one thing. **O** — add a new browser via the
factory without editing tests. **L** — any `WebDriver` impl works wherever the interface is used.
**I** — small focused interfaces over fat ones. **D** — tests depend on the `WebDriver` interface,
not `ChromeDriver`.

**F9. How do you manage test data?**
Externalize it: properties/Excel/CSV/JSON/DB, data builders for objects, and **environment-specific**
sets. For unstable/unavailable dependencies, **mock or virtualize** them. 💡 Lead answer: "test data
+ environment strategy is where most flakiness lives — I treat it as a first-class design concern,
which my SV background sharpened."

**F10. How do you keep tests independent and atomic?**
No shared state between tests, each test sets up and tears down its own data/session, fresh browser
context, no ordering assumptions (avoid hidden `dependsOnMethods` chains). Independence is what makes
parallelism and reliable reruns possible.

**F11. How do you handle flaky tests at the framework level?**
Attack the **root cause**, usually sync or environment: explicit-wait **wrappers** (no `sleep`),
isolated data, **virtualized dependencies**, deterministic test setup, and stable locators. Add a
**retry-with-RCA** policy and quarantine chronic offenders — never blind-rerun to green. 🗣️ *"A
flaky test is a defect in the test; I fix it, I don't paper over it with reruns."*

**F12. Explain the test automation pyramid.**
Many fast **unit** tests at the base, fewer **API/integration** tests in the middle, fewest **UI/E2E**
tests at the top. UI tests are slow and brittle, so reserve them for true user journeys; push logic
checks down to API/unit where they're fast and stable. ⚠️ The "ice-cream cone" anti-pattern
(UI-heavy) rots and slows teams.

**F13. How do you decide what to automate?**
By **ROI and risk**: high-frequency, stable, business-critical, repetitive, data-heavy, and
regression-prone cases first. Don't automate one-off exploratory checks, constantly-changing UI, or
low-value paths. 🗣️ *"Automate the repetitive and risky; explore the rest manually."*

**F14. How do you keep a suite maintainable as the product scales?**
Strict POM, shared utilities, single source of config/data, the pyramid (don't over-invest in UI),
code reviews on test code, naming conventions, and regular **flaky-test cleanup**. Treat automation
code with the same rigor as product code.

**F15. What reporting do you use and why?**
**Allure** (rich: steps, attachments, history/trend, categories — this repo uses it) or **Extent
Reports**. Reports must show **what failed, why, and proof** (screenshot/log) so failures are
triaged without re-running. CI publishes and trends them.

---

## G. API Automation + Service Virtualization

**G1. HTTP methods and status codes you must know.**
**Methods:** GET (read, safe/idempotent), POST (create), PUT (replace, idempotent), PATCH (partial),
DELETE (idempotent). **Codes:** 2xx success (200 OK, 201 Created, 204 No Content), 3xx redirect (301,
304 Not Modified), 4xx client (400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found, 429
Too Many Requests), 5xx server (500, 502, 503).

**G2. REST vs SOAP.**
**REST** — architectural style over HTTP, resource-based, typically JSON, lightweight, stateless.
**SOAP** — a strict XML protocol with WSDL contracts, built-in standards (WS-Security), used in
enterprise/legacy/banking. 💡 Your SV experience likely covered SOAP services — mention it.

**G3. Write a RestAssured test (auth → action → assert).**
```java
String token = given().contentType(JSON).body(Map.of("username", u, "password", p))
    .when().post("/api/auth/login").then().statusCode(200).extract().path("token");

given().header("Authorization", "Bearer " + token)
    .when().get("/api/portfolio")
    .then().statusCode(200).body("holdings.size()", greaterThan(0));
```
Shows the `given().when().then()` BDD style, token extraction, and chained assertions.

**G4. How do you validate a nested JSON field?**
GPath/JsonPath: `then().body("data.user.email", equalTo("a@b.com"))`, or extract and assert in Java.
For structure, use **JSON schema validation** (`matchesJsonSchemaInClasspath(...)`).

**G5. Serialization/deserialization (POJO ↔ JSON).**
Map a request/response to a Java class with Jackson/Gson — RestAssured can serialize a POJO into the
body and deserialize the response (`.extract().as(User.class)`). Cleaner and type-safe vs string
JSON.

**G6. What is Service Virtualization, and when do you use it?**
SV **simulates the behavior of a dependency** (an API/service/queue) that's unavailable, costly,
rate-limited, or hard to drive into specific states — so testing isn't blocked. Use it when the real
dependency is **unstable, third-party, not built yet, or expensive**, or when you need
**deterministic** edge cases (errors, latency, specific data). 💡 This is your differentiator.

**G7. Mock vs stub vs service virtualization — be precise.**
- **Stub** — returns canned responses for specific calls; no logic.
- **Mock** — a stub that **also verifies interactions** (was it called, how many times, with what).
- **Service virtualization** — a **fuller, often stateful** simulation of a real service:
  request matching, response templating, latency/error modeling, protocol fidelity (HTTP/SOAP/JMS/MQ)
  — used at integration/system level, not just unit.
🗣️ *"Stubs answer, mocks also assert, virtual services impersonate a whole dependency."*

**G8. Stateful vs stateless virtual service — example.**
**Stateless** — same request always yields the same response (a price lookup). **Stateful** — the
response depends on prior calls (an account balance that **decreases after a debit**, an order that
moves NEW→SHIPPED). Stateful virtualization models real workflows.

**G9. How do you test an API whose downstream dependency isn't ready?**
**Virtualize the downstream**: stand up a virtual service modeling its contract (incl. error and
latency scenarios) so the team runs full regression independently and shifts testing left. This is
exactly the SV value you bring to an automation team.

**G10. WireMock basics (open-source SV).**
A standalone/embedded HTTP mock server: define **stub mappings** (request match → response), support
**stateful scenarios**, response templating, fault injection, and request **verification**. Good to
mention alongside commercial tools (CA DevTest / Parasoft Virtualize) to show open-source breadth.

**G11. What is contract testing (Pact)?**
**Consumer-driven contract testing**: the consumer defines the contract (expected request/response)
it needs; the provider verifies it honors that contract in its own pipeline. Catches integration
breakages **early** in microservices without slow full end-to-end environments.

**G12. How do API tests fit into CI as a quality gate?**
They're fast and stable, so run them **before** the UI suite as an early gate — fail the build on
contract/behavior breaks. This embodies shift-left: catch defects at the cheapest layer.

**G13. Auth types in APIs.**
**Basic** (base64 user:pass), **Bearer/JWT** (token in `Authorization` header), **OAuth2** (token via
an auth flow), **API key** (header/query). Know how to obtain and reuse a token across requests.

**G14. Idempotency and statelessness — why they matter for testing.**
**Idempotent** operations (GET/PUT/DELETE) can be safely retried — important for reliable test reruns.
**Stateless** servers don't keep client state between requests, so each test request is independent —
easier to parallelize and isolate.

---

## H. CI/CD, Git, Build & Reporting

**H1. Explain the Maven build lifecycle.**
Phases run in order: `validate` → `compile` → `test` → `package` → `verify` → `install` → `deploy`.
Running a phase runs all prior ones. **Surefire** runs unit/TestNG tests in the `test` phase
(`pom.xml` points it at `testng.xml`); **Failsafe** runs integration tests in `verify`.

**H2. What does the Surefire plugin do here?**
Executes the test suite during `mvn test`, reads `suiteXmlFiles` (the `testng.xml`), injects system
properties (browser/url/headless), and (in this repo) attaches the **AspectJ** java agent so Allure
`@Step`s are captured.

**H3. Git workflow you follow as a lead.**
Feature branches off `main`/`develop`, small focused commits, **pull requests** with review, CI must
pass before merge, and a branching strategy (**trunk-based** with short-lived branches, or GitFlow
for release-heavy orgs). Resolve conflicts via rebase/merge; protect `main` with required checks.

**H4. How do you handle a merge conflict?**
Pull latest, attempt merge/rebase, open the conflicted files (`<<<<<<<`/`=======`/`>>>>>>>`), resolve
by keeping the correct combination, test, then commit. Communicate with whoever owns the other
change.

**H5. Jenkins vs GitHub Actions — when each.**
**Jenkins** — self-hosted, highly extensible (plugins), good for complex/on-prem pipelines.
**GitHub Actions** — cloud-native, **YAML pipeline-as-code** living with the repo, zero infra, great
for GitHub projects (this repo uses it). Both support parameterized, scheduled, matrix builds.

**H6. How do you trigger your suite in CI?**
On **every PR** (smoke), on **push to main**, on a **nightly schedule** (full regression), and
**manually** (parameterized: browser/env). Quality gates **fail the build** and block merge on
failure.

**H7. How do you run cross-browser tests in parallel in CI?**
A **matrix** job (chromium/firefox/webkit or chrome/firefox/edge), each running the suite — backed by
Selenium Grid/Docker or a cloud vendor for Selenium, or Playwright's bundled engines. Combine with
`ThreadLocal`/contexts for in-job parallelism.

**H8. How do you publish and trend reports in CI?**
Generate Allure/Extent after the run, **restore history** from a persisted location (this repo uses
the `gh-pages` branch) so the **trend graph** builds across runs, then publish to Pages and upload
artifacts (report, screenshots, logs).

**H9. What's a quality gate?**
An automated pass/fail checkpoint in the pipeline — e.g. all smoke tests pass, coverage ≥ threshold,
no critical defects — that **blocks promotion/merge** if unmet. Enforces the Definition of Done
automatically.

**H10. How would you containerize the test suite?**
A Docker image with JDK + Maven + browsers (or use Playwright's official image / Selenium Grid
containers), run `mvn test` in the container so execution is identical locally and in CI — removing
"works on my machine".

**H11. Walk through a GitHub Actions workflow (from this repo).**
Triggers (`push`/`pull_request`/`schedule`/`workflow_dispatch`) → a `test` job: checkout, set up JDK,
cache Maven, install Playwright browsers, write env/categories, `mvn test`, generate Allure report,
save history, publish to Pages, upload artifacts → a conditional **cross-browser matrix** job. See
[`FRAMEWORK_GUIDE.md`](FRAMEWORK_GUIDE.md) §11.

---

## I. BDD / Cucumber

**I1. What is BDD and what problem does it solve?**
Behavior-Driven Development expresses requirements as **executable, business-readable scenarios**
(Given/When/Then), creating shared understanding between business, dev, and QA and **living
documentation**. It solves the "the spec and the tests drifted apart" problem.

**I2. Gherkin structure.**
`Feature` → `Scenario`/`Scenario Outline` → `Given` (context) / `When` (action) / `Then` (outcome) +
`And`/`But`. `Background` holds common setup; `Examples` feeds a `Scenario Outline`; **tags**
(`@smoke`) filter runs.

**I3. Scenario vs Scenario Outline.**
A **Scenario** is a single concrete case. A **Scenario Outline** is a **parameterized template** run
once per row of its `Examples` table — Gherkin's data-driven testing.

**I4. How do step definitions map to Gherkin? What is glue?**
Each Gherkin step matches a **step definition** (a Java method annotated `@Given/@When/@Then` with a
regex/cucumber-expression). **Glue** is the package(s) where Cucumber looks for those step defs and
hooks (set in `@CucumberOptions`).

**I5. How do you share state between steps?**
Don't use static fields. Use **dependency injection** (PicoContainer/Spring) to inject a shared
**scenario context** object into the step-def classes, scoped per scenario.

**I6. Hooks in Cucumber.**
`@Before`/`@After` (per scenario — setup/teardown, screenshot on fail), tagged hooks
(`@Before("@db")`), and `@BeforeStep`/`@AfterStep`. They replace TestNG's `@BeforeMethod` in a BDD
project.

**I7. When is BDD overkill?**
When there's no business stakeholder reading the scenarios, for low-level/technical API tests, or
when it just adds a Gherkin layer over developer-only tests. 💡 A lead knows **when not** to use a
tool — forcing BDD everywhere creates a maintenance tax with no collaboration payoff.

**I8. How do you avoid a step-definition explosion?**
Write **reusable, parameterized** steps (cucumber expressions), keep steps at a **business** level
(not "click button X"), and push UI detail into page objects the steps call. Reuse over duplication.

---

## J. Test Lead — Strategy, Process & People

**J1. Test strategy vs test plan.**
A **test strategy** is the high-level, often org/programme-wide **approach** (levels, types, tools,
environments, automation philosophy, risk approach) — relatively stable. A **test plan** is
**project/release-specific** (scope, schedule, resources, deliverables, entry/exit criteria). 🗣️
*"Strategy = how we test in general; plan = how we test this."*

**J2. How would you define a test/automation strategy for a new product?**
Cover: **scope & objectives**, **risk assessment** (what hurts most if it breaks), **test levels &
types** (unit/API/UI/perf/security), the **automation approach** (pyramid, what to automate, tooling,
framework), **environments & test data** (incl. virtualization for unstable deps), **CI/CD & quality
gates**, **entry/exit criteria**, **metrics**, and **roles/ownership**. Tie each choice to risk and
ROI.

**J3. How do you decide what/when/how to automate?**
**What** — high ROI: repetitive, stable, business-critical, regression-prone, data-driven.
**When** — as early as feasible (in-sprint), once the feature is stable enough. **How** — at the
**lowest effective layer** (pyramid: prefer API/unit over UI). Don't automate volatile UI or one-offs.

**J4. How do you estimate an automation effort?**
Break work into components (framework setup, per-scenario scripting, maintenance), estimate each via
**historical velocity** or **three-point/PERT** ((optimistic + 4×likely + pessimistic)/6), add buffer
for flakiness/maintenance, and validate against capacity. Express assumptions explicitly.

**J5. What is risk-based testing?**
Prioritize testing by **risk = likelihood × impact**. Focus deepest testing on high-risk areas
(payment, auth, data integrity), lighter coverage on low-risk. Lets you make defensible decisions
when time is short.

**J6. Which QA metrics do you report — and which do you distrust?**
Report: **defect leakage/escaped defects**, **defect density**, **automation coverage** (and pass
rate), **flaky-test rate**, **MTTR**, **requirement traceability**, cycle time. ⚠️ Distrust **raw
test-case count** and **"100% test coverage"** as vanity metrics — they measure activity, not
quality. 💡 Reporting the *right* metrics is a hallmark of a lead.

**J7. Severity vs priority — with examples.**
**Severity** = technical **impact**; **priority** = business **urgency** to fix.
- *High severity, low priority*: app crashes on a deprecated page nobody uses.
- *Low severity, high priority*: a typo in the company logo on the landing page before a launch.

**J8. How do you handle a release with open defects and a fixed go-live?**
Run a **risk-based go/no-go**: assess each open defect's severity/priority, workarounds, and business
impact; present options and a recommendation to stakeholders; document accepted risks; ensure
rollback/monitoring. The decision is the business's — your job is a **clear, data-backed**
recommendation.

**J9. Defect lifecycle.**
New → Assigned → Open/In-Progress → Fixed → **Retest** → Closed (or Reopened); Rejected/Duplicate/
Deferred as needed. As lead you run **triage** (prioritize), drive **RCA**, and track leakage.

**J10. Explain SDLC vs STLC and QA's role in Agile.**
**SDLC** = the whole software lifecycle; **STLC** = the testing lifecycle within it (requirement
analysis → planning → design → environment → execution → closure). In **Agile/Scrum**, QA engages
every ceremony: refine acceptance criteria (planning), test in-sprint, demo (review), and improve in
**retrospectives** — testing is continuous, not a phase.

**J11. How do you introduce automation (or SV) into a team that has none?**
Start with a **high-value, low-risk pilot** (a painful regression suite), prove ROI with **metrics**
(time saved, defects caught), build a maintainable framework, **train and pair** with the team, and
scale gradually with leadership buy-in. 💡 This is your real story — you've done this with SV.

**J12. How do you mentor a struggling automation engineer?**
Diagnose the gap (concept vs tooling vs confidence), pair-program, set small achievable goals, do
**code reviews as teaching**, share resources, and give regular specific feedback. Measure
improvement, not blame.

**J13. How do you convince a sceptical team/PM to invest in automation or SV?**
Speak their language — **time and money**: quantify regression hours saved, defects caught earlier
(cheaper), faster releases, and reduced environment blockers. Run a small POC and show the numbers.
Frame it as **risk reduction and velocity**, not "best practice".

**J14. How do you handle conflict between dev and QA on the quality bar?**
Anchor on **shared goals and data** (the Definition of Done, risk, customer impact), keep it about
the issue not the person, escalate with options if needed, and agree on objective acceptance
criteria up front to prevent recurrence.

**J15. How do you prioritize when everything is urgent?**
By **risk and business value**: clarify true deadlines with stakeholders, focus on critical-path and
high-risk items, communicate trade-offs transparently ("if we do X, Y slips"), and say **no with
data**. A lead manages expectations, not just tasks.

**J16. How do you reduce flaky tests across a team's suite?**
Both **process and tech**: a flaky-test dashboard + quarantine policy, root-cause each (sync, data,
env), enforce explicit waits/stable locators in reviews, virtualize unstable deps, and treat flakies
as defects with owners. Don't let "just rerun it" become culture.

**J17. Test management tools you've used.**
Jira + **Xray/Zephyr** (test management in Jira), **TestRail**, or **HP ALM/qTest** — for test cases,
execution, traceability (RTM), and defect linkage. Reporting rolls up to dashboards for stakeholders.

**J18. How do you evaluate/select a tool (build vs buy)?**
Define requirements, run a time-boxed **POC** against real scenarios, score on fit/learning curve/
ecosystem/cost/support/CI integration/team skills, and weigh build-vs-buy (control vs maintenance).
Decide with data and stakeholder input, not hype.

**J19. What is shift-left (and shift-right)?**
**Shift-left** — test **earlier** (static analysis, unit/API tests, in-sprint automation, SV to
unblock) to catch defects when they're cheap. **Shift-right** — test **in production** (monitoring,
canary, A/B, chaos) to learn from real usage. A modern lead does both.

**J20. How do you measure the success of an automation effort?**
Outcome metrics, not output: **regression time reduced**, **defect leakage down**, **release
frequency up**, **manual effort saved**, **automation ROI**, and **stable** (low-flaky) suites — not
just "number of scripts written".

---

## K. Behavioral & Leadership (STAR)

**K1. How should I structure behavioral answers?**
**STAR**: **S**ituation (brief context) → **T**ask (your responsibility) → **A**ction (what **you**
did) → **R**esult (**quantified** outcome). Keep it ~90 seconds, use "I" for your part and "we" for
team context, and end on a measurable result.

**K2. "Tell me about yourself."**
Use your positioning pitch: SV background → deep API/dependency/environment mastery → now owning
hands-on automation (Java/Selenium/Playwright) and framework design → targeting an automation/test
lead role where you set strategy, build frameworks, and mentor. 60–90 seconds, end with why **this**
role.

**K3. "Why are you moving from SV into automation leadership?"**
🗣️ *"SV taught me that reliable testing is really about dependency and environment strategy — the
same thing that makes automation succeed or fail. I want to own quality end-to-end: the framework,
the strategy, and the team — not just the dependency layer. It's a natural broadening of what I
already do well."*

**K4. "Describe a time you improved quality or efficiency."**
Pick the SV/automation initiative; quantify: *"Built virtual services for two unavailable
dependencies → unblocked full regression → cut environment-related blockers ~60% and shifted testing
left by [X] days."* Numbers make it credible.

**K5. "Tell me about a conflict with a developer/manager."**
Show maturity: a real disagreement, how you anchored on data/shared goals, kept it professional,
reached resolution, and what changed afterward (e.g. agreed acceptance criteria up front).

**K6. "A time you made a quality call under pressure."**
A go/no-go with open defects: how you assessed risk, presented options, recommended, documented
accepted risk, and the outcome. Emphasize **structured decision-making**, not heroics.

**K7. "Your biggest failure / mistake."**
Pick a **real** one with a genuine lesson (e.g. a brittle suite you over-invested in UI, or a missed
risk), own it without blaming others, and show the concrete change you made afterward. ⚠️ Avoid the
fake "I work too hard" answer.

**K8. "How do you keep a team motivated and learning?"**
Ownership and autonomy, learning time/knowledge-sharing, recognition, growth paths, pairing/mentoring,
and shielding the team from churn. Tie motivation to **impact** they can see.

**K9. Build a story bank.**
Write **8 STAR stories** (≤120 words each), mapped to: led an initiative, hard technical problem,
process/metric improvement, mentoring, conflict, pressure/deadline, failure+lesson, influence without
authority. Rehearse the arcs — don't recite verbatim.

**K10. Smart questions to ask the interviewer.**
Team's automation maturity & coverage; biggest current quality pain; CI/CD setup; how success is
measured for **this** role; balance of hands-on vs leadership; how QA engages with product/dev. Asking
these signals seniority.

---

# Part 2 — EaseMyTrade Playwright Framework Deep-Dive

> The sections below are a framework-level walkthrough of the real **EaseMyTrade Playwright + Java +
> TestNG** suite in this repo — ordered to follow the **execution flow** (what it's built on → how a
> test boots → how it runs end to end → reporting & CI). Use this as your "here's a real framework I
> built/can explain" portfolio piece.

## 1. Stack & Tooling

**Q1. What is the technology stack of this framework and why these choices?**
- **Playwright for Java 1.47.0** — browser automation (Chromium, Firefox, WebKit from one API).
- **TestNG 7.9.0** — test runner (annotations, suite ordering, data providers, parallelism).
- **Allure 2.27.0** — rich HTML reporting with steps, attachments, history/trend.
- **AssertJ 3.25.3** — fluent, readable assertions with descriptive `.as(...)` messages.
- **Jackson 2.17.1** — JSON parsing for API-level signal validation.
- **SLF4J + Logback** — logging facade + implementation.
- **AspectJ Weaver 1.9.22** — load-time weaving so Allure's `@Step` annotations are captured.
- **Maven** — build, dependency management, system-property injection.

**Q2. Why Playwright over Selenium for this project?**
- Auto-waiting / actionability checks built in — fewer explicit waits and flakiness.
- A single API drives all three engines (Chromium/Firefox/WebKit); no separate driver binaries.
- First-class `APIRequestContext` for hitting JSON endpoints in the *same* session — this suite
  validates signal logic directly against `/data/market.json` and `/api/live-market`.
- Browser **contexts** are cheap and fully isolated (own cookies/storage), which maps perfectly to
  "one fresh context per test".

**Q3. Why TestNG instead of JUnit?**
- `testng.xml` controls suite composition and **execution order** (`preserve-order="true"`).
- Rich `@DataProvider` support for data-driven tests.
- Lifecycle granularity: `@BeforeSuite` / `@BeforeMethod` / `@AfterMethod` / `@AfterSuite` map
  cleanly to "browser once, context per test".
- `SkipException` for first-class conditional skipping (used for the login-gate).

**Q4. What role does AspectJ play? What breaks without it?**
Allure's `@Step` annotation is implemented via aspects. The `aspectjweaver` java agent is attached
in the Surefire `<argLine>`. Without it, `@Step`-annotated methods would still execute but **would
not appear as steps** in the Allure report — you'd lose the step-by-step breadcrumb tree.

---

## 2. Architecture & Design

**Q5. Describe the layered architecture.**
```
Test Classes (one per feature)         e.g. SignalLogicTest, HomePageTest
        │ extends
BaseTest                               Playwright lifecycle, context per test, failure capture
        │ uses
Page Objects (BasePage + subclasses)   locators + @Step actions + state readers
        │ uses
Utilities (SignalParser, AllureAttachmentHelper)
        │ reads
TestConfig                             URLs, timeouts, thresholds, credentials
        │ produces
Allure Report                          Epic→Feature→Story→Test→Step→Attachments
```

**Q6. What are the key design decisions and their rationale?**

| Decision | Rationale |
|---|---|
| One `Browser` per suite, one `BrowserContext` per test | Browser launch is expensive (do once); context is cheap and isolates cookies/storage to prevent test pollution. |
| `@Step` on every page action | Allure shows the exact action sequence without reading code. |
| Auto screenshot + page source on failure | Failures are self-diagnosable; no need to re-run locally to see what happened. |
| `SignalParser` static utility | All numeric parsing + financial logic in one place, unit-testable in isolation. |
| Soft-fail for live Expert View scans | Those depend on third-party APIs (Yahoo/NSE); a network blip shouldn't fail the suite. |
| `categories.json` | Buckets failures into Product Defect / Signal Logic / Network / Infra in Allure. |

**Q7. Why is `SignalParser` stateless with only static methods?**
It holds pure functions (parse a price, compute R:R, check geometry). No shared state means no
concurrency concerns under parallel execution, and each method is independently testable. The
private constructor (`private SignalParser() {}`) prevents instantiation.

**Q8. How would parallel execution affect this design?**
`Browser` is a `static` shared field — safe to share read-only across threads. `context` and `page`
are **instance** fields created fresh per `@BeforeMethod`, so each test thread gets its own isolated
browser context. As long as page objects don't share mutable state (they don't — they only hold a
`Page` reference), TestNG method/class-level parallelism is safe.

---

## 3. The Page Object Model Layer

**Q9. What does `BasePage` provide and why is it abstract?**
`BasePage` is `abstract` — it's never instantiated directly. It holds the shared `Page` reference,
a per-class `Logger`, common header/footer/nav locators, shared `@Step` actions
(`openIndianIndicesDropdown`, `clickBrandLogo`), and defensive helpers (`textOf`, `attrOf`,
`isVisible`, `waitForVisible`). Every concrete page (`HomePage`, `LoginPage`, …) extends it.

**Q10. Why do locators return `Locator` objects rather than resolve immediately?**
```java
public Locator headerLogo() { return page.locator(".brand"); }
```
Playwright `Locator`s are **lazy** — they describe *how to find* an element, not a snapshot of it.
Resolution + auto-waiting happens at action time (`.click()`, `.innerText()`). This makes locators
reusable and resistant to staleness (unlike Selenium `WebElement`).

**Q11. Why are `textOf()` and `isVisible()` wrapped in try/catch returning a default?**
```java
public boolean isVisible(Locator locator) {
    try { return locator.isVisible(); }
    catch (Exception e) { return false; }   // treat "not found" as not visible
}
```
On a live, data-driven dashboard some elements may not be present yet. These read helpers degrade
gracefully (empty string / `false`) and log at debug, so a *missing* element doesn't throw — the
*test's* assertion decides whether that's a failure, keeping the failure semantics in the test, not
the helper.

**Q12. Why does `IndexPage` use multi-selector readers like `[id*='confidence'], [class*='confidence'], [data-confidence]`?**
The same logical value (signal state, confidence) is rendered with different DOM patterns across
index pages. A comma-separated selector list tries multiple patterns and takes `.first()` match,
making the reader resilient to per-page markup differences without duplicating page objects.

**Q13. How is a page object constructed and used in a test?**
```java
@BeforeMethod
public void openHomePage() {
    navigateTo(TestConfig.HOME_URL);   // BaseTest helper
    homePage = new HomePage(page);     // wrap the live Page
}
```
The page object is a thin wrapper over the already-navigated `Page`. Tests then call semantic
methods (`homePage.niftyZoneSupport()`) instead of raw selectors.

---

## 4. Configuration & Environment

**Q14. How is configuration injected? Walk through `app.base.url`.**
Maven `pom.xml` declares the property and pipes it into Surefire's `systemPropertyVariables`.
`TestConfig` reads it: `System.getProperty("app.base.url", "https://easemytrade.in")`. Override at
runtime with `mvn test -Dapp.base.url=http://localhost:4173`. Same pattern for `browser` and
`headless`.

**Q15. How are credentials resolved, and why two sources?**
```java
TEST_USERNAME = resolveCredential("test.username", "TEST_VIEWER_USERNAME");
```
`resolveCredential` checks the **system property** first (`-Dtest.username=...` for local runs),
then falls back to an **environment variable** (`TEST_VIEWER_USERNAME`, set as a CI secret).
If both are blank it returns `""`, and the framework gracefully skips protected-page tests rather
than failing. This keeps secrets out of source and supports both local and CI usage.

**Q16. What are the three timeout tiers and why three?**

| Constant | Value | Used for |
|---|---|---|
| `DEFAULT_TIMEOUT_MS` | 30s | Normal element waits |
| `NAVIGATION_TIMEOUT_MS` | 60s | Page navigations (can chain redirects) |
| `SLOW_RESOURCE_TIMEOUT_MS` | 120s | Live API scans (Yahoo/NSE are slow & rate-limited) |

Different operations have genuinely different latency profiles; one global timeout would either be
too tight for live scans or too loose for everything else.

**Q17. Where do the business thresholds live?**
In `TestConfig`: `MIN_RISK_REWARD_RATIO = 2.2`, `MIN/MAX_CONFIDENCE_SCORE = 0/100`,
`MIDDAY_MIN_CONFIDENCE = 90`, and `VALID_SIGNAL_STATES[]`. Centralizing them means a rule change is a
one-line edit, not a hunt through test code.

---

## 5. Test Lifecycle — How a Test Boots and Runs (the Flow)

> This is the heart of "explain the flow." Trace one test from suite start to teardown.

**Q18. Walk through the complete lifecycle of a test run, start to finish.**

1. **`@BeforeSuite launchBrowser()`** (once):
   - `Playwright.create()` boots the Playwright driver process.
   - Reads `TestConfig.BROWSER` and launches the matching engine via a `switch` expression, with
     `setHeadless()` and `setSlowMo(headless ? 0 : 50)` (50ms slow-mo when visible, for debugging).
   - Calls `loginAsTestViewer()` to obtain a session cookie (see §7).
2. **`@BeforeMethod createContext(Method)`** (per test):
   - `browser.newContext(...)` with viewport 1440×900, **dark** color scheme, `en-IN` locale,
     `Asia/Kolkata` timezone, `ignoreHTTPSErrors`.
   - Sets default + navigation timeouts on the context.
   - Conditionally adds the session cookie if `useAuthenticatedSession()` is true.
   - `context.newPage()` then attaches **event listeners** (`onPageError`, `onConsoleMessage`,
     `onRequest`, `onResponse`) that log into the framework's logger.
3. **The test's own `@BeforeMethod`** (e.g. `openHomePage()`) navigates and builds the page object.
4. **The `@Test` method** executes: page object actions → state reads → AssertJ assertions →
   Allure attachments.
5. **`@AfterMethod tearDown(ITestResult)`** (per test):
   - Logs PASS/FAIL/SKIP.
   - On **failure**: attaches screenshot, page source, and the exception text to Allure.
   - On **success**: attaches a final-state screenshot.
   - Closes `page` then `context` (frees the per-test isolation).
6. **`@AfterSuite closeBrowser()`** (once): closes `browser` and `playwright`.

**Q19. Why `alwaysRun = true` on the lifecycle methods?**
It guarantees setup/teardown runs even when groups/filters are applied or when a prior config method
fails — critical so the browser always launches and always closes (no leaked processes).

**Q20. Why is the browser launched once but the context created per test?**
Launching a browser is expensive (spawns a real engine); doing it per test would be slow. A
`BrowserContext` is lightweight and gives each test a clean cookie/storage jar — equivalent to a
fresh incognito window — preventing one test's login/state from leaking into the next.

**Q21. How does the framework capture diagnostics on failure?**
In `tearDown`, when `result.getStatus() == FAILURE`:
```java
AllureAttachmentHelper.attachScreenshot(page, "📸 Failure Screenshot");
AllureAttachmentHelper.attachPageSource(page, "📄 Page Source at Failure");
AllureAttachmentHelper.attachText("💥 Exception", result.getThrowable().toString());
```
So every failure in the report carries a screenshot, the full HTML at failure time, and the stack —
enough to triage without re-running.

**Q22. What's the purpose of the page event listeners attached in `createContext`?**
They surface browser-side problems in the test logs: `onPageError` logs uncaught JS errors,
`onConsoleMessage` mirrors console output by severity, and `onRequest`/`onResponse` trace network
(warning on HTTP ≥ 400). This turns the test log into a near-complete picture of what the page did.

---

## 6. Playwright Mechanics in This Framework

**Q23. What is a `BrowserContext` and how does it differ from a `Page`?**
A `BrowserContext` is an isolated session (cookies, storage, permissions) — like an incognito
profile. A `Page` is a single tab within a context. Multiple pages in one context share cookies;
different contexts share nothing. This suite uses one context → one page per test.

**Q24. How does Playwright's auto-waiting help here?**
Locator actions (`click`, `innerText`, `getAttribute`) automatically wait for the element to be
attached, visible, and stable (actionable) up to the timeout. That's why the codebase rarely needs
explicit sleeps — and where it does wait, it uses semantic waits like
`page.waitForURL("**/login/**")` or `locator.waitFor(state=VISIBLE)`.

**Q25. How does the framework set browser context options, and why these specific ones?**
```java
browser.newContext(new Browser.NewContextOptions()
    .setViewportSize(1440, 900)
    .setColorScheme(ColorScheme.DARK)
    .setIgnoreHTTPSErrors(true)
    .setLocale("en-IN")
    .setTimezoneId("Asia/Kolkata"));
```
- `en-IN` + `Asia/Kolkata` → the app is an Indian-market dashboard; locale/timezone affect the live
  IST clock, date logic, and number formatting being tested.
- `DARK` → the app's default theme; matches what users see.
- `ignoreHTTPSErrors` → tolerate cert issues on staging/local URLs.

**Q26. How are the three browsers selected at runtime?**
```java
browser = switch (TestConfig.BROWSER.toLowerCase()) {
    case "firefox" -> playwright.firefox().launch(opts);
    case "webkit"  -> playwright.webkit().launch(opts);
    default        -> playwright.chromium().launch(opts);
};
```
Driven by `-Dbrowser=...`. CI runs a 3-way matrix for cross-browser parity.

---

## 7. Authentication & Protected Pages

**Q27. Explain the authentication flow. Why log in via API instead of the UI?**
`loginAsTestViewer()` (in `@BeforeSuite`) uses Playwright's `APIRequestContext` to `POST` to
`/api/auth/login/` with the test-viewer credentials, reads the `set-cookie` header, extracts the
`emt_session` token, and builds a `Cookie` object. UI login per test would be slow and brittle;
logging in once via API and **injecting the cookie into each context** is fast and reliable.

**Q28. How is the session cookie reused across tests?**
The cookie is a `static` field set once at suite start. In `createContext`, if
`useAuthenticatedSession()` is true and the cookie exists, it's added via `context.addCookies(...)`.
Each fresh context starts already authenticated.

**Q29. How does a test opt out of being authenticated, and why would it?**
```java
protected boolean useAuthenticatedSession() { return true; }   // override → false
```
`LoginPageTest` overrides this to `false` because a valid session changes login-page behavior (it
may auto-redirect away or render differently) — to test the logged-out experience, the cookie must
not be injected.

**Q30. What do `dropAuthenticatedSession()` / `restoreAuthenticatedSession()` solve?**
A single test that loops over several URLs may need to visit the login page mid-test as a
logged-out user, then continue authenticated. `dropAuthenticatedSession()` clears cookies for that
stretch; `restoreAuthenticatedSession()` re-adds the session cookie afterward.

**Q31. What is the "page-access unlock window" and how does the framework handle it?**
Pages like indices/commodities/expert-view are gated client-side: the static HTML loads, then
`app.js`'s `guardProtectedPageAccess()` async-probes access and, if locked, redirects to `/login/`.
The framework handles this via `skipIfRedirectedToLogin(featureName)` — it gives the redirect a
~2s window to settle (`page.waitForURL("**/login/**", timeout=2000)`), and if the page ends up on
login it throws `SkipException` (a *skip*, not a *failure*) because that's expected behavior when
the window is closed.

**Q32. Why does the API login fail *gracefully* (return null) rather than throw?**
If no credentials are configured or login fails, `loginAsTestViewer()` logs a warning and returns
`null`. Protected-page tests then fall back to skipping (via the gate logic) instead of erroring out
the whole suite. The suite still runs all the public-page tests.

---

## 8. Reliability — Retries, Waits, Soft-Fail & Skips

**Q33. Why does `navigateTo` retry navigation, and how?**
`navigateWithRetry` retries `page.navigate(url)` up to 3 times with 400ms pauses. A prior in-flight
navigation (e.g. the previous test's redirect still resolving) can abort the new one with
`net::ERR_ABORTED`; a brief retry absorbs that race instead of failing.

**Q34. Why is `page.title()` wrapped in `titleWithRetry`?**
A redirect still settling (bare domain → www, or the auth guard bouncing to `/login/`) can destroy
the JS execution context, making `page.title()` throw "Execution context was destroyed."
`titleWithRetry` does `waitForLoadState()` + `title()` with up to 3 attempts.

**Q35. What's the difference between a skipped and a failed test here, and why does it matter?**
- **Skip** (`SkipException`): expected, non-defect conditions — login gate closed, API returns 401
  because the unlock window is closed, `market.json` not publicly served.
- **Fail** (assertion): a genuine product/logic defect.

Keeping these separate keeps the pass/fail signal trustworthy — infra/gating noise doesn't masquerade
as a bug.

**Q36. Explain `waitUntil(BooleanSupplier, timeoutMs)`. Why not rely on Playwright auto-wait?**
```java
protected void waitUntil(BooleanSupplier condition, int timeoutMs) {
    long deadline = System.currentTimeMillis() + timeoutMs;
    while (System.currentTimeMillis() < deadline) {
        if (condition.getAsBoolean()) return;
        page.waitForTimeout(500);
    }
}
```
Some async UI updates (Expert View dashboard/AI-systems sections unhiding *after* the first analysis
card renders) aren't tied to a single locator's visibility — they're a composite app-state. A custom
polling predicate handles "wait until this arbitrary condition is true," which auto-wait can't
express.

**Q37. How is "soft-fail" implemented for live-API-dependent tests?**
Tests that hit `/data/market.json` or live scans check the HTTP status; on a non-200 (or a 401),
they `log.warn(...)` and `return` early (or assert the status is within an accepted set including
`401`). Combined with CI `continue-on-error` on those steps, a third-party outage doesn't sink the
run.

---

## 9. API-Level Testing

**Q38. How does the framework test APIs, and why in the same suite as UI tests?**
Via Playwright's `APIRequestContext`:
```java
APIRequestContext api = playwright.request().newContext();
APIResponse response = api.get(TestConfig.BASE_URL + "/api/live-market",
        RequestOptions.create().setTimeout(TestConfig.SLOW_RESOURCE_TIMEOUT_MS));
```
The signal *logic* is best validated against raw JSON (exact numbers, all states), while the UI
tests validate *rendering*. Doing both in one suite means a single run proves the engine is correct
*and* correctly displayed. `api.dispose()` is always called to release the context.

**Q39. How is JSON parsed and validated?**
Jackson `ObjectMapper.readTree(body)` → `JsonNode`. Field presence is checked with `node.has(...)`,
and values read defensively with `path(...).asText("")` / `.asDouble(-1)` so a missing field yields
a sentinel rather than an NPE.

**Q40. Why accept HTTP 200, 304, *and* 401 in `testLiveMarketApiReturnsValidStructure`?**
`200` = fresh data, `304` = valid cached response, `401` = the API is behind the same unlock window
as protected pages (closed window, not a bug). Asserting `isIn(200, 304, 401)` distinguishes "broken
API" from "gated API."

---

## 10. Signal Logic — Business Validation

> These are the most important tests: they prove the trading-signal engine is *mathematically*
> correct, not just that pages render.

**Q41. What core financial rules does the suite validate?**

| Rule | Check |
|---|---|
| Support < Resistance | `supportBelowResistance(s, r)` for all 4 indices |
| Risk:Reward ≥ 2.2 | `calcRiskRewardRatio = |target−entry| / |entry−stop| ≥ 2.2` |
| Buy geometry (CE) | `stop < entry < target` |
| Sell geometry (PE) | `stop > entry > target` |
| Confidence ∈ [0,100] | `isConfidenceInRange` on UI and JSON |
| Valid signal state | state ∈ {Trade Signal, Watchlist Only, No Trade, Hold, Pending} |
| Session guard | if "2 completed trades" note → state ≠ Trade Signal |
| Forward record guard | if "net negative" → state ≠ Trade Signal |

**Q42. Walk through `testBuySignalGeometryFromJson`.**
It fetches `market.json`, iterates the four index keys, and only acts on entries whose
`signal.state == "Trade Signal"`. It parses entry/stop/target, then **branches on direction**: if
`target > entry` it's a buy and must satisfy `isValidBuySetup` + R:R ≥ 2.2; if `target < entry` it's
a sell and must satisfy `isValidSellSetup` + R:R ≥ 2.2. Each branch attaches a geometry report to
Allure. Entries with unpopulated/zero values are skipped (live data may be empty off-hours).

**Q43. How does `SignalParser.parsePrice` handle messy UI values like "₹23,622.90"?**
```java
String cleaned = text.replaceAll("[,₹\\s]", "");   // strip commas, ₹, whitespace
return Double.parseDouble(cleaned);
```
Returns `-1` for `null`, blank, or the placeholder `"--"`, so callers can treat `< 0` as
"not populated yet" and skip rather than fail on missing live data.

**Q44. How is the session guard tested without server internals?**
`testSessionGuardCompletedTradesLimit` reads the signal `note`/`summary` text. If it contains
"2 completed trades", it asserts the state is **not** "Trade Signal" — i.e. the daily 2-trade cap
must have blocked a fresh signal. It's a black-box check of an internal rule via its observable note.

**Q45. Why guard numeric checks with `if (sup > 0 && res > 0)` before asserting?**
Live dashboards may not have levels populated off-market-hours. Asserting on `-1` sentinels would
produce false failures. The pattern is "skip the numeric assertion when data isn't there, fail only
when present-and-wrong" — distinguishing absent data from invalid data.

---

## 11. Data-Driven Testing

**Q46. How is data-driven testing done, with an example?**
TestNG `@DataProvider` returns an `Object[][]`; the test runs once per row.
```java
@DataProvider(name = "indexData")
public Object[][] indexData() { return new Object[][]{ {"NIFTY", NIFTY_URL}, {"BANKNIFTY", BANKNIFTY_URL}, ... }; }

@Test(dataProvider = "indexData")
public void testIndexSupportBelowResistance(String name, String url) { ... }
```
Used for `testPublicPageLoads`, the index/commodity page tests, and
`testSearchExamplesUpdateStatus` (`@DataProvider(name="stockSymbols")` → Reliance, TCS, INFY, HDFC
Bank, SBIN). One method, many cases, each reported separately in Allure.

**Q47. What's the benefit over a `for` loop inside one test?**
Each data row is an independent reported result — one failing index doesn't hide the others, and the
report shows exactly which input failed. A loop would fail-fast on the first bad case and report a
single test.

---

## 12. Reporting — Allure

**Q48. How is the Allure hierarchy structured?**
`@Epic("EaseMyTrade")` → `@Feature("Signal Logic")` → `@Story("Trade Signal Geometry")` →
the `@Test` method → `@Step` breadcrumbs → attachments. This shows up in Allure's **Behaviors** tab
as a navigable tree.

**Q49. What does `AllureAttachmentHelper` provide?**
Typed attachment wrappers: `attachScreenshot` (image/png), `attachPageSource` (text/html),
`attachText` (text/plain for computed values/analysis), `attachJson` (application/json for API
bodies), and `addPageInfo(url,title)` on every navigation. Centralizing them keeps test code clean
and attachment types consistent.

**Q50. What is `categories.json` and what problem does it solve?**
It maps failure message/trace patterns to buckets (Product Defect, Signal Logic, Network/API,
Expert View, Navigation, Infrastructure, Skipped). In the Allure **Categories** tab you instantly
see "is this 5 product bugs or 5 network blips?" — triage by class of failure, not one-by-one.

**Q51. How does the trend graph work across runs?**
CI restores `allure-history/` from the `gh-pages` branch before generating the report and saves new
history after. With ≥2 runs on `main`, Allure renders pass/fail trend over time. (First run shows no
trend — there's no history yet.)

**Q52. How are run-specific details surfaced in the report?**
CI writes an `environment.properties` (Base URL, browser, Java version, CI run number/actor/branch/
commit, tool versions) into `allure-results` before report generation, populating Allure's
Environment panel.

---

## 13. Logging

**Q53. Describe the logging setup.**
SLF4J facade + Logback impl, two appenders: colored console and a rolling file
(`target/test-logs/test.log`, daily rotation, 14-day retention). Levels by namespace:
`com.easemytrade=DEBUG` (full traceability), `io.qameta.allure`/`org.testng=INFO`,
`com.microsoft.playwright=WARN` (suppress request noise), root `WARN`.

**Q54. What are the log prefix conventions and why use them?**
`[ACTION]`, `[STATE]`, `[WAIT]`, `[CHECK]`, `[READ]`, `[FLOW]`, `[PAGE]`, plus `NAV →`/`NAV ✓` and
`📸`. They make logs greppable and let you reconstruct a test's exact behavior from the file alone —
"what did it click, what did it read, where did it wait."

---

## 14. CI/CD — GitHub Actions

**Q55. What triggers the pipeline?**
`push` to `main`/`develop`, `pull_request` to `main`, a `schedule` cron `30 0 * * 1-5` (weekdays
06:00 IST), and `workflow_dispatch` (manual, with browser / base_url / run_cross_browser inputs).

**Q56. Walk through the `test` job steps.**
Checkout → set up JDK 17 → restore Maven cache → restore Allure history from `gh-pages` → install
Playwright browsers + system deps → write `environment.properties` & copy `categories.json` into
`allure-results` → `mvn test` → `mvn allure:report` → save new history → publish report to GitHub
Pages under `reports/{run-number}/` → upload artifacts (results, report, logs) → write a rich job
summary.

**Q57. When does the cross-browser matrix run?**
The `cross-browser` job (3× parallel: Chromium/Firefox/WebKit) runs when the `test` job passes and
`run_cross_browser=true`, **or** on any push to `main`. Each browser uploads its own results.

**Q58. Why install Playwright browsers in CI explicitly?**
The Playwright **Java dependency** doesn't bundle the browser binaries; `playwright install
--with-deps` downloads the engines and OS-level libs the runner needs. Without it, launch fails on a
clean runner.

---

## 15. Scenario / "What Would You Do" Questions

**Q59. A test is flaky — passes locally, fails ~20% in CI. How do you debug it with this framework?**
Open the Allure result: check the **failure screenshot** and **page source** (auto-attached), the
**console/network logs** from the page listeners, and the timing. Likely culprits given the design:
a race the retry/`waitUntil` helpers don't cover, the unlock-window redirect (should be a skip — is
`skipIfRedirectedToLogin` called?), or a live-data field not yet populated (should be guarded by
`> 0`). Reproduce with `-Dheadless=false` and `slowMo` to watch it.

**Q60. How would you add a test for a brand-new "Refresh Data" button?**
1. Add a `@Step` action + locator in the relevant page object.
2. Write a `@Test` with `@Story`/`@Severity`, calling the action, reading state, asserting with
   AssertJ, attaching results, and a screenshot.
3. If it's a new class, register it in `testng.xml`.
(Exactly the pattern in `FRAMEWORK_GUIDE.md` §16.)

**Q61. The signal engine changes the minimum R:R from 2.2 to 2.5. What's the change?**
One line: `MIN_RISK_REWARD_RATIO = 2.5` in `TestConfig`. Every R:R test references the constant, so
nothing else changes — this is the payoff of centralizing thresholds.

**Q62. How would you run only the signal-logic tests against a local build, non-headless, on Firefox?**
```bash
mvn test -Dtest=SignalLogicTest -Dapp.base.url=http://localhost:4173 -Dheadless=false -Dbrowser=firefox
```

**Q63. A reviewer says "your tests skip too much — they're hiding bugs." How do you respond?**
Skips are reserved for *expected, non-defect* states (login gate closed, API gated by unlock window,
static JSON not served) — provably distinct from assertion failures which mean defects. The Allure
Categories tab keeps "Skipped" visible as its own bucket, so skips are auditable, not hidden. If a
skip ever masks a real condition, the fix is to tighten the skip predicate, not to fail on infra.

**Q64. How would you make the suite resilient if `easemytrade.in` is fully down?**
Public-page tests would fail (correctly — the app is down). To distinguish "app down" from "test
bug," the navigation retry + failure attachments already capture the connection error. A pre-flight
health-check test (or CI step) hitting the home URL first could short-circuit the run with a clear
"target unreachable" signal rather than 75 confusing failures.

**Q65. Explain end-to-end what happens when you run `mvn test` on a fresh machine.**
Maven resolves dependencies → Surefire reads `testng.xml` → attaches the AspectJ agent → TestNG
runs suites in order → `@BeforeSuite` launches the browser + API-logs-in → for each test:
fresh context (authenticated if applicable) → navigate → act → assert → attach diagnostics →
close context → `@AfterSuite` closes the browser → Allure results land in `target/allure-results`,
viewable with `mvn allure:serve`.
