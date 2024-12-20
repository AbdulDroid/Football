plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinSerialization)
    id("kotlin-parcelize")
}

val versionMajor = 1
val versionMinor = 0
val versionPatch = 0
var versionClassifier: String? = null
val isSnapShot = false
val minSdkVersion = 21


android {
    compileSdk = 35
    defaultConfig {
        applicationId = "droid.abdul.football"
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

    namespace = "droid.abdul.football"

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
    implementation(libs.androidx.room.ktx)
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

    //Coroutines
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    //Coil for Image loading
    implementation(libs.coil)
    implementation(libs.coil.network.okhttp)
    implementation(libs.coil.svg)

    //Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.kotlinx.serialization)

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
