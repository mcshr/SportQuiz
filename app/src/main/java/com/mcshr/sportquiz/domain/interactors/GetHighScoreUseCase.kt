package com.mcshr.sportquiz.domain.interactors

import com.mcshr.sportquiz.domain.QuizInfoRepository
import com.mcshr.sportquiz.domain.entity.QuizMode
import javax.inject.Inject

class GetHighScoreUseCase @Inject constructor(
    private val repository: QuizInfoRepository
) {
    operator fun invoke(mode: QuizMode): Int {
        return repository.getHighScore(mode)
    }
}