package com.mcshr.sportquiz.data.firestore.dto

data class QuizQuestionDto(
    val text: String = "",
    val correctAnswers: List<String> = emptyList<String>(),
    val options: List<String>? = null,
    val hint: String? = null,
    val mode: String = ""
)