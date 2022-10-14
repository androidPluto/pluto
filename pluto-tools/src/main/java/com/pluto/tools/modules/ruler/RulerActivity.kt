package com.pluto.tools.modules.ruler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pluto.tools.R
import com.pluto.tools.databinding.PlutoToolRulerActivityBinding
import com.pluto.tools.modules.ruler.internal.RulerFragment

class RulerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PlutoToolRulerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().apply {
            this.add(R.id.container, RulerFragment()).commit()
        }
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}
