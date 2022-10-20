package com.pluto.utilities.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import com.pluto.plugin.R
import com.pluto.utilities.spannable.createSpan

@SuppressLint("RestrictedApi")
fun Context.showMoreOptions(view: View, @MenuRes menu: Int, listener: (MenuItem) -> Unit) {
    val popup = PopupMenu(this, view, Gravity.END).apply {
        menuInflater.inflate(menu, this.menu)
        applyFontToMenu(this.menu)
        setOnMenuItemClickListener { item ->
            listener.invoke(item)
            true
        }
    }

    val menuHelper = MenuPopupHelper(this, popup.menu as MenuBuilder, view)
    menuHelper.setForceShowIcon(true)
    menuHelper.gravity = Gravity.END
    menuHelper.show()
}

private fun Context.applyFontToMenu(m: Menu) {
    for (i in 0 until m.size()) {
        applyFontToMenuItem(m.getItem(i))
    }
}

private fun Context.applyFontToMenuItem(mi: MenuItem) {
    if (mi.hasSubMenu()) {
        for (i in 0 until mi.subMenu.size()) {
            applyFontToMenuItem(mi.subMenu.getItem(i))
        }
    }
    mi.title = createSpan {
        append(fontColor(fontSize(semiBold(mi.title), MENU_FONT_SIZE), color(R.color.pluto___text_dark_80)))
    }
}

private const val MENU_FONT_SIZE = 15
