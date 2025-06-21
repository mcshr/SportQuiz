package com.mcshr.sportquiz.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mcshr.sportquiz.R
import com.mcshr.sportquiz.databinding.FragmentQuizBinding
import com.mcshr.sportquiz.domain.entity.QuizMode
import com.mcshr.sportquiz.domain.entity.QuizQuestion
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class QuizFragment : Fragment() {
    private var _binding: FragmentQuizBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("Fragment Quiz binding is null")

    private val viewModel: QuizViewModel by viewModels()
    private val optionButtons: List<Button> by lazy {
        listOf(
            binding.btnOption1,
            binding.btnOption2,
            binding.btnOption3,
            binding.btnOption4
        )
    }

    private var selectedOption: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = when (viewModel.mode) {
            QuizMode.EMOJI -> getString(R.string.emoji_mode)
            QuizMode.TEST -> getString(R.string.test_mode)
            QuizMode.RIDDLE -> getString(R.string.riddle_mode)
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.currentQuestion.observe(viewLifecycleOwner) { question ->
            if (question != null) {
                showQuestion(question)
            } else {
                navigateToResult()
            }
        }
        viewModel.score.observe(viewLifecycleOwner) {
            binding.tvScore.text = getString(R.string.score_format, it)
        }


        optionButtons.forEach { button ->
            button.setOnClickListener {
                selectedOption = button.text.toString()
                optionButtons.forEach { it.isSelected = it == button }
            }
        }

        binding.btnSkip.setOnClickListener {
            viewModel.nextQuestion()
        }

        binding.btnHint.setOnClickListener {
            val hint = viewModel.currentQuestion.value?.hint
            if (!hint.isNullOrBlank()) {
                viewModel.useHint()
                showToast(getString(R.string.hint_format, hint))
            }
        }

        binding.btnCheck.setOnClickListener {
            val answer = when (viewModel.mode) {
                QuizMode.TEST -> selectedOption
                QuizMode.RIDDLE,
                QuizMode.EMOJI -> binding.etAnswer.text.toString()
            }

            if (answer.isNullOrBlank()) {
                showToast(getString(R.string.enter_answer))
                return@setOnClickListener
            }

            val (isCorrect, points) = viewModel.submitAnswer(answer)

            val result = if (isCorrect) {
                getString(R.string.correct_answer, points.toString())
            } else {
                getString(R.string.incorrect_answer)
            }

            showToast(result)

            goToNextQuestionWithDelay()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showQuestion(question: QuizQuestion) {
        binding.tvQuestion.text = question.text

        selectedOption = null

        binding.btnHint.visibility = if (question.hint.isNullOrBlank()) View.GONE else View.VISIBLE


        when (viewModel.mode) {
            QuizMode.TEST -> {
                binding.etAnswer.visibility = View.GONE
                binding.optionsContainer.visibility = View.VISIBLE
                question.options?.forEachIndexed { index, text ->
                    optionButtons[index].text = text
                }
            }

            QuizMode.RIDDLE,
            QuizMode.EMOJI -> {
                binding.etAnswer.visibility = View.VISIBLE
                binding.optionsContainer.visibility = View.GONE
                binding.etAnswer.text.clear()
            }
        }

    }


    private fun goToNextQuestionWithDelay() {
        lifecycleScope.launch {
            delay(1000)
            viewModel.nextQuestion()
        }
    }

    private fun navigateToResult() {
        viewModel.saveFinalScore()
        findNavController().popBackStack()
        //TODO result screen
    }

    private fun showToast(text: String) {
        Toast.makeText(
            requireContext(),
            text,
            Toast.LENGTH_SHORT
        ).show()
    }


}