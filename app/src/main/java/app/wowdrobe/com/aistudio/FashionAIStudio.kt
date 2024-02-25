package app.wowdrobe.com.aistudio

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import app.wowdrobe.com.R
import app.wowdrobe.com.location.ImageState
import app.wowdrobe.com.location.LocationViewModel
import app.wowdrobe.com.ui.theme.appBackground
import app.wowdrobe.com.ui.theme.appGradient
import app.wowdrobe.com.ui.theme.indigo
import app.wowdrobe.com.ui.theme.lightGray
import app.wowdrobe.com.ui.theme.monteNormal
import app.wowdrobe.com.ui.theme.monteSB
import app.wowdrobe.com.ui.theme.textColor
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

import kotlinx.coroutines.launch

@Composable
fun FashionAIStudio(
    viewModel: LocationViewModel,
    navHostController: NavHostController,
) {
    val imageState by viewModel.imageState.collectAsState()
    var text = remember {
        mutableStateOf(TextFieldValue(""))
    }
    var isRecording = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val isAnimationVisible = remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            NewChatTopBar(
                modifier = Modifier.then(if (isAnimationVisible.value) Modifier.blur(10.dp) else Modifier),
                viewModel = viewModel,
                navHostController = navHostController,
                onSave = {

                },
                isAIImage = true,
            )
        },
        bottomBar = {
            NewChatBottomBar(
                modifier = Modifier.then(if (isAnimationVisible.value) Modifier.blur(10.dp) else Modifier),
                navController = navHostController,
                viewModel = viewModel,
                text = text.value,
                onTextChange = {
                    text.value = it
                },
                onSpeechToText = {

                },
                onClick = {
                    viewModel.imagePrompt.value = text.value.text
                    viewModel.fetchImage(text.value.text)
                    text.value = TextFieldValue("")

                }
            )
        }
    ) {
        println(it)
        Box {
            println(it)
            if (imageState is ImageState.NotStarted) {
                Column(
                    modifier = Modifier
                        .background(appBackground)
                        .fillMaxSize()
                        .padding(top = 150.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = monteNormal,
                                color = indigo,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Black
                            )
                        ) {
                            append("Let's Generate ")
                        }
                        append("\n")
                        withStyle(
                            SpanStyle(
                                fontFamily = monteNormal,
                                color = textColor,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append("A Perfect outfit for you !")
                        }
                    })
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "I'm your assistant.",
                        color = textColor,
                        fontFamily = monteSB
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    RepeatedCard(
                        icon = R.drawable.beta,
                        description = "I'm still under development"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    RepeatedCard(
                        icon = R.drawable.remember,
                        description = "I can remember our conversations"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    RepeatedCard(
                        icon = R.drawable.privacy,
                        description = "Your Privacy is completely secured"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    RepeatedCard(
                        icon = R.drawable.tip,
                        description = "Images are generated using AI"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreHoriz,
                            contentDescription = "more",
                            tint = textColor.copy(0.45f),
                            modifier = Modifier.size(40.dp)
                        )

                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .background(appBackground)
                        .fillMaxSize()
                        .then(if (isAnimationVisible.value) Modifier.blur(10.dp) else Modifier)
                ) {

                    when (imageState) {
                        is ImageState.Loading -> {
                            // Display loading indicator or message
                            isAnimationVisible.value = true
                        }
                        is ImageState.Loaded -> {
                            // Display the loaded image
                            isAnimationVisible.value = false
                            val imageData = (imageState as ImageState.Loaded).imageData
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                OpenableImage(
                                    imageUrl = imageData,
                                    contentScale = ContentScale.Crop,
                                ) {
                                    byteArrayToBitmap(imageData)?.let { it1 ->
                                        saveBitmapImage(
                                            it1,
                                            context = context
                                        )
                                    }
                                    Toast.makeText(
                                        context,
                                        "Image Saved to Gallery",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        is ImageState.Error -> {
                            isAnimationVisible.value = false
                            // Handle error state
                            val error = (imageState as ImageState.Error).exception
                            Text(text = "Error: ${error.message}")
                        }

                        else -> {
                            isAnimationVisible.value = false
                        }
                    }
                }
            }
            if (isAnimationVisible.value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val currenanim2 by rememberLottieComposition(
                        spec = LottieCompositionSpec.Asset("aiimage.json")
                    )
                    LottieAnimation(
                        composition = currenanim2,
                        iterations = Int.MAX_VALUE,
                        contentScale = ContentScale.Crop,
                        speed = 0.8f,
                        modifier = Modifier
                            .size(200.dp)
                    )
                }
            }

        }
    }
}


@Composable
fun RepeatedCard(
    icon: Int,
    description: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 19.dp,
                end = 19.dp,
                top = 7.dp,
                bottom = 7.dp
            ),
        backgroundColor = lightGray,
        shape = RoundedCornerShape(40.dp),
        elevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = description,
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .size(35.dp)

            )
            Text(
                text = description,
                color = textColor,
                fontSize = 15.sp,
                fontFamily = monteNormal,
                softWrap = true
            )

        }

    }
}

fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
    return try {
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null // Return null in case of an error
    }
}

@Composable
fun OpenableImage(
    modifier: Modifier = Modifier,
    imageUrl: Any? = null,
    downloadable: Boolean = true,
    contentScale: ContentScale,
    nameOfProfileImage: String? = null,
    onSave: () -> Unit = {}
) {
    Image(
        painter = rememberAsyncImagePainter(model = imageUrl),
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) { onSave() }
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.7f)
            .padding(30.dp)
            .clip(RoundedCornerShape(20.dp))
            .shadow(1.dp),
        contentScale = contentScale,
        contentDescription = null,

    )
}




/**Save Bitmap To Gallery
 * @param bitmap The bitmap to be saved in Storage/Gallery*/
fun saveBitmapImage(bitmap: Bitmap, context: Context) {
    val timestamp = System.currentTimeMillis()

    //Tell the media scanner about the new file so that it is immediately available to the user.
    val values = ContentValues()
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
    values.put(MediaStore.Images.Media.DATE_ADDED, timestamp)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        values.put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + "PalmApi")
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            try {
                val outputStream =  context.contentResolver.openOutputStream(uri)
                if (outputStream != null) {
                    try {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                        outputStream.close()
                    } catch (e: Exception) {
                        Log.e("TAG", "saveBitmapImage: ", e)
                    }
                }
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)

                Toast.makeText( context, "Saved...", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("TAG", "saveBitmapImage: ", e)
            }
        }
    } else {
        val imageFileFolder = File(Environment.getExternalStorageDirectory().toString() + '/' + "PalmApi")
        if (!imageFileFolder.exists()) {
            imageFileFolder.mkdirs()
        }
        val mImageName = "$timestamp.png"
        val imageFile = File(imageFileFolder, mImageName)
        try {
            val outputStream: OutputStream = FileOutputStream(imageFile)
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                Log.e("TAG", "saveBitmapImage: ", e)
            }
            values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            Toast.makeText( context, "Saved...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("TAG", "saveBitmapImage: ", e)
        }
    }
}



