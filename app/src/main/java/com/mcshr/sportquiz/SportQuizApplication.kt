package com.mcshr.sportquiz

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.ads.MobileAds
import com.mcshr.sportquiz.data.firestore.QuizDataUploader
import dagger.hilt.android.HiltAndroidApp
import dagger.multibindings.IntKey
import javax.inject.Inject

@HiltAndroidApp
class SportQuizApplication: Application() {
    //@Inject
    //lateinit var quizDataUploader: QuizDataUploader
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        MobileAds.initialize(this)
    //quizDataUploader.uploadAll()
    }
}