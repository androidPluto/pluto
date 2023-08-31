package com.sampleapp.functions.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.pluto.plugins.network.NetworkRecorder
import com.pluto.plugins.network.internal.interceptor.logic.ProcessedBody
import com.pluto.plugins.network.internal.interceptor.logic.RequestData
import com.pluto.plugins.network.internal.interceptor.logic.ResponseData
import com.sampleapp.R
import com.sampleapp.databinding.FragmentDemoNetworkBinding
import com.sampleapp.functions.network.internal.NetworkViewModel

class DemoNetworkFragment : Fragment(R.layout.fragment_demo_network) {

    private var _binding: FragmentDemoNetworkBinding? = null
    private val binding
        get() = _binding!!

    private val networkViewModel: NetworkViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDemoNetworkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressWarnings("MagicNumber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.postCall.setOnClickListener { networkViewModel.post() }
        binding.getCall.setOnClickListener { networkViewModel.get() }
        binding.getCallKtor.setOnClickListener { networkViewModel.getKtor() }
        binding.postCallKtor.setOnClickListener { networkViewModel.postKtor() }
        binding.xmlCall.setOnClickListener { networkViewModel.xml() }
        binding.formEncodedCall.setOnClickListener { networkViewModel.form() }
        binding.customTrace.setOnClickListener {
            val networkRecorder = NetworkRecorder(
                RequestData(
                    url = "https://google.com",
                    method = "GET",
                    body = ProcessedBody(
                        isValid = true,
                        body = "body",
                        mediaType = "mediaType.name.lowercase()", // todo fix this
                        mediaSubtype = "mediaSubtype.name.lowercase()"
                    ),
                    headers = emptyMap(),
                    timestamp = System.currentTimeMillis(),
                    isGzipped = false
                )
            )
            networkRecorder.onResponse(
                ResponseData(
                    statusCode = 200,
                    body = ProcessedBody(
                        isValid = true,
                        body = "body",
                        mediaType = "mediaType.name.lowercase()",
                        mediaSubtype = "mediaSubtype.name.lowercase()"
                    ),
                    headers = hashMapOf(
                        "custom_header" to "custom header value"
                    ),
                    sendTimeMillis = System.currentTimeMillis(),
                    receiveTimeMillis = System.currentTimeMillis() + 1000,
                    isGzipped = false
                )
            )
        }
    }
}
