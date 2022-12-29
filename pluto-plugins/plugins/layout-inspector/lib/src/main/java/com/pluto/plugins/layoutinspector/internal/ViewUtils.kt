package com.pluto.plugins.layoutinspector.internal

import android.app.Activity
import android.content.ContextWrapper
import android.content.res.Resources
import android.os.Build
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.annotation.GravityInt
import com.pluto.plugin.R
import com.pluto.utilities.extensions.ViewIdInfo
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.getIdInfo
import com.pluto.utilities.spannable.createSpan
import java.lang.reflect.Field

object ViewUtils {

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
                    if (layoutParams.title.toString().contains(targetActivity.javaClass.name) ||
                        getTargetDecorView(targetActivity, decorView) != null
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

    fun View.getIdString(): CharSequence? {
        return getIdInfo()?.let {
            context?.createSpan {
                try {
                    append(semiBold(fontColor(it.packageName, context.color(R.color.pluto___text_dark_60))))
                    append(semiBold(fontColor(":", context.color(R.color.pluto___text_dark_60))))
                    append(semiBold(fontColor(it.typeName, context.color(R.color.pluto___text_dark_60))))
                    append(semiBold(fontColor("/", context.color(R.color.pluto___text_dark_60))))
                    append(semiBold(fontColor(it.entryName, context.color(R.color.pluto___text_dark_80))))
                } catch (e: Resources.NotFoundException) {
                    append(semiBold(fontColor(Integer.toHexString(id), context.color(R.color.pluto___text_dark_80))))
                }
            } ?: run {
                null
            }
        } ?: run {
            null
        }
    }
}
