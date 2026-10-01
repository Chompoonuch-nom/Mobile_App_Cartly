plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.cartly"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.cartly"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

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
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)

//    Splash Screeen
    implementation(libs.core.splashscreen)

    implementation(libs.core)
    implementation(libs.navigation.ui)
    implementation(libs.androidx.navigation.fragment)

//    Retrofit
    implementation (libs.retrofit)
    implementation (libs.retrofit2.converter.gson)

//    Retrofit Adapter for RxJava3
    implementation(libs.retrofit2.adapter.rxjava3)
//    Okhttp
    implementation(libs.logging.interceptor)

//    RxJava 3 + RxAndroid 3 (เวอร์ชันปัจจุบัน)
    implementation(libs.rxjava)
    implementation(libs.rxandroid)
}