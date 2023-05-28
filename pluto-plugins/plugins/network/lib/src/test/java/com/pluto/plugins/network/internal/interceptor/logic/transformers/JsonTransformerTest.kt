package com.pluto.plugins.network.internal.interceptor.logic.transformers

import org.junit.Test

class JsonTransformerTest {

    @Test
    fun test_int_parsing() {
        val actualOutPut =
            JsonTransformer().beautify("""{"int":10}""".trimIndent())

        val expectedOutput = """{
                        |		"int": 10
                        |}""".trimMargin()
        assert(actualOutPut == expectedOutput) {
            "\nExpected output is \n$expectedOutput\nbut actual output is \n$actualOutPut"
        }
    }

    @Test
    fun test_array_parsing() {
        val actualOutPut =
            JsonTransformer().beautify("""[20,10]""".trimIndent())

        val expectedOutput = """[
                        |		20,
                        |		10
                        |]""".trimMargin()
        assert(actualOutPut == expectedOutput) {
            "\nExpected output is \n$expectedOutput\nbut actual output is \n$actualOutPut"
        }
    }
}