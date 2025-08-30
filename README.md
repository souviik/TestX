# Test Automation Framework

A comprehensive test automation framework built with Java, Selenium WebDriver, Cucumber BDD, and TestNG. This framework supports both Web UI automation and REST API testing with robust reporting and parallel execution capabilities.

## Features

- Web UI automation using Selenium WebDriver 4
- REST API testing with REST Assured
- Behavior-driven development with Cucumber and Gherkin
- TestNG for test execution and parallel testing
- Page Object Model design pattern
- Cross-browser testing support (Chrome, Firefox, Edge)
- Multiple reporting formats (Extent, Allure, Cucumber HTML)
- Data-driven testing with Excel, JSON, and properties files
- Comprehensive logging and screenshot capture
- Exception handling and test recovery mechanisms

## System Requirements

- Java JDK 17 or higher
- Maven 3.6 or higher
- Chrome, Firefox, or Edge browsers installed
- Git for version control

### Optional Requirements
- Allure command line tool for advanced reporting
- IntelliJ IDEA (recommended IDE)

## Project Setup

### 1. Maven Configuration
The project uses Maven for dependency management and build lifecycle. All dependencies are defined in `pom.xml` including:
- Selenium WebDriver 4.32.0
- Cucumber 7.22.2
- TestNG 7.11.0
- REST Assured 5.5.2
- Extent Reports 5.1.2
- Allure TestNG 2.29.1

### 2. Driver Management
The framework implements a factory pattern for WebDriver management:
- **BrowserFactory**: Creates browser-specific driver instances
- **DriverManager**: Manages WebDriver lifecycle with ThreadLocal support
- **TargetFactory**: Handles local and remote execution configurations

### 3. Configuration Management
Configuration is managed through properties files:
- `config/config.properties`: Framework settings, timeouts, reporting options
- `config/data.properties`: Test environment data
- `objects/ebay_locators.properties`: Web UI element locators
- `objects/coingecko_api_endpoints.properties`: API endpoint configurations

## Framework Architecture

### Page Object Model (POM)
Web UI tests follow the Page Object Model pattern:
- **Page Classes**: Contain element locators and page-specific methods
- **Step Definitions**: Cucumber step implementations that interact with page objects
- **Test Runners**: Configure and execute feature files

### Reusable Utilities & Helpers
Common functionality is centralized in utility classes:
- **WebUI**: Main keyword common library for web interactions
- **PropertiesHelpers**: Configuration file management
- **ExcelHelpers**: for DataProviders in featuresuites
- **APIHelpers**: for API interactions
- **WebDriverHelpers**: for WebDriver management
- **CaptureHelpers**: Screenshot for reports 
- **LogUtils**: Logging functionality

### Test Data Management
The framework supports multiple data sources:
- Excel files (.xlsx) for tabular test data in feature files
- JSON files for structured test data
- Properties files for configuration data
- Database connections for dynamic data

### Assertions & Validations
Built-in validation methods provide comprehensive test verification:
- Element presence and visibility checks
- Text and attribute validations
- API response validations
- Custom assertion methods with detailed reporting

### Reporting System
Multiple reporting formats are supported:
- **Extent Reports**: Rich HTML reports with screenshots
- **Allure Reports**: Interactive test reporting with trends
- **Cucumber Reports**: Standard HTML and JSON reports
- **Custom Logging**: Detailed execution logs

### Exception Handling & Recovery
Robust error handling ensures test stability:
- Custom exception classes for specific failures
- Retry mechanisms for flaky test scenarios
- Graceful error recovery with detailed logging
- Screenshot capture on test failures

### Parallel Execution
The framework supports parallel test execution:
- TestNG parallel execution at method and class level
- Thread-safe WebDriver management
- Configurable thread counts and execution strategies

### Cross-Browser & Cross-Platform Testing
Multi-browser support through configuration:
- Chrome (default), Firefox, Edge browsers
- Headless execution mode available
- Remote execution support for CI/CD pipelines

## Test Organization

### Web UI Tests
Web UI tests are organized under `src/test/java/com/sdastest/projects/website/`:
- **Pages**: Page object classes containing element locators and methods
- **Step Definitions**: Cucumber step implementations
- **Features**: Gherkin feature files in `src/test/resources/features/`

#### Example: eBay Test Structure
```
website/ebay/
├── pages/
│   ├── EbayHomePage.java
│   ├── EbaySearchResultsPage.java
│   └── EbayProductPage.java
└── stepdefinitions/
    └── EbaySteps.java
```

### API Tests
API tests are organized under `src/test/java/com/sdastest/projects/api/`:
- **Base**: Common API client functionality
- **Client**: Service-specific API clients
- **Step Definitions**: Cucumber steps for API scenarios

#### Example: CoinGecko API Test Structure
```
api/coingecko/
├── client/
│   └── CoinGeckoAPIClient.java
└── stepdefinitions/
    └── CoinGeckoSteps.java
```

### Test Runners
Test execution is configured through runner classes in `src/test/java/com/sdastest/runners/`:
- **TestRunnerEbay.java**: Web UI test execution
- **TestRunnerCoinGecko.java**: API test execution

## Getting Started

### Running Tests

1. **Run all tests:**
   ```bash
   mvn clean test
   ```

2. **Run specific browser:**
   ```bash
   mvn clean test -Dbrowser=chrome
   mvn clean test -Dbrowser=firefox
   mvn clean test -Dbrowser=edge
   ```

3. **Run specific test suite:**
   ```bash
   mvn test -Dtest=TestRunnerEbay
   mvn test -Dtest=TestRunnerCoinGecko
   ```

4. **Run with custom suite files:**
   ```bash
   mvn test -DsuiteXmlFile=src/test/resources/suites/SuiteFeatureEbay.xml
   ```

### Viewing Reports

1. **Extent Reports:**
   - Location: `exports/reports/CucumberExtentReports/CucumberExtentReports.html`
   - Open in browser after test execution

2. **Allure Reports:**
   ```bash
   allure serve target/allure-results
   ```

3. **Cucumber Reports:**
   - Generated automatically in `target/cucumber-reports/`

### Configuration

Key configuration options in `config/config.properties`:
- `BROWSER`: Default browser (chrome/firefox/edge)
- `HEADLESS`: Enable headless execution (true/false)
- `SCREENSHOT_FAILED_STEPS`: Capture screenshots on failures
- `VIDEO_RECORD`: Enable video recording
- `WAIT_EXPLICIT`: Default explicit wait timeout

## Project Structure

```
TestX/
├── src/
│   ├── main/java/com/sdastest/
│   │   ├── config/             # Configuration management
│   │   ├── constants/          # Framework constants
│   │   ├── driver/            # WebDriver management
│   │   ├── helpers/           # Utility helper classes
│   │   ├── keywords/          # Main WebUI keyword library
│   │   ├── reports/           # Reporting utilities
│   │   └── utils/             # Common utility classes
│   └── test/
│       ├── java/com/sdastest/
│       │   ├── common/         # Base test classes
│       │   ├── hooks/          # Cucumber hooks
│       │   ├── listeners/      # TestNG listeners
│       │   ├── projects/       # Test implementations
│       │   │   ├── api/        # API test projects
│       │   │   └── website/    # Web UI test projects
│       │   └── runners/        # Test runner classes
│       └── resources/
│           ├── config/         # Configuration files
│           ├── features/       # Cucumber feature files
│           ├── objects/        # Element locators and endpoints
│           ├── suites/         # TestNG suite XML files
│           └── testdata/       # Test data files
├── target/                     # Build outputs and reports
├── pom.xml                     # Maven configuration
└── README.md                   # Project documentation
```

## Version Control

This project uses Git for version control. Key practices:
- Feature branches for new test implementations
- Descriptive commit messages following conventional commits
- Regular integration with main branch
- CI/CD integration support can be configured through Maven profiles
