package com.example.kkam_backup.util

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.kkam_backup.R

object NotificationHelper {

    const val CHANNEL_ID = "alert_channel"

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "이상행동 알림",
                NotificationManager.IMPORTANCE_HIGH          // heads-up
            ).apply {
                description = "첫번째 클래스 90% 이상 시 팝업 알림"
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 300, 100, 300)
                setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                )
            }
            context.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }

    fun showHeadsUp(
        context: Context,
        id: Int,
        title: String,
        message: String,
        fullScreenIntent: Intent? = null
    ) {
        val pending = PendingIntent.getActivity(
            context,
            id,
            fullScreenIntent ?: Intent(context, fullScreenIntent?.javaClass ?: context.javaClass),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_store_alert)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pending)
            .setPriority(NotificationCompat.PRIORITY_MAX)            // ← 포그라운드 헤드업
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setFullScreenIntent(pending, true)
            .build()

        NotificationManagerCompat.from(context).notify(id, notif)
    }
}
