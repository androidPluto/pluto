package com.pluto.plugin.libinterface

import androidx.fragment.app.FragmentActivity

data class LibraryInfoInterface(val pluginActivityClass: Class<out FragmentActivity>, val selectorActivityClass: Class<out FragmentActivity>)
