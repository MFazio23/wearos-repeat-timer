plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "dev.mfazio.repeattimer"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.mfazio.repeattimer"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val horologistVersion = "0.6.20"

    implementation(platform("androidx.compose:compose-bom:2024.09.01"))

    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.5")
    implementation("androidx.percentlayout:percentlayout:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")

    implementation("androidx.wear.compose:compose-foundation:1.4.0")
    implementation("androidx.wear.compose:compose-material:1.4.0")
    implementation("androidx.wear.protolayout:protolayout:1.2.0")
    implementation("androidx.wear.protolayout:protolayout-expression:1.2.0")
    implementation("androidx.wear.protolayout:protolayout-material:1.2.0")
    implementation("androidx.wear.tiles:tiles:1.4.0")
    implementation("androidx.wear.watchface:watchface-complications-data-source-ktx:1.2.1")

    implementation("com.google.android.horologist:horologist-composables:$horologistVersion")
    implementation("com.google.android.horologist:horologist-compose-layout:$horologistVersion")
    implementation("com.google.android.horologist:horologist-compose-material:$horologistVersion")
    implementation("com.google.android.horologist:horologist-compose-tools:$horologistVersion")
    implementation("com.google.android.horologist:horologist-tiles:$horologistVersion")

    implementation("com.google.android.gms:play-services-wearable:18.2.0")

    implementation("androidx.health:health-services-client:1.1.0-alpha03")

    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("androidx.wear:wear-tooling-preview:1.0.0")

    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    debugImplementation("androidx.wear.tiles:tiles-renderer:1.4.0")
}