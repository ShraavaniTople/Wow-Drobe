package app.wowdrobe.com.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.wowdrobe.com.R
import app.wowdrobe.com.UserDatastore
import app.wowdrobe.com.firebase.firestore.ProfileInfo
import app.wowdrobe.com.navigation.Screens
import app.wowdrobe.com.ui.theme.backGround
import app.wowdrobe.com.ui.theme.monteExtraBold
import app.wowdrobe.com.ui.theme.monteSB
import app.wowdrobe.com.ui.theme.textColor
import app.wowdrobe.com.ui.theme.yellow
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects
import kotlinx.coroutines.launch
import kotlin.random.Random


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WOnBoardingScreen(
    paddingValues: PaddingValues,
    navHostController: NavController,
) {
    val isLoginVisible = remember { mutableStateOf(false) }
    val isRegistered = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val token = stringResource(R.string.default_web_client_id)
    var user by remember { mutableStateOf(Firebase.auth.currentUser) }
    val gso =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)
            .requestEmail().requestProfile()
            .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result ->
            user = result.user
        },
        onAuthError = {
            user = null
        }
    )
    var profileList by remember {
        mutableStateOf<List<ProfileInfo>?>(null)
    }
    val dataStore = UserDatastore(context)
    val coroutineScope = rememberCoroutineScope()
    JetFirestore(path = {
        collection("ProfileInfo")
    }, onRealtimeCollectionFetch = { value, _ ->
        profileList = value?.getListOfObjects()
    }) {
        LaunchedEffect(key1 = user) {
            if (user != null) {
                val registered = profileList?.any { it.email == user?.email }

                coroutineScope.launch {
                    dataStore.saveEmail(user?.email.toString())
                    dataStore.savePfp(user?.photoUrl.toString())
                    dataStore.saveName(user?.displayName.toString())
                }
                navHostController.popBackStack()
                navHostController.navigate(
                    if (registered == true)
                        Screens.SettingUp.route else Screens.CompleteProfile.route
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backGround)
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backGround)
                    .then(
                        if (isLoginVisible.value) {
                            Modifier.blur(6.dp)
                        } else {
                            Modifier
                        }
                    )
                    .drawWithCache {
                        val gradient =
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    backGround.copy(
                                        0.5f
                                    )
                                ),
                                startY = size.height / 8.5f,
                                endY = size.height
                            )
                        onDrawWithContent {
                            drawContent()
                            drawRect(
                                gradient,
                                blendMode = BlendMode.Multiply
                            )
                        }
                    },
            ) {
                itemsIndexed(dummyTasks) { _, _ ->
                    Row(modifier = Modifier.basicMarquee()) {
                        repeat(30) {
                            val task = remember {
                                mutableStateOf(dummyTasks[Random.nextInt(0, dummyTasks.size - 1)])
                            }
                            MarqueeCard(taskItem = task.value)
                        }
                    }
                }

            }

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Spacer(modifier = Modifier.height(30.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.thrift),
                        contentDescription = null,
                        modifier = Modifier
                            .width(500.dp)
                            .height(650.dp),
                        tint = Color.Unspecified

                    )
                    Row(
                        modifier = Modifier
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = {
                                    launcher.launch(googleSignInClient.signInIntent)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.Transparent,
                                    contentColor = Color.White
                                ),
                                border = BorderStroke(1.dp, yellow.copy(alpha = 0.7f)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier,
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Row(
                                        modifier = Modifier,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.wowdrobe),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(40.dp),
                                            tint = Color.Unspecified
                                        )
                                        Spacer(modifier = Modifier.width(7.dp))
                                        Text(
                                            text = "Let's Start",
                                            fontSize = 19.sp,
                                            fontFamily = monteSB,
                                            fontWeight = FontWeight.ExtraBold,
                                            softWrap = true,
                                            modifier = Modifier.fillMaxWidth(0.75f),
                                            textAlign = TextAlign.Center,
                                            color = textColor.copy(0.85f),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}


@Composable
fun MarqueeCard(
    modifier: Modifier = Modifier,
    taskItem: app.wowdrobe.com.login.Tasks,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    Card(
        backgroundColor = Color.Transparent,
        modifier = modifier
            .width(200.dp)
            .height(80.dp)
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                onClick()
            },
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            1.dp,
            if (isSelected) Color.White else Color(0xFFABACAD).copy(alpha = 0.5f)
        ),
        elevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(if (isSelected) 1f else 0.5f)
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = taskItem.icon ?: R.drawable.wowdrobe),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(start = 10.dp),
                    tint = if (isSelected) taskItem.color else taskItem.color.copy(0.85f)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = taskItem.name,
                    fontSize = 18.sp,
                    fontFamily = monteExtraBold,
                    fontWeight = FontWeight.ExtraBold,
                    softWrap = true,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(horizontal = 10.dp),
                    textAlign = TextAlign.Center,
                    color = if (isSelected) taskItem.color else taskItem.color.copy(0.85f),
                )
            }
        }
    }
}
