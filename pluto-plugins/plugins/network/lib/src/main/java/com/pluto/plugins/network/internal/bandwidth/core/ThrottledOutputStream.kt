package com.pluto.plugins.network.internal.bandwidth.core

import java.io.IOException
import java.io.OutputStream

class ThrottledOutputStream constructor(
    outputStream: OutputStream
) :
    OutputStream() {
    private val outputStream: OutputStream
    private val startTime = System.nanoTime()
    private var bytesWrite: Long = 0
    private var totalSleepTime: Long = 0

    init {
        this.outputStream = outputStream
    }

    @Throws(IOException::class)
    override fun write(arg0: Int) {
        throttle()
        outputStream.write(arg0)
        bytesWrite++
    }

    @Throws(IOException::class)
    override fun write(b: ByteArray, off: Int, len: Int) {
        if (len < maxBytesPerSecond) {
            throttle()
            bytesWrite += len
            outputStream.write(b, off, len)
            return
        }
        var currentOffSet = off.toLong()
        var remainingBytesToWrite = len.toLong()
        do {
            throttle()
            remainingBytesToWrite -= maxBytesPerSecond
            bytesWrite += maxBytesPerSecond
            outputStream.write(b, currentOffSet.toInt(), maxBytesPerSecond.toInt())
            currentOffSet += maxBytesPerSecond
        } while (remainingBytesToWrite > maxBytesPerSecond)
        throttle()
        bytesWrite += remainingBytesToWrite
        outputStream.write(b, currentOffSet.toInt(), remainingBytesToWrite.toInt())
    }

    @Throws(IOException::class)
    override fun write(b: ByteArray) {
        this.write(b, 0, b.size)
    }

    @Throws(IOException::class)
    fun throttle() {
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
                bytesWrite
            } else {
                bytesWrite / elapsed
            }
        }

    override fun toString(): String {
        val totalSleepTimeInSeconds = totalSleepTime / SECOND_IN_MILLISECONDS
        return "ThrottledOutputStream{" + "bytesWrite=" + bytesWrite + ", maxBytesPerSecond=" +
            maxBytesPerSecond + ", bytesPerSec=" + bytesPerSec + ", totalSleepTimeInSeconds=" +
            totalSleepTimeInSeconds + '}'
    }

    @Throws(IOException::class)
    override fun close() {
        outputStream.close()
    }

    companion object {
        private const val SLEEP_DURATION_MS: Long = 30
        private const val SECOND_IN_NANOSECONDS = 1_000_000_000L
        private const val SECOND_IN_MILLISECONDS = 1000L
        @JvmStatic
        var maxBytesPerSecond: Long = Long.MAX_VALUE
    }
}
