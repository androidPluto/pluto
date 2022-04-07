package com.pluto.plugins.rooms.db.internal.core

import java.io.IOException
import java.io.PrintWriter
import java.io.Writer

internal class CSVWriter(writer: Writer) {

    private val pw: PrintWriter = PrintWriter(writer)

    fun writeNext(nextLine: Array<String?>) {
        val sb = StringBuffer()
        for (i in nextLine.indices) {
            if (i != 0) {
                sb.append(SEPARATOR)
            }
            val nextElement = nextLine[i] ?: continue
            sb.append(QUOTE_CHARACTER)
            for (element in nextElement) {
                when (element) {
                    ESCAPE_OR_QUOTE_CHARACTER -> sb.append(ESCAPE_OR_QUOTE_CHARACTER).append(element)
                    else -> sb.append(element)
                }
            }
            sb.append(QUOTE_CHARACTER)
        }
        sb.append(NEW_LINE)
        pw.write(sb.toString())
    }

    @Throws(IOException::class)
    fun flush() {
        pw.flush()
    }

    @Throws(IOException::class)
    fun close() {
        pw.flush()
        pw.close()
    }

    companion object {
        const val ESCAPE_OR_QUOTE_CHARACTER = '"'
        const val SEPARATOR = ','
        const val QUOTE_CHARACTER = '"'
        const val NEW_LINE = "\n"
    }
}
