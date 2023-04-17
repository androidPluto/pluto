package com.sampleapp.functions.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pluto.plugins.network.CustomBody
import com.pluto.plugins.network.CustomRequest
import com.pluto.plugins.network.CustomResponse
import com.pluto.plugins.network.PlutoNetwork
import com.sampleapp.R
import com.sampleapp.databinding.FragmentDemoNetworkBinding
import com.sampleapp.functions.network.internal.NetworkViewModel

class DemoNetworkFragment : Fragment(R.layout.fragment_demo_network) {

    private var _binding: FragmentDemoNetworkBinding? = null
    private val binding
        get() = _binding!!

    private val networkViewModel by lazy { ViewModelProvider(this)[NetworkViewModel::class.java] }

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
        binding.xmlCall.setOnClickListener { networkViewModel.xml() }
        binding.formEncodedCall.setOnClickListener { networkViewModel.form() }
        binding.customTrace.setOnClickListener {
            PlutoNetwork.insertCustomTrace(
                request = CustomRequest(
                    url = "https://google.com",
                    method = "GET",
                    body = CustomBody(
                        body = "hello request",
                        mediaType = "text",
                        mediaSubtype = "text"
                    ),
                    headers = emptyMap(),
                    timestamp = System.currentTimeMillis(),
                    isGzipped = false
                ),
                response = CustomResponse(
                    statusCode = 200,
                    body = CustomBody(
                        body = "hello response",
                        mediaType = "text",
                        mediaSubtype = "text"
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
