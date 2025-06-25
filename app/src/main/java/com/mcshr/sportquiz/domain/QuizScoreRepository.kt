package com.mcshr.sportquiz.domain

import com.mcshr.sportquiz.domain.entity.QuizMode

interface QuizScoreRepository {
    fun getHighScore(mode: QuizMode): Int
    fun saveHighScore(mode: QuizMode, score: Int)
}