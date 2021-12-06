package com.mocklets.pluto.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mocklets.pluto.databinding.PlutoActivityPlutoBaseBinding

class PlutoBaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PlutoActivityPlutoBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
