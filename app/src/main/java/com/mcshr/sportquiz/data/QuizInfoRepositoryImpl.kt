package com.mcshr.sportquiz.data

import com.mcshr.sportquiz.domain.QuizInfoRepository
import com.mcshr.sportquiz.domain.entity.QuizMode
import javax.inject.Inject

class QuizInfoRepositoryImpl @Inject constructor(
    private val preferences: SportQuizPreferences
): QuizInfoRepository {
    override fun getHighScore(mode: QuizMode): Int {
        return preferences.getHighScoreForMode(mode)
    }

    override fun saveHighScore(mode: QuizMode, score: Int) {
        preferences.saveHighScoreForMode(mode, score)
    }

    override fun getNickname(): String {
        return preferences.getNickname()
    }

    override fun saveNickname(nickname: String) {
        preferences.saveNickname(nickname)
    }
}