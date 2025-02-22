import java.io.File
import java.util.Properties

object Versioning {
    fun loadVersioningData(): Map<String, Any> {
        val version = mutableMapOf<String, Any>()

        val versionProps = Properties().apply {
            load(File("${System.getProperty("user.dir")}/version.properties").inputStream())
        }

        val versionMajor = versionProps["major"]?.toString()?.toInt() ?: 0
        val versionMinor = versionProps["minor"]?.toString()?.toInt() ?: 0
        val versionPatch = versionProps["patch"]?.toString()?.toInt() ?: 0
        val versionBuild = versionProps["build"]?.toString()?.toInt() ?: 0
        val versionChannel = versionProps["channel"]?.toString() ?: "release"

        version["code"] = (versionMajor * 1_000_000) + (versionMinor * 10_000) + (versionPatch * 100) + versionBuild
        version["name"] = "$versionMajor.$versionMinor.$versionPatch-rc$versionBuild"

        var publishVersion = "$versionMajor.$versionMinor.$versionPatch"
        if (versionChannel != "release") {
            publishVersion = "$publishVersion-$versionChannel$versionBuild"
        }
        version["publish"] = publishVersion

        val gitSha = "git rev-parse --short=10 HEAD".runCommand()?.trim() ?: ""
        version["gitSha"] = gitSha

        return version
    }

    private fun String.runCommand(): String? {
        return try {
            ProcessBuilder("/bin/sh", "-c", this).start().inputStream.bufferedReader().readText()
        } catch (e: Exception) {
            null
        }
    }
}
