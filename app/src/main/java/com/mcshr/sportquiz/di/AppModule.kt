package com.mcshr.sportquiz.di

import android.content.Context
import android.content.SharedPreferences
import com.mcshr.sportquiz.data.SportQuizPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideSharedPrefs(
        @ApplicationContext context: Context
    ): SharedPreferences{
        return  context.getSharedPreferences("SportQuizPrefs", Context.MODE_PRIVATE)
    }
    @Provides
    @Singleton
    fun provideSportQuizPrefs(
        prefs: SharedPreferences
    ): SportQuizPreferences{
        return SportQuizPreferences(prefs)
    }

}