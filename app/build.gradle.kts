import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    // Compose compiler plugin (required for Kotlin 2.0+)
    id("org.jetbrains.kotlin.plugin.compose")

    // Core Android + Kotlin
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // Annotation processing
    id("com.google.devtools.ksp")

    // Dependency injection
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.photodownloader"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.photodownloader"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            freeCompilerArgs.addAll(
                "-Xjvm-default=all", // Better interop with Java interfaces
                "-opt-in=kotlin.RequiresOptIn"
            )
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.7.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Core Android
    implementation(libs.androidx.core.ktx.v1170)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.material3)
    implementation(libs.androidx.compose.material3.window.size.class1)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.androidx.graphics.shapes)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose.v120)

    // Coil (image loading)
    implementation(libs.coil)
    implementation(libs.coil.compose)

    // Network
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Hilt (DI)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Room(local database)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(libs.androidx.room.testing)
}
