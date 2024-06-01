package su.afk.notificationapp

data class ChatState (
    val isEnterToken: Boolean = true,
    val remoteToken: String? = "",
    val messageText: String = ""
)