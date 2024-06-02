package su.afk.notificationapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import su.afk.notificationapp.components.ChatScreen
import su.afk.notificationapp.components.DialogEnterToken
import su.afk.notificationapp.ui.theme.NotificationAppTheme

class FirebasePushActivity : ComponentActivity() {

    private val viewModel: ChatViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //запрашиваем разрещение на уведомление
        requestNotificationPermission()

        setContent {
            NotificationAppTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val state = viewModel.state
                    // Состояние ввода токена
                    if(state.isEnterToken) {
                        DialogEnterToken(
                            token = state.remoteToken,
                            onTokenChange = viewModel::onRemoteTokenChange,
                            onSubmit = viewModel::onSubmitRemoteToken
                        )
                        // Иначе находимся на экране чата
                    } else {
                        ChatScreen(
                            messageText = state.messageText,
                            onMessageChange = {
                                viewModel::onMessageChange
                            },
                            onMessageBroadcast = {
                                viewModel.sendMessage(isBroadcast = true)
                            },
                            onMessageSend = {
                                viewModel.sendMessage(isBroadcast = false)
                            }
                        )
                    }

                }
            }
        }
    }

    private fun requestNotificationPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED // если есть разрешение на уведомления

            // Если нету запрашиваем
            if(!permissionCheck) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }
}
