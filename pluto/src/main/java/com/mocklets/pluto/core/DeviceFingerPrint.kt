package com.mocklets.pluto.core

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.Keep
import com.mocklets.pluto.core.ui.list.ListItem
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.sqrt

@Keep
internal class DeviceFingerPrint(context: Context) : ListItem() {
    val build = BuildData()
    val screen = context.getScreen()
    val software = context.getSoftwareData()
    val isRooted = RootUtil.isDeviceRooted
    val gaId: String? = null
}

private fun Context.getSoftwareData(): SoftwareData {
    return SoftwareData(
        androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID),
        androidAPILevel = Build.VERSION.SDK_INT.toString(),
        androidOs = Build.VERSION.RELEASE,
        appVersion = getVersion(),
        orientation = getOrientation()
    )
}

private const val DENSITY_MULTIPLIER = 160f
private fun Context.getScreen(): Screen {
    val dm = resources.displayMetrics
    val height = dm.heightPixels
    val width = dm.widthPixels
    val x = (width / dm.xdpi.toDouble()).pow(2.0)
    val y = (height / dm.ydpi.toDouble()).pow(2.0)
    val screenInches = sqrt(x + y)
    val rounded = ceil(screenInches)
    val densityDpi = (dm.density * DENSITY_MULTIPLIER).toInt()

    return Screen(
        width = "${width}px",
        height = "${height}px",
        size = "$rounded inches",
        density = "$densityDpi dpi"
    )
}

private fun Context.getOrientation(): String {
    return when (resources.configuration.orientation) {
        1 -> "portrait"
        2 -> "landscape"
        else -> "undefined"
    }
}

private fun Context.getVersion(): VersionData? {
    try {
        val info: PackageInfo = packageManager.getPackageInfo(packageName, 0)
        return VersionData(
            info.versionName,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) info.longVersionCode else info.versionCode.toLong()
        )
    } catch (e: PackageManager.NameNotFoundException) {
        DebugLog.e("device-fingerprint", "error while fetching version info", e)
    }
    return null
}

@Keep
internal data class VersionData(
    val name: String,
    val code: Long
)

@Keep
internal data class Screen(
    val width: String,
    val height: String,
    val size: String,
    val density: String
)

@Keep
internal data class SoftwareData(
    val androidId: String?,
    val androidAPILevel: String?,
    val androidOs: String?,
    val appVersion: VersionData?,
    val orientation: String
)

@Keep
internal data class BuildData(
    val user: String? = Build.USER,
    val host: String? = Build.HOST,
    val id: String? = Build.ID,
    val fingerprint: String? = Build.FINGERPRINT,
    val manufacturer: String? = Build.MANUFACTURER,
    val hardware: String? = Build.HARDWARE,
    val board: String = Build.BOARD,
    val brand: String? = Build.BRAND,
    val bootloader: String? = Build.BOOTLOADER,
    val model: String? = Build.MODEL
)
