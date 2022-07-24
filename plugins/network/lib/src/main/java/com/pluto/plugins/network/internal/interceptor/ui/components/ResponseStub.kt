package com.pluto.plugins.network.internal.interceptor.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.plugin.KeyValuePairData
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkStubDetailsResponseBinding
import com.pluto.plugins.network.internal.interceptor.logic.ExceptionData
import com.pluto.plugins.network.internal.interceptor.logic.ResponseData

internal class ResponseStub : ConstraintLayout {

    private val binding = PlutoNetworkStubDetailsResponseBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
    constructor(context: Context) : super(context, null, 0)

    @SuppressWarnings("UnusedPrivateMember")
    fun set(response: ResponseData?, exception: ExceptionData?) {
        binding.table.set(
            title = context.getString(R.string.pluto_network___tab_response),
            keyValuePairs = arrayListOf<KeyValuePairData>().apply {
                add(
                    KeyValuePairData(
                        key = context.getString(R.string.pluto_network___headers_title),
                        value = ""
                    )
                )
                add(
                    KeyValuePairData(
                        key = context.getString(R.string.pluto_network___body_title),
                        value = ""
                    )
                )
            }
        )
    }
}
