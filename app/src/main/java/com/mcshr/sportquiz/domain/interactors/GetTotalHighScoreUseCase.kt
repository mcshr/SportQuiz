package com.mcshr.sportquiz.domain.interactors

import com.mcshr.sportquiz.domain.SportQuizRepository
import com.mcshr.sportquiz.domain.model.QuizMode
import javax.inject.Inject

class GetTotalHighScoreUseCase @Inject constructor(
    private val repository: SportQuizRepository
) {
    operator fun invoke(): Int {
        return QuizMode.entries
            .toTypedArray()
            .maxOfOrNull { mode->
                repository.getHighScore(mode)
            } ?: 0
    }
}