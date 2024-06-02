package su.afk.notificationapp.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun DialogEnterToken(
    token: String,
    onTokenChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    // доступ к системному буферу обмена через локальный контекст Compose
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = { /*TODO*/ },
        properties = DialogProperties(
            dismissOnBackPress = false, // запрещаем закрывать диалог при клике назад
            dismissOnClickOutside = false // запрещаем закрывать диалог при клике вне области
        )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = token,
                onValueChange = onTokenChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Введите токен юзера")
                },
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            val localToken = Firebase.messaging.token.await() // await для ожидание получения ответа на этой строке
                            clipboardManager.setText(AnnotatedString(localToken)) // помещаем в буфер обмена

                            Toast.makeText(context, "Токен скопирован", Toast.LENGTH_LONG).show()
                        }
                    }
                ) {
                    Text(text = "Copy токен")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = onSubmit
                ) {
                    Text(text = "Send")
                }
            }
        }
    }
}