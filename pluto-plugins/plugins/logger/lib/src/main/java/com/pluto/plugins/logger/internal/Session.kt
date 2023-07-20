package com.pluto.plugins.logger.internal

import java.util.UUID

internal object Session {
    val id: String = UUID.randomUUID().toString()
    var loggerSearchText: String? = null
}
