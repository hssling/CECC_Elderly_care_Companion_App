# CECC Care Companion - Build Environment Setup & Instructions

## 🚀 Quick Start - Build & Test

### Prerequisites Setup

#### 1. Install Java 17
```bash
# Windows
# Download from: https://adoptium.net/temurin/releases/?version=17
# Install and set JAVA_HOME environment variable

# Linux/Mac
sudo apt update
sudo apt install openjdk-17-jdk
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
```

#### 2. Install Android Studio
```bash
# Download from: https://developer.android.com/studio
# Install Android Studio 2023.3+ (Hedgehog or later)
```

#### 3. Set Environment Variables
```bash
# Windows PowerShell
$env:JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-17.x.x.x"
$env:PATH="$env:JAVA_HOME\bin;$env:PATH"
$env:ANDROID_HOME="C:\Users\[username]\AppData\Local\Android\Sdk"

# Linux/Mac
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export ANDROID_HOME=~/Android/Sdk
export PATH=$JAVA_HOME/bin:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$PATH
```

### Build Commands

#### Option A: Android Studio (GUI)
1. **Open Project**
   - Launch Android Studio
   - `File → Open → Select CECCCareCompanion/ folder`
   - Wait for Gradle sync to complete

2. **Build APK**
   - `Build → Build Bundle(s)/APK(s) → Build APK(s)`
   - Wait for build to complete
   - APK location: `app/build/outputs/apk/debug/app-debug.apk`

#### Option B: Command Line
```bash
# Navigate to project
cd CECCCareCompanion

# Build APK
./gradlew assembleDebug

# APK location: app/build/outputs/apk/debug/app-debug.apk
```

### Installation & Testing

#### 1. Enable Developer Options on Android Device
```bash
# On device: Settings → About phone → Tap "Build number" 7 times
# Then: Settings → System → Developer options → Enable "USB debugging"
```

#### 2. Install APK via ADB
```bash
# Connect device via USB
adb devices

# Install APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Launch app
adb shell am start -n com.cecc.carecompanion/.MainActivity
```

#### 3. Manual Installation
1. Copy APK to device storage
2. Use file manager to locate APK
3. Tap APK file to install
4. Grant requested permissions

### Expected Build Output

**Successful Build Indicators:**
- ✅ APK file size: ~15-25 MB
- ✅ No compilation errors
- ✅ All dependencies resolved
- ✅ Assets properly bundled

**APK Contents:**
- 8 screener JSON configurations
- Multi-language string resources
- Material 3 UI components
- Room database entities
- Google Drive sync functionality
- QALY calculation algorithms

### Testing Checklist

#### Phase 1: Installation & Launch
- [ ] APK installs without errors
- [ ] App appears in launcher
- [ ] App launches without crashing
- [ ] Home screen displays correctly

#### Phase 2: Core Features
- [ ] All 8 assessment tools accessible
- [ ] Baseline form loads and calculates QALY
- [ ] Data saves to local database
- [ ] Emergency dialing accessible

#### Phase 3: Advanced Features
- [ ] Google Drive folder selection works
- [ ] Sync functionality operational
- [ ] PIN authentication functional
- [ ] Multi-language switching works

### Troubleshooting

#### Build Issues

**Problem**: Gradle sync fails
```bash
# Solution: Clean and rebuild
./gradlew clean build --refresh-dependencies
```

**Problem**: JAVA_HOME not set
```bash
# Windows
set JAVA_HOME="C:\path\to\jdk-17"
set PATH=%JAVA_HOME%\bin;%PATH%

# Linux/Mac
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

**Problem**: Android SDK not found
```bash
# Install Android SDK or set ANDROID_HOME
export ANDROID_HOME=~/Android/Sdk
```

#### Runtime Issues

**Problem**: App crashes on launch
- Check Android version compatibility (min SDK 24)
- Verify all permissions granted
- Check device storage space

**Problem**: Screeners don't load
- Verify assets/screeners/ folder exists
- Check JSON file formats
- Verify string resources exist

**Problem**: QALY calculation wrong
- Check age and health parameters
- Verify scoring algorithms in GenericScreenerScreen.kt
- Test with known values (age 60, good health = ~0.9 QALY)

### Performance Benchmarks

**Expected Performance:**
- App launch time: < 3 seconds
- Form loading: < 2 seconds
- QALY calculation: < 1 second
- Data save: < 2 seconds
- Memory usage: < 150 MB

### Deployment Package

**Final Deliverables:**
- `app-debug.apk` - Installable APK file
- `README.md` - Comprehensive documentation
- `TESTING_GUIDE.md` - Testing procedures
- `BUILD_INSTRUCTIONS.md` - This file

**File Sizes (Expected):**
- APK: 15-25 MB
- Source code: ~50 MB
- Documentation: ~100 KB

---

## 🎯 Next Steps After Successful Build

1. **Install on Test Device** using ADB or manual installation
2. **Complete Testing Checklist** following TESTING_GUIDE.md
3. **Verify All Features** work as documented
4. **Test QALY Calculations** with sample data
5. **Validate Multi-language Support** (English first)
6. **Test Data Export** functionality

## 🚨 Critical Testing Areas

**Must Test Before Field Use:**
- [ ] All 8 assessment tools load correctly
- [ ] QALY calculations are accurate
- [ ] Data saves and persists
- [ ] Emergency features accessible
- [ ] Forms handle edge cases gracefully

---

**Build Status**: Ready for environment setup
**Target**: Production APK for field testing
**Timeline**: 30-60 minutes setup + build time
