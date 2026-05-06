import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")

    alias(libs.plugins.maven.publish)
}

val version = Versioning.loadVersioningData()
val verPublish = version["publish"] as String


apply<PlutoGradlePlugin>()
android {
    namespace = "com.pluto.plugins.logger"
}

extra["PUBLISH_GROUP_ID"] = "com.androidpluto.plugins"
extra["PUBLISH_ARTIFACT_ID"] = "logger-no-op"
extra["PUBLISH_ARTIFACT_NAME"] = "Android Pluto Logger Plugin"
extra["PUBLISH_ARTIFACT_DESCRIPTION"] = "Plugin to monitor logs in Android Pluto"

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
    api(libs.timber)
}
