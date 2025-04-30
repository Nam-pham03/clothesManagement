plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.project_prm"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.project_prm"
        minSdk = 27
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    // Fire Base
    implementation(platform("com.google.firebase:firebase-bom:33.11.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    // Base
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation ("androidx.paging:paging-runtime:3.1.1")
    implementation(platform(libs.firebase.bom))
    // Room Database
    implementation(libs.room.runtime)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.auth)

    annotationProcessor(libs.room.compiler)
    implementation(libs.firebase.auth.v2231)
    implementation(libs.play.services.auth)

    // DataStore
    implementation(libs.datastore.core.android)

    // Chart
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Lombok
    implementation("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // Unit Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Live
    implementation ("com.github.ZEGOCLOUD:zego_uikit_prebuilt_live_streaming_android:latest.release")


    //chat
    implementation("com.github.ZEGOCLOUD:zego_inapp_chat_uikit_android:+")
    implementation ("com.google.code.gson:gson:2.8.8")
    //Chat bot, virtual room
    implementation ("androidx.activity:activity-ktx:1.4.0")  // Thư viện hỗ trợ ActivityResultContracts
    implementation ("androidx.activity:activity:1.4.0" )     // Thư viện Activity chính
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    implementation ("com.android.support:appcompat-v7:28.0.0")
    implementation ("com.android.support:design:28.0.0")
    implementation ("com.google.android.material:material:1.5.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    //Zalo
    implementation(fileTree(mapOf(
        "dir" to "C:\\Users\\Admin\\Desktop\\ZaloPayLib",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf("")
    )))
    implementation(fileTree(mapOf(
        "dir" to "C:\\Users\\Admin\\Desktop\\ZaloPayLib",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf("")
    )))
    implementation("com.squareup.okhttp3:okhttp:4.6.0")
    implementation("commons-codec:commons-codec:1.14")






}
