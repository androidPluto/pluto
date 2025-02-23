import java.util.Properties

tasks.register("publishAndReleaseWithMavenCredentials") {
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
            // Run the Gradle publish command
            println("🔹 Running Gradle publish task with temporary credentials...")
            exec {
                commandLine("./gradlew", "publishToMavenCentral", "--no-configuration-cache")
//                commandLine("./gradlew", "publishAndReleaseToMavenCentral", "--no-configuration-cache")
            }
        } finally {
            // Revert gradle.properties to original state
            println("🔄 Reverting gradle.properties...")
            gradleFile.writeText(originalContent)
        }
    }
}
