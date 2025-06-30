package com.mcshr.sportquiz.domain.interactors

import com.mcshr.sportquiz.domain.entity.QuizQuestion
import com.mcshr.sportquiz.domain.entity.QuizResult
import javax.inject.Inject

class CheckAnswerUseCase @Inject constructor() {
    operator fun invoke(
        question: QuizQuestion,
        answer: String,
        hintUsed: Boolean
    ): QuizResult{
        val isCorrect = question.correctAnswer.any {
            it.trim().lowercase() == answer.trim().lowercase()
        }
        val points = when {
            !isCorrect -> 0
            hintUsed -> 1
            else -> 2
        }
        return QuizResult(isCorrect, points)
    }
}