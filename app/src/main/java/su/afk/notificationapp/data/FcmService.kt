package su.afk.notificationapp.data

import retrofit2.http.Body
import retrofit2.http.POST
import su.afk.notificationapp.model.SendMessageDto

interface FcmService {

    @POST("/send")
    suspend fun sendMessage(
        @Body body: SendMessageDto
    )

    @POST("/broadcast")
    suspend fun broadcast(
        @Body body: SendMessageDto
    )
}