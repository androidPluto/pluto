package com.mocklets.pluto.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mocklets.pluto.databinding.PlutoActivityPlutoBinding

class PlutoActivity : AppCompatActivity() {
    private lateinit var drawerManager: NavigationDrawerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PlutoActivityPlutoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        drawerManager = NavigationDrawerManager(this, binding) {
        }
    }

    override fun onResume() {
        super.onResume()
        drawerManager.init()
    }
}
