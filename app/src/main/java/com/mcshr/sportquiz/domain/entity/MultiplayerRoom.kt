package com.mcshr.sportquiz.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MultiplayerRoom(
    val id: String,
    val player1: Player?,
    val player2: Player?,
    val mode: QuizMode,
    val currentTurn: Int,
    val currentQuestionIndex: Int,
    val status: String, // "waiting", "active", "finished"
): Parcelable