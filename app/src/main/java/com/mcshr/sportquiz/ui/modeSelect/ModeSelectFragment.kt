package com.mcshr.sportquiz.ui.modeSelect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mcshr.sportquiz.R
import com.mcshr.sportquiz.databinding.FragmentModeSelectBinding
import com.mcshr.sportquiz.domain.entity.QuizMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModeSelectFragment : Fragment() {

    private var _binding: FragmentModeSelectBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentModeSelect binding is null")

    private val viewModel: ModeSelectViewModel by viewModels()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.totalHighScore.observe(viewLifecycleOwner) { score ->
            binding.tvTotalScore.text = getString(R.string.total_score_format, score)
        }

        viewModel.loadHighScore()

        binding.btnEmojiMode.setOnClickListener {
            navigateTo(QuizMode.EMOJI)
        }

        binding.btnRiddleMode.setOnClickListener {
            navigateTo(QuizMode.RIDDLE)
        }

        binding.btnTestMode.setOnClickListener {
            navigateTo(QuizMode.TEST)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun navigateTo(mode: QuizMode){
        val action = ModeSelectFragmentDirections.actionModeSelectFragmentToQuizFragment(
            mode.name
        )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}