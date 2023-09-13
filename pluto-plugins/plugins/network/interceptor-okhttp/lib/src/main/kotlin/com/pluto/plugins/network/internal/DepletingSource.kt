/**
 * Source: chucker - https://github.com/ChuckerTeam/chucker.git
 * License: https://github.com/ChuckerTeam/chucker/blob/develop/LICENSE.txt
 */
package com.pluto.plugins.network.internal

import java.io.IOException
import okio.Buffer
import okio.ForwardingSource
import okio.Source
import okio.blackholeSink
import okio.buffer

internal class DepletingSource(delegate: Source) : ForwardingSource(delegate) {
    private var shouldDeplete = true

    override fun read(sink: Buffer, byteCount: Long) = try {
        val bytesRead = super.read(sink, byteCount)
        if (bytesRead == -1L) shouldDeplete = false
        bytesRead
    } catch (e: IOException) {
        shouldDeplete = false
        throw e
    }

    override fun close() {
        if (shouldDeplete) {
            try {
                delegate.buffer().readAll(blackholeSink())
            } catch (e: IOException) {
                IOException("error occurred while depleting the source", e).printStackTrace()
            }
        }
        shouldDeplete = false

        super.close()
    }
}
