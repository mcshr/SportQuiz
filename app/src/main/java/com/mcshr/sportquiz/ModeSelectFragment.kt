package com.mcshr.sportquiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mcshr.sportquiz.databinding.FragmentModeSelectBinding


class ModeSelectFragment : Fragment() {

    private var _binding: FragmentModeSelectBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentModeSelect binding is null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentModeSelectBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}