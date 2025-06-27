package com.mcshr.sportquiz.domain

import androidx.lifecycle.LiveData
import com.mcshr.sportquiz.data.firestore.dto.MultiplayerRoomDto
import com.mcshr.sportquiz.domain.entity.JoinResult
import com.mcshr.sportquiz.domain.entity.QuizMode

interface MultiplayerRepository {
    suspend fun joinOrCreateRoom(playerName: String, mode: QuizMode): JoinResult?
    suspend fun finishRoom(roomId: String)
    suspend fun leaveRoom(roomId: String, playerNumber: Int)
    fun getRoomLiveData(roomId: String): LiveData<MultiplayerRoomDto?>//TODO make MultiplayerRoom entity + mappers
}