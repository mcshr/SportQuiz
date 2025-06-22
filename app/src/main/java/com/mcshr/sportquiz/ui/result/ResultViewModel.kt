package com.mcshr.sportquiz.ui.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mcshr.sportquiz.domain.entity.QuizMode
import com.mcshr.sportquiz.domain.interactors.GetHighScoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getHighScoreUseCase: GetHighScoreUseCase
) : ViewModel() {
    val mode: QuizMode = QuizMode.valueOf(savedStateHandle["mode"] ?: "EMOJI")
    val score:Int = savedStateHandle["resultScore"]?:-1
    val highScore = getHighScoreUseCase(mode)
}