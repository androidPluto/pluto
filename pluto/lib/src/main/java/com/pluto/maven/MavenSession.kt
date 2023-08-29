package com.pluto.maven

internal object MavenSession {
    var alreadySynced: Boolean = false
    var latestVersion: String? = null
    val releaseUrl: String?
        get() = latestVersion?.let { "https://github.com/androidPluto/pluto/releases/tag/v$it" } ?: run { null }
}
