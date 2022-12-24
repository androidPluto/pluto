package com.pluto.plugins.layoutinspector.internal.attributes.parser

import com.pluto.utilities.list.ListItem

class Attribute: ListItem {
    var category: String? = null
    var attrName: String
    var attrValue: String
    @Edit
    var attrType = Edit.NORMAL

    constructor(attrName: String, attrValue: String) {
        this.attrName = attrName
        this.attrValue = attrValue
    }

    constructor(attrName: String, attrValue: String, @Edit attrType: Int) {
        this.attrName = attrName
        this.attrValue = attrValue
        this.attrType = attrType
    }

    annotation class Edit {
        companion object {
            var NORMAL = 0x00
            var LAYOUT_WIDTH = 0x01
            var LAYOUT_HEIGHT = 0x02
            var VISIBILITY = 0x03
            var PADDING_LEFT = 0x04
            var PADDING_RIGHT = 0x05
            var PADDING_TOP = 0x06
            var PADDING_BOTTOM = 0x07
            var ALPHA = 0x08
            var TEXT_SIZE = 0x10
            var TEXT_COLOR = 0x11
            var TEXT = 0x12
            var SCALE_TYPE = 0x13
            var MARGIN_LEFT = 0x14
            var MARGIN_RIGHT = 0x15
            var MARGIN_TOP = 0x16
            var MARGIN_BOTTOM = 0x17
        }
    }
}