package su.afk.notificationapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okio.IOException
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import su.afk.notificationapp.data.FcmService
import su.afk.notificationapp.model.NotificationBody
import su.afk.notificationapp.model.SendMessageDto

class ChatViewModel(
//    api: FcmService // в идеале di но подключать ради теста не стал
): ViewModel() {

    var state by mutableStateOf(ChatState())
        private set // запрещаем изменение из вне

    private val api: FcmService = Retrofit.Builder()
        .baseUrl("http://192.168.1.100:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create()

    init {
        viewModelScope.launch {
            // создаем и подписываемся на тему chat
            // что бы получать уведомления broadcast для всех устройств которые подписаны на этот топик/канал
            Firebase.messaging.subscribeToTopic("chat").await()
        }
    }

    // обновление токена через ввода dialog
    fun onRemoteTokenChange(newToken: String) {
        state = state.copy(
            remoteToken = newToken
        )
    }

    // меняем состояние ввода на окно chatScreen
    fun onSubmitRemoteToken() {
        state = state.copy(
            isEnterToken = false
        )
    }

    // текст для отправки уведомления
    fun onMessageChange(message: String) {
        state = state.copy(
            messageText = message
        )
    }

    // отправка сообщения
    fun sendMessage(isBroadcast: Boolean) {
        viewModelScope.launch {


            val messageNotification = SendMessageDto(
                // если isBroadcast false то берем токен и отправляем конкретному устройству
                // иначе token null и это уведомление ждя всех
                token = if(isBroadcast) null else state.remoteToken,
                notification = NotificationBody(
                    title = "Новое сообщение",
                    body = state.messageText
                )
            )

            try {
                if (isBroadcast) {
                    api.broadcast(messageNotification) // отпрвляем всем
                } else {
                    api.sendMessage(messageNotification) // отпрвляем на конкретное устройство
                }

                // сбрасываем стейт сообщения что бы отправить новое
                state = state.copy(
                    messageText = ""
                )
            } catch (e: HttpException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }



        }
    }
}