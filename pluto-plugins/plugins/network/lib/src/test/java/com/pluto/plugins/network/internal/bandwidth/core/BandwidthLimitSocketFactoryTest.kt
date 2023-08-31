
import com.pluto.plugins.network.internal.bandwidth.core.BandwidthLimitSocketFactory
import com.pluto.plugins.network.internal.bandwidth.core.ThrottledInputStream
import com.pluto.plugins.network.internal.bandwidth.core.ThrottledOutputStream
import java.io.ByteArrayInputStream
import java.io.IOException
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Buffer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class BandwidthLimitSocketFactoryTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val factory = BandwidthLimitSocketFactory()
        okHttpClient = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .cache(null)
            .socketFactory(factory)
            .build()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testUploadSpeed() {
        ThrottledOutputStream.maxBytesPerSecond = 1024 * 512
        ThrottledInputStream.maxBytesPerSecond = Long.MAX_VALUE
        val dataSizeBytes = 1024 * 1024

        mockWebServer.enqueue(MockResponse())

        val request = Request.Builder()
            .url(mockWebServer.url("/"))
            .put(MockRequestBody(dataSizeBytes.toLong()))
            .header("Connection", "close")
            .build()

        val startTime = System.currentTimeMillis()
        okHttpClient.newCall(request).execute()
        val endTime = System.currentTimeMillis()

        val uploadTimeSeconds = (endTime - startTime) / 1000.0
        val uploadSpeedMbps = dataSizeBytes * 8.0 / uploadTimeSeconds / 1024 / 1024
        println("Upload Speed: $uploadSpeedMbps Mbps")
        Assert.assertEquals(1024 / 512, uploadTimeSeconds.toInt())
    }

    @Test
    fun testDownloadSpeed() {
        ThrottledOutputStream.maxBytesPerSecond = Long.MAX_VALUE
        ThrottledInputStream.maxBytesPerSecond = 1024 * 512
        val dataSizeBytes = 1024 * 1024

        mockWebServer.enqueue(MockResponse())

        val request = Request.Builder()
            .url(mockWebServer.url("/"))
            .put(MockRequestBody(dataSizeBytes.toLong()))
            .header("Connection", "close")
            .build()

        val startTime = System.currentTimeMillis()
        okHttpClient.newCall(request).execute()
        val endTime = System.currentTimeMillis()

        val uploadTimeSeconds = (endTime - startTime) / 1000.0
        val uploadSpeedMbps = dataSizeBytes * 8.0 / uploadTimeSeconds / 1024 / 1024
        println("Upload Speed: $uploadSpeedMbps Mbps")
        Assert.assertEquals(1024 / 512, uploadTimeSeconds.toInt())
    }
    // Similar test for download speed using mockWebServer.enqueue
}

// Placeholder for MockRequestBody
class MockRequestBody(private val contentLength: Long) : okhttp3.RequestBody() {
    override fun contentType() = null

    override fun contentLength() = contentLength

    @Throws(IOException::class)
    override fun writeTo(sink: okio.BufferedSink) {
        val buffer = Buffer()
        val inputStream = ByteArrayInputStream(ByteArray(contentLength.toInt()))
        buffer.readFrom(inputStream)
        sink.write(buffer, contentLength)
    }
}
