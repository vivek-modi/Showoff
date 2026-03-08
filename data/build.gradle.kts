plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.serialization)
    alias(libs.plugins.koin.compiler)
}
android {
    namespace = "com.android.recipe.data"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}
dependencies {
    implementation(project(":domain"))
    implementation(project(":core:network"))

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core.coroutines)
    implementation(libs.serialization.json)
    implementation(libs.androidx.core.ktx)

    // JUnit Jupiter
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)

    // coroutine testing
    testImplementation(libs.kotlinx.coroutines.test)

    // ktor types used in tests
    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.mock)

    androidTestImplementation(libs.androidx.junit)
}
