package com.pluto.utilities.device

import android.os.Build
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/** Credits: https://stackoverflow.com/questions/1101380/determine-if-running-on-a-rooted-device/8097801
 *
 * @author Kevin Kowalewski
 */
internal class RootUtil private constructor() {
    companion object {
        val isDeviceRooted: Boolean
            get() = checkRootMethod1() || checkRootMethod2() || checkRootMethod3()

        private fun checkRootMethod1(): Boolean {
            val buildTags = Build.TAGS
            return buildTags != null && buildTags.contains("test-keys")
        }

        private fun checkRootMethod2(): Boolean {
            val paths = arrayOf(
                "/system/app/Superuser.apk",
                "/sbin/su",
                "/system/bin/su",
                "/system/xbin/su",
                "/data/local/xbin/su",
                "/data/local/bin/su",
                "/system/sd/xbin/su",
                "/system/bin/failsafe/su",
                "/data/local/su",
                "/su/bin/su"
            )
            for (path in paths) {
                if (File(path).exists()) return true
            }
            return false
        }

        @Suppress("TooGenericExceptionCaught", "SwallowedException")
        private fun checkRootMethod3(): Boolean {
            var process: Process? = null
            return try {
                process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
                val stream = BufferedReader(InputStreamReader(process.inputStream))
                stream.readLine() != null
            } catch (t: Throwable) {
//            DebugLog.e("root-utils", "exception occurred", t)
                false
            } finally {
                process?.destroy()
            }
        }
    }
}
