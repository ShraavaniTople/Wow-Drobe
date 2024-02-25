package app.wowdrobe.com.aistudio

import android.content.Intent
import android.util.Log
import android.view.ViewTreeObserver
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.streams.toList
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.sharp.ArrowBackIos
import androidx.compose.material.icons.sharp.Bookmark
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.wowdrobe.com.R
import app.wowdrobe.com.location.LocationViewModel
import app.wowdrobe.com.navigation.Screens
import app.wowdrobe.com.ui.theme.appBackground
import app.wowdrobe.com.ui.theme.blue
import app.wowdrobe.com.ui.theme.green
import app.wowdrobe.com.ui.theme.lightGray
import app.wowdrobe.com.ui.theme.monteSB
import app.wowdrobe.com.ui.theme.textColor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewChatBottomBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: LocationViewModel,
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    onSpeechToText: () -> Unit,
    onClick: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val isKeyboardOpen by keyboardAsState()
    Column {4
        Divider(thickness = 1.dp, color = textColor.copy(0.5f), modifier = Modifier.padding(vertical =4.dp))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(appBackground)
                .padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 0.dp,
                    bottom = 16.dp
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isKeyboardOpen == Keyboard.Closed) {
                Icon(
                    painter = painterResource(id = R.drawable.sparkle),
                    contentDescription = "Share",
                    tint = green,
                    modifier = Modifier
                        .size(35.dp)
                        .padding(end = 10.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.mic),
                    contentDescription = "Microphone",
                    tint = blue,
                    modifier = Modifier
                        .size(39.dp)
                        .padding(end = 10.dp)
                        .clickable {
                            onSpeechToText()
                        }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.Center
            ) {
                val containerColor = lightGray
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        onTextChange(it)
                    },
                    label = {
                        Text(text = "Ask me anything", color = textColor)
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = containerColor,
                        focusedIndicatorColor = containerColor,
                        unfocusedIndicatorColor = containerColor,
                        disabledIndicatorColor = containerColor
                        ),
                    modifier = Modifier
                        .padding(10.dp)

                )

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    Icons.Filled.Send,
                    contentDescription = "Send",
                    tint = blue,
                    modifier = Modifier
                        .size(39.dp)
                        .padding(end = 10.dp)
                        .clickable {
                            onClick()
                            keyboardController?.hide()
                        }
                )
            }


        }
    }
}

enum class Keyboard {
    Opened, Closed
}

@Composable
fun keyboardAsState(): State<Keyboard> {
    val keyboardState = remember { mutableStateOf(Keyboard.Closed) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = android.graphics.Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
                Keyboard.Opened
            } else {
                Keyboard.Closed
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}