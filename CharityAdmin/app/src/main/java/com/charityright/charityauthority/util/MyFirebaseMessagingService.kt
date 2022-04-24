package com.charityright.charityauthority.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.charityright.charityauthority.AuthenticationActivity
import com.charityright.charityauthority.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        CustomSharedPref.init(applicationContext)
        CustomSharedPref.write("DeviceToken",p0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("msg", "onMessageReceived: " + remoteMessage.data)
        val intent = Intent(this, AuthenticationActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = "Default"

        val bm = BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_launcher)

        val builder: Notification.Builder = Notification.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(bm)
            .setContentTitle(remoteMessage.data["title"])
            .setContentText(remoteMessage.data["body"]).setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            manager.createNotificationChannel(channel)
        }
        manager.notify("Charity"+System.currentTimeMillis(), System.currentTimeMillis().toInt(), builder.build())
    }
}