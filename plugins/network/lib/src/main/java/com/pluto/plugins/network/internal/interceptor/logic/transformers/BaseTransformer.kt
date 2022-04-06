package com.pluto.plugins.network.internal.interceptor.logic.transformers

import com.pluto.plugins.network.internal.interceptor.logic.BODY_INDENTATION

internal interface BaseTransformer {
    fun beautify(plain: CharSequence, indent: Int = BODY_INDENTATION): CharSequence?
    fun flatten(plain: CharSequence): String
}
