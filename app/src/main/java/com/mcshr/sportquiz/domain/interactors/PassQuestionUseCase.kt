package com.mcshr.sportquiz.domain.interactors

import com.mcshr.sportquiz.domain.QuizQuestionRepository
import com.mcshr.sportquiz.domain.entity.QuizQuestion
import javax.inject.Inject

class PassQuestionUseCase @Inject constructor(
    private val repository: QuizQuestionRepository
) {
    operator fun invoke(question: QuizQuestion){
        repository.saveQuestionAsPassed(question)
    }
}