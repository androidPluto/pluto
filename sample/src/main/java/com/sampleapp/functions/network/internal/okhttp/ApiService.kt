package com.sampleapp.functions.network.internal.okhttp

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @Headers("Authorization: Bearer e42ed4a6-f346-4f0b-ad49-02c66dcca91a")
    @POST("post?scope=points,preferences")
    suspend fun form(@Field("title") title: String, @Field("diff") diff: String): Any

    @Headers("Authorization: Bearer e42ed4a6-f346-4f0b-ad49-02c66dcca91a")
    @POST("post/new")
    suspend fun post(@Body hashMapOf: Any): Any

    @Headers("Authorization: Bearer e42ed4a6-f346-4f0b-ad49-02c66dcca91a")
    @GET("get")
    suspend fun get(): Any

    @Headers(
        "type: xml",
        "x-app-theme: light",
        "Authorization: Bearer e42ed4a6-f346-4f0b-ad49-02c66dcca91a"
    )
    @POST("xml")
    suspend fun xml(@Body hashMapOf: RequestBody): Any

    // https://studio.apollographql.com/public/SpaceX-pxxbxen/variant/current/home
    @POST("https://spacex-production.up.railway.app/")
    suspend fun graphql(@Body body: Any): Any
}
