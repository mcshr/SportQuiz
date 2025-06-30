package com.mcshr.sportquiz.ui.multiplayerQuiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.mcshr.sportquiz.BuildConfig
import com.mcshr.sportquiz.R
import com.mcshr.sportquiz.databinding.FragmentMultiplayerQuizBinding
import com.mcshr.sportquiz.domain.entity.MultiplayerRoom
import com.mcshr.sportquiz.domain.entity.QuizMode
import com.mcshr.sportquiz.domain.entity.QuizQuestion
import com.mcshr.sportquiz.ui.multiplayerQuiz.MultiplayerQuizViewModel.RoomUiState
import com.mcshr.sportquiz.ui.utils.formatPlayerName
import com.mcshr.sportquiz.ui.utils.setDebounceOnClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MultiplayerQuizFragment : Fragment() {

    private var _binding: FragmentMultiplayerQuizBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("Fragment MultiplayerQuiz Binding is null")

    private val viewModel: MultiplayerQuizViewModel by viewModels()

    private val optionButtons: List<Button> by lazy {
        listOf(
            binding.layoutQuestion.btnOption1,
            binding.layoutQuestion.btnOption2,
            binding.layoutQuestion.btnOption3,
            binding.layoutQuestion.btnOption4
        )
    }

    private var selectedOption: String? = null
    private var rewardedAd: RewardedAd? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMultiplayerQuizBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupToolbar()
        loadRewardedAd()
        setupButtons()

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is RoomUiState.Loading -> showLoadingScreen()
                is RoomUiState.WaitingForOtherPlayer -> showWaitingScreen(
                    getString(R.string.waiting_for_player)
                )

                is RoomUiState.RoomReadyButQuestionsLoading -> showWaitingScreen(
                    getString(R.string.loading_questions)
                )

                is RoomUiState.Game -> {
                    showGameScreen(state.room)
                }

                is RoomUiState.NoQuestions -> showErrorScreen(getString(R.string.error_no_questions))
                is RoomUiState.Deleted -> showErrorScreen(getString(R.string.error_no_room))
                is RoomUiState.Finished -> {
                    showGameScreen(state.room)
                    navigateToResultScreen(state.room, state.playerNumber)
                }
            }
        }

        viewModel.currentQuestion.observe(viewLifecycleOwner) { question ->
            if (question != null) showQuestion(question)
        }

        binding.btnRetry.setOnClickListener {
            viewModel.joinOrCreateRoom()
        }
        binding.btnExit.setOnClickListener {
            findNavController().popBackStack()
        }
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.title = when (viewModel.mode) {
            QuizMode.EMOJI -> getString(R.string.emoji_mode)
            QuizMode.TEST -> getString(R.string.test_mode)
            QuizMode.RIDDLE -> getString(R.string.riddle_mode)
        }
    }

    private fun showLoadingScreen() {
        binding.loadingScreen.isVisible = true
        binding.tvLoadingMsg.isVisible = false
        binding.roomInfo.isVisible = false
        binding.layoutQuestion.root.isVisible = false
        binding.loadingBar.isVisible = true
        binding.errorMessage.isVisible = false
        binding.btnRetry.isVisible = false
        binding.btnExit.isVisible = false
    }

    private fun showErrorScreen(msg: String) {
        binding.tvLoadingMsg.isVisible = false
        binding.loadingScreen.isVisible = true
        binding.loadingBar.isVisible = false
        binding.errorMessage.text = msg
        binding.errorMessage.isVisible = true
        binding.btnRetry.isVisible = true
        binding.btnExit.isVisible = true
        binding.layoutQuestion.root.isVisible = false
        binding.roomInfo.isVisible = false
    }

    private fun showWaitingScreen(msg: String) {
        showLoadingScreen()
        binding.tvLoadingMsg.isVisible = true
        binding.tvLoadingMsg.text = msg
    }

    private fun showGameScreen(room: MultiplayerRoom) {
        binding.loadingScreen.isVisible = false
        binding.roomInfo.isVisible = true
        binding.layoutQuestion.root.isVisible = true
        updateProgress(room)
        showTurn(room)
    }

    private fun updateProgress(room: MultiplayerRoom) {
        val index = room.currentQuestionIndex + 1
        val total = viewModel.totalQuestions
        if (index <= total) {
            binding.tvProgress.text = getString(R.string.progress_text_format, index, total)
        }
        binding.progressBar.max = total
        binding.progressBar.progress = index - 1
        binding.tvPlayer1.text = getString(
            R.string.player_score_format,
            formatPlayerName(room.player1),
            room.player1?.score ?: "-"
        )
        binding.tvPlayer2.text = getString(
            R.string.player_score_format,
            formatPlayerName(room.player2),
            room.player2?.score ?: "-"
        )
    }

    private fun showTurn(room: MultiplayerRoom) {
        val activeColor = ContextCompat.getColor(requireContext(), R.color.secondaryYellow)
        val defaultColor = ContextCompat.getColor(requireContext(), R.color.textSecondary)
        when (room.currentTurn) {
            1 -> {
                binding.arrowPlayer1.isVisible = true
                binding.arrowPlayer2.isVisible = false
                binding.tvPlayer1.setTextColor(activeColor)
                binding.tvPlayer2.setTextColor(defaultColor)
            }

            2 -> {
                binding.arrowPlayer2.isVisible = true
                binding.arrowPlayer1.isVisible = false
                binding.tvPlayer2.setTextColor(activeColor)
                binding.tvPlayer1.setTextColor(defaultColor)
            }
        }
    }

    private fun showQuestion(question: QuizQuestion) {
        binding.layoutQuestion.tvQuestion.text = question.text
        binding.layoutQuestion.tvQuestionIsNew.isVisible = !question.isPassed
        selectedOption = null

        val showHint = !question.hint.isNullOrBlank()
        binding.layoutQuestion.btnHint.visibility = if (showHint) View.VISIBLE else View.GONE

        when (viewModel.mode) {
            QuizMode.TEST -> {
                binding.layoutQuestion.etAnswer.visibility = View.GONE
                binding.layoutQuestion.optionsContainer.visibility = View.VISIBLE
                question.options?.forEachIndexed { index, option ->
                    optionButtons[index].text = option
                }
                optionButtons.forEach { it.isSelected = false }
            }

            QuizMode.EMOJI, QuizMode.RIDDLE -> {
                binding.layoutQuestion.optionsContainer.visibility = View.GONE
                binding.layoutQuestion.etAnswer.visibility = View.VISIBLE
                binding.layoutQuestion.etAnswer.text.clear()
            }
        }
    }

    private fun setupButtons() {
        binding.layoutQuestion.btnCheck.setDebounceOnClickListener {
            if (!viewModel.canSubmitAnswer()) {
                showToast(getString(R.string.not_your_turn))
                return@setDebounceOnClickListener
            }
            val answer = when (viewModel.mode) {
                QuizMode.TEST -> selectedOption
                QuizMode.EMOJI, QuizMode.RIDDLE -> binding.layoutQuestion.etAnswer.text.toString()
            }

            if (answer.isNullOrBlank()) {
                showToast(getString(R.string.enter_answer))
                return@setDebounceOnClickListener
            }

            val result = viewModel.submitAnswer(answer)

            val resultMsg = if (result.isCorrect) {
                getString(R.string.correct_answer, result.earnedPoints.toString())
            } else {
                getString(R.string.incorrect_answer)
            }
            showToast(resultMsg)
        }

        binding.layoutQuestion.btnSkip.setDebounceOnClickListener {

            if (!viewModel.canSubmitAnswer()) {
                showToast(getString(R.string.not_your_turn))
                return@setDebounceOnClickListener
            }
            viewModel.submitAnswer(null)
        }

        binding.layoutQuestion.btnHint.setDebounceOnClickListener {
            if (!viewModel.canSubmitAnswer()) {
                showToast(getString(R.string.not_your_turn))
                return@setDebounceOnClickListener
            }
            val hint = viewModel.currentQuestion.value?.hint ?: return@setDebounceOnClickListener
            rewardedAd?.show(requireActivity()) {
                viewModel.useHint()
                showLongToastWithDelay(getString(R.string.hint_format, hint))
                loadRewardedAd()
            } ?: showToast(getString(R.string.ad_not_ready))
        }

        optionButtons.forEach { button ->
            button.setOnClickListener {
                selectedOption = button.text.toString()
                optionButtons.forEach { it.isSelected = it == button }
            }
        }
    }


    private fun loadRewardedAd() {
        RewardedAd.load(
            requireContext(),
            BuildConfig.AD_REWARDED_ID,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                }
            }
        )
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun showLongToastWithDelay(text: String) {
        lifecycleScope.launch {
            delay(1000)
            Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
        }
    }

    private fun navigateToResultScreen(room: MultiplayerRoom, playerNum: Int) {
        val action = MultiplayerQuizFragmentDirections
            .actionMultiplayerQuizFragmentToMultiplayerResultFragment(room, playerNum)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!requireActivity().isChangingConfigurations) {
            viewModel.leaveRoom()
        }
    }
}