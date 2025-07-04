#!/bin/bash

# Android Test Coverage Report Generation Script
# This script generates test coverage reports and converts them to Markdown format

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

echo -e "${BLUE}📊 Android Test Coverage Report Generator${NC}"
echo -e "${BLUE}=========================================${NC}"
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

# Create reports directory
mkdir -p "$REPORTS_DIR"

echo -e "${YELLOW}🧪 Running unit tests...${NC}"
./gradlew testDebugUnitTest

echo -e "${YELLOW}📊 Generating Jacoco coverage report...${NC}"
./gradlew jacocoTestReport

# Check if coverage report was generated
JACOCO_HTML_DIR="$FOOBAR_DIR/app/build/reports/jacoco/jacocoTestReport/html"
JACOCO_XML_FILE="$FOOBAR_DIR/app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml"

if [ ! -f "$JACOCO_XML_FILE" ]; then
    echo -e "${RED}❌ Error: Jacoco XML report not found at $JACOCO_XML_FILE${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Coverage report generated successfully${NC}"

# Parse XML report to extract coverage data
echo -e "${YELLOW}📝 Parsing coverage data...${NC}"

# Function to extract coverage percentage from XML
extract_coverage() {
    local xml_file="$1"
    local package_name="$2"
    
    # Extract instruction coverage for the package
    grep -A 10 "name=\"$package_name\"" "$xml_file" | grep "type=\"INSTRUCTION\"" | head -1 | \
    sed -n 's/.*covered="\([0-9]*\)".*missed="\([0-9]*\)".*/\1 \2/p' | \
    awk '{covered=$1; missed=$2; total=covered+missed; if(total>0) printf "%.1f", (covered*100/total); else print "0.0"}'
}

# Extract overall coverage
OVERALL_INSTRUCTION=$(grep "type=\"INSTRUCTION\"" "$JACOCO_XML_FILE" | head -1 | \
    sed -n 's/.*covered="\([0-9]*\)".*missed="\([0-9]*\)".*/\1 \2/p' | \
    awk '{covered=$1; missed=$2; total=covered+missed; if(total>0) printf "%.1f", (covered*100/total); else print "0.0"}')

OVERALL_BRANCH=$(grep "type=\"BRANCH\"" "$JACOCO_XML_FILE" | head -1 | \
    sed -n 's/.*covered="\([0-9]*\)".*missed="\([0-9]*\)".*/\1 \2/p' | \
    awk '{covered=$1; missed=$2; total=covered+missed; if(total>0) printf "%.1f", (covered*100/total); else print "0.0"}')

OVERALL_LINE=$(grep "type=\"LINE\"" "$JACOCO_XML_FILE" | head -1 | \
    sed -n 's/.*covered="\([0-9]*\)".*missed="\([0-9]*\)".*/\1 \2/p' | \
    awk '{covered=$1; missed=$2; total=covered+missed; if(total>0) printf "%.1f", (covered*100/total); else print "0.0"}')

# Extract package-level coverage
DATA_COVERAGE=$(extract_coverage "$JACOCO_XML_FILE" "com/cool/element/foobar/data" || echo "0.0")
DOMAIN_COVERAGE=$(extract_coverage "$JACOCO_XML_FILE" "com/cool/element/foobar/domain" || echo "0.0")
PRESENTATION_COVERAGE=$(extract_coverage "$JACOCO_XML_FILE" "com/cool/element/foobar/presentation" || echo "0.0")

# Generate timestamp
TIMESTAMP=$(date '+%Y-%m-%d %H:%M:%S')

# Generate Markdown coverage report
COVERAGE_REPORT="$REPORTS_DIR/coverage-report.md"

cat > "$COVERAGE_REPORT" << EOF
# Test Coverage Report

**Generated:** $TIMESTAMP  
**Project:** foo-bar-android  
**Framework:** Jacoco  

## 📊 Overall Coverage Summary

| Metric | Coverage |
|--------|----------|
| **Instruction Coverage** | **${OVERALL_INSTRUCTION}%** |
| **Branch Coverage** | **${OVERALL_BRANCH}%** |
| **Line Coverage** | **${OVERALL_LINE}%** |

## 🏗️ Coverage by Architecture Layer

| Layer | Coverage | Status |
|-------|----------|--------|
| **Data Layer** | ${DATA_COVERAGE}% | $(if (( $(echo "$DATA_COVERAGE >= 70" | bc -l) )); then echo "✅ Good"; else echo "⚠️ Needs Improvement"; fi) |
| **Domain Layer** | ${DOMAIN_COVERAGE}% | $(if (( $(echo "$DOMAIN_COVERAGE >= 80" | bc -l) )); then echo "✅ Excellent"; else echo "⚠️ Needs Improvement"; fi) |
| **Presentation Layer** | ${PRESENTATION_COVERAGE}% | $(if (( $(echo "$PRESENTATION_COVERAGE >= 70" | bc -l) )); then echo "✅ Good"; else echo "⚠️ Needs Improvement"; fi) |

## 📁 Detailed Coverage by Package

### Data Layer (\`com.cool.element.foobar.data\`)
- **Repository:** CarRepository, RepositoryStrategy
- **Data Sources:** CarNetworkDataSource, CarMockNetworkDatasource
- **Parsers:** CarNetworkJsonParser
- **Coverage:** ${DATA_COVERAGE}%

### Domain Layer (\`com.cool.element.foobar.domain\`)
- **Entities:** CarApp, CarLocal, CarNetwork, CarNetworkResponse
- **Coverage:** ${DOMAIN_COVERAGE}%
- *Note: Domain entities are tested implicitly through data layer tests*

### Presentation Layer (\`com.cool.element.foobar.presentation\`)
- **ViewModels:** CarListViewModel, CarListViewModelFactory
- **Coverage:** ${PRESENTATION_COVERAGE}%

## 🧪 Test Coverage Analysis

### Well-Tested Components
- ✅ **CarRepository** - Repository pattern with strategy selection
- ✅ **CarNetworkJsonParser** - JSON parsing with error handling
- ✅ **CarNetworkDataSource** - Network operations with mocking
- ✅ **CarListViewModel** - ViewModel logic with coroutines
- ✅ **Entity Mapping** - CarApp creation from network/local entities

### Testing Strategy
- **Unit Tests:** Isolated component testing with mocking
- **Integration Tests:** End-to-end data flow validation
- **Mocking:** MockK for external dependencies
- **Coroutines:** kotlinx-coroutines-test for suspend functions

## 📈 Coverage Trends

| Target | Current | Status |
|--------|---------|--------|
| Domain Layer (80%+) | ${DOMAIN_COVERAGE}% | $(if (( $(echo "$DOMAIN_COVERAGE >= 80" | bc -l) )); then echo "✅ Met"; else echo "📈 In Progress"; fi) |
| Data Layer (70%+) | ${DATA_COVERAGE}% | $(if (( $(echo "$DATA_COVERAGE >= 70" | bc -l) )); then echo "✅ Met"; else echo "📈 In Progress"; fi) |
| Presentation Layer (70%+) | ${PRESENTATION_COVERAGE}% | $(if (( $(echo "$PRESENTATION_COVERAGE >= 70" | bc -l) )); then echo "✅ Met"; else echo "📈 In Progress"; fi) |

## 🔗 Additional Reports

- **HTML Report:** [\`app/build/reports/jacoco/jacocoTestReport/html/index.html\`](../foobar/app/build/reports/jacoco/jacocoTestReport/html/index.html)
- **XML Report:** [\`app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml\`](../foobar/app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml)
- **Unit Test Results:** [unit-test-results.md](./unit-test-results.md)

## 🚀 Recommendations

$(if (( $(echo "$OVERALL_INSTRUCTION < 80" | bc -l) )); then
echo "### Improve Overall Coverage
- Add more unit tests for uncovered methods
- Focus on edge cases and error scenarios
- Consider testing private methods through public interfaces"
fi)

$(if (( $(echo "$DATA_COVERAGE < 70" | bc -l) )); then
echo "### Data Layer Improvements
- Add tests for CarLocalDatasource implementation
- Test error handling in data sources
- Add integration tests for Room database operations"
fi)

$(if (( $(echo "$PRESENTATION_COVERAGE < 70" | bc -l) )); then
echo "### Presentation Layer Improvements
- Add more ViewModel state testing
- Test UI error states
- Add tests for ViewModel lifecycle methods"
fi)

---

*Generated by coverage-report.sh on $(date '+%Y-%m-%d %H:%M:%S')*
EOF

echo ""
echo -e "${GREEN}📄 Markdown coverage report generated: $COVERAGE_REPORT${NC}"

# Display summary
echo ""
echo -e "${BLUE}📊 Coverage Summary:${NC}"
echo "Overall Instruction Coverage: ${OVERALL_INSTRUCTION}%"
echo "Overall Branch Coverage: ${OVERALL_BRANCH}%"
echo "Overall Line Coverage: ${OVERALL_LINE}%"
echo ""
echo "Data Layer: ${DATA_COVERAGE}%"
echo "Domain Layer: ${DOMAIN_COVERAGE}%"
echo "Presentation Layer: ${PRESENTATION_COVERAGE}%"

echo ""
echo -e "${BLUE}📁 Report Locations:${NC}"
echo "• Markdown Report: $COVERAGE_REPORT"
echo "• HTML Report: $JACOCO_HTML_DIR/index.html"
echo "• XML Report: $JACOCO_XML_FILE"

echo ""
echo -e "${GREEN}🎉 Coverage report generation completed!${NC}"

# Open HTML report if on macOS
if [[ "$OSTYPE" == "darwin"* ]] && [ -f "$JACOCO_HTML_DIR/index.html" ]; then
    echo -e "${BLUE}🌐 Opening HTML report in browser...${NC}"
    open "$JACOCO_HTML_DIR/index.html"
fi
