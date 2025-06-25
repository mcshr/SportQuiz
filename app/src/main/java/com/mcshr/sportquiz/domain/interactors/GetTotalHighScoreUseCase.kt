package com.mcshr.sportquiz.domain.interactors

import com.mcshr.sportquiz.domain.QuizScoreRepository
import com.mcshr.sportquiz.domain.entity.QuizMode
import javax.inject.Inject

class GetTotalHighScoreUseCase @Inject constructor(
    private val repository: QuizScoreRepository
) {
    operator fun invoke(): Int {
        return QuizMode.entries
            .toTypedArray()
            .maxOfOrNull { mode->
                repository.getHighScore(mode)
            } ?: 0
    }
}