// import com.vanniktech.maven.publish.SonatypeHost
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.jetbrains.compose)
  alias(libs.plugins.detekt)
  // alias(libs.plugins.publish)
}

kotlin {
  jvm("desktop")
  android()

  sourceSets {
    named("commonMain") {
      dependencies {
        implementation(compose.ui)
        implementation(compose.foundation)
        implementation(compose.material)
      }
    }
  }
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

  sourceSets {
    named("main") {
      manifest.srcFile("src/androidMain/AndroidManifest.xml")
      res.srcDirs("src/androidMain/res")
    }
  }

  dependencies {
    coreLibraryDesugaring(libs.android.desugar)
  }

  testOptions {
    unitTests {
      isIncludeAndroidResources = true
    }
  }
}

plugins.withType<KotlinBasePluginWrapper> {
  with(extensions.getByType<KotlinProjectExtension>()) {
    jvmToolchain {
      require(this is JavaToolchainSpec)
      languageVersion.set(JavaLanguageVersion.of(libs.versions.jdk.get()))
      vendor.set(JvmVendorSpec.AZUL)
    }

    sourceSets.configureEach {
      languageSettings.optIn("kotlin.RequiresOptIn")
    }
  }

  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
      allWarningsAsErrors = true
      jvmTarget = libs.versions.jdk.get()
      sourceCompatibility = libs.versions.jdk.get()
      targetCompatibility = libs.versions.jdk.get()
      freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
  }
}

dependencies {
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

// mavenPublish {
//   sonatypeHost = SonatypeHost.S01
// }
