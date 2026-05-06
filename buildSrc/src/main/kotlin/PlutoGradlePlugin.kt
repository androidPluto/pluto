import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion

class PlutoGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        applyPlugins(project)
        setProjectConfig(project)
    }

    private fun applyPlugins(target: Project) {
        target.apply {
            plugin("android-library")
            plugin("kotlin-android")
        }
    }

    private fun setProjectConfig(target: Project) {
        val version = Versioning.loadVersioningData()
        val verCode = version["code"] as Int
        val verPublish = version["publish"] as String
        val verGitSHA = version["gitSha"] as String
        target.android().apply {
            compileSdk = ProjectConfig.compileSdk
            buildToolsVersion = ProjectConfig.buildTools
            buildFeatures {
                buildConfig = true
                viewBinding = true
            }
            defaultConfig {
                minSdk = ProjectConfig.minSdk
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
                sourceCompatibility = JavaVersion.toVersion(ProjectConfig.java)
                targetCompatibility = JavaVersion.toVersion(ProjectConfig.java)
            }
            lint {
                abortOnError = false
                targetSdk = ProjectConfig.targetSdk
            }
        }
    }

    private fun Project.android(): LibraryExtension {
        return extensions.getByType(LibraryExtension::class.java)
    }
}