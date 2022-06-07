package com.sampleapp.functions.exceptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sampleapp.R
import com.sampleapp.databinding.FragmentDemoExceptionBinding

class DemoExceptionFragment : Fragment(R.layout.fragment_demo_exception) {
    private var _binding: FragmentDemoExceptionBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDemoExceptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nullPointer.setOnClickListener { throw NullPointerException("test exception") }
        binding.illegalState.setOnClickListener { throw IllegalStateException("test exception") }
    }
}
