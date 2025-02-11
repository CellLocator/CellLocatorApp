import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

fun getBuildNumber(): Int {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    val currentDate = LocalDate.now()
    val currentDateTime = LocalDateTime.now()
    val secondsSinceMidnight = Duration.between(
        currentDateTime.toLocalDate().atStartOfDay(),
        currentDateTime
    ).seconds
    val twoDigitSuffix = ((secondsSinceMidnight / 86400.0) * 99).toInt().coerceIn(0, 99)
    val formattedDate = currentDate.format(dateFormatter)
    val buildNumber = "$formattedDate${"%02d".format(twoDigitSuffix)}".toInt()
    return buildNumber
}


android {
    namespace = "com.celllocator.app"
    compileSdk = 35


    buildFeatures {
        compose = true
    }

    defaultConfig {
        applicationId = "com.celllocator.app"
        minSdk = 31
        targetSdk = 34
        versionCode = getBuildNumber()
        versionName = "0.0-DEV"

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
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":netmonster-core"))
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.eventbus)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.timber)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}