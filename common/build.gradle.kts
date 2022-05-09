plugins {
  id("date-input-view-android-library")
  id("date-input-view-detekt")
  id("date-input-view-publish")
}

dependencies {
  implementation(libs.androidx.constraintLayout)
  implementation(libs.androidx.core.ktx)
  implementation(libs.google.material)
}
