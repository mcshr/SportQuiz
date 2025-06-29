package com.mcshr.sportquiz.data

import com.mcshr.sportquiz.data.firestore.QuizRemoteDataSource
import com.mcshr.sportquiz.domain.QuizQuestionRepository
import com.mcshr.sportquiz.domain.entity.QuizMode
import com.mcshr.sportquiz.domain.entity.QuizQuestion
import javax.inject.Inject

class QuizQuestionRepositoryImpl @Inject constructor(
    private val dataSource: QuizRemoteDataSource,
    private val preferences: SportQuizPreferences
) : QuizQuestionRepository {
    override suspend fun getQuestions(mode: QuizMode): List<QuizQuestion> {
        val passedQuestions = preferences.getPassedQuestionIds()
        return dataSource.getQuestionsByMode(mode).map {
            QuizQuestion(
                text = it.text,
                correctAnswer = it.correctAnswers,
                options = it.options,
                hint = it.hint,
                id = it.id,
                isPassed = passedQuestions.contains(it.id)
            )
        }
    }

    override fun saveQuestionAsPassed(question: QuizQuestion) {
        question.id?.let{
            preferences.markAsPassed(it)
        }
    }
}