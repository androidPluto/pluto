package com.sampleapp.functions.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.postCall.setOnClickListener { networkViewModel.post() }
        binding.getCall.setOnClickListener { networkViewModel.get() }
        binding.xmlCall.setOnClickListener { networkViewModel.xml() }
        binding.formEncodedCall.setOnClickListener { networkViewModel.form() }
    }
}
