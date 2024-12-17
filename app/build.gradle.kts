plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
}

val versionMajor = 0
val versionMinor = 0
val versionPatch = 1
var versionClassifier: String? = null
val isSnapShot = false
val minSdkVersion = 21


android {
    compileSdk = 35
    defaultConfig {
        applicationId = "com.sterlingbankng.football"
        minSdk = minSdkVersion
        targetSdk = 35
        versionCode = generateVersionCode()
        versionName = generateVersionName()
        val API_KEY = providers.gradleProperty("API_KEY").getOrNull() ?: ""
        buildConfigField("String", "X_AUTH_TOKEN", API_KEY)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    namespace = "com.sterlingbankng.football"

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    //Support libraries
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    //Kotlin
    implementation(libs.kotlin.stdlib.jdk7)
    implementation(libs.androidx.core.ktx)

    //Architecture components
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.rxjava2)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.androidx.room.testing)
    ksp(libs.androidx.lifecycle.compiler)
    ksp(libs.androidx.room.compiler)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.viewmodel)
    androidTestImplementation(libs.koin.test)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.adapter.rxjava2)

    //RxAndroid
    implementation(libs.rxjava.rxandroid)

    //Coil for Image loading
    implementation(libs.coil)
    implementation(libs.coil.network.okhttp)
    implementation(libs.coil.svg)

    //Google JSON Parser
    implementation(libs.gson)

    //OkHttp
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.legacy.support.v4)

    //Test libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.rules)
    // Kotlin extensions for androidx.test.ext.junit
    androidTestImplementation(libs.androidx.junit.ktx)
}

private fun generateVersionCode(): Int {
    return minSdkVersion * 10000000 + versionMajor * 10000 +
            versionMinor * 100 + versionPatch
}

private fun generateVersionName(): String {
    var versionName = "${versionMajor}.${versionMinor}.${versionPatch}"
    if (versionClassifier == null) {
        if (isSnapShot) {
            versionClassifier = "SNAPSHOT"
        }
    }

    if (versionClassifier != null) {
        versionName += "-${versionClassifier}"
    }

    return versionName
}
