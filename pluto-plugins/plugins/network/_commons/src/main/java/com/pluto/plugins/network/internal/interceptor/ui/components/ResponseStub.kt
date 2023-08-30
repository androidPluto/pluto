package com.pluto.plugins.network.internal.interceptor.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.VISIBLE
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.plugins.network.commons.R
import com.pluto.plugins.network.commons.databinding.PlutoNetworkStubDetailsResponseBinding
import com.pluto.plugins.network.internal.interceptor.logic.ExceptionData
import com.pluto.plugins.network.internal.interceptor.logic.ResponseData
import com.pluto.plugins.network.internal.interceptor.ui.DetailsFragment.Companion.ACTION_OPEN_RES_BODY
import com.pluto.plugins.network.internal.interceptor.ui.DetailsFragment.Companion.ACTION_OPEN_RES_HEADERS
import com.pluto.utilities.extensions.color
import com.pluto.utilities.spannable.setSpan
import com.pluto.utilities.views.keyvalue.KeyValuePairData

internal class ResponseStub : ConstraintLayout {

    private val binding = PlutoNetworkStubDetailsResponseBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
    constructor(context: Context) : super(context, null, 0)

    fun set(response: ResponseData?, exception: ExceptionData?, onAction: (String) -> Unit) {
        binding.loaderGroup.visibility = GONE
        binding.response.visibility = GONE
        binding.exceptionGroup.visibility = GONE

        exception?.setup(context, binding) ?: run {
            response?.setup(context, binding, onAction) ?: run {
                binding.loaderGroup.visibility = VISIBLE
            }
        }
    }
}

private fun ResponseData.setup(context: Context, binding: PlutoNetworkStubDetailsResponseBinding, onAction: (String) -> Unit) {
    binding.response.visibility = VISIBLE
    binding.response.set(
        title = context.getString(R.string.pluto_network___tab_response),
        keyValuePairs = arrayListOf<KeyValuePairData>().apply {
            add(
                getHeadersData(context, headers) {
                    onAction.invoke(ACTION_OPEN_RES_HEADERS)
                }
            )
            add(
                getBodyData(context, body) {
                    onAction.invoke(ACTION_OPEN_RES_BODY)
                }
            )
        }
    )
}

private fun ExceptionData.setup(context: Context, binding: PlutoNetworkStubDetailsResponseBinding) {
    binding.exceptionGroup.visibility = VISIBLE
    binding.exceptionDetails.setSpan {
        append(semiBold(fontColor("${name}\n", context.color(R.color.pluto___text_dark_80))))
        message?.let { append(it) }
    }
}
