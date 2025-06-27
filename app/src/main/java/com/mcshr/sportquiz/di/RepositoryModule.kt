package com.mcshr.sportquiz.di

import com.mcshr.sportquiz.data.MultiplayerRepositoryImpl
import com.mcshr.sportquiz.data.QuizQuestionRepositoryImpl
import com.mcshr.sportquiz.data.QuizScoreRepositoryImpl
import com.mcshr.sportquiz.domain.MultiplayerRepository
import com.mcshr.sportquiz.domain.QuizQuestionRepository
import com.mcshr.sportquiz.domain.QuizScoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(
        impl: QuizScoreRepositoryImpl
    ): QuizScoreRepository

    @Binds
    @Singleton
    abstract fun bindQuestionsRepo(
        impl: QuizQuestionRepositoryImpl
    ): QuizQuestionRepository

    @Binds
    @Singleton
    abstract fun bindMultiplayerRepo(
        impl: MultiplayerRepositoryImpl
    ): MultiplayerRepository
}