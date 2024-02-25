package app.wowdrobe.com.buyClothes

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import app.wowdrobe.com.location.LocationViewModel
import app.wowdrobe.com.navigation.Screens
import app.wowdrobe.com.reportwaste.DialogBox
import app.wowdrobe.com.ui.theme.CardColor
import app.wowdrobe.com.ui.theme.CardTextColor
import app.wowdrobe.com.ui.theme.appBackground
import app.wowdrobe.com.ui.theme.indigo
import app.wowdrobe.com.ui.theme.monteSB
import app.wowdrobe.com.ui.theme.textColor
import coil.compose.AsyncImage
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


@Composable
fun CollectWasteInfo(
    navController: NavHostController,
    viewModel: LocationViewModel
) {
    val context = LocalContext.current
    var isDialogVisible by remember { mutableStateOf(false) }
    var isDialogMainVisible by remember { mutableStateOf(false) }
    val isWithin = isWithinRadius(
        viewModel.latitude,
        viewModel.longitude,
        viewModel.theirLatitude.value,
        viewModel.theirLongitude.value
    )
    BackHandler {
        viewModel.locationNo.value = ""
        viewModel.address.value = ""
        viewModel.distance.value = ""
        viewModel.time.value = ""
        viewModel.wastePhoto.value = ""
        viewModel.theirLongitude.value = 0.0
        viewModel.theirLatitude.value = 0.0
        viewModel.tags.value = listOf()
        navController.popBackStack()
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.getPlaces()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appBackground)
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
                    text = "Buy Thrifted Outfit",
                    color = textColor,
                    fontFamily = monteSB,
                    fontSize = 25.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        WasteItemCard(
            locationNo = viewModel.locationNo.value,
            address = viewModel.address.value,
            distance = viewModel.distance.value,
            time = viewModel.time.value,
            isCollectedInfo = true,
            isEllipsis = false,
            tags = viewModel.tags.value,
            onCollected = {
                val gmmIntentUri =
                    Uri.parse(
                        "google.navigation:q=${viewModel.theirLatitude.value}," +
                                "${viewModel.theirLongitude.value}"
                    )
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                context.startActivity(mapIntent)

            },
        )
        Spacer(modifier = Modifier.height(30.dp))
        var imageUrlState by remember {
            mutableStateOf("")
        }
        LaunchedEffect(key1 = Unit) {
            val imageUrl = withContext(Dispatchers.IO) {
                try {
                    getDownloadUrlFromPath(viewModel.wastePhoto.value)
                } catch (e: Exception) {
                    ""
                }
            }
            imageUrlState = imageUrl
        }
        if (imageUrlState != "") {
            AsyncImage(
                model = imageUrlState,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(bottom = 30.dp)
                    .clip(RoundedCornerShape(30.dp)),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    if (!isWithin) {
                        isDialogVisible = true
                    } else {
                        isDialogMainVisible = true
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
                    text = "Buy it",
                    color = indigo,
                    fontSize = 12.sp,
                    fontFamily = monteSB,
                    modifier = Modifier.padding(bottom = 4.dp),
                    maxLines = 1,
                    softWrap = true
                )
            }
        }

        DialogBox(
            isVisible = isDialogMainVisible,
            title = "Let's checkout",
            description = "",
            successRequest = {
                isDialogVisible = false
                viewModel.beforeCollectedPath.value = viewModel.wastePhoto.value
                navController.navigate(Screens.CollectedWasteSuccess.route)
            },
            dismissRequest = {
                isDialogMainVisible = false
            }
        )

    }

}

suspend fun getDownloadUrlFromPath(path: String): String {
    val storageRef = FirebaseStorage.getInstance().reference
    val fileRef = storageRef.child(path)
    return fileRef.downloadUrl.await().toString()
}

fun isWithinRadius(
    sourceLat: Double,
    sourceLon: Double,
    destLat: Double,
    destLon: Double
): Boolean {
    val earthRadius = 6371 // Radius of the Earth in kilometers

    val dLat = Math.toRadians(destLat - sourceLat)
    val dLon = Math.toRadians(destLon - sourceLon)

    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(sourceLat)) * cos(Math.toRadians(destLat)) *
            sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    val distance = earthRadius * c

    return distance <= 1.2 // Check if distance is within x km radius
}