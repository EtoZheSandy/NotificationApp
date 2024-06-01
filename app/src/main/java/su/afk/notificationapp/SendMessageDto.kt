package su.afk.notificationapp

import android.app.Notification

data class SendMessageDto(
    val token: String?,
    val notification: NotificationBody
)

data class NotificationBody(
    val title: String,
    val body: String
)
