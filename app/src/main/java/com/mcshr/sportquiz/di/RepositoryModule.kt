package com.mcshr.sportquiz.di

import com.mcshr.sportquiz.data.SportQuizRepositoryImpl
import com.mcshr.sportquiz.domain.SportQuizRepository
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
        impl: SportQuizRepositoryImpl
    ): SportQuizRepository
}