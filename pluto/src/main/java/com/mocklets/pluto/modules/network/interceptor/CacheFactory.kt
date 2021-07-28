/**
 * Source: chucker - https://github.com/ChuckerTeam/chucker.git
 */
package com.mocklets.pluto.modules.network.interceptor

import java.io.File
import java.io.IOException
import java.util.concurrent.atomic.AtomicLong

internal object CacheFactory {
    private val uniqueIdGenerator = AtomicLong()

    fun create(directory: File) = create(directory, fileName = "pluto-${uniqueIdGenerator.getAndIncrement()}")

    private fun create(directory: File, fileName: String): File? = try {
        File(directory, fileName).apply {
            if (exists() && !delete()) {
                throw IOException("Failed to delete file $this")
            }
            parentFile?.mkdirs()
            if (!createNewFile()) {
                throw IOException("File $this already exists")
            }
        }
    } catch (e: IOException) {
        IOException("An error occurred while creating a Pluto file", e).printStackTrace()
        null
    }
}
