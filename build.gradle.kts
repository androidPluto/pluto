import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import io.gitlab.arturbosch.detekt.Detekt

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.dokka)
//    id("com.android.library")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.parcelize")
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ktlint) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        // for testing Pluto staged repository
        maven("https://s01.oss.sonatype.org/content/groups/staging/")
    }
}

subprojects {
//    if (project.name != "pluto-no-op") {
    pluginManager.withPlugin("kotlin-android") {
        apply (from = "$rootDir/scripts/static-analysis/code-analysis.gradle")
    }
//    }
}

val installGitHook by tasks.registering(Copy::class) {
    from(File(rootProject.rootDir, "pre-commit"))
    into(File(rootProject.rootDir, ".git/hooks"))
    fileMode = "0777".toInt(8)
}

//val clean by tasks.registering(Delete::class) {
//    dependsOn(installGitHook)
//    delete(rootProject.buildDir)
//}

tasks.withType<Detekt>().configureEach {
    exclude(".*/resources/.*,.*/build/.*")
}

val prCheck by tasks.registering {
    dependsOn(":sample:assembleDebug")
    dependsOn(":pluto:validateChanges")
}

apply (from = "$rootDir/scripts/project-dependancy-graph.gradle")
apply (from = "$rootDir/maven-versions.gradle.kts")
apply(from = "$rootDir/publishTasks.gradle.kts")