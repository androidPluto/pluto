package com.pluto.plugins.network.internal.share

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pluto.plugins.network.R
import com.pluto.plugins.network.internal.interceptor.logic.ApiCallData
import com.pluto.utilities.list.ListItem

internal class ShareOptionsViewModel : ViewModel() {

    val shareOptions: LiveData<List<ShareOptionType>>
        get() = _shareOptions
    private val _shareOptions = MutableLiveData<List<ShareOptionType>>()

    fun generate(apiData: ApiCallData) {
        val list = mutableListOf<ShareOptionType>().apply {
            add(ShareOptionType.All)
            add(ShareOptionType.CURL)
            add(ShareOptionType.Header)
            add(ShareOptionType.Request)
            add(ShareOptionType.Response(enable = apiData.response != null || apiData.exception != null))
        }
        _shareOptions.postValue(list)
    }
}

@Keep
internal sealed class ShareOptionType(@DrawableRes val icon: Int, val title: String, val subtitle: String? = null, val enabled: Boolean = true) : ListItem() {
    object All : ShareOptionType(R.drawable.pluto_network___ic_share_all, "Complete API data")
    object CURL : ShareOptionType(R.drawable.pluto_network___ic_share_curl, "Request cURL code")
    object Request : ShareOptionType(R.drawable.pluto_network___ic_proxy_base_request_dark, "Only Request data")
    class Response(enable: Boolean) :
        ShareOptionType(R.drawable.pluto_network___ic_proxy_base_response_dark, "Only Response data", "~ Waiting for response", enable)
    object Header : ShareOptionType(R.drawable.pluto_network___ic_share_all, "Share in parts")
}
