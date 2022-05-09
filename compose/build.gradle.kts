plugins {
  id("date-input-view-android-library")
  id("date-input-view-detekt")
  id("date-input-view-publish")
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
