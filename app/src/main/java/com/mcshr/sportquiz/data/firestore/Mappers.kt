package com.mcshr.sportquiz.data.firestore

import com.mcshr.sportquiz.data.firestore.dto.MultiplayerRoomDto
import com.mcshr.sportquiz.data.firestore.dto.PlayerDto
import com.mcshr.sportquiz.domain.entity.MultiplayerRoom
import com.mcshr.sportquiz.domain.entity.Player
import com.mcshr.sportquiz.domain.entity.QuizMode


fun MultiplayerRoomDto.toDomain(roomId: String): MultiplayerRoom {
    return MultiplayerRoom(
        id = roomId,
        player1 = player1?.let { Player(it.name, it.score) },
        player2 = player2?.let { Player(it.name, it.score) },
        mode = QuizMode.valueOf(mode),
        currentTurn = currentTurn,
        currentQuestionIndex = currentQuestionIndex,
        status = status
    )
}

fun MultiplayerRoom.toDto(): MultiplayerRoomDto {
    return MultiplayerRoomDto(
        player1 = player1?.let { PlayerDto(it.name, it.score) },
        player2 = player2?.let { PlayerDto(it.name, it.score) },
        mode = mode.name,
        currentTurn = currentTurn,
        currentQuestionIndex = currentQuestionIndex,
        status = status,
        createdAt = null
    )
}
