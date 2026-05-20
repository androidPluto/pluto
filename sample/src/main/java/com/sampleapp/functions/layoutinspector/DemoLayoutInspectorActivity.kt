package com.sampleapp.functions.layoutinspector

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.sampleapp.R
import com.sampleapp.databinding.ActivityDemoLayoutInspectorBinding

class DemoLayoutInspectorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityDemoLayoutInspectorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.appBar.updatePadding(top = insets.top)
            v.updatePadding(left = insets.left, right = insets.right, bottom = insets.bottom)
            WindowInsetsCompat.CONSUMED
        }

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
