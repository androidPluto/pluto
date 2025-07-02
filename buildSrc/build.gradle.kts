import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies{
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
    implementation("com.android.tools.build:gradle:8.6.0")
}

val compileKotlin:KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}