/**
 * Source: chucker - https://github.com/ChuckerTeam/chucker/blob/develop/library/src/main/java/com/chuckerteam/chucker/internal/support/CacheDirectoryProvider.kt
 * License: https://github.com/ChuckerTeam/chucker/blob/develop/LICENSE.txt
 */
package com.mocklets.pluto.modules.network.interceptor

import java.io.File

/**
 * An interface that returns a reference to a cache directory where temporary files can be
 * saved.
 */
internal fun interface CacheDirectoryProvider {
    fun provide(): File?
}
