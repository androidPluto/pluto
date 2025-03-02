import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    id("com.vanniktech.maven.publish") version "0.28.0"
}

val version = Versioning.loadVersioningData()
val verCode = version["code"] as Int
val verPublish = version["publish"] as String
val verGitSHA = version["gitSha"] as String

android {
    namespace = "com.pluto.plugins.logger"

    compileSdk = libs.versions.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.buildTools.get()

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
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

extra["PUBLISH_GROUP_ID"] = "com.plutolib.plugins"
extra["PUBLISH_ARTIFACT_ID"] = "logger-no-op"
extra["PUBLISH_ARTIFACT_NAME"] = "Android Pluto Logger Plugin"
extra["PUBLISH_ARTIFACT_DESCRIPTION"] = "Plugin to monitor logs in Android Pluto"

mavenPublishing {
    // Define coordinates for the published artifact
    coordinates(
        groupId = extra["PUBLISH_GROUP_ID"] as String,
        artifactId = extra["PUBLISH_ARTIFACT_ID"] as String,
        version = verPublish
    )

    // Configure POM metadata for the published artifact
    pom {
        name.set(extra["PUBLISH_ARTIFACT_NAME"] as String)
        description.set(extra["PUBLISH_ARTIFACT_DESCRIPTION"] as String)
        inceptionYear.set(project.findProperty("pom.inceptionYear") as? String)
        url.set(project.findProperty("pom.url") as? String)

        licenses {
            license {
                name.set(project.findProperty("pom.license.name") as? String)
                url.set(project.findProperty("pom.license.url") as? String)
            }
        }

        // Specify developers information
        developers {
            developer {
                id.set(project.findProperty("pom.developer.id") as? String)
                name.set(project.findProperty("pom.developer.name") as? String)
                email.set(project.findProperty("pom.developer.email") as? String)
            }
        }

        // Specify SCM information
        scm {
            connection.set(project.findProperty("pom.scm.connection") as? String)
            developerConnection.set(project.findProperty("pom.scm.developerConnection") as? String)
            url.set(project.findProperty("pom.scm.url") as? String)
        }
    }

    // Configure publishing to Maven Central
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    // Enable GPG signing for all publications
    signAllPublications()
}

dependencies {
    implementation(libs.androidx.core)
    api(libs.timber)
}
