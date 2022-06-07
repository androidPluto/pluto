package com.sampleapp.functions.roomsdatabase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sampleapp.R
import com.sampleapp.databinding.FragmentDemoRoomsDatabaseBinding
import com.sampleapp.functions.roomsdatabase.db.SampleDatabase
import com.sampleapp.functions.roomsdatabase.db.entity.Admin
import com.sampleapp.functions.roomsdatabase.db.entity.User
import com.sampleapp.functions.roomsdatabase.db2.Sample2Database
import com.sampleapp.functions.roomsdatabase.db2.entity.UserV2
import java.util.Random

@SuppressWarnings("UnderscoresInNumericLiterals")
class DemoRoomsDatabaseFragment : Fragment(R.layout.fragment_demo_rooms_database) {
    private var _binding: FragmentDemoRoomsDatabaseBinding? = null
    private val binding
        get() = _binding!!

    private val genders = arrayOf("Male", "Female")
    private val phoneNumberRange = 9900000000..9999999999
    private val ageRange = 1..100
    private val range = 100
    private val db: SampleDatabase by lazy { SampleDatabase.getInstance(requireContext()) }
    private val db2: Sample2Database by lazy { Sample2Database.getInstance(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDemoRoomsDatabaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
    }
}
