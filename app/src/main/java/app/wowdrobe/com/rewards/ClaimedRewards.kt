package app.wowdrobe.com.rewards

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import app.wowdrobe.com.R
import app.wowdrobe.com.bottombar.BottomBar
import app.wowdrobe.com.components.permissions.PermissionDrawer
import app.wowdrobe.com.firebase.firestore.ProfileInfo
import app.wowdrobe.com.location.LocationViewModel
import app.wowdrobe.com.navigation.Screens
import app.wowdrobe.com.ui.theme.appBackground
import app.wowdrobe.com.ui.theme.monteBold
import app.wowdrobe.com.ui.theme.monteNormal
import app.wowdrobe.com.ui.theme.textColor
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects

@OptIn(
    ExperimentalPermissionsApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun ClaimedRewardsScreen(
    navController: NavHostController,
    email: String,
    name: String,
    pfp: String,
    viewModel: LocationViewModel
) {
    val onClick = {
        viewModel.rewardImage.value =
            "https://img.freepik.com/premium-psd/headphone-giveaway-contestpromotion-instagram-facebook-social-media-post-template_501590-116.jpg?w=2000"
        viewModel.rewardTitle.value = "Boat Headphones"
        viewModel.rewardDescription.value =
            "Immerse yourself in exceptional audio quality with these headphones," +
                    " designed for ultimate comfort and delivering a truly immersive sound experience. Whether for music, movies, or calls, these headphones will elevate your audio enjoyment to new heights"
        viewModel.rewardNoOfPoints.value = 60
        navController.navigate(Screens.RewardsDetails.route)
    }
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    val permissionDrawerState = rememberBottomDrawerState(
        if (permissionState.allPermissionsGranted) BottomDrawerValue.Closed else BottomDrawerValue.Open
    )
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
    val cList = listOf("Market", "My Rewards")
    var tabIndex by remember { mutableStateOf(0) }
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
                }
            }
        }

        PermissionDrawer(
            drawerState = permissionDrawerState,
            permissionState = permissionState,
            rationaleText = "To continue, allow Report Waste2Wealth to access your device's location" +
                    ". Tap Settings > Permission, and turn \"Access Location On\" on.",
            withoutRationaleText = "Location permission required for functionality of this app." +
                    " Please grant the permission.",
        ) {
            Scaffold(bottomBar = {
                BottomBar(navController = navController)
            }) {
                println(it)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(appBackground)
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
                        Column {
                            Text(
                                text = "Claimed Rewards",
                                color = textColor,
                                fontSize = 25.sp,
                                fontFamily = monteBold,
                            )
                            Text(
                                text = "Push yourself for more",
                                color = Color.LightGray,
                                fontSize = 13.sp,
                                fontFamily = monteBold,
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp, end = 0.dp, start = 20.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.padding(end = 25.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.coins),
                                    contentDescription = "coins",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(end = 5.dp),
                                    tint = Color.Unspecified
                                )
                                Text(
                                    text = pointsEarned.toString(),
                                    color = textColor,
                                    fontSize = 15.sp,
                                    softWrap = true,
                                    fontFamily = monteNormal,
                                )
                            }

                        }
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(appBackground),
                        contentPadding = PaddingValues(top = 1.dp, bottom = 0.dp)
                    ) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()

                            ) {

//                                Spacer(modifier = Modifier.height(50.dp))
                                Row {
                                    Image(
                                        painter = painterResource(id = R.drawable.rewardgeneral),
                                        contentDescription = "",
                                        Modifier
                                            .height(100.dp)
                                            .fillMaxWidth(), contentScale = ContentScale.FillBounds
                                    )

                                }

                                LazyRow() {
                                    items(21) { listItem ->
                                        Card(
                                            modifier = Modifier
                                                .height(40.dp)
                                                .width(40.dp)
                                                .padding(5.dp)
                                        ) {
                                            Text(
                                                text = "1",
                                                fontWeight = FontWeight.Medium,
                                                modifier = Modifier.padding(2.dp),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }

//                                Spacer(modifier = Modifier.height(20.dp))

                                Row(
                                    modifier = Modifier.padding(
                                        vertical = 4.dp,
                                        horizontal = 18.dp,
                                    ),

                                ) {
                                    Text(
                                        text = "Your Rewards",
                                        fontSize = 25.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center
                                    )
                                }


                                    LazyVerticalGrid()
                                    LazyVerticalGrid()





                            }

                        }
                    }
                }
            }
        }

    }
}




@Composable
fun LazyVerticalGrid() {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .height(180.dp)
            .padding(start = 25.dp, end = 25.dp),
//                                        .offset(y = (-55).dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.Center
    ) {
        items(1) {

            Column(modifier = Modifier.padding(5.dp)) {
                Card(
                    modifier = Modifier.padding(0.dp),
                    backgroundColor = Color(0xFF3C3E41),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(
                            top = 30.dp,
                            start = 40.dp
                        )
                    ) {
                        Card(
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                                .padding(5.dp),
                            CircleShape
                        ) {
                            Text(
                                text = "1",
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(2.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .background(color = Color.Red)
                        ) {

                        }
                        Row(
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth()
                                .background(color = Color.Black)
                        ) {

                            Column(
                                modifier = Modifier
                                    .padding(
                                        horizontal = 10.dp,
                                        vertical = 10.dp
                                    ),

                                ) {
                                Text(
                                    text = "Flat $100 off",
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp,
                                    fontFamily = monteBold,
                                    textAlign = TextAlign.Center,

                                    )
                                Text(
                                    text = "Cashback on Paytm to a friend",
                                    color = Color.White,
                                    fontSize = 7.sp,
                                    fontFamily = monteBold,
                                    textAlign = TextAlign.Center
                                )

                            }

                        }
                    }


                }
            }
        }


        items(1) {

            Column(modifier = Modifier.padding(5.dp)) {
                Card(
                    modifier = Modifier.padding(0.dp),
                    backgroundColor = Color(0xFF3C3E41),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(
                            top = 30.dp,
                            start = 40.dp
                        )
                    ) {
                        Card(
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                                .padding(5.dp),
                            CircleShape
                        ) {
                            Text(
                                text = "1",
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(2.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp)
                                .background(color = Color.Green)
                        ) {

                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(color = Color.White),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "Activating",
                                color = Color.Blue,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                fontFamily = monteBold,
                                textAlign = TextAlign.Center,

                                )

                        }
                        Row(
                            modifier = Modifier
                                .height(55.dp)
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp)
                                .background(color = Color.Black),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "Cashback on Paytm to a friend",
                                color = Color.White,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )

                        }
                    }


                }
            }
        }


    }
}