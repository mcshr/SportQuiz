package com.mcshr.sportquiz.data

import com.mcshr.sportquiz.domain.SportQuizRepository
import com.mcshr.sportquiz.domain.entity.QuizMode
import javax.inject.Inject

class SportQuizRepositoryImpl @Inject constructor(
    private val preferences: SportQuizPreferences
): SportQuizRepository {
    override fun getHighScore(mode: QuizMode): Int {
        return preferences.getHighScoreForMode(mode)
    }

    override fun saveHighScore(mode: QuizMode, score: Int) {
        preferences.saveHighScoreForMode(mode, score)
    }
}