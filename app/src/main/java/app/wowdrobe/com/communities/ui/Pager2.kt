package app.wowdrobe.com.communities.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwipeUp
import androidx.compose.material.icons.sharp.PrivacyTip
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import app.wowdrobe.com.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import app.wowdrobe.com.communities.CommunitiesViewModel
import app.wowdrobe.com.components.permissions.Grapple
import app.wowdrobe.com.firebase.firestore.updateInfoToFirebase
import app.wowdrobe.com.profile.ProfileImage
import app.wowdrobe.com.reportwaste.DialogBox
import app.wowdrobe.com.ui.theme.CardColor
import app.wowdrobe.com.ui.theme.CardTextColor
import app.wowdrobe.com.ui.theme.appBackground
import app.wowdrobe.com.ui.theme.monteBold
import app.wowdrobe.com.ui.theme.monteSB
import app.wowdrobe.com.ui.theme.textColor
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class, ExperimentalMotionApi::class)
@Composable
fun Pager2(
    viewModel: CommunitiesViewModel,
    progress2: MutableState<Float>,
    padding: PaddingValues,
    allCommunities: List<DummyCards>?,
    name: String,
    email: String,
    userAddress: String,
    gender: String,
    phoneNumber: String,
    organization: String,
    pointsEarned: Int,
    pointsRedeemed: Int,
    noOfTimesReported: Int,
    noOfTimesCollected: Int,
    noOfTimesActivity: Int,
    communities: MutableState<MutableList<String>>,
    isMemberships: Boolean
) {
    val pagerState = rememberPagerState()
    val selectedItempage = remember { mutableStateOf(0) }
    Log.i("Selected item page", selectedItempage.value.toString())
    val contentPadding = PaddingValues(
        start = 40.dp, end = (40.dp), top = 30.dp, bottom = padding.calculateBottomPadding() + 15.dp
    )
    val register = remember { mutableStateOf(false) }
    val currentTitle = remember { mutableStateOf("") }
    val context = LocalContext.current
    var isCOinVisible by remember {
        mutableStateOf(false)
    }

    allCommunities?.let { community ->
        Box(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(
                verticalAlignment = Alignment.Top,
                count = community.size,
                state = pagerState,
                modifier = Modifier,
                contentPadding = if (viewModel.expandedState.value < 0.5f) {
                    contentPadding
                } else {
                    PaddingValues(0.dp)
                },
                userScrollEnabled = viewModel.expandedState.value < 0.5f,
            ) { page: Int ->
                val progressState = remember { mutableStateOf(0f) }
                val draggableState = rememberDraggableState(onDelta = { delta ->
                    val dragProgress = -delta / 200
                    val newProgress = progressState.value + dragProgress
                    progressState.value = newProgress.coerceIn(0f, 1f)
                    viewModel.expandedState.value = newProgress.coerceIn(0f, 1f)
                    viewModel.currentPage.value = pagerState.currentPage
                })
                val draggableState2 = rememberDraggableState(onDelta = { delta ->
                    val dragProgress = -delta / 200
                    val newProgress = progress2.value + dragProgress
                    progress2.value = newProgress.coerceIn(0f, 1f)
                    viewModel.expandedState2.value = newProgress.coerceIn(0f, 1f)
                    viewModel.currentPage.value = pagerState.currentPage
                })


                val motionScene = remember {
                    context.resources.openRawResource(R.raw.motion_scene).readBytes()
                        .decodeToString()
                }
                val motionScene2 = remember {
                    context.resources.openRawResource(R.raw.scene2).readBytes().decodeToString()
                }

                MotionLayout(
                    motionScene = MotionScene(
                        content = if (viewModel.expandedState.value < 0.9f) {
                            motionScene
                        } else {
                            motionScene2
                        }
                    ),
                    progress = if (viewModel.expandedState.value < 0.9f) {
                        progressState.value
                    } else {
                        progress2.value
                    },
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Card(
                        backgroundColor =
                        CardColor, border = BorderStroke(
                            width = 1.dp,
                            color = Color(0xFFE8A87C).fromHex(community[page].cardColor)
                        ), shape = RoundedCornerShape(40.dp), modifier = Modifier
                            .layoutId("card")
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 0.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    ProfileImage(
                                        imageUrl = community[page].image,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                            .fillMaxHeight(if (viewModel.expandedState.value < 0.9f) 0.35f else 1f)
                                            .clip(RoundedCornerShape(30.dp))
                                            .draggable(
                                                state = draggableState,
                                                orientation = Orientation.Vertical,
                                                startDragImmediately = true,
                                            )
                                            .then(
                                                if (viewModel.expandedState.value < 0.9f)
                                                    Modifier
                                                else Modifier.rotate(-90f)
                                            ),
                                        contentScale = ContentScale.FillBounds,
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = community[page].name,
                                    color = CardTextColor,
                                    fontSize = 20.sp,
                                    fontFamily = monteBold,
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    softWrap = true
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Date of Establishment",
                                    color = Color.Gray,
                                    fontSize = 12.sp,
                                    fontFamily = monteBold,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = community[page].dateOfEstablishment,
                                    color = CardTextColor,
                                    fontSize = 15.sp,
                                    fontFamily = monteBold,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Active Region",
                                    color = Color.Gray,
                                    fontSize = 12.sp,
                                    fontFamily = monteBold,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = community[page].activeRegion,
                                    color = CardTextColor,
                                    fontSize = 15.sp,
                                    fontFamily = monteBold,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "No of Members",
                                            color = Color.Gray,
                                            fontSize = 12.sp,
                                            fontFamily = monteBold,
                                            modifier = Modifier.padding(horizontal = 10.dp)
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = community[page].members.toString(),
                                            color = CardTextColor,
                                            fontSize = 15.sp,
                                            fontFamily = monteBold,
                                            modifier = Modifier.padding(horizontal = 10.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Rating",
                                            color = Color.Gray,
                                            fontSize = 12.sp,
                                            fontFamily = monteBold,
                                            modifier = Modifier.padding(horizontal = 10.dp)
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = "${community[page].ratings} / 5",
                                            color = CardTextColor,
                                            fontSize = 15.sp,
                                            fontFamily = monteBold,
                                            modifier = Modifier.padding(horizontal = 10.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = {
                                        register.value = true
                                        currentTitle.value = community[page].name
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = appBackground,
                                        contentColor = textColor,
                                        disabledBackgroundColor = appBackground
                                    ),
                                    shape = RoundedCornerShape(35.dp),
                                    modifier = Modifier.padding(start = 10.dp),
                                    enabled = !isMemberships
                                ) {
                                    Text(
                                        text = if(isMemberships) "Registered" else "Register",
                                        color = textColor,
                                        fontSize = 12.sp,
                                        fontFamily = monteSB,
                                        modifier = Modifier.padding(bottom = 4.dp),
                                        maxLines = 1,
                                        softWrap = true,
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .draggable(
                                            state = draggableState,
                                            orientation = Orientation.Vertical,
                                            startDragImmediately = true,
                                        ),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.SwipeUp,
                                        contentDescription = null,
                                        tint = CardTextColor,
                                        modifier = Modifier
                                            .padding(bottom = 4.dp)
                                            .draggable(
                                                state = draggableState,
                                                orientation = Orientation.Vertical,
                                                startDragImmediately = true,
                                            )
                                            .size(30.dp),
                                    )

                                }
                            }

                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .layoutId("bottomSheet")
                            .draggable(
                                state = draggableState2,
                                orientation = Orientation.Vertical,
                                startDragImmediately = true,
                            )
                            .padding(10.dp),
                        backgroundColor = CardColor,
                        elevation = 10.dp,
                        shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .draggable(
                                            state = draggableState2,
                                            orientation = Orientation.Vertical,
                                            startDragImmediately = true,
                                        ),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Grapple(
                                        modifier = Modifier
                                            .padding(bottom = 0.dp)
                                            .requiredHeight(20.dp)
                                            .requiredWidth(55.dp)
                                            .alpha(0.22f), color = CardTextColor
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 10.dp)
                                        .draggable(
                                            state = draggableState2,
                                            orientation = Orientation.Vertical,
                                            startDragImmediately = true,
                                        ),
                                ) {
                                    Text(
                                        text = "Drives Conducted",
                                        fontSize = 21.sp,
                                        color = CardTextColor,
                                        fontWeight = FontWeight.Normal,
                                        modifier = Modifier.padding(start = 14.dp)
                                    )
                                }
                                val sortedDrives = remember {
                                    community[page].drives.sortedWith(compareByDescending<Drive> {
                                        it.status == DriveStatus.UPCOMING // Sort by upcoming drives first
                                    }.thenByDescending {
                                        it.status == DriveStatus.PASSED // Then sort by passed drives
                                    })
                                }
                                LazyCard(
                                    list = sortedDrives,
                                    viewModel = viewModel,
                                )
                            }
                        }


                    }

                }
                if (register.value) {
                    DialogBox(
                        isVisible = register.value,
                        title = "Do you want to register for this Community?",
                        description = "We will share your name and contact details with the Community",
                        tint = textColor,
                        icon = Icons.Sharp.PrivacyTip,
                        successRequest = {
                            isCOinVisible = true
                            communities.value.add(community[pagerState.currentPage].name)
                            updateInfoToFirebase(
                                context,
                                name = name,
                                email = email,
                                phoneNumber = phoneNumber,
                                gender = gender,
                                organization = organization,
                                address = userAddress,
                                pointsEarned = pointsEarned,
                                pointsRedeemed = pointsRedeemed,
                                noOfTimesReported = noOfTimesReported,
                                noOfTimesCollected = noOfTimesCollected + 1,
                                noOfTimesActivity = noOfTimesActivity,
                                communities = communities.value
                            )
                            register.value = false
                        },
                        dismissRequest = {
                            register.value = false
                        }
                    )
                }
            }
            if (isCOinVisible) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    val currenanim by rememberLottieComposition(
                        spec = LottieCompositionSpec.Asset("confetti.json")
                    )
                    LottieAnimation(
                        composition = currenanim,
                        iterations = 1,
                        contentScale = ContentScale.Crop,
                        speed = 1f,
                        modifier = Modifier
                            .fillMaxSize()
                            .size(250.dp)
                    )
                }

            }
            LaunchedEffect(key1 = isCOinVisible){
                if (isCOinVisible){
                    delay(2000)
                    isCOinVisible = false
                }
            }

        }
    }

}








