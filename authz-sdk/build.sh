#!/bin/bash
set -e

### 🔐 CREDENTIALS (Hardcoded - DO NOT COMMIT THIS FILE)
export SONATYPE_USERNAME="A8c6IO"
export SONATYPE_PASSWORD="2DNK3JqefEpJhUYi9Yy75PGqt9eWFBAOV"
export GPG_PASSPHRASE="iliveingurugram"

### 🧱 Version bump type (default: patch)
VERSION_TYPE=${1:-patch}

# Get current version from pom.xml
CURRENT_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
IFS='.' read -r MAJOR MINOR PATCH <<< "$CURRENT_VERSION"

case $VERSION_TYPE in
  major)
    ((MAJOR++)); MINOR=0; PATCH=0 ;;
  minor)
    ((MINOR++)); PATCH=0 ;;
  patch)
    ((PATCH++)) ;;
  *)
    echo "Usage: ./build.sh [major|minor|patch]"
    exit 1 ;;
esac

NEW_VERSION="$MAJOR.$MINOR.$PATCH"
echo "🔢 Updating version: $CURRENT_VERSION → $NEW_VERSION"

# Update version in pom.xml
mvn versions:set -DnewVersion=$NEW_VERSION -q

echo "🚀 Building and deploying version $NEW_VERSION (tests skipped)..."

# Deploy to Maven Central (staging)
mvn clean deploy -P release -DskipTests \
  -Dgpg.passphrase="$GPG_PASSPHRASE" \
  --settings settings.xml

echo "✅ Successfully published version $NEW_VERSION"