package com.mocklets.pluto.modules.exceptions

import androidx.annotation.Keep
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.Locale

/**
 * [Exception] to represent an ANR. This [Exception]'s stack trace will be the current stack trace of the given [Thread]
 */
@Keep
class ANRException(thread: Thread) : Exception("ANR detected") {

    val threadStateMap: String
    internal val threadStateList: List<ProcessThread>

    init {
        stackTrace = thread.stackTrace
        threadStateMap = generateProcessMap()
        threadStateList = generateProcessList()
    }

    /**
     * Logs the current process and all its threads
     */
    private fun generateProcessMap(): String {
        val bos = ByteArrayOutputStream()
        val ps = PrintStream(bos)
        printProcessMap(ps)
        return String(bos.toByteArray())
    }

    /**
     * Prints the current process and all its threads
     *
     * @param ps the [PrintStream] to which the
     * info is written
     */
    private fun printProcessMap(ps: PrintStream) {
        // Get all stack traces in the system
        val stackTraces = Thread.getAllStackTraces()
        ps.println("Process map:")
        for (thread in stackTraces.keys) {
            if (!stackTraces[thread].isNullOrEmpty()) {
                printThread(ps, Locale.getDefault(), thread, stackTraces[thread]!!)
                ps.println()
            }
        }
    }

    /**
     * Prints the given thread
     * @param ps the [PrintStream] to which the
     * info is written
     * @param l the [Locale] to use
     * @param thread the [Thread] to print
     * @param stack the [Thread]'s stack trace
     */
    private fun printThread(ps: PrintStream, l: Locale, thread: Thread, stack: Array<StackTraceElement>) {
        ps.println(String.format(l, "\t%s (%s)", thread.name, thread.state))
        for (element in stack) {
            element.apply {
                ps.println(String.format(l, "\t\t%s.%s(%s:%d)", className, methodName, fileName, lineNumber))
            }
        }
    }

    private fun generateProcessList(): List<ProcessThread> {
        val list = arrayListOf<ProcessThread>()
        val stackTraces = Thread.getAllStackTraces()
        for (thread in stackTraces.keys) {
            if (!stackTraces[thread].isNullOrEmpty()) {
                val process = ProcessThread(thread.name, thread.state.name, stackTraces[thread]!!.asStringArray())
                list.add(process)
            }
        }
        return list
    }
}
