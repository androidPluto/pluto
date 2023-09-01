package com.pluto.plugins.network.internal.bandwidth.core

import java.io.IOException
import java.io.InputStream

class ThrottledInputStream constructor(
    inputStream: InputStream
) :
    InputStream() {
    private val inputStream: InputStream
    private val startTime = System.nanoTime()
    private var totalBytesRead: Long = 0
    private var totalSleepTime: Long = 0

    init {
        this.inputStream = inputStream
    }

    @Throws(IOException::class)
    override fun close() {
        inputStream.close()
    }

    @Throws(IOException::class)
    override fun read(): Int {
        throttle()
        val data = inputStream.read()
        if (data != -1) {
            totalBytesRead++
        }
        return data
    }

    @Throws(IOException::class)
    override fun read(b: ByteArray): Int {
        throttle()
        val readLen = inputStream.read(b)
        if (readLen != -1) {
            totalBytesRead += readLen.toLong()
        }
        return readLen
    }

    @Throws(IOException::class)
    override fun read(b: ByteArray, off: Int, len: Int): Int {
        throttle()
        val readLen = inputStream.read(b, off, len)
        if (readLen != -1) {
            totalBytesRead += readLen.toLong()
        }
        return readLen
    }

    @Throws(IOException::class)
    private fun throttle() {
        while (bytesPerSec > maxBytesPerSecond) {
            totalSleepTime += try {
                Thread.sleep(SLEEP_DURATION_MS)
                SLEEP_DURATION_MS
            } catch (e: InterruptedException) {
                println("Thread interrupted" + e.message)
                throw IOException("Thread interrupted", e)
            }
        }
    }

    /**
     * Return the number of bytes read per second
     */
    private val bytesPerSec: Long
        get() {
            val elapsed = (System.nanoTime() - startTime) / SECOND_IN_NANOSECONDS
            return if (elapsed == 0L) {
                totalBytesRead
            } else {
                totalBytesRead / elapsed
            }
        }

    override fun toString(): String {
        val totalSleepTimeInSecond = totalSleepTime / SECOND_IN_MILLISECONDS
        return "ThrottledInputStream{bytesRead=$totalBytesRead, " +
            "maxBytesPerSec=$maxBytesPerSecond, bytesPerSec=$bytesPerSec," +
            " totalSleepTimeInSeconds=$totalSleepTimeInSecond}"
    }

    companion object {
        private const val SLEEP_DURATION_MS: Long = 30
        private const val SECOND_IN_NANOSECONDS = 1_000_000_000L
        private const val SECOND_IN_MILLISECONDS = 1000L

        @JvmStatic
        var maxBytesPerSecond: Long = Long.MAX_VALUE
    }
}
