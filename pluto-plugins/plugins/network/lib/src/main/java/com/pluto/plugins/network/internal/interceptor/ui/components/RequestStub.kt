package com.pluto.plugins.network.internal.interceptor.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkStubDetailsRequestBinding
import com.pluto.plugins.network.internal.interceptor.logic.RequestData
import com.pluto.plugins.network.internal.interceptor.ui.DetailsFragment.Companion.ACTION_OPEN_REQ_BODY
import com.pluto.plugins.network.internal.interceptor.ui.DetailsFragment.Companion.ACTION_OPEN_REQ_HEADERS
import com.pluto.plugins.network.internal.interceptor.ui.DetailsFragment.Companion.ACTION_OPEN_REQ_PARAMS
import com.pluto.utilities.views.keyvalue.KeyValuePairData

internal class RequestStub : ConstraintLayout {

    private val binding = PlutoNetworkStubDetailsRequestBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
    constructor(context: Context) : super(context, null, 0)

    fun set(request: RequestData, onAction: (String) -> Unit) {
        binding.table.set(
            title = context.getString(R.string.pluto_network___tab_request),
            keyValuePairs = arrayListOf<KeyValuePairData>().apply {
                add(
                    getHeadersData(context, request.headers) {
                        onAction.invoke(ACTION_OPEN_REQ_HEADERS)
                    }
                )
                add(
                    getQueryParamsData(context, request.url) {
                        onAction.invoke(ACTION_OPEN_REQ_PARAMS)
                    }
                )
                add(
                    getBodyData(context, request.body) {
                        onAction.invoke(ACTION_OPEN_REQ_BODY)
                    }
                )
            }
        )
    }
}
