import io.gitlab.arturbosch.detekt.Detekt

plugins {
  alias(libs.plugins.androidApp)
  alias(libs.plugins.kotlin)
  alias(libs.plugins.detekt)
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

kotlin {
  jvmToolchain {
    require(this is JavaToolchainSpec)
    languageVersion.set(JavaLanguageVersion.of(libs.versions.jdk.get()))
    vendor.set(JvmVendorSpec.AZUL)
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
  kotlinOptions {
    jvmTarget = libs.versions.jdk.get()

    allWarningsAsErrors = true
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

  detektPlugins(libs.detekt)
  detektPlugins(libs.detektEygraber.formatting)
  detektPlugins(libs.detektEygraber.style)
}

detekt {
  toolVersion = libs.versions.detekt.get()

  source.from("build.gradle.kts")

  autoCorrect = true
  parallel = true

  buildUponDefaultConfig = true

  config = project.files("${project.rootDir}/detekt.yml")
}

tasks.withType<Detekt>().configureEach {
  // Target version of the generated JVM bytecode. It is used for type resolution.
  jvmTarget = libs.versions.jdk.get()
}
