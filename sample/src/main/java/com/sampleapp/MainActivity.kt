package com.sampleapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.pluto.Pluto
import com.sampleapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val networkViewModel by lazy { ViewModelProvider(this).get(NetworkViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        PlutoLog.v(TAG_ACTION, "MainActivity onCreate")
        Test().javaTest()

        handleAPIManageCTAs()
        handleExceptionCTAs()
        handleSharedPrefCTAs()
        handleAppPropertiesCTAs()
    }

    private fun handleAppPropertiesCTAs() {
        binding.appPropertiesCta.setOnClickListener {
//            saveAppProperties()
//            startActivity(Intent(this, SecondActivity::class.java))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
            }
        }
    }

    private fun handleSharedPrefCTAs() {
        binding.sharedPrefCta.setOnClickListener {
            resetSharedPreferences(this)
            Pluto.open("sharedPref")
        }
    }

    private fun handleExceptionCTAs() {
        binding.exceptionCta.setOnClickListener {
            Pluto.showNotch(false)
//            throw NullPointerException("Custom Exception")
        }

        binding.deadlockCta.setOnClickListener {
            Pluto.showNotch(true)
//            TestingThreadANR.testDeadLock()
        }

        binding.sleepCta.setOnClickListener {
            TestingThreadANR.testSleep()
        }

        binding.infiniteLoopCta.setOnClickListener {
            TestingThreadANR.testInfiniteLoop()
        }
    }

    private fun handleAPIManageCTAs() {
        binding.postCall.setOnClickListener {
//            networkViewModel.post()
        }

        binding.getCall.setOnClickListener {
//            networkViewModel.get()
        }

        binding.xmlCall.setOnClickListener {
            networkViewModel.xml()
        }

        binding.formEncodedCall.setOnClickListener {
            networkViewModel.form()
        }
    }

//    override fun onStart() {
//        super.onStart()
// //        PlutoLog.v(TAG_ACTION, "MainActivity onStart")
//    }
//
//    override fun onResume() {
//        super.onResume()
// //        PlutoLog.v(TAG_ACTION, "MainActivity onResume")
//    }
//
//    private fun getAttrMap(): HashMap<String, Any?> = hashMapOf(
//        "screen" to "MainActivity",
//        "timestamp" to System.currentTimeMillis()
//    )

    companion object {
        const val TAG_ACTION = "action"
        const val TAG_CLICK = "click_event"
    }
}
