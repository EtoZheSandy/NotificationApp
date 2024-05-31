package su.afk.notificationapp.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import su.afk.notificationapp.MainActivity
import su.afk.notificationapp.R

class NotificationService(
    private val context: Context
) {
    // Для запуска уведомления в канал
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//    private val notificationManager = NotificationManagerCompat.from(this)

    fun showNotification(text: String) {
        val intentActivity = Intent(context, MainActivity::class.java)  // intent для открытия активити из уведомления
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            intentActivity,
            PendingIntent.FLAG_IMMUTABLE
        ) // отложенное намерение

        val incrementIntent = PendingIntent.getBroadcast(
            context,
            2,
            Intent(context, BroadcastNotificationCounter::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        // создание уведмоления
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_offer)
            .setContentTitle("Вам пришел оффер")
            .setContentText("Возьмите меня на работу: $text")
            .setPriority(NotificationCompat.PRIORITY_HIGH) //зависит от того насколько важно уведомление, со звуком/в строке состояние/обычное
            .setContentIntent(activityPendingIntent)
            .addAction( // Кнопка при клике по которой
                R.drawable.ic_offer,
                "Добавить оффер",
                incrementIntent // запускается getBroadcast
            )
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }


    companion object{
        const val CHANNEL_ID = "ChannelId"
        const val CHANNEL_NAME = "ChannelName"
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }
}