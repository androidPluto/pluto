package com.mocklets.pluto.ui

import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import com.mocklets.pluto.R
import com.mocklets.pluto.databinding.PlutoActivityPlutoBinding

// todo delete this file
class PlutoActivity : AppCompatActivity() {
    private lateinit var drawerManager: NavigationDrawerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PlutoActivityPlutoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        drawerManager = NavigationDrawerManager(this, binding) {
            val fragment = it.getView()
            supportFragmentManager.beginTransaction().apply {
                this.runOnCommit { it.onPluginViewCreated(it.savedInstance) }
                this.replace(R.id.container, fragment).commit()
            }
            binding.drawerLayout.closeDrawer(Gravity.START, true)
        }
    }

    override fun onResume() {
        super.onResume()
        drawerManager.init()
    }
}
