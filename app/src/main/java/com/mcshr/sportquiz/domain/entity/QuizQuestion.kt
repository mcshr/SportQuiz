package com.mcshr.sportquiz.domain.entity

data class QuizQuestion(
    val text: String,
    val correctAnswer: List<String>,
    val options: List<String>? = null, // tests
    val hint: String? = null           // emoji riddle
)
