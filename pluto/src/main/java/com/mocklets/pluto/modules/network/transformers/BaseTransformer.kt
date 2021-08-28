package com.mocklets.pluto.modules.network.transformers

import com.mocklets.pluto.modules.network.BODY_INDENTATION

internal interface BaseTransformer {
    fun beautify(plain: CharSequence, indent: Int = BODY_INDENTATION): CharSequence?
    fun flatten(plain: CharSequence): String
}
