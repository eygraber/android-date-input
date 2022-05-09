plugins {
  id("com.android.application")
  id("date-input-view-kotlin")
  id("date-input-view-detekt")
}

android {
  compileSdk = libs.versions.android.sdk.compile.get().toInt()

  buildFeatures {
    compose = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.jetpack.compose.compiler.get()
  }

  defaultConfig {
    applicationId = "com.eygraber.date_time_input"
    minSdk = libs.versions.android.sdk.min.get().toInt()
    targetSdk = libs.versions.android.sdk.target.get().toInt()
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    named("release") {
      isMinifyEnabled = true
      isShrinkResources = true
    }
  }

  compileOptions {
    isCoreLibraryDesugaringEnabled = true
    sourceCompatibility = JavaVersion.toVersion(libs.versions.jdk.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.jdk.get())
  }
}

dependencies {
  coreLibraryDesugaring(libs.android.desugar)

  implementation(projects.compose)
  implementation(projects.xml)

  implementation(libs.jetpack.compose.material)
  implementation(libs.jetpack.compose.themeAdapter)

  implementation(libs.androidx.appCompat)
  implementation(libs.androidx.constraintLayout)
  implementation(libs.androidx.core.ktx)
  implementation(libs.google.material)
}
