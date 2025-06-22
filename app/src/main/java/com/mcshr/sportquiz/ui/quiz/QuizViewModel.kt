package com.mcshr.sportquiz.ui.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mcshr.sportquiz.domain.entity.QuizMode
import com.mcshr.sportquiz.domain.entity.QuizQuestion
import com.mcshr.sportquiz.domain.interactors.CalculatePointsUseCase
import com.mcshr.sportquiz.domain.interactors.GetQuestionsUseCase
import com.mcshr.sportquiz.domain.interactors.SaveHighScoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getQuestionsUseCase: GetQuestionsUseCase,
    private val saveHighScoreUseCase: SaveHighScoreUseCase,
    private val calculatePointsUseCase: CalculatePointsUseCase
) : ViewModel() {

    val mode: QuizMode = QuizMode.valueOf(savedStateHandle.get<String>("mode") ?: "EMOJI")

    private val questions: List<QuizQuestion> = getQuestionsUseCase(mode)
    private var currentIndex = 0
    private var hintUsed = false

    private val _currentQuestion = MutableLiveData<QuizQuestion?>()
    val currentQuestion: LiveData<QuizQuestion?>
        get() = _currentQuestion

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    init {
        loadCurrentQuestion()
    }

    private fun loadCurrentQuestion() {
        hintUsed = false
        _currentQuestion.value = questions.getOrNull(currentIndex)
    }

    fun submitAnswer(answer: String): Pair<Boolean, Int> {
        val question = _currentQuestion.value ?: return false to 0
        val isCorrect = question.correctAnswer.any {
            it.trim().lowercase() == answer.trim().lowercase()
        }
        val earnedPoints = calculatePointsUseCase(isCorrect, hintUsed)
        _score.value = (_score.value ?: 0) + earnedPoints
        return isCorrect to earnedPoints
    }

    fun saveFinalScore() {
        val finalScore = score.value?:0
        saveHighScoreUseCase(mode, finalScore)
    }


    fun nextQuestion() {
        currentIndex++
        loadCurrentQuestion()
    }

    fun useHint() {
        hintUsed = true
    }

}
