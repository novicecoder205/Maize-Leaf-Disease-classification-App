plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.rkpandey.leafclassification"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rkpandey.leafclassification"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        mlModelBinding = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)      // AndroidX Core KTX library
    implementation(libs.androidx.appcompat)    // AndroidX AppCompat library
    implementation(libs.material)               // Material Components library
    implementation(libs.androidx.activity)      // AndroidX Activity library
    implementation(libs.androidx.constraintlayout) // ConstraintLayout library

    implementation(libs.tensorflow.lite.support)  // TensorFlow Lite Support library
    implementation(libs.tensorflow.lite.metadata)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx) // TensorFlow Lite Metadata library

    testImplementation(libs.junit)               // JUnit for unit tests
    androidTestImplementation(libs.androidx.junit) // AndroidX JUnit for Android tests
    androidTestImplementation(libs.androidx.espresso.core) // Espresso Core for UI testing
    //SplashScreen
    implementation("androidx.core:core-splashscreen:1.0.1")


}