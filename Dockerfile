# Use Ubuntu as base image
FROM ubuntu:22.04

# Set environment variables
ENV ANDROID_SDK_ROOT=/opt/android-sdk
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH="${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/platform-tools"
ENV DEBIAN_FRONTEND=noninteractive

# Install required packages
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    curl \
    openjdk-17-jdk \
    git \
    && rm -rf /var/lib/apt/lists/*

# Set Java 17 as default
RUN update-alternatives --set java /usr/lib/jvm/java-17-openjdk-amd64/bin/java
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

# Download and install Android SDK
RUN mkdir -p ${ANDROID_SDK_ROOT}/cmdline-tools && \
    cd ${ANDROID_SDK_ROOT}/cmdline-tools && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip && \
    unzip commandlinetools-linux-11076708_latest.zip && \
    mv cmdline-tools latest && \
    rm commandlinetools-linux-11076708_latest.zip

# Install Android SDK components
RUN yes | sdkmanager --licenses && \
    sdkmanager "platform-tools" "platforms;android-35" "build-tools;35.0.0" \
    "system-images;android-35;google_apis;x86_64" "emulator"

# Install Android NDK (if needed)
RUN sdkmanager "ndk;27.0.12077973"

# Create a non-root user
RUN useradd -ms /bin/bash developer
USER developer
WORKDIR /home/developer

# Copy project files
COPY --chown=developer:developer . /home/developer/project/

# Set working directory
WORKDIR /home/developer/project

# Expose common Android development ports
EXPOSE 5554 5555

# Default command
CMD ["/bin/bash"]
