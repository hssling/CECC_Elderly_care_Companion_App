#!/bin/bash

# CECC Care Companion - Docker Build Script
# This script builds the Android project inside the Docker container

set -e

echo "🚀 Starting CECC Care Companion build in Docker..."
echo "Java version: $(java -version)"
echo "Android SDK: $ANDROID_HOME"

# Clean previous builds
echo "🧹 Cleaning previous builds..."
./gradlew clean

# Build the project
echo "🔨 Building the project..."
./gradlew build

# Assemble release APK
echo "📱 Assembling release APK..."
./gradlew assembleRelease

# Check if APK was created successfully
APK_PATH="app/build/outputs/apk/release/app-release.apk"
if [ -f "$APK_PATH" ]; then
    APK_SIZE=$(du -h "$APK_PATH" | cut -f1)
    echo "✅ Build successful!"
    echo "📦 Release APK created: $APK_PATH ($APK_SIZE)"
    echo "🔍 APK location: $(pwd)/$APK_PATH"
else
    echo "❌ Build failed - APK not found at $APK_PATH"
    exit 1
fi

# Optional: Install APK to connected device/emulator
if adb devices | grep -q "device$"; then
    echo "📲 Installing APK to connected device..."
    adb install -r "$APK_PATH"
    echo "✅ APK installed successfully!"
else
    echo "ℹ️  No Android device/emulator connected"
    echo "💡 To install the APK:"
    echo "   1. Connect an Android device or start an emulator"
    echo "   2. Run: adb install $APK_PATH"
fi

echo "🎉 Build completed successfully!"
