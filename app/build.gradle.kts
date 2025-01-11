plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.umc_7th_hackathon"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.umc_7th_hackathon"
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

    viewBinding {
        enable = true
    }

    dataBinding {
        enable = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // room
    implementation("androidx.room:room-runtime:2.5.0")
    implementation("androidx.room:room-ktx:2.5.0")
    kapt("androidx.room:room-compiler:2.6.0")

    implementation("com.google.android.material:material:1.8.0")

    implementation("com.google.android.gms:play-services-maps:17.0.1")

    //naver map
    implementation("com.naver.maps:map-sdk:3.20.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.2")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.0")
    implementation("com.google.code.gson:gson:2.8.8")
    implementation ("com.google.android.gms:play-services-location:21.0.1")

    // json <-> 객체 변환하기 위해 사용
    implementation("com.google.code.gson:gson:2.8.7")

    //retrofit 네트워크 라이브러리 관련
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    //cameraX
    implementation("androidx.camera:camera-core:1.3.0-alpha04")
    implementation("androidx.camera:camera-camera2:1.3.0-alpha04")
    implementation("androidx.camera:camera-lifecycle:1.3.0-alpha04")
    implementation("androidx.camera:camera-video:1.3.0-alpha04") // VideoCapture 및 Recorder
    implementation("androidx.camera:camera-view:1.3.0-alpha04")
    implementation("androidx.camera:camera-extensions:1.3.0-alpha04")
}