package su.afk.notificationapp

import retrofit2.http.Body
import retrofit2.http.POST

interface FcmService {

    @POST("/send_token")
    suspend fun sendMessage(
        @Body body: SendMessageDto
    )

    @POST("/broadcast")
    suspend fun broadcast(
        @Body body: SendMessageDto
    )
}