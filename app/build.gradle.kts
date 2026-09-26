plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.simplecalculator"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.simplecalculator"
        minSdk = 23
        targetSdk = 37
        versionCode = 2
        versionName = "1.0.1git"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}