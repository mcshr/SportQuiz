package com.mcshr.sportquiz.ui.multiplayerQuiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mcshr.sportquiz.data.firestore.dto.MultiplayerRoomDto
import com.mcshr.sportquiz.databinding.FragmentMultiplayerQuizBinding
import com.mcshr.sportquiz.domain.entity.QuizMode
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class MultiplayerQuizFragment : Fragment() {

    private var _binding: FragmentMultiplayerQuizBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("Fragment MultiplayerQuiz Binding is null")

    private val viewModel: MultiplayerQuizViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMultiplayerQuizBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //val playerName = "player"+ Random.nextInt().toString()
        val playerName = "Maxim"
        val mode = QuizMode.EMOJI

        viewModel.joinOrCreateRoom(playerName, mode)

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MultiplayerQuizViewModel.RoomUiState.Loading -> showLoading()
                is MultiplayerQuizViewModel.RoomUiState.Ready -> showRoomContent(state.room)
                is MultiplayerQuizViewModel.RoomUiState.Deleted -> showRoomDeleted()
            }
        }

        binding.btnRetry.setOnClickListener {
            viewModel.joinOrCreateRoom(playerName, mode)
        }
        binding.btnExit.setOnClickListener {
            findNavController().popBackStack()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showLoading(){
        binding.loadingScreen.isVisible = true
        binding.contentScreen.isVisible = false

        binding.loadingBar.isVisible = true
        binding.errorMessage.isVisible = false
        binding.btnRetry.isVisible = false
        binding.btnExit.isVisible = false
    }

    private fun showRoomContent(room: MultiplayerRoomDto){
        binding.loadingScreen.isVisible = false
        binding.contentScreen.isVisible = true

        binding.tvRoomStatus.text = room.status
        binding.tvPlayers.text =  "Гравець №1: ${room.player1?.name?:"-"}\n Гравець №2: ${room.player2?.name?:"-"}"

    }

    private fun showRoomDeleted(){
        binding.loadingScreen.isVisible = true
        binding.contentScreen.isVisible = false

        binding.loadingBar.isVisible = false
        binding.errorMessage.isVisible = true
        binding.btnRetry.isVisible = true
        binding.btnExit.isVisible = true
    }

    override fun onDestroyView() {
        viewModel.leaveRoom()
        _binding = null
        super.onDestroyView()
    }

}