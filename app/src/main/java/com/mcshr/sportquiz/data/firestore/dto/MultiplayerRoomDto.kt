package com.mcshr.sportquiz.data.firestore.dto

data class MultiplayerRoomDto(
    val player1: PlayerDto? = null,
    val player2: PlayerDto? = null,
    val mode: String = "",
    val currentTurn: Int = 1,
    val currentQuestionIndex: Int = 0,
    val status: String = "waiting", // "waiting", "active", "finished"
    val createdAt: Long = System.currentTimeMillis(),
)
