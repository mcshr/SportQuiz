package com.mcshr.sportquiz.domain.interactors

import com.mcshr.sportquiz.domain.QuizInfoRepository
import com.mcshr.sportquiz.domain.entity.QuizMode
import javax.inject.Inject

class GetTotalHighScoreUseCase @Inject constructor(
    private val repository: QuizInfoRepository
) {
    operator fun invoke(): Int {
        return QuizMode.entries
            .toTypedArray()
            .maxOfOrNull { mode->
                repository.getHighScore(mode)
            } ?: 0
    }
}