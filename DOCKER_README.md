# CECC Care Companion - Docker Development Environment

This Docker setup provides a complete Android development environment with all required dependencies for building the CECC Care Companion Android application.

## 🚀 Quick Start

### Prerequisites
- Docker Desktop (Windows/macOS) or Docker Engine (Linux)
- At least 8GB of available RAM
- At least 10GB of free disk space

### Build and Run

1. **Build the Docker image:**
   ```bash
   docker-compose build
   ```

2. **Start the development environment:**
   ```bash
   docker-compose up -d
   ```

3. **Access the container:**
   ```bash
   docker-compose exec android-dev bash
   ```

4. **Build the Android project:**
   ```bash
   ./build-in-docker.sh
   ```

## 📋 What's Included

### Base Environment
- **Ubuntu 22.04 LTS** - Stable Linux base
- **OpenJDK 17** - Compatible Java version for Android development
- **Git** - Version control system

### Android Development Tools
- **Android SDK** - Latest stable version
- **Platform Tools** - ADB, Fastboot, etc.
- **Build Tools 35.0.0** - Latest build tools
- **Android 35 (API Level 35)** - Target Android version
- **Android NDK 27** - Native development kit
- **Emulator Support** - Android emulator tools

### Project Structure
- **Gradle Wrapper** - Build automation
- **Kotlin 1.9.25** - Programming language
- **Android Gradle Plugin 8.5.3** - Build system

## 🔧 Usage Examples

### Basic Build Commands

```bash
# Clean and build debug APK
./gradlew clean build

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Check for dependencies
./gradlew dependencies

# Generate APK bundle
./gradlew bundleRelease
```

### Development Workflow

```bash
# 1. Start the container
docker-compose up -d

# 2. Access the development environment
docker-compose exec android-dev bash

# 3. Navigate to project directory
cd /home/developer/project

# 4. Build the project
./build-in-docker.sh

# 5. Check the generated APK
ls -la app/build/outputs/apk/release/
```

### Using Android Emulator

```bash
# List available emulators
emulator -list-avds

# Start an emulator (if available)
emulator -avd Your_AVD_Name &

# Install APK to connected device/emulator
adb install app/build/outputs/apk/release/app-release.apk
```

## 🛠️ Troubleshooting

### Common Issues

1. **Build fails with Java version error**
   - Ensure you're using Java 17 (configured in Dockerfile)
   - Check that JAVA_HOME is set correctly

2. **Android SDK not found**
   - Make sure Docker container has enough memory (8GB+ recommended)
   - Check that Android SDK components are installed

3. **Gradle build cache issues**
   - Clear Gradle cache: `./gradlew clean`
   - Or remove the gradle_cache volume: `docker-compose down -v`

4. **Permission issues**
   - Ensure files are readable: `chmod -R 755 .` (inside container)

### Checking Environment

```bash
# Check Java version
java -version

# Check Android SDK
echo $ANDROID_HOME
ls $ANDROID_HOME/platform-tools/

# Check available devices
adb devices

# Check Gradle version
./gradlew --version
```

## 📁 File Structure

```
CECCCareCompanion/
├── Dockerfile              # Docker image definition
├── docker-compose.yml      # Docker services configuration
├── build-in-docker.sh      # Build script for container
├── DOCKER_README.md        # This file
├── app/                    # Android application module
│   ├── src/               # Source code
│   └── build.gradle.kts   # App-level build configuration
├── build.gradle.kts       # Project-level build configuration
├── gradle.properties      # Gradle configuration
└── gradlew*               # Gradle wrapper
```

## 🔄 Development Workflow

1. **Make code changes** on your host machine
2. **Build inside Docker** using the containerized environment
3. **Test on device/emulator** connected to Docker container
4. **Deploy** the generated APK

## 🚀 Performance Tips

- Use **Docker volumes** for faster builds (configured in docker-compose.yml)
- Allocate **sufficient memory** to Docker (8GB+ recommended)
- Use **build caches** by not running `clean` unnecessarily
- Consider using **multi-stage builds** for production images

## 📚 Additional Resources

- [Android Developer Documentation](https://developer.android.com/)
- [Docker Documentation](https://docs.docker.com/)
- [Gradle Documentation](https://docs.gradle.org/)
- [Kotlin Documentation](https://kotlinlang.org/docs/)

## 🔍 Monitoring

```bash
# Check container logs
docker-compose logs -f android-dev

# Check container status
docker-compose ps

# Access container shell
docker-compose exec android-dev bash

# Stop the environment
docker-compose down
```

## 🎯 Next Steps

1. ✅ Set up Docker environment (this document)
2. 🔄 Build the Android project
3. 📱 Test on Android device/emulator
4. 🚀 Deploy to production

---

*Happy coding! 🎉*
