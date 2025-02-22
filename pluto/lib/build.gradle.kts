plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

val version = Versioning.loadVersioningData()
val verCode = version["code"] as Int
val verPublish = version["publish"] as String
val verGitSHA = version["gitSha"] as String

extra["PUBLISH_GROUP_ID"] = "com.plutolib"
extra["PUBLISH_ARTIFACT_ID"] = "pluto"
extra["PUBLISH_VERSION"] = verPublish

android {
    namespace = "com.pluto"
    resourcePrefix = "pluto___"
    compileSdk = libs.versions.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.buildTools.get()

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        buildConfigField("String", "VERSION_NAME", "\"$verPublish\"")
        buildConfigField("long", "VERSION_CODE", "$verCode")
        buildConfigField("String", "GIT_SHA", "\"$verGitSHA\"")
    }

    buildTypes {
        getByName("release") {
//            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    }

    kotlinOptions {
        jvmTarget = libs.versions.java.get()
    }

    lint {
        abortOnError = false
        targetSdk = libs.versions.targetSdk.get().toInt()
    }
}

dependencies {
    api(project(":pluto-plugins:base:lib"))

    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.okhttp)
    implementation(libs.okio)

    implementation(libs.moshi)
    ksp(libs.moshi.codegen)
}
