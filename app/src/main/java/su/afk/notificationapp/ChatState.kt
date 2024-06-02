package su.afk.notificationapp

data class ChatState (
    val isEnterToken: Boolean = true, // экран ввода токена (диалог)
    val remoteToken: String = "", // токен устройства
    val messageText: String = "" // текст для уведомления
)