package com.mcshr.sportquiz.data

import com.mcshr.sportquiz.domain.QuizScoreRepository
import com.mcshr.sportquiz.domain.entity.QuizMode
import javax.inject.Inject

class QuizScoreRepositoryImpl @Inject constructor(
    private val preferences: SportQuizPreferences
): QuizScoreRepository {
    override fun getHighScore(mode: QuizMode): Int {
        return preferences.getHighScoreForMode(mode)
    }

    override fun saveHighScore(mode: QuizMode, score: Int) {
        preferences.saveHighScoreForMode(mode, score)
    }
}