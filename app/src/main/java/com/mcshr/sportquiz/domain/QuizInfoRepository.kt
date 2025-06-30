package com.mcshr.sportquiz.domain

import com.mcshr.sportquiz.domain.entity.QuizMode

interface QuizInfoRepository {
    fun getHighScore(mode: QuizMode): Int
    fun saveHighScore(mode: QuizMode, score: Int)

    fun getNickname(): String
    fun saveNickname(nickname: String)
}