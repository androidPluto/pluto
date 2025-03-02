import java.util.Properties

/**
 * Usage
 * ./gradlew publishOnMavenCentral -PshouldRelease=false
 *
 * shouldRelease=true will publish & release the build. no manual intervention needed
 * shouldRelease=false will only publish the build, need to manually release from https://central.sonatype.com/publishing/deployments
 */
tasks.register("publishOnMavenCentral") {
    val shouldRelease = project.findProperty("shouldRelease")?.toString()?.toBoolean() ?: false

    doLast {
        val gradleFile = file("$rootDir/gradle.properties")
        val credentialsFile = file("$rootDir/mavenCredentials.properties")

        if (!credentialsFile.exists()) {
            throw GradleException("❌ Credential file not found: $credentialsFile")
        }

        // Read existing gradle.properties content into a mutable map
        val gradleProperties = Properties().apply { load(gradleFile.inputStream()) }.toMutableMap()

        // Read credentials from credential.properties
        val credentials = Properties().apply { load(credentialsFile.inputStream()) }

        // Backup original gradle.properties content
        val originalContent = gradleFile.readText()

        // Override common keys & keep non-overlapping keys untouched
        credentials.forEach { (key, value) -> gradleProperties[key.toString()] = value.toString() }
        gradleProperties["signing.secretKeyRingFile"] = "$rootDir/secring.gpg"

        // Write updated gradle.properties back to file
        gradleFile.writer().use { writer ->
            gradleProperties.forEach { (key, value) -> writer.write("$key=$value\n") }
        }

        try {
            val releaseCommand =
                if (shouldRelease) "publishAndReleaseToMavenCentral" else "publishToMavenCentral"
            val releaseCommandMessage = if (shouldRelease) "Publish & Release" else "Publish"
            // Run the Gradle publish command
            println("🔹 Running Gradle publish task : $releaseCommandMessage")
            project.exec {
                commandLine("./gradlew", releaseCommand, "--no-configuration-cache")
            }
            println("✅ $releaseCommandMessage successful!")
            println("Validate the deployment at https://central.sonatype.com/publishing/deployments")
        } finally {
            // Revert gradle.properties to original state
            println("🔄 Reverting gradle.properties...")
            gradleFile.writeText(originalContent)
        }
    }
}
