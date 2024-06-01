package su.afk.notificationapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class ChatViewModel(
//    api: FcmService
): ViewModel() {

    var state by mutableStateOf(ChatState())
        private set

    private val api: FcmService = Retrofit.Builder()
        .baseUrl("http://192.168.1.100:8080/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create()


    fun onRemoteTokenChange(newToken: String) {
        state = state.copy(
            remoteToken = newToken
        )
    }

    fun onSubmitRemoteToken() {
        state = state.copy(
            isEnterToken = false
        )
    }

    fun onMessageChange(message: String) {
        state = state.copy(
            messageText = message
        )
    }

    fun sendMessage(isBroadcast: Boolean) {
        viewModelScope.launch {
            val messageDto = SendMessageDto(
                token = if(isBroadcast) null else state.remoteToken,
                notification = NotificationBody(
                    title = "Новое сообщение",
                    body = state.messageText
                )
            )

            try {
                if (isBroadcast) {
                    api.broadcast(messageDto) // отпрвляем всем
                } else {
                    api.sendMessage(messageDto) // отпрвляем на конкретное устройство
                }

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