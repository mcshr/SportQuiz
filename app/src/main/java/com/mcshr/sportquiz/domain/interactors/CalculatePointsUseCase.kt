package com.mcshr.sportquiz.domain.interactors

import javax.inject.Inject

class CalculatePointsUseCase @Inject constructor() {
    operator fun invoke(isCorrect: Boolean, hintUsed: Boolean): Int {
        return when {
            !isCorrect -> 0
            hintUsed -> 1
            else -> 2
        }
    }
}
