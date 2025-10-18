@echo off
echo ========================================
echo CECC Care Companion - Environment Setup
echo ========================================

echo.
echo Step 1: Checking Java installation...
java -version >nul 2>&1
if errorlevel 1 (
    echo ❌ Java not found!
    echo Please install Java 17 from: https://adoptium.net/temurin/releases/?version=17
    echo After installation, set JAVA_HOME environment variable
    pause
    exit /b 1
) else (
    echo ✅ Java found
    java -version
)

echo.
echo Step 2: Checking Android Studio...
if exist "%PROGRAMFILES%\Android\Android Studio\bin\studio64.exe" (
    echo ✅ Android Studio found
) else (
    echo ❌ Android Studio not found!
    echo Please install Android Studio 2023.3+ from: https://developer.android.com/studio
    pause
    exit /b 1
)

echo.
echo Step 3: Setting environment variables...
for /f "tokens=*" %%i in ('java -cp . -version 2^>^&1 ^| findstr /i version') do set JAVA_VERSION=%%i
echo Java Version: %JAVA_VERSION%

echo.
echo Step 4: Building APK...
echo Opening Android Studio project...
echo.
echo Instructions:
echo 1. Android Studio will open the CECCCareCompanion project
echo 2. Wait for Gradle sync to complete (may take 5-10 minutes)
echo 3. Build APK: Build ^> Build Bundle(s)/APK(s) ^> Build APK(s)
echo 4. APK location: app/build/outputs/apk/debug/app-debug.apk
echo.
echo Press any key to continue...
pause >nul

echo.
echo ========================================
echo Build Instructions Summary:
echo ========================================
echo.
echo 1. Open Android Studio
echo 2. File ^> Open ^> Select CECCCareCompanion folder
echo 3. Wait for Gradle sync
echo 4. Build ^> Build APK(s)
echo 5. Find APK in: app/build/outputs/apk/debug/
echo.
echo Expected APK size: 15-25 MB
echo Build time: 2-5 minutes
echo.
pause
