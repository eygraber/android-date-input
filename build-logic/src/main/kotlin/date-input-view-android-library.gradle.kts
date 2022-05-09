@file:Suppress("UnstableApiUsage")

import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()

plugins {
  id("com.android.library")
  kotlin("android")
  id("org.gradle.android.cache-fix")
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

  publishing {
    multipleVariants {
      allVariants()
      withSourcesJar()
      withJavadocJar()
    }
  }

  dependencies {
    coreLibraryDesugaring(libs.android.desugar)
  }
}
