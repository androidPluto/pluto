package com.pluto.utilities.share.csv

class CSVFormatter private constructor() {

    companion object {
        fun write(nextLine: Array<String?>): String {
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
            return sb.toString()
        }

        private const val ESCAPE_OR_QUOTE_CHARACTER = '"'
        private const val SEPARATOR = ','
        private const val QUOTE_CHARACTER = '"'
        private const val NEW_LINE = "\n"
    }
}
