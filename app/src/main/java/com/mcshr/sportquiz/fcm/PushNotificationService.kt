package com.mcshr.sportquiz.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mcshr.sportquiz.R

class PushNotificationService: FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title?:""
        val msg = message.notification?.body?:""
        showNotification(title, msg)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("TOKEN", "Refreshed token: $token")

    }

    private fun showNotification(title: String, msg: String){
        val channelId = "default_channel_id"
        val chanelName = "default_channel_name"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            chanelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(title)
            .setContentText(msg)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}