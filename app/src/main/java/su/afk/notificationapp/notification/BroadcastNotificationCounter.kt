package su.afk.notificationapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BroadcastNotificationCounter: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val service = NotificationService(context = context)
        Counter.value++
        service.showNotification(text = Counter.value.toString()) // вызываем уведомление
    }

}