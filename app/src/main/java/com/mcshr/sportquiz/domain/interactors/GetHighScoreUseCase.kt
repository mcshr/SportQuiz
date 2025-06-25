package com.mcshr.sportquiz.domain.interactors

import com.mcshr.sportquiz.domain.QuizScoreRepository
import com.mcshr.sportquiz.domain.entity.QuizMode
import javax.inject.Inject

class GetHighScoreUseCase @Inject constructor(
    private val repository: QuizScoreRepository
) {
    operator fun invoke(mode: QuizMode): Int {
        return repository.getHighScore(mode)
    }
}