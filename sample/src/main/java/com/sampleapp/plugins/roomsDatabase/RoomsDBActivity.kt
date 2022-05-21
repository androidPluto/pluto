package com.sampleapp.plugins.roomsDatabase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sampleapp.databinding.ActivityRoomsDbBinding
import com.sampleapp.plugins.roomsDatabase.db.SampleDatabase
import com.sampleapp.plugins.roomsDatabase.db.entity.Admin
import com.sampleapp.plugins.roomsDatabase.db.entity.User
import com.sampleapp.plugins.roomsDatabase.db2.Sample2Database
import com.sampleapp.plugins.roomsDatabase.db2.entity.UserV2
import java.util.Random

@SuppressWarnings("UnderscoresInNumericLiterals")
class RoomsDBActivity : AppCompatActivity() {

    private val genders = arrayOf("Male", "Female")
    private val phoneNumberRange = 9900000000..9999999999
    private val ageRange = 1..100
    private val range = 100
    private val db: SampleDatabase by lazy { SampleDatabase.getInstance(applicationContext) }
    private val db2: Sample2Database by lazy { Sample2Database.getInstance(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRoomsDbBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.insertUser.setOnClickListener {
            val name = "User${Random().nextInt(range)}"
            db.userDao().insert(
                User(
                    0,
                    name,
                    genders.random(),
                    ageRange.random(),
                    phoneNumberRange.random().toString(),
                    "$name@gmail.com",
                    true
                )
            )
        }

        binding.insertAdmin.setOnClickListener {
            val name = "Admin${Random().nextInt(range)}"
            db.adminDao().insert(
                Admin(
                    0,
                    name,
                    isAdmin = true,
                    canWrite = false
                )
            )
        }

        binding.insertUser2.setOnClickListener {
            val name = "User${Random().nextInt(range)}"
            db2.userDao().insert(
                UserV2(
                    0,
                    "2-$name",
                    genders.random(),
                    ageRange.random(),
                    phoneNumberRange.random().toString(),
                    "$name@yahoo.com",
                    false
                )
            )
        }

        binding.close.setOnClickListener {
            finish()
        }
    }
}
