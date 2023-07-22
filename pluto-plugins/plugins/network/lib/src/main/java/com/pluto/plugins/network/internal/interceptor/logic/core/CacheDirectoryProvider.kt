/**
 * Source: chucker - https://github.com/ChuckerTeam/chucker.git
 * License: https://github.com/ChuckerTeam/chucker/blob/develop/LICENSE.txt
 */
package com.pluto.plugins.network.internal.interceptor.logic.core

import java.io.File

/**
 * An interface that returns a reference to a cache directory where temporary files can be
 * saved.
 */
fun interface CacheDirectoryProvider {
    fun provide(): File?
}
