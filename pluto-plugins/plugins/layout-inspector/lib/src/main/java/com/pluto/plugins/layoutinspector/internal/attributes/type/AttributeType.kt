package com.pluto.plugins.layoutinspector.internal.attributes.type

import android.widget.ImageView
import com.pluto.plugins.layoutinspector.internal.attributes.parser.LayoutParamDimens
import com.pluto.plugins.layoutinspector.internal.attributes.parser.ParserUtils
import com.pluto.plugins.layoutinspector.internal.attributes.parser.ParserUtils.Companion.formatGravity
import com.pluto.plugins.layoutinspector.internal.attributes.parser.ParserUtils.Companion.formatLayoutParam
import com.pluto.plugins.layoutinspector.internal.attributes.parser.ParserUtils.Companion.formatVisibility
import com.pluto.utilities.extensions.px2dp
import com.pluto.utilities.extensions.px2sp

internal open class AttributeType<in T>(val title: String, val tag: MutableAttributeTag = MutableAttributeTag.Immutable) {
    open fun serialise(value: T): CharSequence? = value?.toString() ?: null //do not remove null check
}

sealed class MutableAttributeTag {
    object Immutable : MutableAttributeTag()
    object LayoutWidth : MutableAttributeTag()
    object LayoutHeight : MutableAttributeTag()
    object Visibility : MutableAttributeTag()
    object PaddingLeft : MutableAttributeTag()
    object PaddingRight : MutableAttributeTag()
    object PaddingTop : MutableAttributeTag()
    object PaddingBottom : MutableAttributeTag()
    object MarginLeft : MutableAttributeTag()
    object MarginRight : MutableAttributeTag()
    object MarginTop : MutableAttributeTag()
    object MarginBottom : MutableAttributeTag()
    object Alpha : MutableAttributeTag()
    object LineHeight : MutableAttributeTag()
    object TextSize : MutableAttributeTag()
    object TextColor : MutableAttributeTag()
    object Text : MutableAttributeTag()
    object ScaleType : MutableAttributeTag()
}

internal class AttributeTypeScaleType(title: String) : AttributeType<ImageView.ScaleType>(title, MutableAttributeTag.ScaleType) {
    override fun serialise(value: ImageView.ScaleType): CharSequence? = value.name
}

internal class AttributeTypeColor(title: String, mutableTag: MutableAttributeTag) : AttributeType<Int>(title, mutableTag) {
    override fun serialise(value: Int): CharSequence? = "#" + ParserUtils.formatColor(value)
}

internal class AttributeTypeDimenSP(title: String, mutableTag: MutableAttributeTag) : AttributeType<Float>(title, mutableTag) {
    override fun serialise(value: Float): CharSequence? = "${value.px2sp.toInt()} sp"
}

internal class AttributeTypeDimenDP(title: String, mutableTag: MutableAttributeTag) : AttributeType<Float>(title, mutableTag) {
    override fun serialise(value: Float): CharSequence? = "${value.px2dp.toInt()} dp"
}

internal class AttributeTypeGravity(title: String) : AttributeType<Int>(title) {
    override fun serialise(value: Int): CharSequence? = formatGravity(value)
}

internal class AttributeTypeVisibility(title: String) : AttributeType<Int>(title, MutableAttributeTag.Visibility) {
    override fun serialise(value: Int): CharSequence? = formatVisibility(value)
}

internal class AttributeTypeLayoutParams(title: String, mutableTag: MutableAttributeTag) : AttributeType<LayoutParamDimens>(title, mutableTag) {
    override fun serialise(value: LayoutParamDimens): CharSequence? = formatLayoutParam(value)
}