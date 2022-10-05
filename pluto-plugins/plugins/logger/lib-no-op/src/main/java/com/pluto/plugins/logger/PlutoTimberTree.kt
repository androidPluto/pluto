package com.pluto.plugins.logger

import timber.log.Timber

class PlutoTimberTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {}
}

@SuppressWarnings("UnusedPrivateMember", "EmptyFunctionBlock")
fun Timber.Tree.event(event: String, attr: HashMap<String, Any?>?) {}
