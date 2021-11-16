package com.mocklets.pluto.plugin

data class DeveloperDetails(
    val vcsLink: String? = null,
    val website: String? = null,
    val developer: Developer? = null
)

data class Developer(
    val github: String? = null,
    val twitter: String? = null
)
