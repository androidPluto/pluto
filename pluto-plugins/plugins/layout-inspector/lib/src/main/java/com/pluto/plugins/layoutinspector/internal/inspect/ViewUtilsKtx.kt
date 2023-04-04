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
internal fun Activity.getFrontView(): View {
    try {
        val windowManagerImplClazz: Class<*> = Class.forName("android.view.WindowManagerImpl")
        val windowManagerGlobalClazz: Class<*> = Class.forName("android.view.WindowManagerGlobal")
        val viewRootImplClazz: Class<*> = Class.forName("android.view.ViewRootImpl")

        val globalField: Field = windowManagerImplClazz.getDeclaredField("mGlobal")
        globalField.isAccessible = true

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            val viewField = windowManagerGlobalClazz.getDeclaredField("mViews")
            viewField.isAccessible = true
            val views = viewField[globalField[windowManager]] as List<View>
            for (view in views.reversed()) {
                getDecorView(view)?.let {
                    return it
                }
            }
        } else {
            val rootsField: Field = windowManagerGlobalClazz.getDeclaredField("mRoots")
            rootsField.isAccessible = true
            val viewRootImplList: List<*> = rootsField[globalField[windowManager]] as List<*>
            for (rootImpl in viewRootImplList.reversed()) {
                val windowAttributesField: Field = viewRootImplClazz.getDeclaredField("mWindowAttributes")
                windowAttributesField.isAccessible = true
                val viewField: Field = viewRootImplClazz.getDeclaredField("mView")
                viewField.isAccessible = true
                val decorView = viewField[rootImpl] as View
                val layoutParams = windowAttributesField[rootImpl] as WindowManager.LayoutParams
                if (layoutParams.title.toString().contains(javaClass.name) || getDecorView(decorView) != null) {
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
private fun Activity.getDecorView(decorView: View): View? {
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

internal fun View.getIdString(): CharSequence? = try {
    getIdInfo()?.let {
        context?.createSpan {
            append(semiBold(fontColor(it.packageName, context.color(R.color.pluto___text_dark_60))))
            append(semiBold(fontColor(":", context.color(R.color.pluto___text_dark_60))))
            append(semiBold(fontColor(it.typeName, context.color(R.color.pluto___text_dark_60))))
            append(semiBold(fontColor("/", context.color(R.color.pluto___text_dark_60))))
            append(semiBold(fontColor(it.entryName, context.color(R.color.pluto___text_dark_80))))
        } ?: run { null }
    } ?: run { null }
} catch (e: Resources.NotFoundException) {
    e.printStackTrace()
    Integer.toHexString(id)
}
