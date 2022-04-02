package com.sampleapp.plugins.exceptions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sampleapp.databinding.ActivityExceptionBinding
import java.lang.NullPointerException

class ExceptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityExceptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.close.setOnClickListener {
            finish()
        }

        binding.button.setOnClickListener {
            throw NullPointerException("test exception")
        }
    }
}
