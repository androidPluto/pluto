package com.pluto.maven

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class MavenData(
    val response: MavenResponse
)

@JsonClass(generateAdapter = true)
internal data class MavenResponse(
    val numFound: Int,
    val docs: List<MavenArtifactDetails>
)

@JsonClass(generateAdapter = true)
internal data class MavenArtifactDetails(
    val id: String,
    val latestVersion: String
)
