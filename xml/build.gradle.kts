plugins {
  id("date-time-input-android-library")
  id("date-time-input-detekt")
  id("date-time-input-publish")
}

dependencies {
  api(projects.common)

  implementation(libs.androidx.constraintLayout)
  implementation(libs.androidx.core.ktx)
  implementation(libs.google.material)
}
