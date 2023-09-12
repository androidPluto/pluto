package com.pluto.plugins.network

import android.content.Context
import com.pluto.plugins.network.internal.bandwidth.core.BandwidthDefaults
import com.pluto.plugins.network.internal.bandwidth.core.BandwidthLimitSocketFactory
import com.pluto.plugins.network.internal.bandwidth.core.DnsDelay
import com.pluto.plugins.network.internal.bandwidth.core.ThrottledInputStream
import com.pluto.plugins.network.internal.bandwidth.core.ThrottledOutputStream
import com.pluto.plugins.network.internal.interceptor.logic.ApiCallData
import com.pluto.plugins.network.internal.interceptor.logic.NetworkCallsRepo
import com.pluto.plugins.network.internal.interceptor.logic.asExceptionData
import com.pluto.plugins.network.internal.interceptor.logic.core.CacheDirectoryProvider
import com.pluto.utilities.DebugLog
import com.pluto.utilities.settings.SettingsPreferences
import java.math.BigInteger
import java.util.UUID
import okhttp3.OkHttpClient

object PlutoNetwork {
    internal var cacheDirectoryProvider: CacheDirectoryProvider? = null
        private set

    internal fun initialize(context: Context) {
        cacheDirectoryProvider = CacheDirectoryProvider { context.applicationContext.filesDir }
    }

    /**
     * Log a custom network trace to Pluto.
     * Allows to connect non-OkHttp based systems to track network calls.
     * @see <a href="https://github.com/androidPluto/pluto/issues/8">https://github.com/plutolib/pluto/issues/8</a>
     *
     * @param request custom request data
     * @param response custom response data
     * @param exception failure exception
     */
    @JvmOverloads
    fun logCustomTrace(request: CustomRequest, response: CustomResponse? = null, exception: Throwable? = null) {
        if (response == null && exception == null) {
            DebugLog.e("pluto_network", "Skipping custom trace logging! Response & Exception both cannot be null at once.")
        } else {
            val apiCallData = ApiCallData(
                id = UUID.nameUUIDFromBytes("${System.currentTimeMillis()}::${request.url}".toByteArray()).toString(),
                request = request.toRequestData(),
                response = response?.toResponseData(),
                exception = exception?.asExceptionData(),
                isCustomTrace = true
            )
            NetworkCallsRepo.set(apiCallData)
        }
    }

    fun OkHttpClient.Builder.enableBandwidthMonitor(): OkHttpClient.Builder {
        updateBandwidthLimitValues()
        return dns(dns)
            .socketFactory(BandwidthLimitSocketFactory())
    }

    fun updateBandwidthLimitValues() {
        if (SettingsPreferences.isBandwidthLimitEnabled) {
            ThrottledInputStream.maxBytesPerSecond =
                BigInteger.valueOf(SettingsPreferences.bandWidthLimitDownloadMbps)
                    .multiply(BigInteger.valueOf(MBPS_TO_BPS)).toLong()
            ThrottledOutputStream.maxBytesPerSecond =
                BigInteger.valueOf(SettingsPreferences.bandWidthLimitUploadMbps)
                    .multiply(BigInteger.valueOf(MBPS_TO_BPS)).toLong()
            dns.timeoutMilliSeconds = SettingsPreferences.bandWidthDnsResolutionDelay
        } else {
            ThrottledInputStream.maxBytesPerSecond = BandwidthDefaults.FULL_NETWORK_SPEED_DOWNLOAD
            ThrottledOutputStream.maxBytesPerSecond = BandwidthDefaults.FULL_NETWORK_SPEED_UPLOAD
            dns.timeoutMilliSeconds = BandwidthDefaults.NO_DELAY
        }
    }

    private val dns = DnsDelay(0)

    private const val MBPS_TO_BPS: Long = 1_000_000L
}
