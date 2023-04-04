package com.pluto.plugins.layoutinspector.internal.inspect

import android.annotation.SuppressLint
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

@SuppressLint("SoonBlockedPrivateApi", "DiscouragedPrivateApi", "PrivateApi")
@SuppressWarnings("TooGenericExceptionCaught", "NestedBlockDepth")
internal fun Activity.tryGetTheFrontView(): View {
    try {
        val windowManagerImplClazz: Class<*> = Class.forName("android.view.WindowManagerImpl")
        val windowManagerGlobalClazz: Class<*> = Class.forName("android.view.WindowManagerGlobal")
        val viewRootImplClazz: Class<*> = Class.forName("android.view.ViewRootImpl")

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            val mGlobalField: Field = windowManagerImplClazz.getDeclaredField("mGlobal")
            mGlobalField.isAccessible = true
            val mViewsField = windowManagerGlobalClazz.getDeclaredField("mViews")
            mViewsField.isAccessible = true
            val views = mViewsField[mGlobalField[windowManager]] as List<View>
            for (i in views.indices.reversed()) {
                getTargetDecorView(views[i])?.let {
                    return it
                }
            }
        } else {
            val mGlobalField: Field = windowManagerImplClazz.getDeclaredField("mGlobal")
            mGlobalField.isAccessible = true
            val mRootsField: Field = windowManagerGlobalClazz.getDeclaredField("mRoots")
            mRootsField.isAccessible = true
            val viewRootImpls: List<*> = mRootsField[mGlobalField[windowManager]] as List<*>
            for (i in viewRootImpls.indices.reversed()) {
                val rootImpl = viewRootImpls[i]!!
                val mWindowAttributesField: Field = viewRootImplClazz.getDeclaredField("mWindowAttributes")
                mWindowAttributesField.isAccessible = true
                val mViewField: Field = viewRootImplClazz.getDeclaredField("mView")
                mViewField.isAccessible = true
                val decorView = mViewField[rootImpl] as View
                val layoutParams = mWindowAttributesField[rootImpl] as WindowManager.LayoutParams
                if (layoutParams.title.toString().contains(javaClass.name) || getTargetDecorView(decorView) != null) {
                    return decorView
                }
            }
        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return window.peekDecorView()
}

@SuppressWarnings("LoopWithTooManyJumpStatements")
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
        e.printStackTrace()
        return Integer.toHexString(id)
    }
}
