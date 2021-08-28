package com.mocklets.pluto.modules.network.transformers

import com.mocklets.pluto.core.DebugLog
import com.mocklets.pluto.modules.network.LOGTAG
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.sax.SAXSource
import javax.xml.transform.sax.SAXTransformerFactory
import javax.xml.transform.stream.StreamResult
import org.xml.sax.InputSource

internal class XmlBaseTransformer : BaseTransformer {

    @Suppress("TooGenericExceptionCaught")
    override fun beautify(plain: CharSequence, indent: Int): CharSequence? {
        return try {
            val serializer: Transformer = SAXTransformerFactory.newInstance().newTransformer()
            serializer.setOutputProperty(OutputKeys.INDENT, "yes")
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "$indent")
            val xmlSource = SAXSource(InputSource(ByteArrayInputStream(plain.toString().toByteArray())))
            val res = StreamResult(ByteArrayOutputStream())
            serializer.transform(xmlSource, res)
            String((res.outputStream as ByteArrayOutputStream).toByteArray())
        } catch (e: Exception) {
            DebugLog.e(LOGTAG, "xml parsing failed", e)
            plain
        }
    }

    override fun flatten(plain: CharSequence): String? {
        return plain.toString()
    }
}
