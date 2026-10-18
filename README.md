# Noon E-Commerce Test Automation

An end-to-end UI test automation project for [noon.com](https://www.noon.com) (Egypt), built with **Java 26**, **Selenium WebDriver**, and **TestNG** as a **Maven** project. It covers product filtering, cart behavior with budget constraints, and negative search scenarios.

---

## Demo

> 🎥 **Screen recording:** 


https://github.com/user-attachments/assets/8f65eb5c-79f6-4e2a-a6c0-c1ca3a7fa8b0



## Test Scenarios

| # | Scenario | Description |
|---|----------|-------------|
| 1 | **Filter by Price** | Open the Electronics category, select the Samsung brand, apply a min/max price range, and verify every displayed product falls inside the range. |
| 2 | **Filter by Rate** | Apply a minimum product rating using the rating slider and verify all products meet the target rating. |
| 3 | **Filter by Price + Rate** | Apply both filters together and verify each product satisfies both conditions (using `SoftAssert` so all failures are reported in one run). |
| 4 | **Add to Cart within Budget** | Add products to the cart while keeping the total **within a 10,000 EGP budget**, restricted to products with **free shipping**, then verify the cart contents and total. |
| 5 | **Invalid Search Keywords** | Data-driven negative test: search with invalid or nonsense keywords and verify the "no results" behavior. |

---

## Tech Stack

| Tool / Library | Version | Purpose |
|----------------|---------|---------|
| Java | 26 | Programming language |
| Maven | – | Build and dependency management |
| Selenium | 4.48.0 | Browser automation |
| TestNG | 7.12.0 | Test framework, assertions, and data providers |
| Gson | 2.14.0 | Reading JSON test data and config |
| Apache POI | 5.5.1 | Excel file handling |
| Apache POI OOXML | 5.5.1 | `.xlsx` support |
| OpenCSV | 5.12.0 | Reading CSV test data (e.g. invalid search keywords) |
| Log4j2 (core + api) | 2.26.1 | Logging |
| Allure TestNG | 2.35.5 | Test reporting |

---

## Project Structure

```
noon-automation/
├── src/
│   ├── main/java/
│   │   ├── pages/                # Page Object Model classes
│   │   │   └── filterProducts/   # FilterProductPage, ProductInfo, ...
│   │   └── utils/                # Config handler, JSON/CSV/Excel readers, helpers
│   └── test/
│       ├── java/
│       │   ├── baseTest/         # BaseTest (driver setup / teardown)
│       │   └── filterProductsTest/
│       └── resources/            # config.properties, JSON & CSV test data, log4j2.xml
├── pom.xml
└── README.md
```


## Design Highlights

- **Page Object Model (POM)** to keep locators and page actions separate from the test logic.
- **Data-driven testing** using JSON (price range,invalid search keyword) and CSV (noon products) and properties file for small data like (url, browser name ).
- **Soft assertions** so a single run reports every failing product, not just the first.
- **Logging** with Log4j2 and **rich reporting** with Allure.
  
## Challenges and Solutions

This project had a number of real-world automation challenges, mostly around the price and rating filter widgets.

### 1. Entering the min/max price
The price inputs did not always accept typed values reliably. Several approaches were tried:
- Scrolling to the min input field and typing directly
- Selenium `Actions` class with keyboard actions 
- Click and hold interactions

### 2. Controlling the rating slider
The rating slider is a custom component that does not respond well to normal `sendKeys` or drag interactions. Approaches tried:
- Keyboard actions (arrow keys) on the slider handle
- Click and hold with offset movement
- **JavaScript execution** to set and move the slider value directly, which was the most reliable approach

### 3. Scrolling to a specific brand
The brand list lives in a scrollable sidebar, so the brand checkbox is often not visible. The fix was to scroll the element into view with JavaScript (`scrollIntoView`) before clicking.

### 4. Dynamic content and flaky elements
Filters reload the product grid asynchronously, which causes stale elements. Explicit waits and re-locating elements after each filter action solved most of this.

---

## Prerequisites

- **JDK 26** installed and `JAVA_HOME` configured
- **Maven** installed
- **Google Chrome** (matching the Selenium-managed driver)
- **Allure Commandline** (only needed to view reports)

Verify your setup:

```bash
java -version
mvn -version
allure --version
```
## How to Run By CMD(Command Line)

### 1. Clone the repository

```bash
git clone <(https://github.com/Salmakhaled123/Noon)>
cd noon
```

### 2. Run the whole project

```bash
mvn clean install
```
This compiles the project, runs all the tests, and generates the Allure results in `target/allure-results`.

## Allure Report

After the test run, generate the report:

```bash
allure generate target/allure-results --clean -o allure-report
```

Then open it in your browser:

```bash
allure open allure-report
```

## Notes

- Test data (price range, keywords) is stored in external JSON/CSV files so it can be changed without touching the code.
- Because the target site is live, product availability, prices, and page behavior can change and may affect results.
- This project is for learning and practice purposes only.

