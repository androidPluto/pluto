import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.maven.publish)
}

val version = Versioning.loadVersioningData()
val verCode = version["code"] as Int
val verPublish = version["publish"] as String
val verGitSHA = version["gitSha"] as String

android {
    namespace = "com.pluto.plugins.bundle.core"
    compileSdk = libs.versions.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.buildTools.get()

    buildFeatures {
        buildConfig = true
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

extra["PUBLISH_GROUP_ID"] = "com.androidpluto.plugins"
extra["PUBLISH_ARTIFACT_ID"] = "bundle-core"
extra["PUBLISH_ARTIFACT_NAME"] = "Android Pluto Plugin Bundle"
extra["PUBLISH_ARTIFACT_DESCRIPTION"] = "Bundle module for Android Pluto plugins"

mavenPublishing {
    coordinates(
        groupId = extra["PUBLISH_GROUP_ID"] as String,
        artifactId = extra["PUBLISH_ARTIFACT_ID"] as String,
        version = verPublish
    )
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
        developers {
            developer {
                id.set(project.findProperty("pom.developer.id") as? String)
                name.set(project.findProperty("pom.developer.name") as? String)
                email.set(project.findProperty("pom.developer.email") as? String)
            }
        }
        scm {
            connection.set(project.findProperty("pom.scm.connection") as? String)
            developerConnection.set(project.findProperty("pom.scm.developerConnection") as? String)
            url.set(project.findProperty("pom.scm.url") as? String)
        }
    }
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}

dependencies {
    api(project(":pluto-plugins:plugins:exceptions:lib"))
    api(project(":pluto-plugins:plugins:network:lib"))
//    api(project(":pluto-plugins:plugins:network:interceptor-ktor:lib"))
//    api(project(":pluto-plugins:plugins:network:interceptor-okhttp:lib"))
    api(project(":pluto-plugins:plugins:shared-preferences:lib"))
    api(project(":pluto-plugins:plugins:logger:lib"))
    api(project(":pluto-plugins:plugins:datastore:lib"))
    api(project(":pluto-plugins:plugins:rooms-database:lib"))
    api(project(":pluto-plugins:plugins:layout-inspector:lib"))
}
