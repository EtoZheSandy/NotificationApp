package su.afk.notificationapp.model

data class SendMessageDto(
    val token: String?,
    val notification: NotificationBody
)

data class NotificationBody(
    val title: String,
    val body: String
)
