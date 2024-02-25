package app.wowdrobe.com.buyClothes

import android.Manifest
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Feed
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import app.wowdrobe.com.firebase.firestore.ProfileInfo
import app.wowdrobe.com.firebase.firestore.calculatePointsEarned
import app.wowdrobe.com.firebase.firestore.updateCollectedWasteToFirebase
import app.wowdrobe.com.firebase.firestore.updateInfoToFirebase
import app.wowdrobe.com.location.LocationViewModel
import app.wowdrobe.com.login.TextFieldWithIcons
import app.wowdrobe.com.navigation.Screens
import app.wowdrobe.com.rewards.levels
import app.wowdrobe.com.ui.theme.CardColor
import app.wowdrobe.com.ui.theme.CardTextColor
import app.wowdrobe.com.ui.theme.appBackground
import app.wowdrobe.com.ui.theme.indigo
import app.wowdrobe.com.ui.theme.monteSB
import app.wowdrobe.com.ui.theme.textColor
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.firebase.storage.FirebaseStorage
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream

@OptIn(
    ExperimentalPermissionsApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun SuccessfullyCollected(
    navController: NavHostController,
    viewModel: LocationViewModel,
    email: String,
    name: String,
    pfp: String
) {
    val radioOptions = listOf("Yes", "No")
    val depositedWaste = listOf("Yes", "No")
    var receiver by remember { mutableStateOf(radioOptions[0]) }
    var receiver2 by remember { mutableStateOf(radioOptions[0]) }
    var feedback by remember { mutableStateOf(TextFieldValue("")) }
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA
        )
    )
    val permissionDrawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
    val gesturesEnabled by remember { derivedStateOf { permissionDrawerState.isOpen } }
    var imageBitmap by remember {
        mutableStateOf<ImageBitmap?>(null)
    }

    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
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
    var communities by remember {
        mutableStateOf(mutableListOf(""))
    }
    var maxReported by remember {
        mutableStateOf(0)
    }
    var maxCollected by remember {
        mutableStateOf(0)
    }
    var maxCommunity by remember {
        mutableStateOf(0)
    }
    val context = LocalContext.current
    var isCOinVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = {
            println("Bitmaps is ${it?.asImageBitmap()}")
            imageBitmap = it?.asImageBitmap()
            viewModel.getPlaces()
            bitmap = it


        }
    )

    JetFirestore(path = {
        collection("ProfileInfo")
    }, onRealtimeCollectionFetch = { value, _ ->
        profileList = value?.getListOfObjects()
        maxReported = (profileList?.map { it.noOfTimesReported.toDouble() } ?: emptyList())
            .max().toInt()

        maxCollected = (profileList?.map { it.noOfTimesCollected.toDouble() } ?: emptyList())
            .max().toInt()
        maxCommunity = (profileList?.map { it.communities.size.toDouble() } ?: emptyList())
            .max().toInt()
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
                    communities = i.communities.toMutableList()
                }
            }
        }


                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(appBackground)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp, start = 0.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBackIos,
                                contentDescription = "",
                                tint = textColor,
                                modifier = Modifier
                                    .padding(start = 15.dp)
                                    .size(25.dp)
                                    .clickable {
                                        navController.popBackStack()
                                    }
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .offset(x = (-10).dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Checkout",
                                    color = textColor,
                                    fontFamily = monteSB,
                                    fontSize = 25.sp
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(bottom = 5.dp, top = 10.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Are you sure you want to buy it ?",
                                color = textColor,
                                fontSize = 16.sp,
                                fontFamily = monteSB,
                                modifier = Modifier.padding(start = 13.dp)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 0.dp, bottom = 10.dp, start = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            depositedWaste.forEach { value ->
                                Row(
                                    modifier = Modifier
                                        .selectable(
                                            selected = (value == receiver),
                                            role = Role.RadioButton,
                                            onClick = {
                                                receiver = value
                                            }
                                        )
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = value,
                                        color = indigo,
                                        fontFamily = monteSB
                                    )
                                    RadioButton(
                                        selected = (value == receiver),
                                        onClick = {
                                            receiver = value
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = indigo,
                                            unselectedColor = Color.Gray
                                        )
                                    )
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 0.dp, top = 10.dp)
                                .height(50.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Would like to donate Rs 1. for planting a tree.",
                                color = textColor,
                                fontSize = 16.sp,
                                fontFamily = monteSB,
                                modifier = Modifier.padding(start = 13.dp)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 0.dp, bottom = 10.dp, start = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            radioOptions.forEach { value ->
                                Row(
                                    modifier = Modifier
                                        .selectable(
                                            selected = (value == receiver2),
                                            role = Role.RadioButton,
                                            onClick = {
                                                receiver2 = value
                                            }
                                        )
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = value,
                                        color = indigo,
                                        fontFamily = monteSB
                                    )
                                    RadioButton(
                                        selected = (value == receiver2),
                                        onClick = {
                                            receiver2 = value
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = indigo,
                                            unselectedColor = Color.Gray
                                        )
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            TextFieldWithIcons(
                                textValue = "Enter your Address",
                                placeholder = "Start typing here",
                                icon = Icons.Filled.Feed,
                                mutableText = feedback,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Default,
                                onValueChanged = {
                                    if (it.text.length <= 200) {
                                        feedback = it
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Maximum 200 words",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }
                            )
                        }

                    }
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 100.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Button(
                            onClick = {
                                Toast.makeText(context, "Please Wait", Toast.LENGTH_SHORT)
                                    .show()
                                if (bitmap != null && feedback.text != "") {
                                    val storageRef = FirebaseStorage.getInstance().reference
                                    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
                                    var imageName = (1..10)
                                        .map { allowedChars.random() }
                                        .joinToString("")
                                    imageName = "Collected/${email}/${imageName}.jpg"
                                    val imageRef =
                                        storageRef.child(imageName) // Set desired storage location

                                    val baos = ByteArrayOutputStream()
                                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                                    val imageData = baos.toByteArray()

                                    val uploadTask = imageRef.putBytes(imageData)
                                    uploadTask.addOnSuccessListener { _ ->
                                    }.addOnFailureListener { exception ->
                                        println("Firebase storage exception $exception")
                                    }.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            imageRef.downloadUrl.addOnSuccessListener {
                                                println("Download url is $it")
                                                updateCollectedWasteToFirebase(
                                                    context = context,
                                                    address = viewModel.address.value,
                                                    latitude = viewModel.latitude,
                                                    longitude = viewModel.longitude,
                                                    imagePath = imageName,
                                                    timeStamp = System.currentTimeMillis(),
                                                    userEmail = email ?: "",
                                                    isWasteCollected = receiver == "Yes",
                                                    allWasteCollected = receiver2 == "Yes",
                                                    feedBack = feedback.text,
                                                    beforeCollectedPath = viewModel.beforeCollectedPath.value
                                                )
                                                viewModel.getCurrentLevel(
                                                    points = pointsEarned + calculatePointsEarned(
                                                        noOfTimesReported,
                                                        noOfTimesCollected,
                                                        noOfTimesActivity,
                                                        maxReported,
                                                        maxCollected,
                                                        maxCommunity
                                                    ),
                                                    levels = levels
                                                )
                                                viewModel.pointsEarned = pointsEarned + calculatePointsEarned(
                                                    noOfTimesReported,
                                                    noOfTimesCollected,
                                                    noOfTimesActivity,
                                                    maxReported,
                                                    maxCollected,
                                                    maxCommunity
                                                )
                                                updateInfoToFirebase(
                                                    context,
                                                    name = name,
                                                    email = email,
                                                    phoneNumber = phoneNumber,
                                                    gender = gender,
                                                    organization = organization,
                                                    address = userAddress,
                                                    pointsEarned = pointsEarned + calculatePointsEarned(
                                                        noOfTimesReported,
                                                        noOfTimesCollected,
                                                        noOfTimesActivity,
                                                        isCollectedWaste = true,
                                                        maxReportedValue = maxReported,
                                                        maxCollectedValue = maxCollected,
                                                        maxCommunitiesJoinedValue = maxCommunity
                                                    ),
                                                    pointsRedeemed = pointsRedeemed,
                                                    noOfTimesReported = noOfTimesReported,
                                                    noOfTimesCollected = noOfTimesCollected + 1,
                                                    noOfTimesActivity = noOfTimesActivity,
                                                    communities = communities


                                                    )
                                                isCOinVisible = true

                                            }
                                        }

                                    }

                                } else {
                                    Toast.makeText(
                                        context,
                                        "Please Fill All Fields",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = CardColor,
                                contentColor = CardTextColor
                            ),
                            shape = RoundedCornerShape(35.dp),
                            modifier = Modifier.padding(start = 10.dp)
                        ) {
                            Text(
                                text = "Checkout",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontFamily = monteSB,
                                modifier = Modifier.padding(bottom = 4.dp),
                                maxLines = 1,
                                softWrap = true
                            )
                        }
                    }
                }

                if (isCOinVisible) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        val currenanim by rememberLottieComposition(
                            spec = LottieCompositionSpec.Asset("coins.json")
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
                            viewModel.showLevelDialog = true
                            navController.navigate(Screens.Dashboard.route)
                        }
                    }

                }
            }
        }

fun countWords(text: String): Int {
    val words = text.trim().split("\\s+".toRegex())
    return words.size
}