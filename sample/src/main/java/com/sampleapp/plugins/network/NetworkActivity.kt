package com.sampleapp.plugins.network

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sampleapp.databinding.ActivityNetworkBinding
import com.sampleapp.plugins.network.internal.NetworkViewModel

class NetworkActivity : AppCompatActivity() {

    private val networkViewModel by lazy { ViewModelProvider(this).get(NetworkViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNetworkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.close.setOnClickListener {
            finish()
        }

        binding.postCall.setOnClickListener {
            networkViewModel.post()
        }

        binding.getCall.setOnClickListener {
            networkViewModel.get()
        }

        binding.xmlCall.setOnClickListener {
            networkViewModel.xml()
        }

        binding.formEncodedCall.setOnClickListener {
            networkViewModel.form()
        }
    }
}
