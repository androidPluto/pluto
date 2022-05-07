package com.sampleapp.plugins.roomsDatabase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sampleapp.databinding.ActivityRoomsDbBinding
import com.sampleapp.plugins.roomsDatabase.db.AppDatabase
import com.sampleapp.plugins.roomsDatabase.db.entity.User
import java.util.Random

@SuppressWarnings("UnderscoresInNumericLiterals")
class RoomsDBActivity : AppCompatActivity() {

    private val genders = arrayOf("Male", "Female")
    private val phoneNumberRange = 9900000000..9999999999
    private val ageRange = 1..100
    private val range = 100
    private val db: AppDatabase by lazy { AppDatabase.getInstance(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRoomsDbBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.insert.setOnClickListener {
            val name = "User${Random().nextInt(range)}"
            db.personDAO().insert(
                User(
                    0,
                    name,
                    genders.random(),
                    ageRange.random(),
                    phoneNumberRange.random().toString(),
                    "$name@gmail.com"
                )
            )
        }

        binding.close.setOnClickListener {
            finish()
        }
    }
}
