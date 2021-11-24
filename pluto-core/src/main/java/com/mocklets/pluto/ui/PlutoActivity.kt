package com.mocklets.pluto.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mocklets.pluto.databinding.PlutoActivityPlutoBinding

class PlutoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PlutoActivityPlutoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val drawerManager = NavigationDrawerManager(this, binding) {
        }
        drawerManager.init()
    }
}
