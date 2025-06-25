package com.mcshr.sportquiz.data.firestore

import com.mcshr.sportquiz.data.firestore.dto.QuizQuestionDto
import com.mcshr.sportquiz.domain.entity.QuizQuestion

fun QuizQuestionDto.toDomain(): QuizQuestion{
    return QuizQuestion(
        text = text,
        correctAnswer = correctAnswer,
        options = options,
        hint = hint
    )
}