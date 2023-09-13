package com.pluto.plugins.network.internal.interceptor.logic.transformers

import com.pluto.plugins.network.internal.interceptor.logic.LOGTAG
import com.pluto.utilities.DebugLog
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.sax.SAXSource
import javax.xml.transform.sax.SAXTransformerFactory
import javax.xml.transform.stream.StreamResult
import org.xml.sax.InputSource

internal class XmlTransformer : BaseTransformer {

    @Suppress("TooGenericExceptionCaught")
    override fun beautify(plain: CharSequence): CharSequence {
        return try {
            val serializer: Transformer = SAXTransformerFactory.newInstance().newTransformer()
            serializer.setOutputProperty(OutputKeys.INDENT, "yes")
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", INDENTATION_TEXT)
            val xmlSource = SAXSource(InputSource(ByteArrayInputStream(plain.toString().toByteArray())))
            val res = StreamResult(ByteArrayOutputStream())
            serializer.transform(xmlSource, res)
            String((res.outputStream as ByteArrayOutputStream).toByteArray())
        } catch (e: Exception) {
            DebugLog.e(LOGTAG, "xml parsing failed : $plain", e)
            plain
        }
    }

    override fun flatten(plain: CharSequence): String {
        return plain.toString().replace("\n", "").replace("\\s+".toRegex(), "")
    }

    companion object {
        private const val INDENTATION_TEXT = "\t\t"
    }
}
