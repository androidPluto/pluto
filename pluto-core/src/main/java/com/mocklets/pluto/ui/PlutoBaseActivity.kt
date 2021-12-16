package com.mocklets.pluto.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.R
import com.mocklets.pluto.applifecycle.AppState
import com.mocklets.pluto.databinding.PlutoActivityPlutoBaseBinding

class PlutoBaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PlutoActivityPlutoBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Pluto.currentPlugin.removeObservers(this)
        Pluto.currentPlugin.observe(
            this,
            {
                val fragment = it.getView()
                supportFragmentManager.beginTransaction().apply {
                    this.runOnCommit { it.onPluginViewCreated(it.savedInstance) }
                    this.replace(R.id.container, fragment).commit()
                }
            }
        )

        Pluto.appState.observe(
            this,
            {
                if (it is AppState.Background) {
                    finish()
                }
            }
        )
    }
}
