package app.wowdrobe.com.aistudio

import android.content.Intent
import android.util.Log
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.wowdrobe.com.location.LocationViewModel
import app.wowdrobe.com.ui.theme.appBackground
import app.wowdrobe.com.ui.theme.monteSB
import app.wowdrobe.com.ui.theme.textColor

@Composable
fun NewChatTopBar(
    modifier: Modifier = Modifier,
    viewModel: LocationViewModel,
    onSave: () -> Unit = {},
    isAIImage: Boolean = false,
    navHostController: NavHostController
) {
    val texts = listOf("New Chat")
    val aiImage = listOf("AI Images")
    val context = LocalContext.current
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(appBackground)
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 20.dp,
                    bottom = 16.dp
                ),
        ) {

            Icon(
                imageVector = Icons.Sharp.ArrowBackIos,
                contentDescription = "Back",
                tint = textColor,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        navHostController.navigateUp()
                    }
            )
            if (isAIImage) {
                TypewriterText(
                    texts = aiImage,
                    text = "",
                    modifier = Modifier.padding(start = 30.dp),
                    delay = 50L
                )
            }
            if (!isAIImage) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Sharp.Bookmark,
                        contentDescription = "Bookmark",
                        tint = textColor,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                onSave()
                            }
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Icon(
                        imageVector = Icons.Outlined.Feedback,
                        contentDescription = "Share",
                        tint = textColor,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_SEND)
                                intent.putExtra(
                                    Intent.EXTRA_EMAIL,
                                    arrayOf("kailashps.1011@gmail.com")
                                )
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback:Evolve with AI")
                                intent.setType("message/rfc822")
                                context.startActivity(
                                    Intent.createChooser(
                                        intent,
                                        "Please share your feedback"
                                    )
                                )
                            }
                    )


                }
            }

        }
        Divider(
            thickness = 1.dp,
            color = textColor.copy(0.5f),
            modifier = Modifier.padding(vertical =4.dp)
        )
    }
}

@Composable
fun TypewriterText(
    modifier: Modifier = Modifier,
    texts: List<String>,
    text: String,
    delay: Long = 160,
    fontFamily: FontFamily = monteSB,
    fontSize: TextUnit = 20.sp,
    softWrap: Boolean = false,
    afterAnimation: () -> Unit = {},
) {
    var textIndex by remember {
        mutableStateOf(0)
    }
    var textToDisplay by remember {
        mutableStateOf("")
    }

    LaunchedEffect(
        key1 = texts,
    ) {
        try {
            while (textIndex < texts.size) {
                if (textIndex == texts.size - 1) {
                    delay(1000)
                    afterAnimation()
                }
                delay(1000)
                texts[textIndex].forEachIndexed { charIndex, _ ->
                    textToDisplay = texts[textIndex]
                        .substring(
                            startIndex = 0,
                            endIndex = charIndex + 1,
                        )
                    delay(delay)
                }
                textIndex = (textIndex + 1)
                delay(6000)
            }
        } catch (e: Exception) {
            Log.i("AfterAnimation", "AfterAnimationCalled")
            afterAnimation()
            Log.i("AfterAnimation", "AfterAnimationCallOver")
            textToDisplay = text
        }
    }

    Text(
        text = textToDisplay,
        fontSize = fontSize,
        modifier = modifier,
        fontFamily = fontFamily,
        softWrap = softWrap,
        color = textColor
    )
}


fun String.splitToCodePoints(): List<String> {
    return codePoints()
        .toList()
        .map {
            String(Character.toChars(it))
        }
}