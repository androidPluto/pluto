package com.pluto.plugins.network.internal.interceptor.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.plugin.KeyValuePairData
import com.pluto.plugin.utilities.extensions.toast
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkStubDetailsRequestBinding
import com.pluto.plugins.network.internal.interceptor.logic.RequestData

internal class RequestStub : ConstraintLayout {

    private val binding = PlutoNetworkStubDetailsRequestBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
    constructor(context: Context) : super(context, null, 0)

    fun set(request: RequestData) {
        binding.table.set(
            title = context.getString(R.string.pluto_network___tab_request),
            keyValuePairs = arrayListOf<KeyValuePairData>().apply {
                add(
                    getHeadersData(context, request.headers) {
                        context.toast("request header clicked")
                    }
                )
                add(
                    getQueryParamsData(context, request.url) {
                        context.toast("request params clicked")
                    }
                )
                add(
                    getBodyData(context, request.body) {
                        context.toast("request body clicked")
                    }
                )
            }
        )
    }
}
