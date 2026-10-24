package com.pluto.plugins.logger.internal

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LogsProcessorTest {
    @Test
    fun doesNotPrintWhenConsoleOutputIsDisabled() {
        var printed = false

        LogsProcessor.printToConsoleIfEnabled(false) {
            printed = true
        }

        assertFalse(printed)
    }

    @Test
    fun printsWhenConsoleOutputIsEnabled() {
        var printed = false

        LogsProcessor.printToConsoleIfEnabled(true) {
            printed = true
        }

        assertTrue(printed)
    }
}
