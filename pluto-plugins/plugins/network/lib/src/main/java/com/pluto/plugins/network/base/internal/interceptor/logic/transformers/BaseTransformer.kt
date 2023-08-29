package com.pluto.plugins.network.base.internal.interceptor.logic.transformers

internal interface BaseTransformer {
    fun beautify(plain: CharSequence): CharSequence
    fun flatten(plain: CharSequence): String
}
