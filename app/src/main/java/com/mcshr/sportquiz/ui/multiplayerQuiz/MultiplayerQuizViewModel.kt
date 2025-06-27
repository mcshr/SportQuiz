package com.mcshr.sportquiz.ui.multiplayerQuiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcshr.sportquiz.data.firestore.dto.MultiplayerRoomDto
import com.mcshr.sportquiz.domain.MultiplayerRepository
import com.mcshr.sportquiz.domain.entity.QuizMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MultiplayerQuizViewModel @Inject constructor(
    private val repository: MultiplayerRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<RoomUiState>(RoomUiState.Loading)
    val uiState: LiveData<RoomUiState> get() = _uiState

    private val _playerNumber = MutableLiveData<Int>()
    val playerNumber: LiveData<Int> get() = _playerNumber

    private var roomId: String? = null
    private var roomListener: LiveData<MultiplayerRoomDto?>? = null

    fun joinOrCreateRoom(playerName: String, mode: QuizMode) {
        _uiState.value = RoomUiState.Loading
        viewModelScope.launch {
            val result = repository.joinOrCreateRoom(playerName, mode)
            if (result != null) {
                roomId = result.roomId
                _playerNumber.value = result.playerNumber
                listenToRoom(result.roomId)
            } else {
                _uiState.value = RoomUiState.Deleted
            }
        }
    }

    private fun listenToRoom(id: String) {
        roomListener = repository.getRoomLiveData(id).also { liveData ->
            liveData.observeForever { room ->
                _uiState.value = when {
                    room == null -> RoomUiState.Deleted
                    else -> RoomUiState.Ready(room)
                }
            }
        }
    }

    fun leaveRoom() {
        val id = roomId ?: return
        val player = playerNumber.value ?: return
        viewModelScope.launch {
            repository.leaveRoom(id, player)
        }
    }

    fun finishRoom() {
        val id = roomId ?: return
        viewModelScope.launch {
            repository.finishRoom(id)
        }
    }

    sealed class RoomUiState {
        object Loading : RoomUiState()
        data class Ready(val room: MultiplayerRoomDto) : RoomUiState()
        object Deleted : RoomUiState()
    }
}