plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("kotlin-kapt")
  id("org.jetbrains.kotlin.plugin.serialization")
}

android {
  namespace = "com.cecc.carecompanion"
  compileSdk = 35
  defaultConfig {
    applicationId = "com.cecc.carecompanion"
    minSdk = 24
    targetSdk = 35
    versionCode = 1
    versionName = "1.0.0"
    vectorDrawables { useSupportLibrary = true }
  }
  buildTypes {
    release {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
    debug { isMinifyEnabled = false }
  }
  compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
  kotlinOptions { jvmTarget = "17" }
  buildFeatures { compose = true; buildConfig = true }
  composeOptions { kotlinCompilerExtensionVersion = "1.5.8" }
  packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
}

dependencies {
  val composeBom = platform("androidx.compose:compose-bom:2024.09.02")
  implementation(composeBom)
  androidTestImplementation(composeBom)

  implementation("androidx.core:core-ktx:1.13.1")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
  implementation("androidx.activity:activity-compose:1.9.2")
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-tooling-preview")
  debugImplementation("androidx.compose.ui:ui-tooling")
  implementation("androidx.compose.material3:material3:1.3.0")
  implementation("androidx.navigation:navigation-compose:2.8.3")

  // Room
  implementation("androidx.room:room-runtime:2.6.1")
  implementation("androidx.room:room-ktx:2.6.1")
  kapt("androidx.room:room-compiler:2.6.1")

  // WorkManager
  implementation("androidx.work:work-runtime-ktx:2.9.1")

  // Media
  implementation("androidx.media3:media3-exoplayer:1.4.1")
  implementation("androidx.media3:media3-ui:1.4.1")

  // JSON
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

  // Kotlinx Serialization
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.3")

  // Biometric Authentication
  implementation("androidx.biometric:biometric:1.1.0")

  // DataStore Preferences
  implementation("androidx.datastore:datastore-preferences:1.1.1")

  // Storage Access Framework
  implementation("androidx.activity:activity-ktx:1.9.2")

  // Coroutines
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

  // File operations
  implementation("androidx.documentfile:documentfile:1.0.1")
}
