plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kotlin.serialization)
}

val version = Versioning.loadVersioningData()
val verCode = version["code"] as Int
val verPublish = version["publish"] as String

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.buildTools.get()

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.sampleapp"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = verCode
        versionName = verPublish

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        /**
         * build type with lib modules
         */
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        create("debugMaven") {
            initWith(getByName("debug"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks.add("debug")
        }

        /**
         * build type with lib-no-op modules
         */
        create("debugNoOp") {
            initWith(getByName("debug"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks.add("debug")
        }
        create("debugNoOpMaven") {
            initWith(getByName("debug"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks.add("debug")
        }

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    }

    kotlinOptions {
        jvmTarget = libs.versions.java.get()
    }
    namespace = "com.sampleapp"
}

dependencies {
    /* Local Op dependencies */
    "debugImplementation"(project(":pluto:lib"))
    "debugImplementation"(project(":pluto-plugins:bundle:lib"))

    /* Maven Op dependencies */
    "debugMavenImplementation"("com.plutolib:pluto:$verPublish")
    "debugMavenImplementation"("com.plutolib.plugins:bundle-core:$verPublish")

    /* Local NoOp dependencies */
    "debugNoOpImplementation"(project(":pluto:lib-no-op"))
    "debugNoOpImplementation"(project(":pluto-plugins:bundle:lib-no-op"))

    /* Maven NoOp dependencies */
    "debugNoOpMavenImplementation"("com.plutolib:pluto-no-op:$verPublish")
    "debugNoOpMavenImplementation"("com.plutolib.plugins:bundle-core-no-op:$verPublish")

    "releaseImplementation"("com.plutolib:pluto:$verPublish")
    "releaseImplementation"("com.plutolib.plugins:bundle-core:$verPublish")

    /**
     * Other dependencies
     */
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.google.material)
    implementation(libs.androidx.annotation)

    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.runtime)

    implementation(libs.kotlinx.coroutines.core)

    /**
     * Network plugin test dependency
     */
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.ktor.client.core.jvm)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.okhttp)
    implementation(libs.okio)

    implementation(libs.moshi)
    ksp(libs.moshi.codegen)

    implementation(libs.datastore.preferences)
    debugImplementation(libs.leakcanary.android)

    implementation(libs.androidx.navigation.fragment)

    implementation(libs.room)
    ksp(libs.room.compiler)
}
