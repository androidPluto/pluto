package com.sampleapp.functions.logger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pluto.plugins.logger.PlutoLog
import com.pluto.plugins.logger.event
import com.sampleapp.R
import com.sampleapp.databinding.FragmentDemoLoggerBinding
import java.lang.NullPointerException
import timber.log.Timber

class DemoLoggerFragment : Fragment(R.layout.fragment_demo_logger) {
    private var _binding: FragmentDemoLoggerBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDemoLoggerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressWarnings("StringLiteralDuplication")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Pluto Logs demo
         */
        binding.logDebug.setOnClickListener { PlutoLog.d("debug", "debug cta clicked") }
        binding.logError.setOnClickListener { PlutoLog.e("error", "error cta clicked", NullPointerException("demo")) }
        binding.logVerbose.setOnClickListener { PlutoLog.v("verbose", "verbose cta clicked", NullPointerException("demo")) }
        binding.logInfo.setOnClickListener { PlutoLog.i("info", "info cta clicked") }
        binding.logEvent.setOnClickListener {
            PlutoLog.event(
                "event", "event cta clicked",
                hashMapOf(
                    "attr_name" to "demo attr",
                    "timestamp" to System.currentTimeMillis()
                )
            )
            PlutoLog.event("event", "event cta clicked without attrs")
        }

        /**
         * Timber Logs demo
         */
        binding.timberDebug.setOnClickListener { Timber.d("timber_debug cta clicked") }
        binding.timberError.setOnClickListener { Timber.tag("error").e(NullPointerException("demo"), "timber_error cta clicked") }
        binding.timberVerbose.setOnClickListener { Timber.tag("error").v(NullPointerException("demo"), "timber_verbose cta clicked") }
        binding.timberInfo.setOnClickListener { Timber.i("timber_info cta clicked") }
        binding.timberEvent.setOnClickListener {
            Timber.tag("event").event(
                "timber_event cta clicked",
                hashMapOf(
                    "attr_name" to "demo attr",
                    "timestamp" to System.currentTimeMillis()
                )
            )
            Timber.tag("event").event("timber_event cta clicked")
        }
    }
}
