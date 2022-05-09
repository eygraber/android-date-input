plugins {
  id("date-time-input-android-library")
  id("date-time-input-detekt")
  id("date-time-input-publish")
}

dependencies {
  implementation(projects.xml)

  implementation(libs.androidx.constraintLayout)
  implementation(libs.androidx.core.ktx)
  implementation(libs.google.material)
  implementation(libs.kotlinx.coroutines.core)
}
