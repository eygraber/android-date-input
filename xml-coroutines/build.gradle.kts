import com.vanniktech.maven.publish.SonatypeHost
import io.gitlab.arturbosch.detekt.Detekt

plugins {
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.kotlin)
  alias(libs.plugins.detekt)
  alias(libs.plugins.publish)
}

android {
  compileSdk = libs.versions.android.sdk.compile.get().toInt()

  defaultConfig {
    consumerProguardFile(project.file("consumer-rules.pro"))

    minSdk = libs.versions.android.sdk.min.get().toInt()

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    named("release") {
      isMinifyEnabled = false
    }
    named("debug") {
      isMinifyEnabled = false
    }
  }

  compileOptions {
    isCoreLibraryDesugaringEnabled = true
    sourceCompatibility = JavaVersion.toVersion(libs.versions.jdk.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.jdk.get())
  }

  testOptions {
    unitTests {
      isIncludeAndroidResources = true
    }
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
  implementation(projects.xml)

  implementation(libs.androidx.constraintLayout)
  implementation(libs.androidx.core.ktx)
  implementation(libs.google.material)
  implementation(libs.kotlinx.coroutines.core)

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

mavenPublish {
  sonatypeHost = SonatypeHost.S01
}
