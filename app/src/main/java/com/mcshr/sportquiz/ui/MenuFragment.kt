package com.mcshr.sportquiz.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mcshr.sportquiz.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("Fragment Menu Binding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnSoloMode.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToModeSelectFragment()
            findNavController().navigate(action)
        }
        binding.btnMultiMode.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToMultiplayerSetupFragment()
            findNavController().navigate(action)
        }

        binding.btnRules.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}