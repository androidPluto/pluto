package com.sampleapp.functions.layoutinspector

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sampleapp.R
import com.sampleapp.databinding.ActivityDemoLayoutInspectorBinding
import com.sampleapp.functions.layoutinspector.ui.main.DemoLayoutInspectorFragment

class DemoLayoutInspectorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDemoLayoutInspectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.close.setOnClickListener {
            finish()
        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, DemoLayoutInspectorFragment())
                .commitNow()
        }
    }
}
