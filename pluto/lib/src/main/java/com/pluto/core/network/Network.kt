package com.pluto.core.network

import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Singleton object that provides network services for Pluto.
 *
 * This class configures and manages Retrofit and OkHttp clients for making
 * network requests to Pluto's API. It provides methods to obtain service
 * interfaces for different API endpoints.
 */
internal object Network {

    /** Read timeout in seconds for network requests */
    private const val READ_TIMEOUT = 30L

    /**
     * Lazily initialized Retrofit instance configured with Moshi converter
     * and the OkHttp client.
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.pluto.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    /**
     * OkHttp client configured with appropriate timeouts and interceptors.
     */
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptors()
        .build()

    /**
     * Creates and returns a service interface for the specified class.
     *
     * @param cls The class of the service interface to create
     * @return An implementation of the service interface
     */
    fun <T> getService(cls: Class<T>): T {
        return retrofit.create(cls)
    }

    /**
     * Creates and returns a lazily initialized service interface.
     *
     * This method uses reified type parameters to avoid having to pass
     * the class explicitly.
     *
     * @return A lazy-initialized implementation of the service interface
     */
    inline fun <reified T> getService(): Lazy<T> {
        return lazy {
            getService(T::class.java)
        }
    }
}

/**
 * Extension function to add interceptors to an OkHttpClient.Builder.
 *
 * Currently commented out, but can be used to add logging or other
 * interceptors as needed.
 *
 * @return The builder with interceptors added
 */
private fun OkHttpClient.Builder.addInterceptors(): OkHttpClient.Builder {
//    addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    return this
}
