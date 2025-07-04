#!/bin/bash

# Android Project Build Script
# This script builds the Android project and verifies compilation

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Get the repository root directory
REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
FOOBAR_DIR="$REPO_ROOT/foobar"

echo -e "${BLUE}🏗️  Android Project Build Script${NC}"
echo -e "${BLUE}================================${NC}"
echo "Repository root: $REPO_ROOT"
echo "Project directory: $FOOBAR_DIR"
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

echo -e "${YELLOW}🧹 Cleaning previous build artifacts...${NC}"
./gradlew clean

echo -e "${YELLOW}🔍 Running lint checks...${NC}"
./gradlew lint

echo -e "${YELLOW}🏗️  Building debug APK...${NC}"
./gradlew assembleDebug

echo -e "${YELLOW}🏗️  Building release APK...${NC}"
./gradlew assembleRelease

echo -e "${YELLOW}📦 Running full project build...${NC}"
./gradlew build

echo ""
echo -e "${GREEN}✅ Build completed successfully!${NC}"
echo ""
echo -e "${BLUE}📊 Build Summary:${NC}"
echo "• Clean: ✅"
echo "• Lint: ✅"
echo "• Debug APK: ✅"
echo "• Release APK: ✅"
echo "• Full Build: ✅"
echo ""

# Check if APKs were generated
DEBUG_APK="$FOOBAR_DIR/app/build/outputs/apk/debug/app-debug.apk"
RELEASE_APK="$FOOBAR_DIR/app/build/outputs/apk/release/app-release.apk"

if [ -f "$DEBUG_APK" ]; then
    echo -e "${GREEN}📱 Debug APK generated: ${DEBUG_APK}${NC}"
else
    echo -e "${YELLOW}⚠️  Debug APK not found${NC}"
fi

if [ -f "$RELEASE_APK" ]; then
    echo -e "${GREEN}📱 Release APK generated: ${RELEASE_APK}${NC}"
else
    echo -e "${YELLOW}⚠️  Release APK not found${NC}"
fi

echo ""
echo -e "${GREEN}🎉 Project build verification complete!${NC}"
