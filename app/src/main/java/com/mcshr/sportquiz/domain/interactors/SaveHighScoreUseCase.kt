package com.mcshr.sportquiz.domain.interactors

import com.mcshr.sportquiz.domain.QuizInfoRepository
import com.mcshr.sportquiz.domain.entity.QuizMode
import javax.inject.Inject

class SaveHighScoreUseCase @Inject constructor(
    private val repository: QuizInfoRepository
) {
    operator fun invoke(mode: QuizMode, score: Int) {
        val highScore = repository.getHighScore(mode)
        if(score>highScore){
            repository.saveHighScore(mode, score)
        }
    }
}