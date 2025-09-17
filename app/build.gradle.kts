plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.aurafit.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.aurafit.app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "PROVIDER", "\"TRYON_API\"") // or "PIXELCUT", "NANOBANANA"

    }

    buildTypes {
        debug {
            buildConfigField("String", "PIXELCUT_URL",  project.property("PIXELCUT_URL").toString())
            buildConfigField("String", "PIXELCUT_API_KEY",  project.property("PIXELCUT_API_KEY").toString())
        }
        release {
            isMinifyEnabled = false
            buildConfigField("String", "PROVIDER", "")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material3)
    implementation(libs.androidx.media3.effect)

    implementation(libs.coil)
    implementation(libs.coil.compose)

    implementation(libs.ktor.core)
    implementation(libs.ktor.okhttp)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.logging)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.storage.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}