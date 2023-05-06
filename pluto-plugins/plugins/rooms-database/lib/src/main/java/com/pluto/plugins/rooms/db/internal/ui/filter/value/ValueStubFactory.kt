package com.pluto.plugins.rooms.db.internal.ui.filter.value

import android.content.Context
import com.pluto.plugins.rooms.db.internal.ColumnModel
import com.pluto.plugins.rooms.db.internal.FilterRelation
import com.pluto.plugins.rooms.db.internal.ui.filter.value.components.BaseValueStub
import com.pluto.plugins.rooms.db.internal.ui.filter.value.components.BetweenValueStub
import com.pluto.plugins.rooms.db.internal.ui.filter.value.components.ComparisonValueStub
import com.pluto.plugins.rooms.db.internal.ui.filter.value.components.InValueStub
import com.pluto.plugins.rooms.db.internal.ui.filter.value.components.LikeValueStub
import com.pluto.plugins.rooms.db.internal.ui.filter.value.components.StringValueStub

internal class ValueStubFactory private constructor() {
    companion object {
        fun getStub(context: Context, relation: FilterRelation, column: ColumnModel): BaseValueStub {
            return when (relation) {
                FilterRelation.Between -> BetweenValueStub(context)
                FilterRelation.In -> InValueStub(context, column)
                FilterRelation.Like -> LikeValueStub(context, column)
                FilterRelation.LessThan,
                FilterRelation.LessThanOrEquals,
                FilterRelation.GreaterThan,
                FilterRelation.GreaterThanOrEquals ->
                    ComparisonValueStub(context, column)

                else -> StringValueStub(context, column)
            }
        }
    }
}
