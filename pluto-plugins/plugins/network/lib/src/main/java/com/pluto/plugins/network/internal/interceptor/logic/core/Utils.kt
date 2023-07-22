package com.pluto.plugins.network.internal.interceptor.logic.core

import io.ktor.http.Url
import java.util.UUID

fun getRequestID(url: String) =
    UUID.nameUUIDFromBytes("${System.currentTimeMillis()}::$url".toByteArray()).toString()

fun String.asUrl() = Url(this)
