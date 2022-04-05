package com.example.meet

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class InvitationService() : FirebaseMessagingService() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("InvitationService", "Message received")
        val notificationTitle = message.notification!!.title
        val notificationText = message.notification!!.body
        val NOTIFICATION_CHANNEL_ID = "INVITATION_NOTIFICATION"
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "New channel", NotificationManager.IMPORTANCE_DEFAULT)
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        val builder = Notification.Builder(this, NOTIFICATION_CHANNEL_ID).setContentTitle(notificationTitle).
            setContentText(notificationText).setSmallIcon(R.drawable.ic_launcher_background).setAutoCancel(true)
        NotificationManagerCompat.from(this).notify(1, builder.build())

        super.onMessageReceived(message)
    }
}