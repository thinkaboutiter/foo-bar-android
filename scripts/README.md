# Build & Test Scripts

This directory contains shell scripts for building the Android project and running tests.

## 📜 Available Scripts

### 🏗️ `build.sh`
Builds the Android project and verifies compilation.

**Usage:**
```bash
./scripts/build.sh
```

**What it does:**
- Cleans previous build artifacts
- Runs lint checks
- Builds debug APK
- Builds release APK
- Runs full project build
- Provides build summary with APK locations

### 🧪 `run-tests.sh`
Executes all unit tests and generates a detailed test report.

**Usage:**
```bash
./scripts/run-tests.sh
```

**What it does:**
- Runs all unit tests (domain, data, presentation, integration)
- Generates test execution report in Markdown format
- Provides test statistics and summary
- Saves report to `reports/unit-test-results.md`

### 📊 `coverage-report.sh`
Generates test coverage reports in multiple formats.

**Usage:**
```bash
./scripts/coverage-report.sh
```

**What it does:**
- Runs unit tests
- Generates Jacoco coverage report
- Converts coverage data to Markdown format
- Provides coverage analysis by architecture layer
- Saves report to `reports/coverage-report.md`
- Opens HTML report in browser (macOS)

## 📁 Output Locations

### Reports Directory (`/reports/`)
- `unit-test-results.md` - Test execution report
- `coverage-report.md` - Coverage analysis report

### Build Outputs (`/foobar/app/build/`)
- `outputs/apk/debug/app-debug.apk` - Debug APK
- `outputs/apk/release/app-release.apk` - Release APK
- `reports/tests/testDebugUnitTest/index.html` - HTML test report
- `reports/jacoco/jacocoTestReport/html/index.html` - HTML coverage report

## 🚀 Quick Start

1. **Build the project:**
   ```bash
   ./scripts/build.sh
   ```

2. **Run all tests:**
   ```bash
   ./scripts/run-tests.sh
   ```

3. **Generate coverage report:**
   ```bash
   ./scripts/coverage-report.sh
   ```

## 🔧 Prerequisites

- macOS or Linux environment
- Android SDK installed
- Java 11+ installed
- `bc` calculator utility (for coverage percentages)

## 📊 Test Coverage Targets

| Layer | Target Coverage | Current Status |
|-------|----------------|---------------|
| Domain Layer | 80%+ | ✅ (via data layer tests) |
| Data Layer | 70%+ | ✅ |
| Presentation Layer | 70%+ | ✅ |

## 🏗️ Architecture Testing

The scripts validate the Clean Architecture implementation:

- **Domain Layer:** Tested implicitly through data layer tests
- **Data Layer:** Repository, data sources, and parsers
- **Presentation Layer:** ViewModels and factories
- **Integration:** End-to-end data flow validation

## 🐛 Troubleshooting

### Script Permission Issues
```bash
chmod +x scripts/*.sh
```

### Gradle Wrapper Issues
```bash
cd foobar && chmod +x gradlew
```

### Missing Dependencies
Ensure all testing dependencies are installed:
- MockK
- kotlinx-coroutines-test
- Google Truth
- Turbine
- Robolectric

---

*These scripts are part of the Android Clean Architecture unit testing setup.*
