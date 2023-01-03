package com.sampleapp.functions.layoutinspector

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sampleapp.databinding.ActivityDemoLayoutInspectorBinding

class DemoLayoutInspectorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDemoLayoutInspectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.close.setOnClickListener {
            finish()
        }
    }
}