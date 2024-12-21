package com.sampleapp.functions.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sampleapp.R
import com.sampleapp.databinding.FragmentDemoNetworkBinding
import com.sampleapp.functions.network.internal.custom.CustomViewModel
import com.sampleapp.functions.network.internal.ktor.KtorViewModel
import com.sampleapp.functions.network.internal.okhttp.OkhttpViewModel

class DemoNetworkFragment : Fragment(R.layout.fragment_demo_network) {

    private var _binding: FragmentDemoNetworkBinding? = null
    private val binding
        get() = _binding!!

    private val okhttpViewModel: OkhttpViewModel by viewModels()
    private val ktorViewModel: KtorViewModel by viewModels()
    private val customViewModel: CustomViewModel by viewModels()

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
        binding.graphqlQuery.setOnClickListener { okhttpViewModel.graphqlQuery() }
        binding.graphqlQueryError.setOnClickListener { okhttpViewModel.graphqlQueryError() }
        binding.graphqlMutation.setOnClickListener { okhttpViewModel.graphqlMutation() }
        binding.graphqlMutationError.setOnClickListener { okhttpViewModel.graphqlMutationError() }
        binding.postCall.setOnClickListener { okhttpViewModel.post() }
        binding.getCall.setOnClickListener { okhttpViewModel.get() }
        binding.getCallKtor.setOnClickListener { ktorViewModel.get() }
        binding.postCallKtor.setOnClickListener { ktorViewModel.post() }
        binding.xmlCall.setOnClickListener { okhttpViewModel.xml() }
        binding.formEncodedCall.setOnClickListener { okhttpViewModel.form() }
        binding.customTrace.setOnClickListener { customViewModel.customTrace() }
    }
}
