package com.pluto.plugins.layoutinspector.internal

import android.app.Activity
import android.content.ContextWrapper
import android.content.res.Resources
import android.os.Build
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.pluto.plugin.R
import com.pluto.utilities.extensions.color
import com.pluto.utilities.spannable.createSpan
import java.lang.reflect.Field

object ViewUtils {

    // see View.java
    // generateViewId(), isViewIdGenerated()
    private fun isViewIdGenerated(id: Int): Boolean {
        return id and -0x1000000 == 0 && id and 0x00FFFFFF != 0
    }

    fun formatGravity(value: String): Int {
        val start = if (value.contains("start")) Gravity.START else 0
        val top = if (value.contains("top")) Gravity.TOP else 0
        val end = if (value.contains("end")) Gravity.END else 0
        val bottom = if (value.contains("bottom")) Gravity.BOTTOM else 0
        return start or top or end or bottom
    }

    fun parseGravity(value: Int): String {
        val start = if (existGravity(value, Gravity.START)) "start" else null
        val top = if (existGravity(value, Gravity.TOP)) "top" else null
        val end = if (existGravity(value, Gravity.END)) "end" else null
        val bottom = if (existGravity(value, Gravity.BOTTOM)) "bottom" else null
        val sb = StringBuilder()
        sb.append(if (!TextUtils.isEmpty(start)) "$start|" else "")
        sb.append(if (!TextUtils.isEmpty(top)) "$top|" else "")
        sb.append(if (!TextUtils.isEmpty(end)) "$end|" else "")
        sb.append(if (!TextUtils.isEmpty(bottom)) "$bottom|" else "")
        var result = sb.toString()
        if (result.endsWith("|")) {
            result = result.substring(0, result.lastIndexOf("|"))
        }
        return result
    }

    private fun existGravity(value: Int, attr: Int): Boolean {
        return value and attr == attr
    }

    fun tryGetTheFrontView(targetActivity: Activity): View {
        try {
            val windowManager = targetActivity.windowManager
            val mGlobalField: Field = Reflect28Util.getDeclaredField(Reflect28Util.forName("android.view.WindowManagerImpl"), "mGlobal")
            mGlobalField.isAccessible = true
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                val mViewsField = Class.forName("android.view.WindowManagerGlobal").getDeclaredField("mViews")
                mViewsField.isAccessible = true
                val views = mViewsField[mGlobalField[windowManager]] as List<View>
                for (i in views.indices.reversed()) {
                    val targetView = getTargetDecorView(targetActivity, views[i])
                    if (targetView != null) {
                        return targetView
                    }
                }
            } else {
                val mRootsField: Field = Reflect28Util.getDeclaredField(Reflect28Util.forName("android.view.WindowManagerGlobal"), "mRoots")
                mRootsField.isAccessible = true
                val viewRootImpls: List<*> =
                    mRootsField[mGlobalField[windowManager]] as List<*>
                for (i in viewRootImpls.indices.reversed()) {
                    val clazz: Class<*> = Reflect28Util.forName("android.view.ViewRootImpl")
                    val `object` = viewRootImpls[i]!!
                    val mWindowAttributesField: Field = Reflect28Util.getDeclaredField(clazz, "mWindowAttributes")
                    mWindowAttributesField.isAccessible = true
                    val mViewField: Field = Reflect28Util.getDeclaredField(clazz, "mView")
                    mViewField.isAccessible = true
                    val decorView = mViewField[`object`] as View
                    val layoutParams = mWindowAttributesField[`object`] as WindowManager.LayoutParams
                    if (layoutParams.title.toString().contains(targetActivity.javaClass.name)
                        || getTargetDecorView(targetActivity, decorView) != null
                    ) {
                        return decorView
                    }
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return targetActivity.window.peekDecorView()
    }

    private fun getTargetDecorView(targetActivity: Activity, decorView: View): View? {
        var targetView: View? = null
        var context = decorView.context
        if (context === targetActivity) {
            targetView = decorView
        } else {
            while (context is ContextWrapper && context !is Activity) {
                val baseContext = context.baseContext ?: break
                if (baseContext === targetActivity) {
                    targetView = decorView
                    break
                }
                context = baseContext
            }
        }
        return targetView
    }

    fun View.getIdString(): CharSequence {
        val resources = this.resources
        return context?.createSpan {
            if (id != View.NO_ID && !isViewIdGenerated(id)) {
                try {
                    val pkgName: String = when (id and -0x1000000) {
                        0x7f000000 -> "app"
                        0x01000000 -> "android"
                        else -> resources.getResourcePackageName(id)
                    }
                    val typename: String = resources.getResourceTypeName(id)
                    val entryName: String = resources.getResourceEntryName(id)

                    append(semiBold(fontColor(pkgName, context.color(R.color.pluto___text_dark_60))))
                    append(semiBold(fontColor(":", context.color(R.color.pluto___text_dark_60))))
                    append(semiBold(fontColor(typename, context.color(R.color.pluto___text_dark_60))))
                    append(semiBold(fontColor("/", context.color(R.color.pluto___text_dark_60))))
                    append(semiBold(fontColor(entryName, context.color(R.color.pluto___text_dark_80))))
                } catch (e: Resources.NotFoundException) {
                    append(semiBold(fontColor(Integer.toHexString(id), context.color(R.color.pluto___text_dark_80))))
                }
            } else {
                append(regular(italic(fontColor("NO_ID", context.color(R.color.pluto___text_dark_60)))))
            }
        } ?: run {
            "NO_ID"
        }
    }

}
