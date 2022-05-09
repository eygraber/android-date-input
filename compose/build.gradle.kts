plugins {
  id("date-time-input-android-library")
  id("date-time-input-detekt")
  id("date-time-input-publish")
}

android {
  buildFeatures {
    compose = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.jetpack.compose.compiler.get()
  }
}

dependencies {
  api(projects.common)

  implementation(libs.accompanist.flow)
  implementation(libs.androidx.activity)
  implementation(libs.jetpack.compose.material)
  implementation(libs.jetpack.compose.tooling)
}
