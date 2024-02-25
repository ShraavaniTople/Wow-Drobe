package app.wowdrobe.com.communities.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.wowdrobe.com.firebase.firestore.ProfileInfo
import app.wowdrobe.com.ui.theme.appBackground
import app.wowdrobe.com.ui.theme.textColor
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects
import app.wowdrobe.com.communities.CommunitiesViewModel
import app.wowdrobe.com.newcommunities.CommunitiesScreen
import app.wowdrobe.com.newcommunities.CommunityInfo
import app.wowdrobe.com.newcommunities.Form
import app.wowdrobe.com.profile.ProfileImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CommunitiesSection(
    name: String,
    paddingValues: PaddingValues,
    email: String
) {

    val viewModel: CommunitiesViewModel = remember { CommunitiesViewModel() }
    var profileList by remember {
        mutableStateOf<List<ProfileInfo>?>(null)
    }
    var userAddress by remember {
        mutableStateOf("")
    }
    var phoneNumber by remember {
        mutableStateOf("")
    }
    var gender by remember {
        mutableStateOf("")
    }
    var organization by remember {
        mutableStateOf("")
    }
    var pointsEarned by remember {
        mutableStateOf(0)
    }
    var pointsRedeemed by remember {
        mutableStateOf(0)
    }
    var noOfTimesReported by remember {
        mutableStateOf(0)
    }
    var noOfTimesCollected by remember {
        mutableStateOf(0)
    }
    var noOfTimesActivity by remember {
        mutableStateOf(0)
    }
    var communities by remember { mutableStateOf<List<DummyCards>?>(null) }
    var allCommunities by remember { mutableStateOf<List<DummyCards>?>(null) }
    var isMemberShips by remember { mutableStateOf(false) }
    var isLaunched by remember { mutableStateOf(false) }
    var myCommunities = remember {
        mutableStateOf(mutableListOf(""))
    }
    var isFormVisible = remember {
        mutableStateOf(false)
    }
    var selectedType by remember { mutableStateOf(TypeOfCommunities.ALL_COMMUNITIES.names) }
    LaunchedEffect(key1 = Unit) {
        isLaunched = true
    }
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true,
        animationSpec = tween(
            durationMillis = 250,
            delayMillis = 0,
            easing = FastOutLinearInEasing
        )
    )

    LaunchedEffect(key1 = modalSheetState.isVisible){
        if(!modalSheetState.isVisible){
            isFormVisible.value = false
        }
    }
    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetContent = {
            if (isFormVisible.value) {
                Form(modalSheetState)
            } else {
                CommunityInfo(communitiesViewModel = viewModel)
            }
        }
    ) {
        JetFirestore(path = {
            collection("Communities")
        }, onRealtimeCollectionFetch = { values, _ ->
            communities = values?.getListOfObjects()
            allCommunities = values?.getListOfObjects()

        }) {
            JetFirestore(path = {
                collection("ProfileInfo")
            }, onRealtimeCollectionFetch = { value, _ ->
                profileList = value?.getListOfObjects()
            }) {
                if (profileList != null) {
                    for (i in profileList!!) {
                        if (i.email == email) {
                            userAddress = i.address ?: ""
                            gender = i.gender ?: ""
                            phoneNumber = i.phoneNumber ?: ""
                            organization = i.organization ?: ""
                            pointsEarned = i.pointsEarned
                            pointsRedeemed = i.pointsRedeemed
                            noOfTimesReported = i.noOfTimesReported
                            noOfTimesCollected = i.noOfTimesCollected
                            noOfTimesActivity = i.noOfTimesActivity
                            myCommunities.value = i.communities.toMutableList()
                            if (isLaunched) {
                                allCommunities = communities?.filter {
                                    it.name !in myCommunities.value
                                }
                                isLaunched = false
                            }
                        }
                    }
                }
                LaunchedEffect(key1 = selectedType) {
                    when (selectedType) {
                        TypeOfCommunities.ALL_COMMUNITIES.names -> {
                            allCommunities = communities?.filter {
                                it.name !in myCommunities.value
                            }
                            isMemberShips = false
                        }

                        TypeOfCommunities.Memberships.names -> {
                            allCommunities =
                                communities?.filter { it.name in myCommunities.value }
                            isMemberShips = true
                        }

                    }
                }

                var progress2 = remember { mutableStateOf(0f) }
                val visible =
                    animateFloatAsState(
                        if (progress2.value > 0.35f) 1f else 0f,
                        label = ""
                    ).value
                val inVisible =
                    animateFloatAsState(
                        if (progress2.value > 0.35f) 0f else 1f,
                        label = ""
                    ).value
                val isLoading = remember { mutableStateOf(false) }
                var expanded by remember { mutableStateOf(false) }

                LaunchedEffect(key1 = allCommunities) {
                    if (allCommunities?.isEmpty() == true || allCommunities == null) {
                        isLoading.value = true
                    }
                }
                LaunchedEffect(key1 = isLoading.value) {
                    if (isLoading.value) {
                        delay(500)
                        isLoading.value = false
                    }
                }
                if (isLoading.value) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        val currenanim by rememberLottieComposition(
                            spec = LottieCompositionSpec.Asset("loading.json")
                        )
                        LottieAnimation(
                            composition = currenanim,
                            iterations = Int.MAX_VALUE,
                            contentScale = ContentScale.Crop,
                            speed = 1f,
                            modifier = Modifier
                                .size(300.dp)
                        )
                    }
                } else {
                    allCommunities?.let { community ->
                        Column(
                            modifier = Modifier
                                .background(appBackground)
                        ) {
                            AnimatedVisibility(
                                visible = viewModel.expandedState.value < 0.5f,
                                enter = expandVertically(tween(400)) + fadeIn(tween(400)),
                                exit = shrinkVertically(tween(400)) + fadeOut(tween(400)),
                                modifier = Modifier.background(Color.Transparent)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 35.dp,
                                            bottom = 35.dp,
                                            start = 20.dp,
                                            end = 20.dp
                                        ),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.clickable {
                                            expanded = true
                                        },
                                    ) {
//                                    ExposedDropdownMenuBox(
//                                        modifier = Modifier,
//                                        expanded = expanded,
//                                        onExpandedChange = { expanded = it }
//                                    ) {
//                                        Row(
//                                            verticalAlignment = Alignment.Bottom,
//                                            modifier = Modifier,
//                                        ) {
//                                            Text(
//                                                text = selectedType,
//                                                color = textColor,
//                                                fontSize = 30.sp,
//                                                fontFamily = monteBold,
//                                            )
//                                            Icon(
//                                                imageVector = Icons.Default.ArrowDropDown,
//                                                contentDescription = null,
//                                                tint = textColor,
//                                                modifier = Modifier
//                                                    .size(25.dp)
//                                                    .padding(bottom = 7.dp)
//                                            )
//                                        }
//
//                                        DropdownMenu(
//                                            expanded = expanded,
//                                            onDismissRequest = { expanded = false },
//                                            modifier = Modifier
//                                                .weight(1f)
//                                                .background(appBackground)
//                                        ) {
//                                            typeOfCommunities.forEach { type ->
//                                                DropdownMenuItem(
//                                                    onClick = {
//                                                        selectedType = type
//                                                        expanded = false
//                                                    }
//                                                ) {
//                                                    Text(
//                                                        text = type,
//                                                        color = textColor
//                                                    )
//                                                }
//                                            }
//                                        }
//                                    }
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 15.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            modifier = Modifier,
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
//                                        Icon(
//                                            painter = painterResource(id = R.drawable.coins),
//                                            contentDescription = "coins",
//                                            modifier = Modifier
//                                                .size(30.dp)
//                                                .padding(end = 5.dp),
//                                            tint = Color.Unspecified
//                                        )
//                                        AutoResizedText(
//                                            text = pointsEarned.toString(),
//                                            color = textColor,
//                                            fontSize = 15.sp,
//                                            softWrap = false,
//                                            fontFamily = monteNormal,
//                                        )
                                            Text(
                                                text = "Communities Events",
                                                color = textColor,
                                                fontSize = 30.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }
                            AnimatedVisibility(
                                visible = viewModel.expandedState.value > 0.5f,
                                enter = expandVertically() + fadeIn(),
                                exit = shrinkVertically() + fadeOut(),
                                modifier = Modifier.background(Color.Transparent)
                            ) {
                                if (progress2.value > 0.5f) {
                                    AnimatedVisibility(
                                        visible = visible > 0f,
                                        enter = fadeIn() + slideInHorizontally(),
                                        exit = fadeOut() + slideOutHorizontally(),
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    top = 30.dp,
                                                    end = 25.dp
                                                )
                                                .graphicsLayer {
                                                    alpha = visible
                                                },
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Card(
                                                backgroundColor =
                                                Color(0xFFE91E63).fromHex(
                                                    community[viewModel.currentPage.value].cardColor
                                                ),
                                                border = BorderStroke(
                                                    width = 1.dp,
                                                    color = Color(0xFFE91E63).fromHex(
                                                        community[viewModel.currentPage.value].cardColor
                                                    )
                                                ),
                                                shape = RoundedCornerShape(30.dp),
                                                modifier = Modifier
                                                    .width(160.dp)
                                                    .height(100.dp)
                                            ) {
                                                ProfileImage(
                                                    imageUrl = community[viewModel.currentPage.value].image,
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .clip(RoundedCornerShape(30.dp)),
                                                )
                                            }
                                            Text(
                                                text = community[viewModel.currentPage.value].name,
                                                fontSize = 21.sp,
                                                fontWeight = FontWeight.Bold,
                                                softWrap = true,
                                                modifier = Modifier.padding(start = 0.dp),
                                                color = textColor,

                                                )

                                        }
                                    }


                                } else {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()

                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    top = 30.dp,
                                                    start = 10.dp,
                                                    end = 25.dp
                                                ),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = community[viewModel.currentPage.value].name,
                                                fontSize = 25.sp,
                                                fontWeight = FontWeight.Bold,
                                                softWrap = true,
                                                modifier = Modifier
                                                    .graphicsLayer {
                                                        alpha = inVisible
                                                    },
                                                color = textColor,

                                                )

                                        }
                                        Spacer(modifier = Modifier.height(20.dp))
                                    }
                                }

                            }
                            CommunitiesScreen(
                                viewModel,
                                progress2,
                                paddingValues,
                                allCommunities,
                                name,
                                email,
                                userAddress,
                                gender,
                                phoneNumber,
                                organization,
                                pointsEarned,
                                pointsRedeemed,
                                noOfTimesReported,
                                noOfTimesCollected,
                                noOfTimesActivity,
                                myCommunities,
                                isMemberShips,
                                modalSheetState,
                                isFormVisible
                            )
                        }
                        Log.i("ExpandedState", "HomeScreen: ${viewModel.expandedState.value}")

                    }
                }
                val coroutineScope = rememberCoroutineScope()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 100.dp, end = 20.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Card(shape = CircleShape, backgroundColor = textColor) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            tint = appBackground,
                            modifier = Modifier
                                .size(35.dp)
                                .padding(5.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    isFormVisible.value = true
                                    coroutineScope.launch {
                                        if (modalSheetState.isVisible) {
                                            // Hide the bottom sheet
                                            modalSheetState.hide()
                                            isFormVisible.value = false
                                        } else {
                                            // Show the bottom sheet
                                            modalSheetState.show()


                                        }
                                    }
                                }
                        )
                    }
                }
            }
        }
    }


}








