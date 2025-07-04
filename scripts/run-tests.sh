#!/bin/bash

# Android Unit Test Execution Script
# This script runs all unit tests and provides detailed reporting

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

# Get the repository root directory
REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
FOOBAR_DIR="$REPO_ROOT/foobar"
REPORTS_DIR="$REPO_ROOT/reports"

echo -e "${BLUE}🧪 Android Unit Test Execution Script${NC}"
echo -e "${BLUE}====================================${NC}"
echo "Repository root: $REPO_ROOT"
echo "Project directory: $FOOBAR_DIR"
echo "Reports directory: $REPORTS_DIR"
echo ""

# Check if we're in the right directory
if [ ! -d "$FOOBAR_DIR" ]; then
    echo -e "${RED}❌ Error: foobar directory not found at $FOOBAR_DIR${NC}"
    exit 1
fi

# Change to project directory
cd "$FOOBAR_DIR"

# Check if gradlew exists
if [ ! -f "./gradlew" ]; then
    echo -e "${RED}❌ Error: gradlew not found in $FOOBAR_DIR${NC}"
    exit 1
fi

# Make gradlew executable
chmod +x ./gradlew

# Function to run tests with error handling
run_test_suite() {
    local test_type="$1"
    local gradle_task="$2"
    local description="$3"
    
    echo -e "${YELLOW}🔬 Running $description...${NC}"
    
    if ./gradlew "$gradle_task" --continue; then
        echo -e "${GREEN}✅ $description completed successfully${NC}"
        return 0
    else
        echo -e "${RED}❌ $description failed${NC}"
        return 1
    fi
}

# Initialize test results
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0
TEST_RESULTS=""

echo -e "${PURPLE}🧹 Cleaning previous test results...${NC}"
./gradlew clean

echo ""
echo -e "${PURPLE}📋 Test Execution Plan:${NC}"
echo "1. Domain layer tests (via data layer)"
echo "2. Data layer tests"
echo "3. Presentation layer tests"
echo "4. Integration tests"
echo ""

# Run unit tests
echo -e "${YELLOW}🔬 Executing all unit tests...${NC}"
TEST_START_TIME=$(date +%s)

if ./gradlew testDebugUnitTest --continue; then
    echo -e "${GREEN}✅ Unit tests completed successfully${NC}"
    TEST_SUCCESS=true
else
    echo -e "${RED}❌ Some unit tests failed${NC}"
    TEST_SUCCESS=false
fi

TEST_END_TIME=$(date +%s)
TEST_DURATION=$((TEST_END_TIME - TEST_START_TIME))

echo ""
echo -e "${BLUE}📊 Test Execution Summary${NC}"
echo -e "${BLUE}=========================${NC}"
echo "Duration: ${TEST_DURATION}s"

# Check for test results
TEST_RESULTS_DIR="$FOOBAR_DIR/app/build/test-results/testDebugUnitTest"
if [ -d "$TEST_RESULTS_DIR" ]; then
    echo -e "${GREEN}📄 Test results available at: $TEST_RESULTS_DIR${NC}"
    
    # Count test results if XML files exist
    if ls "$TEST_RESULTS_DIR"/*.xml 1> /dev/null 2>&1; then
        # Parse XML results to get test counts
        for xml_file in "$TEST_RESULTS_DIR"/*.xml; do
            if [ -f "$xml_file" ]; then
                # Extract test counts from XML (simplified parsing)
                tests=$(grep -o 'tests="[0-9]*"' "$xml_file" | grep -o '[0-9]*' || echo "0")
                failures=$(grep -o 'failures="[0-9]*"' "$xml_file" | grep -o '[0-9]*' || echo "0")
                
                TOTAL_TESTS=$((TOTAL_TESTS + tests))
                FAILED_TESTS=$((FAILED_TESTS + failures))
            fi
        done
        PASSED_TESTS=$((TOTAL_TESTS - FAILED_TESTS))
    fi
fi

# Generate simple test report
TIMESTAMP=$(date '+%Y-%m-%d %H:%M:%S')
TEST_REPORT="$REPORTS_DIR/unit-test-results.md"

mkdir -p "$REPORTS_DIR"

cat > "$TEST_REPORT" << EOF
# Unit Test Execution Report

**Generated:** $TIMESTAMP  
**Duration:** ${TEST_DURATION}s  
**Status:** $(if [ "$TEST_SUCCESS" = true ]; then echo "✅ PASSED"; else echo "❌ FAILED"; fi)

## Test Statistics

| Metric | Count |
|--------|-------|
| Total Tests | $TOTAL_TESTS |
| Passed | $PASSED_TESTS |
| Failed | $FAILED_TESTS |
| Success Rate | $(if [ $TOTAL_TESTS -gt 0 ]; then echo "scale=1; $PASSED_TESTS * 100 / $TOTAL_TESTS" | bc; else echo "0"; fi)% |

## Test Suites

### ✅ Data Layer Tests
- CarRepositoryTest
- CarNetworkJsonParserTest  
- CarNetworkDataSourceTest
- CarMockNetworkDatasourceTest
- RepositoryStrategyTest

### ✅ Presentation Layer Tests
- CarListViewModelTest
- CarListViewModelFactoryTest

### ✅ Integration Tests
- DataFlowIntegrationTest

## Test Execution Details

- **Project:** foo-bar-android
- **Test Framework:** JUnit 4
- **Mocking Framework:** MockK
- **Assertion Library:** Google Truth
- **Coroutines Testing:** kotlinx-coroutines-test

## Files Tested

### Domain Layer (tested via data layer)
- \`CarApp.kt\` - Application entity mapping
- \`CarLocal.kt\` - Local database entity
- \`CarNetwork.kt\` - Network response entity

### Data Layer
- \`CarRepository.kt\` - Repository implementation
- \`CarNetworkJsonParser.kt\` - JSON parsing logic
- \`CarNetworkDataSource.kt\` - Network data source
- \`CarMockNetworkDatasource.kt\` - Mock data source

### Presentation Layer
- \`CarListViewModel.kt\` - ViewModel logic
- \`CarListViewModelFactory.kt\` - Factory pattern

## Additional Information

- Test results location: \`$TEST_RESULTS_DIR\`
- HTML reports: \`$FOOBAR_DIR/app/build/reports/tests/testDebugUnitTest/index.html\`
- Coverage reports: Use \`./scripts/coverage-report.sh\` to generate
EOF

echo ""
echo -e "${GREEN}📄 Test report generated: $TEST_REPORT${NC}"

# Display summary
echo ""
echo -e "${BLUE}🎯 Final Results:${NC}"
if [ $TOTAL_TESTS -gt 0 ]; then
    echo "Total Tests: $TOTAL_TESTS"
    echo "Passed: $PASSED_TESTS"
    echo "Failed: $FAILED_TESTS"
    echo "Success Rate: $(echo "scale=1; $PASSED_TESTS * 100 / $TOTAL_TESTS" | bc)%"
else
    echo "Test count information not available"
fi

echo ""
if [ "$TEST_SUCCESS" = true ]; then
    echo -e "${GREEN}🎉 All unit tests completed successfully!${NC}"
    exit 0
else
    echo -e "${RED}💥 Some tests failed. Check the reports for details.${NC}"
    exit 1
fi
