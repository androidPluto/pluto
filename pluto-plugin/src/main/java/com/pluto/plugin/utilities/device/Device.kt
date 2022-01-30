package com.pluto.plugin.utilities.device

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.Keep
import com.pluto.plugin.utilities.DebugLog
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.sqrt

data class Device(val context: Context) {
    val build: BuildData = BuildData()
    val screen: ScreenData = context.getScreen()
    val software: SoftwareData = context.getSoftwareData()
    val app: AppData? = context.getAppData()
}

private fun Context.getSoftwareData(): SoftwareData {
    return SoftwareData(
        androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID),
        androidAPILevel = Build.VERSION.SDK_INT.toString(),
        androidOs = Build.VERSION.RELEASE,
        isRooted = RootUtil.isDeviceRooted
    )
}

private const val DENSITY_MULTIPLIER = 160f
private fun Context.getScreen(): ScreenData {
    val dm = resources.displayMetrics
    val height = dm.heightPixels
    val width = dm.widthPixels
    val x = (width / dm.xdpi.toDouble()).pow(2.0)
    val y = (height / dm.ydpi.toDouble()).pow(2.0)
    val screenInches = sqrt(x + y)
    val rounded = ceil(screenInches)
    val densityDpi = (dm.density * DENSITY_MULTIPLIER).toInt()

    return ScreenData(
        widthPx = width,
        heightPx = height,
        sizeInches = rounded,
        density = densityDpi,
        orientation = getOrientation()
    )
}

private fun Context.getOrientation(): String {
    return when (resources.configuration.orientation) {
        1 -> "portrait"
        2 -> "landscape"
        else -> "undefined"
    }
}

private fun Context.getAppData(): AppData? {
    try {
        val info: PackageInfo = packageManager.getPackageInfo(packageName, 0)
        return AppData(
            name = info.applicationInfo.name,
            packageName = info.packageName,
            version = VersionData(
                name = info.versionName,
                code = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) info.longVersionCode else info.versionCode.toLong()
            )
        )
    } catch (e: PackageManager.NameNotFoundException) {
        DebugLog.e("device-fingerprint", "error while fetching version info", e)
    }
    return null
}

@Keep
data class VersionData(
    val name: String,
    val code: Long
)

@Keep
data class AppData(
    val name: String,
    val packageName: String,
    val version: VersionData
)

@Keep
data class ScreenData(
    val widthPx: Int,
    val heightPx: Int,
    val sizeInches: Double,
    val density: Int,
    val orientation: String
)

@Keep
data class SoftwareData(
    val androidId: String?,
    val androidAPILevel: String?,
    val androidOs: String?,
    val isRooted: Boolean
)

@Keep
data class BuildData(
    val user: String? = Build.USER,
    val host: String? = Build.HOST,
    val id: String? = Build.ID,
    val fingerprint: String? = Build.FINGERPRINT,
    val manufacturer: String? = Build.MANUFACTURER,
    val hardware: String? = Build.HARDWARE,
    val board: String = Build.BOARD,
    val brand: String? = Build.BRAND,
    val bootloader: String? = Build.BOOTLOADER,
    val model: String? = Build.MODEL,
    val gaId: String? = null
)
