package com.example.kkam_backup.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHelper(private val context: Context) {

    private val manager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val channelId = "anomaly_channel"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "이상 행동 알림",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "무인 매장 이상 행동 알림 채널"
            }
            manager.createNotificationChannel(channel)
        }
    }

    fun sendAnomalyAlert() {
        val notification = NotificationCompat.Builder(context, channelId)
            .apply {
                // 시스템 기본 아이콘 사용 (프로젝트 내 리소스가 없을 경우)
                setSmallIcon(android.R.drawable.ic_dialog_alert)
                setContentTitle("이상 행동 감지")
                setContentText("매장에서 이상 행동이 감지되었습니다.")
                priority = NotificationCompat.PRIORITY_HIGH
                setAutoCancel(true)
            }
            .build()

        manager.notify(1001, notification)
    }
}