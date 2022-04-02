package com.sampleapp.plugins.logger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pluto.plugins.logger.PlutoLog
import com.pluto.plugins.logger.event
import com.sampleapp.databinding.ActivityLoggerBinding
import java.lang.NullPointerException
import timber.log.Timber

class LoggerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoggerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.close.setOnClickListener {
            finish()
        }

        /**
         * Pluto Logs demo
         */
        binding.logDebug.setOnClickListener {
            PlutoLog.d("demo", "debug cta clicked")
        }
        binding.logError.setOnClickListener {
            PlutoLog.e("demo", "error cta clicked", NullPointerException("demo"))
        }
        binding.logVerbose.setOnClickListener {
            PlutoLog.v("demo", "verbose cta clicked", NullPointerException("demo"))
        }
        binding.logInfo.setOnClickListener {
            PlutoLog.i("demo", "info cta clicked")
        }
        binding.logEvent.setOnClickListener {
            PlutoLog.event(
                "demo", "event cta clicked",
                hashMapOf(
                    "attr_name" to "demo attr",
                    "timestamp" to System.currentTimeMillis()
                )
            )
        }

        /**
         * Timber Logs demo
         */
        binding.timberDebug.setOnClickListener {
            Timber.d("timber_debug cta clicked")
        }
        binding.timberError.setOnClickListener {
            Timber.tag("error").e(NullPointerException("demo"), "timber_error cta clicked")
        }
        binding.timberVerbose.setOnClickListener {
            Timber.tag("error").v(NullPointerException("demo"), "timber_verbose cta clicked")
        }
        binding.timberInfo.setOnClickListener {
            Timber.i("timber_info cta clicked")
        }
        binding.timberEvent.setOnClickListener {
            Timber.tag("event").event(
                "timber_event cta clicked",
                hashMapOf(
                    "attr_name" to "demo attr",
                    "timestamp" to System.currentTimeMillis()
                )
            )
        }
    }
}
