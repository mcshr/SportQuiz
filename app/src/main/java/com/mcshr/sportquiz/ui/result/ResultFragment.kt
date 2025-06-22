package com.mcshr.sportquiz.ui.result

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mcshr.sportquiz.R
import com.mcshr.sportquiz.databinding.FragmentResultBinding
import com.mcshr.sportquiz.domain.entity.QuizMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException(" FragmentResultBinding is null")

    private val viewModel: ResultViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentResultBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvScore.text = getString(R.string.your_score_format, viewModel.score)
        binding.tvMode.text = when(viewModel.mode){
            QuizMode.EMOJI -> getString(R.string.result_mode_emoji)
            QuizMode.RIDDLE -> getString(R.string.result_mode_riddle)
            QuizMode.TEST -> getString(R.string.result_mode_test)
        }
        binding.tvHighScore.text = getString(R.string.high_score_format, viewModel.highScore)
        binding.btnHome.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnRetry.setOnClickListener {
            val action = ResultFragmentDirections.actionResultFragmentToQuizFragment(viewModel.mode.name)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}