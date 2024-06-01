package su.afk.notificationapp.notification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // update new token to post my server
        // когда приходит новый токен его нужно отправить к себе на бэк для привязки к юзеру
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // respond to received message

    }
}