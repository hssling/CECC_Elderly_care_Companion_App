#!/bin/bash

echo "========================================"
echo "CECC Care Companion - Environment Setup"
echo "========================================"
echo

# Function to check command existence
check_command() {
    if ! command -v $1 &> /dev/null; then
        echo "❌ $1 not found!"
        return 1
    else
        echo "✅ $1 found"
        return 0
    fi
}

echo "Step 1: Checking Java installation..."
if check_command java; then
    echo "Java version: $(java -version 2>&1 | head -n1)"
    echo "JAVA_HOME: $JAVA_HOME"
fi

echo
echo "Step 2: Checking Android Studio..."
if [ -d "/Applications/Android Studio.app" ] || [ -d "$HOME/Android/Sdk" ]; then
    echo "✅ Android Studio/SDK found"
else
    echo "❌ Android Studio not found!"
    echo "Please install Android Studio 2023.3+ from: https://developer.android.com/studio"
    exit 1
fi

echo
echo "Step 3: Setting environment variables..."
export JAVA_HOME=${JAVA_HOME:-$(dirname $(dirname $(readlink -f $(which java))))}
export ANDROID_HOME=${ANDROID_HOME:-$HOME/Android/Sdk}
export PATH=$JAVA_HOME/bin:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$PATH

echo "JAVA_HOME: $JAVA_HOME"
echo "ANDROID_HOME: $ANDROID_HOME"

echo
echo "Step 4: Building APK..."
echo "========================================"
echo "Build Instructions:"
echo "========================================"
echo
echo "1. Open Android Studio"
echo "2. File → Open → Select CECCCareCompanion/ folder"
echo "3. Wait for Gradle sync to complete (5-10 minutes)"
echo "4. Build → Build Bundle(s)/APK(s) → Build APK(s)"
echo "5. APK location: app/build/outputs/apk/debug/app-debug.apk"
echo
echo "Expected APK size: 15-25 MB"
echo "Build time: 2-5 minutes"
echo
echo "Alternative command line build:"
echo "./gradlew assembleDebug"
echo

# Check if user wants to build via command line
read -p "Do you want to attempt command line build? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "Attempting command line build..."
    if check_command gradle; then
        echo "Building APK..."
        ./gradlew assembleDebug
        if [ $? -eq 0 ]; then
            echo "✅ Build successful!"
            echo "APK location: app/build/outputs/apk/debug/app-debug.apk"
        else
            echo "❌ Build failed. Please use Android Studio GUI method."
        fi
    else
        echo "Gradle not found. Please use Android Studio GUI method."
    fi
fi

echo
echo "========================================"
echo "Installation Instructions:"
echo "========================================"
echo
echo "1. Enable USB debugging on Android device:"
echo "   Settings → About phone → Tap 'Build number' 7 times"
echo "   Settings → System → Developer options → USB debugging"
echo
echo "2. Connect device via USB"
echo
echo "3. Install APK:"
echo "   adb install app/build/outputs/apk/debug/app-debug.apk"
echo
echo "4. Launch app from device launcher"
echo
echo "========================================"
echo "Testing Checklist:"
echo "========================================"
echo "✅ App launches without crashing"
echo "✅ Home screen shows forms and tools"
echo "✅ All 8 assessment tools accessible"
echo "✅ Baseline form calculates QALY"
echo "✅ Data saves successfully"
echo "✅ Emergency dialing works"
echo
echo "For detailed testing guide, see: TESTING_GUIDE.md"
