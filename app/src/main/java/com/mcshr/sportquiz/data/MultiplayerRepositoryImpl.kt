package com.mcshr.sportquiz.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.mcshr.sportquiz.data.firestore.dto.MultiplayerRoomDto
import com.mcshr.sportquiz.data.firestore.dto.PlayerDto
import com.mcshr.sportquiz.domain.MultiplayerRepository
import com.mcshr.sportquiz.domain.entity.JoinResult
import com.mcshr.sportquiz.domain.entity.QuizMode
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MultiplayerRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : MultiplayerRepository {

    private val rooms = firestore.collection("multiplayer_rooms")

    override suspend fun joinOrCreateRoom(playerName: String, mode: QuizMode): JoinResult? {
        return try {
            val waitingRoom = rooms
                .whereEqualTo("status", "waiting")
                .whereEqualTo("mode", mode.name)
                .whereEqualTo("player2", null)
                .orderBy("createdAt")
                .limit(1)
                .get()
                .await()
                .documents
                .firstOrNull()

            if (waitingRoom != null) {
                val roomId = waitingRoom.id
                val roomRef = rooms.document(roomId)
                val success = firestore.runTransaction { transaction ->
                    val snapshot = transaction.get(roomRef)
                    if (snapshot.getString("status") == "waiting" && snapshot.get("player2") == null) {
                        transaction.update(
                            roomRef, mapOf(
                                "player2" to PlayerDto(name = playerName),
                                "status" to "in_progress"
                            )
                        )
                        true
                    } else false
                }.await()

                if (success) {
                    JoinResult(roomId, 2)
                } else {
                    createRoom(playerName, mode)
                }
            } else {
                createRoom(playerName, mode)
            }
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun createRoom(playerName: String, mode: QuizMode): JoinResult? {
        val newRoomRef = rooms.document()
        val session = MultiplayerRoomDto(
            player1 = PlayerDto(name = playerName),
            mode = mode.name,
            currentTurn = 1,
            currentQuestionIndex = 0,
            status = "waiting",
            createdAt = System.currentTimeMillis()
        )

        newRoomRef.set(session).await()
        return JoinResult(newRoomRef.id, 1)
    }


    override suspend fun leaveRoom(roomId: String, playerNumber: Int) {
        val room = rooms.document(roomId)
        try {
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(room)
                val updatedData = when (playerNumber) {
                    1 -> mapOf("player1" to null, "status" to "waiting")
                    2 -> mapOf("player2" to null, "status" to "waiting")
                    else -> emptyMap()
                }

                val player1 = snapshot.get("player1")
                val player2 = snapshot.get("player2")

                val isPlayer1Leaving = playerNumber == 1
                val isPlayer2Leaving = playerNumber == 2

                val willRoomBeEmpty = when {
                    isPlayer1Leaving && player2 == null -> true
                    isPlayer2Leaving && player1 == null -> true
                    else -> false
                }

                if (willRoomBeEmpty) {
                    transaction.delete(room)
                } else {
                    //             transaction.update(room, updatedData) TODO
                    transaction.delete(room)
                }
            }.await()
        } catch (e: Exception) { }
    }

    override fun getRoomLiveData(roomId: String): LiveData<MultiplayerRoomDto?> {
        val liveData = MutableLiveData<MultiplayerRoomDto?>()
        rooms.document(roomId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                liveData.postValue(null)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                liveData.postValue(snapshot.toObject(MultiplayerRoomDto::class.java))
            } else {
                liveData.postValue(null)
            }
        }
        return liveData
    }

    override suspend fun finishRoom(roomId: String) {
        try {
            rooms.document(roomId)
                .update("status", "finished")
                .await()
        } catch (e: Exception) {
        }
    }
}