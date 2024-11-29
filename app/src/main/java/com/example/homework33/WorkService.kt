package com.example.homework33

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class WorkService : Service() {

    private val CHANNEL_ID = "work_service_channel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel() // Создание канала уведомлений
        startForeground(1, createNotification("Служба запущена!")) // Запуск уведомления
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Здесь можно добавить логику для работы сервиса
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Work Service Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for Work Service notifications"
            }

            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(content: String): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Работа")
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        // Здесь можно добавить код для завершения работы сервиса
    }
}