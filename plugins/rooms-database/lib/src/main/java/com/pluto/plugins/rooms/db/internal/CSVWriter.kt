package com.pluto.plugins.rooms.db.internal

import java.io.IOException
import java.io.PrintWriter
import java.io.Writer

internal class CSVWriter @JvmOverloads constructor(
    writer: Writer,
    private val separator: Char = DEFAULT_SEPARATOR,
    private val quoteChar: Char = DEFAULT_QUOTE_CHARACTER,
    private val escapeChar: Char = DEFAULT_ESCAPE_CHARACTER,
    private val lineEnd: String = DEFAULT_LINE_END
) {
    private val pw: PrintWriter = PrintWriter(writer)

    fun writeNext(nextLine: Array<String?>) {
        val sb = StringBuffer()
        for (i in nextLine.indices) {
            if (i != 0) {
                sb.append(separator)
            }
            val nextElement = nextLine[i] ?: continue
            if (quoteChar != NO_QUOTE_CHARACTER) sb.append(quoteChar)
            for (element in nextElement) {
                if (escapeChar != NO_ESCAPE_CHARACTER && element == quoteChar) {
                    sb.append(escapeChar).append(element)
                } else if (escapeChar != NO_ESCAPE_CHARACTER && element == escapeChar) {
                    sb.append(escapeChar).append(element)
                } else {
                    sb.append(element)
                }
            }
            if (quoteChar != NO_QUOTE_CHARACTER) sb.append(quoteChar)
        }
        sb.append(lineEnd)
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
        /** The character used for escaping quotes.  */
        const val DEFAULT_ESCAPE_CHARACTER = '"'

        /** The default separator to use if none is supplied to the constructor.  */
        const val DEFAULT_SEPARATOR = ','

        /**
         * The default quote character to use if none is supplied to the
         * constructor.
         */
        const val DEFAULT_QUOTE_CHARACTER = '"'

        /** The quote constant to use when you wish to suppress all quoting.  */
        const val NO_QUOTE_CHARACTER = '\u0000'

        /** The escape constant to use when you wish to suppress all escaping.  */
        const val NO_ESCAPE_CHARACTER = '\u0000'

        /** Default line terminator uses platform encoding.  */
        const val DEFAULT_LINE_END = "\n"
    }
}
