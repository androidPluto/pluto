package com.pluto.plugins.layoutinspector.internal.inspect

import android.app.Activity
import android.content.ContextWrapper
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.view.WindowManager
import com.pluto.plugin.R
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.getIdInfo
import com.pluto.utilities.spannable.createSpan
import java.lang.reflect.Field

internal fun Activity.tryGetTheFrontView(): View {
    try {
        val mGlobalField: Field = Reflect28Util.getDeclaredField(Reflect28Util.forName("android.view.WindowManagerImpl"), "mGlobal")
        mGlobalField.isAccessible = true
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            val mViewsField = Class.forName("android.view.WindowManagerGlobal").getDeclaredField("mViews")
            mViewsField.isAccessible = true
            val views = mViewsField[mGlobalField[windowManager]] as List<View>
            for (i in views.indices.reversed()) {
                getTargetDecorView(views[i])?.let {
                    return it
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
                if (layoutParams.title.toString().contains(javaClass.name) ||
                    getTargetDecorView(decorView) != null
                ) {
                    return decorView
                }
            }
        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return window.peekDecorView()
}

private fun Activity.getTargetDecorView(decorView: View): View? {
    var targetView: View? = null
    var context = decorView.context
    if (context === this) {
        targetView = decorView
    } else {
        while (context is ContextWrapper && context !is Activity) {
            val baseContext = context.baseContext ?: break
            if (baseContext === this) {
                targetView = decorView
                break
            }
            context = baseContext
        }
    }
    return targetView
}

internal fun View.getIdString(): CharSequence? {
    try {
        return getIdInfo()?.let {
            context?.createSpan {
                append(semiBold(fontColor(it.packageName, context.color(R.color.pluto___text_dark_60))))
                append(semiBold(fontColor(":", context.color(R.color.pluto___text_dark_60))))
                append(semiBold(fontColor(it.typeName, context.color(R.color.pluto___text_dark_60))))
                append(semiBold(fontColor("/", context.color(R.color.pluto___text_dark_60))))
                append(semiBold(fontColor(it.entryName, context.color(R.color.pluto___text_dark_80))))
            } ?: run {
                null
            }
        } ?: run {
            null
        }
    } catch (e: Resources.NotFoundException) {
        return Integer.toHexString(id)
    }
}
