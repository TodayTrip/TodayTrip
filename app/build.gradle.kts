import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "com.twoday.todaytrip"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.twoday.todaytrip"
        minSdk = 26
        targetSdk = 34
        versionCode = 4
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "TOUR_API_KEY", properties.getProperty("TOUR_API_KEY"))
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
        viewBinding = true
        buildConfig = true
        dataBinding = true
    }
}
dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.activity:activity-ktx:1.8.2")
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    // Fragment
    implementation ("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    // BottomNavigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    // William Charts
    implementation ("com.diogobernardino:williamchart:3.10.1")
    // Map
    implementation("com.naver.maps:map-sdk:3.17.0")
    implementation("com.google.android.gms:play-services-location:21.1.0")
    // Retrofit
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.1.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:4.4.1")
    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")
    // HttpHeaders
    implementation(platform("com.google.firebase:firebase-bom:32.7.3"))
    implementation("com.google.firebase:firebase-analytics")
    // Lottie animation
    implementation("com.airbnb.android:lottie:6.3.0")
    // ViewPager2
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.0")
    // Coil
    implementation("io.coil-kt:coil:2.0.0-rc03")
    // balloon
    implementation ("com.github.skydoves:balloon:1.4.6")
    // Word cloud
    implementation("com.github.jolenechong:androidWordCloud:1.0.0") {
        // exclude due to duplicate classes with the
        // edu.stanford.nlp:stanford-corenlp dependency for data processing
        exclude(group="com.sun.xml.bind", module="jaxb-core")
        exclude(group="com.sun.xml.bind", module="jaxb-impl")
    }
    // Skeleton
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
    // FishBun
    implementation ("io.github.sangcomz:fishbun:1.1.1")
    // SwipeRefreshLayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.google.android.material:material:1.11.0")
}