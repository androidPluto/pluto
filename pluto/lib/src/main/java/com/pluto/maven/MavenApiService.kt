package com.pluto.maven

import retrofit2.http.GET

internal interface MavenApiService {

    @GET("https://search.maven.org/solrsearch/select?q=g:com.plutolib+AND+a:pluto")
    suspend fun getLatestVersion(): MavenData
}
