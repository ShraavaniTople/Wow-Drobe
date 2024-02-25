package app.wowdrobe.com.dashboard

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import app.wowdrobe.com.buyClothes.WasteItemCard
import app.wowdrobe.com.buyClothes.convertDistance
import app.wowdrobe.com.buyClothes.distance
import app.wowdrobe.com.buyClothes.getTimeAgo
import app.wowdrobe.com.R
import app.wowdrobe.com.communities.ui.DummyCards
import app.wowdrobe.com.firebase.firestore.ProfileInfo
import app.wowdrobe.com.firebase.firestore.WasteItem
import app.wowdrobe.com.location.LocationViewModel
import app.wowdrobe.com.navigation.Screens
import app.wowdrobe.com.profile.ProfileImage
import app.wowdrobe.com.rewards.LevelDialogBox
import app.wowdrobe.com.rewards.levels
import app.wowdrobe.com.ui.theme.appBackground
import app.wowdrobe.com.ui.theme.blue
import app.wowdrobe.com.ui.theme.green
import app.wowdrobe.com.ui.theme.indigo
import app.wowdrobe.com.ui.theme.lightGray
import app.wowdrobe.com.ui.theme.monteBold
import app.wowdrobe.com.ui.theme.monteNormal
import app.wowdrobe.com.ui.theme.monteSB
import app.wowdrobe.com.ui.theme.orange
import app.wowdrobe.com.ui.theme.textColor
import app.wowdrobe.com.ui.theme.yellow
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects
import kotlinx.coroutines.delay
import kotlin.system.exitProcess


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewDashboard(
    navController: NavHostController,
    viewModel: LocationViewModel,
    email: String,
    name: String,
    pfp: String
) {
    var profileList by remember {
        mutableStateOf<List<ProfileInfo>?>(null)
    }
    var address by remember {
        mutableStateOf("")
    }
    var animStart by remember {
        mutableStateOf(false)
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
    var isCOinVisible by remember {
        mutableStateOf(false)
    }
    var communities by remember { mutableStateOf<List<DummyCards>?>(null) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animStart) 0f else viewModel.currentProgress,
        label = "",
        animationSpec = tween(500)
    )
    LaunchedEffect(key1 = viewModel.showLevelDialog) {
        if (viewModel.showLevelDialog) {
            animStart = true
            delay(1000)
            animStart = false
        }
    }
    var allWastes by remember { mutableStateOf<List<WasteItem>?>(null) }
    var storedWastes by remember { mutableStateOf<List<WasteItem>?>(null) }
    val activity = (LocalContext.current as? Activity)
    BackHandler {
        activity?.finishAndRemoveTask()
        exitProcess(0)
    }
    JetFirestore(path = {
        collection("AllWastes")
    }, onRealtimeCollectionFetch = { values, _ ->
        allWastes = values?.getListOfObjects()
        storedWastes = values?.getListOfObjects()

    }) {
        JetFirestore(path = {
            collection("Communities")
        }, onRealtimeCollectionFetch = { values, _ ->
            communities = values?.getListOfObjects()
        }) {
            JetFirestore(path = {
                collection("ProfileInfo")
            }, onRealtimeCollectionFetch = { value, _ ->
                profileList = value?.getListOfObjects()
            }) {
                if (profileList != null) {
                    for (i in profileList!!) {
                        if (i.email == email) {
                            address = i.address ?: ""
                            gender = i.gender ?: ""
                            phoneNumber = i.phoneNumber ?: ""
                            organization = i.organization ?: ""
                            pointsEarned = i.pointsEarned
                            pointsRedeemed = i.pointsRedeemed
                        }
                    }
                }
                LaunchedEffect(key1 = pointsEarned) {
                    animStart = true
                    if (pointsEarned != 0) {
                        viewModel.pointsEarned = pointsEarned
                        viewModel.getCurrentLevel(pointsEarned, levels)
                        viewModel.currentLevel.value?.let {
                            viewModel.currentProgress = viewModel.getCurrentLevelProgress(
                                pointsEarned,
                                levels
                            )
                            viewModel.remainingPoints = viewModel.pointsRemainingForNextLevel(
                                pointsEarned,
                                levels
                            )
                            delay(1000)
                            animStart = false
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(appBackground)
                            .verticalScroll(rememberScrollState())
                            .then(
                                if (viewModel.showLevelDialog) Modifier.blur(10.dp) else Modifier
                            )
                    ) {
                        Card(
                            modifier = Modifier
                                .clip(RoundedCornerShape(0.dp, 0.dp, 50.dp, 50.dp))
                                .fillMaxWidth(),
                            backgroundColor = lightGray,

                            ) {
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 45.dp,
                                            bottom = 15.dp,
                                            end = 25.dp,
                                            start = 25.dp
                                        ),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Welcome Back",
                                            color = indigo,
                                            fontSize = 13.sp,
                                            fontFamily = monteSB,
                                            modifier = Modifier.padding(bottom = 7.dp)
                                        )
                                        Text(
                                            text = name,
                                            color = textColor,
                                            fontSize = 20.sp,
                                            fontFamily = monteBold,
                                            modifier = Modifier.padding(bottom = 7.dp)
                                        )
                                        Text(
                                            text = "What would you like to buy today ?",
                                            color = indigo,
                                            fontSize = 13.sp,
                                            fontFamily = monteSB,
                                            modifier = Modifier.padding(bottom = 7.dp)
                                        )
                                    }
                                    ProfileImage(
                                        imageUrl = pfp,
                                        modifier = Modifier
                                            .size(70.dp)
                                            .border(
                                                width = 1.dp,
                                                color = yellow,
                                                shape = CircleShape
                                            )
                                            .padding(2.dp)
                                            .clip(CircleShape)
                                            .clickable {
                                                navController.navigate(Screens.Profile.route)
                                            }
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 20.dp, start = 25.dp, end = 25.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(top = 15.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "Points Earned",
                                            color = orange,
                                            fontSize = 14.sp,
                                            fontFamily = monteBold,
                                            softWrap = true,
                                            modifier = Modifier.padding(start = 7.dp)
                                        )
                                        Row(modifier = Modifier.padding(end = 0.dp, top = 7.dp)) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.coins),
                                                contentDescription = "coins",
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .padding(end = 5.dp),
                                                tint = Color.Unspecified
                                            )
                                            Text(
                                                text = pointsEarned.toString(),
                                                color = textColor,
                                                fontSize = 15.sp,
                                                fontFamily = monteNormal,
                                            )
                                        }

                                    }
                                    Column(
                                        modifier = Modifier
                                            .padding(top = 15.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "Points Redeemed",
                                            color = orange,
                                            fontSize = 14.sp,
                                            fontFamily = monteBold,
                                            softWrap = true,
                                            modifier = Modifier.padding(start = 7.dp)
                                        )
                                        Row(modifier = Modifier.padding(end = 0.dp, top = 7.dp)) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.coins),
                                                contentDescription = "coins",
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .padding(end = 5.dp),
                                                tint = Color.Unspecified
                                            )
                                            Text(
                                                text = pointsRedeemed.toString(),
                                                color = textColor,
                                                fontSize = 15.sp,
                                                fontFamily = monteNormal,
                                            )
                                        }


                                    }
                                }
                                Spacer(modifier = Modifier.height(15.dp))
                            }
                        }



                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                                .clickable {
                                    viewModel.showLevelDialog = true
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.Start) {
                                Text(
                                    text = "Current Progress",
                                    color = indigo,
                                    fontSize = 20.sp,
                                    fontFamily = monteBold,
                                    modifier = Modifier.padding(bottom = 7.dp)
                                )
                                Text(
                                    text = "${viewModel.remainingPoints} more points to reach next level",
                                    color = textColor,
                                    fontSize = 9.sp,
                                    fontFamily = monteBold,
                                    modifier = Modifier.padding(start = 0.dp)
                                )
                            }

                            ArcComposable(
                                modifier = Modifier.padding(end = 25.dp),
                                text = "${(viewModel.currentProgress * 100).toInt()}%",
                                progress = animatedProgress
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                                .clickable {
                                    viewModel.showLevelDialog = true
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Try our AI Studio",
                                color = indigo,
                                fontSize = 20.sp,
                                fontFamily = monteBold,
                                modifier = Modifier.padding(bottom = 7.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(7.dp))

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 13.dp)
                                .clickable {
                                    navController.navigate(Screens.AIStudio.route)
                                },
                            value = "",
                            enabled = false,
                            onValueChange = {

                            },
                            label = {
                                Text(text = "Make the perfect outfit for you!", color = textColor)
                            },
                            trailingIcon = {
                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.wowdrobe),
                                        contentDescription = "Photos",
                                        tint = blue,
                                        modifier = Modifier
                                            .size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Icon(
                                        painter = painterResource(id = R.drawable.mic),
                                        contentDescription = "Microphone",
                                        tint = blue,
                                        modifier = Modifier
                                            .size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))


                                }
                            },
                            shape = RoundedCornerShape(20.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = lightGray,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))



                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                                .clickable {
                                    viewModel.showLevelDialog = true
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Explore our collection",
                                color = indigo,
                                fontSize = 20.sp,
                                fontFamily = monteBold,
                                modifier = Modifier.padding(bottom = 7.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                                .clickable {
                                    viewModel.showLevelDialog = true
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Topwear",
                                color = yellow,
                                fontSize = 14.sp,
                                fontFamily = monteBold,
                                modifier = Modifier.padding(bottom = 7.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(7.dp))

                        LazyRow() {
                            itemsIndexed(allWastes?.filter { wasteItem ->
                                wasteItem.tag.any { tagWithoutTips ->
                                    tagWithoutTips.name.equals("T-Shirts", ignoreCase = true) or tagWithoutTips.name.equals(
                                        "Tops",
                                        ignoreCase = true
                                    ) or tagWithoutTips.name.equals("Shirts", ignoreCase = true)
                                }
                            } ?: emptyList()) { index, wasteItem ->
                                WasteItemCard(
                                    modifier = Modifier.animateItemPlacement(),
                                    locationNo = "Location ${index + 1}",
                                    address = wasteItem.address,
                                    distance = "${
                                        convertDistance(
                                            distance(
                                                viewModel.latitude,
                                                viewModel.longitude,
                                                wasteItem.latitude,
                                                wasteItem.longitude
                                            )
                                        )
                                    } away",
                                    time = getTimeAgo(wasteItem.timeStamp),
                                    tags = wasteItem.tag.map {
                                        it.mapWithTips()
                                    },
                                    isList = true,
                                    imageUrlState = wasteItem.imagePath,
                                    isTagsVisble = false
                                ) {
                                    viewModel.locationNo.value = "Location ${index + 1}"
                                    viewModel.address.value = wasteItem.address
                                    viewModel.distance.value = "${
                                        convertDistance(
                                            distance(
                                                viewModel.latitude,
                                                viewModel.longitude,
                                                wasteItem.latitude,
                                                wasteItem.longitude
                                            )
                                        )
                                    } away"
                                    viewModel.time.value = getTimeAgo(wasteItem.timeStamp)
                                    viewModel.wastePhoto.value = wasteItem.imagePath
                                    viewModel.theirLatitude.value = wasteItem.latitude
                                    viewModel.theirLongitude.value = wasteItem.longitude
                                    viewModel.tags.value = wasteItem.tag.map {
                                        it.mapWithTips()
                                    }
                                    println("Collected time ${viewModel.time.value}")
                                    navController.navigate(Screens.CollectWasteInfo.route)
                                }

                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                                .clickable {
                                    viewModel.showLevelDialog = true
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Bottomwear",
                                color = yellow,
                                fontSize = 14.sp,
                                fontFamily = monteBold,
                                modifier = Modifier.padding(bottom = 7.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(7.dp))

                        LazyRow() {
                            itemsIndexed(allWastes?.filter { wasteItem ->
                                wasteItem.tag.any { tagWithoutTips ->
                                    tagWithoutTips.name.equals("Jeans", ignoreCase = true) or tagWithoutTips.name.equals(
                                        "Skirts",
                                        ignoreCase = true
                                    ) or tagWithoutTips.name.equals("Trousers", ignoreCase = true)
                                }
                            } ?: emptyList()) { index, wasteItem ->
                                WasteItemCard(
                                    modifier = Modifier.animateItemPlacement(),
                                    locationNo = "Location ${index + 1}",
                                    address = wasteItem.address,
                                    distance = "${
                                        convertDistance(
                                            distance(
                                                viewModel.latitude,
                                                viewModel.longitude,
                                                wasteItem.latitude,
                                                wasteItem.longitude
                                            )
                                        )
                                    } away",
                                    time = getTimeAgo(wasteItem.timeStamp),
                                    tags = wasteItem.tag.map {
                                        it.mapWithTips()
                                    },
                                    isList = true,
                                    imageUrlState = wasteItem.imagePath,
                                    isTagsVisble = false
                                ) {
                                    viewModel.locationNo.value = "Location ${index + 1}"
                                    viewModel.address.value = wasteItem.address
                                    viewModel.distance.value = "${
                                        convertDistance(
                                            distance(
                                                viewModel.latitude,
                                                viewModel.longitude,
                                                wasteItem.latitude,
                                                wasteItem.longitude
                                            )
                                        )
                                    } away"
                                    viewModel.time.value = getTimeAgo(wasteItem.timeStamp)
                                    viewModel.wastePhoto.value = wasteItem.imagePath
                                    viewModel.theirLatitude.value = wasteItem.latitude
                                    viewModel.theirLongitude.value = wasteItem.longitude
                                    viewModel.tags.value = wasteItem.tag.map {
                                        it.mapWithTips()
                                    }
                                    println("Collected time ${viewModel.time.value}")
                                    navController.navigate(Screens.CollectWasteInfo.route)
                                }

                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                                .clickable {
                                    viewModel.showLevelDialog = true
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Outerwear",
                                color = yellow,
                                fontSize = 14.sp,
                                fontFamily = monteBold,
                                modifier = Modifier.padding(bottom = 7.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(7.dp))

                        LazyRow() {
                            itemsIndexed(allWastes?.filter { wasteItem ->
                                wasteItem.tag.any { tagWithoutTips ->
                                    tagWithoutTips.name.equals("Jackets", ignoreCase = true) or tagWithoutTips.name.equals(
                                        "Coats",
                                        ignoreCase = true
                                    ) or tagWithoutTips.name.equals("Sweaters", ignoreCase = true)
                                }
                            } ?: emptyList()) { index, wasteItem ->
                                WasteItemCard(
                                    modifier = Modifier.animateItemPlacement(),
                                    locationNo = "Location ${index + 1}",
                                    address = wasteItem.address,
                                    distance = "${
                                        convertDistance(
                                            distance(
                                                viewModel.latitude,
                                                viewModel.longitude,
                                                wasteItem.latitude,
                                                wasteItem.longitude
                                            )
                                        )
                                    } away",
                                    time = getTimeAgo(wasteItem.timeStamp),
                                    tags = wasteItem.tag.map {
                                        it.mapWithTips()
                                    },
                                    isList = true,
                                    imageUrlState = wasteItem.imagePath,
                                    isTagsVisble = false
                                ) {
                                    viewModel.locationNo.value = "Location ${index + 1}"
                                    viewModel.address.value = wasteItem.address
                                    viewModel.distance.value = "${
                                        convertDistance(
                                            distance(
                                                viewModel.latitude,
                                                viewModel.longitude,
                                                wasteItem.latitude,
                                                wasteItem.longitude
                                            )
                                        )
                                    } away"
                                    viewModel.time.value = getTimeAgo(wasteItem.timeStamp)
                                    viewModel.wastePhoto.value = wasteItem.imagePath
                                    viewModel.theirLatitude.value = wasteItem.latitude
                                    viewModel.theirLongitude.value = wasteItem.longitude
                                    viewModel.tags.value = wasteItem.tag.map {
                                        it.mapWithTips()
                                    }
                                    println("Collected time ${viewModel.time.value}")
                                    navController.navigate(Screens.CollectWasteInfo.route)
                                }

                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                                .clickable {
                                    viewModel.showLevelDialog = true
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Accessories",
                                color = yellow,
                                fontSize = 14.sp,
                                fontFamily = monteBold,
                                modifier = Modifier.padding(bottom = 7.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(7.dp))

                        LazyRow() {
                            itemsIndexed(allWastes?.filter { wasteItem ->
                                wasteItem.tag.any { tagWithoutTips ->
                                    tagWithoutTips.name.equals("Scarves", ignoreCase = true) or tagWithoutTips.name.equals(
                                        "Hats",
                                        ignoreCase = true
                                    ) or tagWithoutTips.name.equals("Bags", ignoreCase = true)
                                }
                            } ?: emptyList()) { index, wasteItem ->
                                WasteItemCard(
                                    modifier = Modifier.animateItemPlacement(),
                                    locationNo = "Location ${index + 1}",
                                    address = wasteItem.address,
                                    distance = "${
                                        convertDistance(
                                            distance(
                                                viewModel.latitude,
                                                viewModel.longitude,
                                                wasteItem.latitude,
                                                wasteItem.longitude
                                            )
                                        )
                                    } away",
                                    time = getTimeAgo(wasteItem.timeStamp),
                                    tags = wasteItem.tag.map {
                                        it.mapWithTips()
                                    },
                                    isList = true,
                                    imageUrlState = wasteItem.imagePath,
                                    isTagsVisble = false
                                ) {
                                    viewModel.locationNo.value = "Location ${index + 1}"
                                    viewModel.address.value = wasteItem.address
                                    viewModel.distance.value = "${
                                        convertDistance(
                                            distance(
                                                viewModel.latitude,
                                                viewModel.longitude,
                                                wasteItem.latitude,
                                                wasteItem.longitude
                                            )
                                        )
                                    } away"
                                    viewModel.time.value = getTimeAgo(wasteItem.timeStamp)
                                    viewModel.wastePhoto.value = wasteItem.imagePath
                                    viewModel.theirLatitude.value = wasteItem.latitude
                                    viewModel.theirLongitude.value = wasteItem.longitude
                                    viewModel.tags.value = wasteItem.tag.map {
                                        it.mapWithTips()
                                    }
                                    println("Collected time ${viewModel.time.value}")
                                    navController.navigate(Screens.CollectWasteInfo.route)
                                }

                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))


                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 40.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Card(
                                modifier = Modifier
                                    .padding(5.dp),
                                backgroundColor = Color.Transparent,
                                elevation = 0.dp
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    ProfileImage(
                                        imageUrl = R.drawable.sell,
                                        modifier = Modifier
                                            .size(70.dp)
                                            .padding(2.dp)
                                            .clickable {
                                                navController.navigate(Screens.ReportWaste.route)
                                            }
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(
                                        text = "Sell Clothes",
                                        color = textColor,
                                        fontSize = 13.sp,
                                        fontFamily = monteNormal,
                                        softWrap = true
                                    )
                                }
                            }

                            Card(
                                modifier = Modifier
                                    .padding(5.dp),
                                backgroundColor = Color.Transparent,
                                elevation = 0.dp
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    ProfileImage(
                                        imageUrl = R.drawable.buy,
                                        modifier = Modifier
                                            .size(70.dp)
                                            .padding(2.dp)
                                            .clickable {
                                                navController.navigate(Screens.CollectWasteLists.route)
                                            }
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(
                                        text = "Buy Clothes",
                                        color = textColor,
                                        fontSize = 13.sp,
                                        fontFamily = monteNormal,
                                        softWrap = true
                                    )
                                }
                            }

//                        Card(
//                            modifier = Modifier
//                                .padding(5.dp),
//                            backgroundColor = Color.Transparent,
//                            elevation = 0.dp
//                        ) {
//                            Column(
//                                verticalArrangement = Arrangement.Center,
//                                horizontalAlignment = Alignment.CenterHorizontally
//                            ) {
//                                ProfileImage(
//                                    imageUrl = R.drawable.ic_rewards,
//                                    modifier = Modifier
//                                        .size(70.dp)
//                                        .border(
//                                            width = 1.dp,
//                                            color = textColor,
//                                            shape = CircleShape
//                                        )
//                                        .padding(2.dp)
//                                        .clip(CircleShape)
//                                        .clickable {
//                                            navController.navigate(Screens.Rewards.route)
//                                        }
//                                )
//                                Spacer(modifier = Modifier.height(5.dp))
//                                Text(
//                                    text = "Rewards",
//                                    color = textColor,
//                                    fontSize = 13.sp,
//                                    fontFamily = monteNormal,
//                                    softWrap = true
//                                )
//                            }
//                        }

                        }



                        Spacer(modifier = Modifier.height(20.dp))
                        val lastTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp)
                        ) {
                            Spacer(modifier = Modifier.height(25.dp))
                            Text(
                                text = "Wowdrobe",
                                color = lastTextColor.copy(0.75f),
                                fontSize = 33.sp,
                                fontFamily = monteSB,
                            )
                            Spacer(modifier = Modifier.height(0.dp))
                            Text(
                                text = "Revive-Revibe-Restyle",
                                color = lastTextColor.copy(0.5f),
                                fontSize = 23.sp,
                                fontFamily = monteSB,
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Crafted with ❤️ by Team Gemini",
                                color = lastTextColor.copy(0.75f),
                                fontSize = 10.sp,
                                fontFamily = monteSB,
                            )
                        }

                        Spacer(modifier = Modifier.height(130.dp))

                    }
                    if (viewModel.showLevelDialog) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            viewModel.currentLevel.value?.let { currentlevel ->
                                isCOinVisible = viewModel.isNewLevelUnlocked(
                                    currentlevel,
                                    viewModel.pointsEarned,
                                    levels
                                )
                                LevelDialogBox(
                                    level = currentlevel,
                                    progress = viewModel.getCurrentLevelProgress(
                                        viewModel.pointsEarned,
                                        levels
                                    ),
                                    isLevelUp = viewModel.isNewLevelUnlocked(
                                        currentlevel,
                                        viewModel.pointsEarned,
                                        levels
                                    ),
                                    isVisible = viewModel.showLevelDialog
                                ) {
                                    viewModel.showLevelDialog = false
                                }
                            }
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
                        LaunchedEffect(key1 = isCOinVisible) {
                            if (isCOinVisible) {
                                delay(4000)
                                navController.popBackStack()
                            }
                        }

                    }
                }
            }

        }
    }
}

@Composable
fun ArcComposable(
    modifier: Modifier,
    text: String,
    progress: Float = 0.5f, // Progress value between 0.0 and 1.0
    completedColor: Color = green,
    remainingColor: Color = lightGray,
) {
    val sweepAngle = 180f * progress
    Box(
        modifier = modifier.background(Color.Transparent)
    ) {
        Canvas(modifier = Modifier.size(70.dp)) {
            // Draw the remaining part of the arc
            drawArc(
                color = remainingColor,
                -180f + sweepAngle,
                180f - sweepAngle,
                useCenter = false,
                size = Size(size.width, size.height),
                style = Stroke(8.dp.toPx(), cap = StrokeCap.Round)
            )

            // Draw the completed part of the arc
            drawArc(
                color = completedColor,
                -180f,
                sweepAngle,
                useCenter = false,
                size = Size(size.width, size.height),
                style = Stroke(8.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Text(
            modifier = Modifier.align(alignment = Alignment.Center),
            text = text,
            color = textColor,
            fontSize = 20.sp
        )
    }
}
