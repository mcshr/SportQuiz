package com.mcshr.sportquiz.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.mcshr.sportquiz.BuildConfig
import com.mcshr.sportquiz.R
import com.mcshr.sportquiz.databinding.FragmentQuizBinding
import com.mcshr.sportquiz.domain.entity.QuizMode
import com.mcshr.sportquiz.domain.entity.QuizQuestion
import com.mcshr.sportquiz.ui.quiz.QuizViewModel.LoadingState
import com.mcshr.sportquiz.ui.utils.setDebounceOnClickListener
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
    private var isInteractionLocked = false

    private var rewardedAd: RewardedAd? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        loadRewardedAd()
        setupLoading()
        observeProgress()
        setupButtons()

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


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupButtons() {
        binding.btnSkip.setDebounceOnClickListener {
            if (isInteractionLocked) return@setDebounceOnClickListener
            viewModel.nextQuestion()
        }

        binding.btnHint.setDebounceOnClickListener {
            if (isInteractionLocked) return@setDebounceOnClickListener
            val hint = viewModel.currentQuestion.value?.hint
            if (hint.isNullOrBlank()) {
                return@setDebounceOnClickListener
            }
            rewardedAd?.let { ad ->
                ad.show(requireActivity(), OnUserEarnedRewardListener { rewardItem ->
                    viewModel.useHint()
                    showLongToastWithDelay(getString(R.string.hint_format, hint))
                    loadRewardedAd()
                })
            } ?: run {
                showToast(getString(R.string.ad_not_ready))
            }
        }

        binding.btnCheck.setDebounceOnClickListener {
            val answer = when (viewModel.mode) {
                QuizMode.TEST -> selectedOption
                QuizMode.RIDDLE,
                QuizMode.EMOJI -> binding.etAnswer.text.toString()
            }

            if (answer.isNullOrBlank()) {
                showToast(getString(R.string.enter_answer))
                return@setDebounceOnClickListener
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

        optionButtons.forEach { button ->
            button.setOnClickListener {
                selectedOption = button.text.toString()
                optionButtons.forEach { it.isSelected = it == button }
            }
        }
    }

    private fun observeProgress() {
        val mediatorLiveData = MediatorLiveData<Pair<Int, Int>>()
        mediatorLiveData.addSource(viewModel.currentQuestionIndex) { index ->
            val total = viewModel.totalQuestions.value ?: 0
            mediatorLiveData.value = index to total
        }

        mediatorLiveData.addSource(viewModel.totalQuestions) { total ->
            val index = viewModel.currentQuestionIndex.value ?: 0
            mediatorLiveData.value = index to total
        }

        mediatorLiveData.observe(viewLifecycleOwner) { (index, total) ->
            if (index <= total) {
                binding.tvProgress.text = getString(R.string.progress_text_format, index, total)
            }
            binding.progressBar.max = total
            binding.progressBar.progress = index - 1
        }
    }

    private fun setupLoading() {
        viewModel.loadingState.observe(viewLifecycleOwner) { state ->
            when (state) {
                LoadingState.LOADING -> {
                    binding.loadingScreen.isVisible = true
                    binding.loadingBar.isVisible = true
                    binding.errorMessage.isVisible = false
                    binding.retryButton.isVisible = false
                }

                LoadingState.SUCCESS -> {
                    binding.loadingScreen.isVisible = false
                }

                LoadingState.ERROR -> {
                    binding.loadingScreen.isVisible = true
                    binding.loadingBar.isVisible = false
                    binding.errorMessage.isVisible = true
                    binding.retryButton.isVisible = true
                }
            }
        }
        binding.retryButton.setOnClickListener {
            viewModel.loadQuestions()
        }
    }


    private fun setupToolbar() {
        binding.toolbar.title = when (viewModel.mode) {
            QuizMode.EMOJI -> getString(R.string.emoji_mode)
            QuizMode.TEST -> getString(R.string.test_mode)
            QuizMode.RIDDLE -> getString(R.string.riddle_mode)
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun loadRewardedAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            requireContext(),
            BuildConfig.AD_REWARDED_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }
            }
        )

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
                optionButtons.forEach { it.isSelected = false }
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
            isInteractionLocked = true
            delay(1000)
            isInteractionLocked = false
            viewModel.nextQuestion()
        }
    }

    private fun navigateToResult() {
        viewModel.saveFinalScore()
        val score = viewModel.score.value ?: 0
        val mode = viewModel.mode.name
        val action = QuizFragmentDirections.actionQuizFragmentToResultFragment(
            resultScore = score, mode = mode
        )
        findNavController().navigate(action)
    }


    private fun showToast(text: String) {
        Toast.makeText(
            requireContext(),
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showLongToastWithDelay(text: String) {
        lifecycleScope.launch {
            delay(1000L)
            Toast.makeText(
                requireContext(),
                text,
                Toast.LENGTH_LONG
            ).show()
        }
    }

}