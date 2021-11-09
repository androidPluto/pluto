package com.mocklets.pluto.network.internal.transformers

import com.mocklets.pluto.network.internal.BODY_INDENTATION

internal interface BaseTransformer {
    fun beautify(plain: CharSequence, indent: Int = BODY_INDENTATION): CharSequence?
    fun flatten(plain: CharSequence): String
}
