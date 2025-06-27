package com.mcshr.sportquiz.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mcshr.sportquiz.databinding.FragmentRulesBinding


class RulesFragment : Fragment() {

    private var _binding: FragmentRulesBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentHomeBinding is null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRulesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.startButton.setOnClickListener {
            val action = RulesFragmentDirections.actionHomeFragmentToModeSelectFragment()
            findNavController().navigate(action)
        }

        super.onViewCreated(view, savedInstanceState)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}