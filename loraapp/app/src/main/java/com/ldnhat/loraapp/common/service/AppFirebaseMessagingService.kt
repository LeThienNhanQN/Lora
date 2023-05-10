package com.ldnhat.loraapp.common.service

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ldnhat.loraapp.R
import com.ldnhat.loraapp.utils.constants.Constants

class AppFirebaseMessagingService : FirebaseMessagingService() {
    private val CHANNEL_ID = "push_notification_id"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("TOKEN", token)
        val sharedPref = getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(Constants.DEVICE_TOKEN, token).apply()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val notification: RemoteMessage.Notification? = message.notification
        val title = notification?.title
        val msg = notification?.body
        Log.d("msg: ", msg.toString())
        sendNotification(title, msg)
    }

    private fun sendNotification(title: String?, msg: String?) {
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this@AppFirebaseMessagingService, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.mipmap.ic_launcher)

        val notification: Notification = notificationBuilder.build()
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }
}