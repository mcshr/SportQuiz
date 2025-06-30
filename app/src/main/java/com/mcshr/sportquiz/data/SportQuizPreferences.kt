package com.mcshr.sportquiz.data

import android.content.SharedPreferences
import com.mcshr.sportquiz.domain.entity.QuizMode
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
            QuizMode.EMOJI -> SCORE_EMOJI_KEY
            QuizMode.RIDDLE -> SCORE_RIDDLE_KEY
            QuizMode.TEST ->  SCORE_TEST_KEY
        }
    }

    fun markAsPassed(questionId:String){
        val currentSet = getPassedQuestionIds().toMutableSet()
        currentSet.add(questionId)
        prefs.edit{ putStringSet(PASSED_QUESTIONS_KEY, currentSet)}
    }

    fun getPassedQuestionIds():Set<String>{
        return prefs.getStringSet(PASSED_QUESTIONS_KEY, emptySet())?:emptySet()
    }

    fun getNickname(): String = prefs.getString( NICKNAME_KEY, "") ?: ""

    fun saveNickname(nickname: String) {
        prefs.edit { putString( NICKNAME_KEY, nickname) }
    }

    companion object{
        private const val PASSED_QUESTIONS_KEY = "passed_questions_key"
        private const val SCORE_EMOJI_KEY ="score_emoji_key"
        private const val SCORE_RIDDLE_KEY = "score_riddle_key"
        private const val SCORE_TEST_KEY = "score_test_key"
        private const val NICKNAME_KEY = "nickname"
    }

}