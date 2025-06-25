package com.mcshr.sportquiz.data

import com.mcshr.sportquiz.data.firestore.QuizRemoteDataSource
import com.mcshr.sportquiz.data.firestore.toDomain
import com.mcshr.sportquiz.domain.QuizQuestionRepository
import com.mcshr.sportquiz.domain.entity.QuizMode
import com.mcshr.sportquiz.domain.entity.QuizQuestion
import javax.inject.Inject

class QuizQuestionRepositoryImpl @Inject constructor(
    private val dataSource: QuizRemoteDataSource
): QuizQuestionRepository {
    override suspend fun getQuestions(mode: QuizMode): List<QuizQuestion> {
        return dataSource.getQuestionsByMode(mode).map{
            it.toDomain()
        }
    }
}