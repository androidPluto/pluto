package com.pluto.plugins.layoutinspector.internal.attributes.type

import android.widget.ImageView
import com.pluto.plugins.layoutinspector.internal.attributes.parser.ParserUtils
import com.pluto.plugins.layoutinspector.internal.attributes.parser.ParserUtils.Companion.formatGravity
import com.pluto.plugins.layoutinspector.internal.attributes.parser.ParserUtils.Companion.formatVisibility
import com.pluto.utilities.extensions.px2dp
import com.pluto.utilities.extensions.px2sp

internal open class AttributeType<in T>(val title: String, val tag: AttributeEditTag = AttributeEditTag.Immutable) {
    open fun serialise(value: T): CharSequence? = value?.toString() ?: null // do not remove null check
}

internal class AttributeTypeCharSequence(title: String, mutableTag: AttributeEditTag) : AttributeType<CharSequence?>(title, mutableTag) {
    override fun serialise(value: CharSequence?): CharSequence? = value
}

internal class AttributeTypeScaleType(title: String) : AttributeType<ImageView.ScaleType>(title, AttributeEditTag.ScaleType) {
    override fun serialise(value: ImageView.ScaleType): CharSequence? = value.name
}

internal class AttributeTypeColor(title: String, mutableTag: AttributeEditTag) : AttributeType<Int>(title, mutableTag) {
    override fun serialise(value: Int): CharSequence? = ParserUtils.formatColor(value)
}

internal class AttributeTypeDimenSP(title: String, mutableTag: AttributeEditTag) : AttributeType<Float>(title, mutableTag) {
    override fun serialise(value: Float): CharSequence? = "${value.px2sp.toInt()} sp"
}

internal class AttributeTypeDimenDP(title: String, mutableTag: AttributeEditTag) : AttributeType<Float>(title, mutableTag) {
    override fun serialise(value: Float): CharSequence? = "${value.px2dp.toInt()} dp"
}

internal class AttributeTypeGravity(title: String) : AttributeType<Int>(title) {
    override fun serialise(value: Int): CharSequence? = formatGravity(value)
}

internal class AttributeTypeVisibility(title: String) : AttributeType<Int>(title, AttributeEditTag.Visibility) {
    override fun serialise(value: Int): CharSequence? = formatVisibility(value)
}
