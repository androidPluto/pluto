
import com.pluto.plugins.network.internal.bandwidth.core.BandwidthLimitSocketFactory
import com.pluto.plugins.network.internal.bandwidth.core.ThrottledInputStream
import com.pluto.plugins.network.internal.bandwidth.core.ThrottledOutputStream
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import kotlin.math.ceil
import kotlin.random.Random
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
        Assert.assertEquals(
            dataSizeBytes / ThrottledOutputStream.maxBytesPerSecond, uploadTimeSeconds.toInt()
        )
    }

    @Test
    fun testDownloadSpeed() {
        ThrottledOutputStream.maxBytesPerSecond = Long.MAX_VALUE
        ThrottledInputStream.maxBytesPerSecond = 1024 * 512
        val dataSizeBytes = 1024 * 1024
        mockWebServer.enqueue(
            MockResponse().setHeader("Content-Length", dataSizeBytes).setResponseCode(200).setBody(
                String(
                    Random.nextBytes(ByteArray(dataSizeBytes)), StandardCharsets.UTF_8
                )
            )
        )

        val request = Request.Builder()
            .url(mockWebServer.url("/"))
            .put(MockRequestBody(0))
            .build()

        val startTime = System.currentTimeMillis()
        val execute = okHttpClient.newCall(request).execute()
        execute.body.string()
        val byteCount = (execute.headers["Content-Length"])?.toInt() ?: 0
        val endTime = System.currentTimeMillis()

        val downloadTimeSeconds = (endTime - startTime) / 1000.0
        Assert.assertEquals(
            ceil(byteCount * 1f / ThrottledInputStream.maxBytesPerSecond).toInt(),
            downloadTimeSeconds.toInt()
        )
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
