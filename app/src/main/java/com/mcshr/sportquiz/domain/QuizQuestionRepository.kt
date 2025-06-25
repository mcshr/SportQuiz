package com.mcshr.sportquiz.domain

import com.mcshr.sportquiz.domain.entity.QuizMode
import com.mcshr.sportquiz.domain.entity.QuizQuestion

interface QuizQuestionRepository {
    suspend fun getQuestions(mode: QuizMode): List<QuizQuestion>
}