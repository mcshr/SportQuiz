package com.mcshr.sportquiz.data.firestore

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.mcshr.sportquiz.data.firestore.dto.QuizQuestionDto
import com.mcshr.sportquiz.domain.entity.QuizMode
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuizRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getQuestionsByMode(mode:QuizMode):List <QuizQuestionDto> {
        return try {
           firestore.collection("questions")
                .whereEqualTo("mode", mode.name).get().await()
                .documents.mapNotNull {
                    it.toObject(QuizQuestionDto::class.java)
                }
        }catch (e: Exception){
            Log.d("FIREBASE_EXCEPTION", e.toString())
            emptyList()
        }
    }
}