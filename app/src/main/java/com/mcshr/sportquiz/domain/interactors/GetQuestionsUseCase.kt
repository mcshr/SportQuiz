package com.mcshr.sportquiz.domain.interactors

import com.mcshr.sportquiz.domain.QuizQuestionRepository
import com.mcshr.sportquiz.domain.entity.QuizMode
import com.mcshr.sportquiz.domain.entity.QuizQuestion
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val repository: QuizQuestionRepository
) {
    suspend operator fun invoke(mode: QuizMode): List<QuizQuestion> {
        return repository.getQuestions(mode)
    }
}