package com.mcshr.sportquiz.ui.multiplayerQuiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcshr.sportquiz.domain.MultiplayerRepository
import com.mcshr.sportquiz.domain.entity.MultiplayerRoom
import com.mcshr.sportquiz.domain.entity.QuizMode
import com.mcshr.sportquiz.domain.entity.QuizQuestion
import com.mcshr.sportquiz.domain.entity.QuizResult
import com.mcshr.sportquiz.domain.interactors.CheckAnswerUseCase
import com.mcshr.sportquiz.domain.interactors.GetNicknameUseCase
import com.mcshr.sportquiz.domain.interactors.GetQuestionsUseCase
import com.mcshr.sportquiz.domain.interactors.PassQuestionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MultiplayerQuizViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: MultiplayerRepository,
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val checkAnswerUseCase: CheckAnswerUseCase,
    private val passQuestionUseCase: PassQuestionUseCase,
    getNicknameUseCase: GetNicknameUseCase
) : ViewModel() {
    val mode: QuizMode = QuizMode.valueOf(savedStateHandle.get<String>("mode") ?: "EMOJI")
    val playerName = getNicknameUseCase()

    private val _uiState = MutableLiveData<RoomUiState>(RoomUiState.Loading)
    val uiState: LiveData<RoomUiState> get() = _uiState

    private val _playerNumber = MutableLiveData<Int>()
    val playerNumber: LiveData<Int> get() = _playerNumber

    private var roomId: String? = null
    private var roomListener: LiveData<MultiplayerRoom?>? = null
    private var roomObserver: Observer<MultiplayerRoom?>? = null

    private var questions: List<QuizQuestion> = emptyList()
    private var hintUsed: Boolean = false

    private val _currentQuestion = MutableLiveData<QuizQuestion?>()
    val currentQuestion: LiveData<QuizQuestion?> get() = _currentQuestion

    var totalQuestions = 0


    private var currentRoom: MultiplayerRoom? = null

    val isMyTurn: Boolean
        get() = currentRoom?.currentTurn == playerNumber.value

    fun canSubmitAnswer(): Boolean = isMyTurn && currentQuestion.value != null

    fun joinOrCreateRoom() {
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

    init {
        joinOrCreateRoom()
    }

    private fun listenToRoom(id: String) {
        roomObserver?.let {
            roomListener?.removeObserver(it)
        }
        roomListener = repository.getRoomLiveData(id).also { liveData ->
            roomObserver = Observer { room -> handleRoom(room) }
            liveData.observeForever(roomObserver!!)
        }
    }

    private fun handleRoom(room: MultiplayerRoom?) {
        if (room == null) {
            _uiState.value = RoomUiState.Deleted
        } else {
            currentRoom = room
            if (room.player1 == null || room.player2 == null) {
                _uiState.value = RoomUiState.WaitingForOtherPlayer(room)
            } else {
                if (questions.isEmpty()) {
                    loadQuestions()
                } else {
                    loadCurrentQuestion()
                }
            }
        }
    }

    private fun loadQuestions() {
        val room = currentRoom ?: return
        viewModelScope.launch {
            _uiState.value = RoomUiState.RoomReadyButQuestionsLoading(room)
            val loaded = getQuestionsUseCase(mode)
            if (loaded.isNotEmpty()) {
                questions = loaded
                loadCurrentQuestion()
            } else {
                _uiState.value = RoomUiState.NoQuestions
            }
        }
    }

    private fun loadCurrentQuestion() {
        val room = currentRoom ?: return
        if (room.currentQuestionIndex >= questions.size) {
            finishRoom()
            return
        }
        totalQuestions = questions.size
        _currentQuestion.value = questions.getOrNull(room.currentQuestionIndex)
        hintUsed = false
        _uiState.value = RoomUiState.Game(room)
    }

    fun submitAnswer(answer: String?): QuizResult {
        val room = currentRoom ?: return QuizResult(false, 0)
        val question = currentQuestion.value ?: return QuizResult(false, 0)
        val result = answer?.let {
            checkAnswerUseCase(question, it, hintUsed)
        } ?: QuizResult(false, 0)
        viewModelScope.launch {
            val updatedRoom = room.copy(
                currentTurn = if (room.currentTurn == 1) 2 else 1,
                currentQuestionIndex = room.currentQuestionIndex + 1,
                player1 = if (playerNumber.value == 1) {
                    room.player1?.copy(score = (room.player1.score) + result.earnedPoints)
                } else room.player1,
                player2 = if (playerNumber.value == 2) {
                    room.player2?.copy(score = (room.player2.score) + result.earnedPoints)
                } else room.player2
            )
            repository.updateRoom(updatedRoom)
            passQuestionUseCase(question)
        }

        return result
    }

    fun leaveRoom() {
        val id = roomId ?: return
        val player = playerNumber.value ?: return
        viewModelScope.launch {
            repository.leaveRoom(id, player)
        }
    }

    fun finishRoom() {
        val room = currentRoom ?: return
        val playerNum = playerNumber.value ?: return
        viewModelScope.launch {
            repository.finishRoom(room.id)
        }
        _uiState.value = RoomUiState.Finished(room, playerNum)
    }

    fun useHint() {
        hintUsed = true
    }

    override fun onCleared() {
        super.onCleared()
        roomObserver?.let {
            roomListener?.removeObserver(it)
        }
    }

    sealed class RoomUiState {
        object Loading : RoomUiState()
        data class WaitingForOtherPlayer(val room: MultiplayerRoom) : RoomUiState()
        data class RoomReadyButQuestionsLoading(val room: MultiplayerRoom) : RoomUiState()
        data class Game(val room: MultiplayerRoom) : RoomUiState()
        object NoQuestions : RoomUiState()
        object Deleted : RoomUiState()
        data class Finished(val room: MultiplayerRoom, val playerNumber: Int) : RoomUiState()
    }
}