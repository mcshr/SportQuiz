package com.mcshr.sportquiz.ui.modeSelect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcshr.sportquiz.domain.interactors.GetTotalHighScoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ModeSelectViewModel @Inject constructor(
    private val getTotalHighScoreUseCase: GetTotalHighScoreUseCase
): ViewModel() {
    private val _totalHighScore = MutableLiveData<Int>()
    val totalHighScore: LiveData<Int> = _totalHighScore

    fun loadHighScore() {
        _totalHighScore.value = getTotalHighScoreUseCase()
    }
}