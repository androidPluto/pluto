package com.pluto.plugins.logger

import com.pluto.plugins.logger.internal.LogsProcessor
import com.pluto.plugins.logger.internal.LogsProcessor.Companion.LOG_EVENT_PRIORITY
import com.pluto.plugins.logger.internal.LogsProcessor.Companion.stackTraceElement
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.math.BigDecimal
import timber.log.Timber

class PlutoTimberTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == LOG_EVENT_PRIORITY) {
            val eventData = eventExtractor(message)
            LogsProcessor.processEvent(tag ?: "pluto_timber", eventData.first, eventData.second, Thread.currentThread().getStackTraceElement())
        } else {
            LogsProcessor.process(priority, tag ?: "pluto_timber", messageExtractor(message), t, Thread.currentThread().getStackTraceElement())
        }
    }

    @SuppressWarnings("NestedBlockDepth")
    private fun eventExtractor(message: String): Pair<String, HashMap<String, Any?>?> {
        val length = message.length
        var newline = message.indexOf('\t', 0)
        newline = if (newline != -1) newline else length
        val end = newline.coerceAtMost(MAX_LOG_LENGTH)

        val event = message.substring(0, end)
        val attrString = if (end < length) {
            val moshi = Moshi.Builder().build()
            val moshiAdapter: JsonAdapter<Map<String, Any?>?> = moshi.adapter(Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java))
            val map = moshiAdapter.fromJson(message.substring(end + 1, length))
            val hashMap = HashMap<String, Any?>()
            if (!map.isNullOrEmpty()) {
                map.entries.forEach {
                    hashMap[it.key] = when (it.value) {
                        is Double -> BigDecimal(it.value.toString()).longValueExact()
                        else -> it.value
                    }
                }
                hashMap
            } else {
                null
            }
        } else {
            null
        }
        return Pair(event, attrString)
    }

    private fun messageExtractor(message: String): String {
        val length = message.length
        var newline = message.indexOf('\n', 0)
        newline = if (newline != -1) newline else length
        val end = newline.coerceAtMost(MAX_LOG_LENGTH)
        return message.substring(0, end)
    }

    private fun Thread.getStackTraceElement(): StackTraceElement {
        val index = if (name == "main") MAIN_THREAD_INDEX else DAEMON_THREAD_INDEX
        return stackTraceElement(index)
    }

    companion object {
        private const val MAX_LOG_LENGTH = 4000
        private const val MAIN_THREAD_INDEX = 9
        private const val DAEMON_THREAD_INDEX = 8
    }
}

fun Timber.Tree.event(event: String, attr: HashMap<String, Any?>?) {
    val moshi = Moshi.Builder().build()
    val moshiAdapter: JsonAdapter<Map<String, Any?>?> = moshi.adapter(Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java))
    log(LOG_EVENT_PRIORITY, "$event\t${moshiAdapter.toJson(attr)}")
}
