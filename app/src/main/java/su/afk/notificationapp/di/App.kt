package su.afk.notificationapp.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import su.afk.notificationapp.MainActivity
import su.afk.notificationapp.notification.NotificationService.Companion.CHANNEL_ID
import su.afk.notificationapp.notification.NotificationService.Companion.CHANNEL_NAME

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        createChannelNotification()
    }

    fun createChannelNotification(){
        val channel = NotificationChannel(
            CHANNEL_ID, // Id канала
            CHANNEL_NAME,  // Name канала
            NotificationManager.IMPORTANCE_DEFAULT // Важность уведомлений
        )
        channel.description = "Канал для важных уведомлений"

        // менеджер уведмолений для создание канала уведомления
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}