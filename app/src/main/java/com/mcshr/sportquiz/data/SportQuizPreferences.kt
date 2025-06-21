package com.mcshr.sportquiz.data

import android.content.SharedPreferences
import com.mcshr.sportquiz.domain.model.QuizMode
import javax.inject.Inject
import androidx.core.content.edit

class SportQuizPreferences @Inject constructor(
    private val prefs: SharedPreferences
) {
    fun getHighScoreForMode(mode: QuizMode):Int{
        return prefs.getInt(mode.toStringKey(), 0)
    }
    fun saveHighScoreForMode(mode: QuizMode, highScore:Int){
        prefs.edit { putInt(mode.toStringKey(), highScore) }
    }
    private fun QuizMode.toStringKey(): String{
        return when(this){
            QuizMode.EMOJI -> "score_emoji_key"
            QuizMode.RIDDLE -> "score_riddle_key"
            QuizMode.TEST -> "score_test_key"
        }
    }
}