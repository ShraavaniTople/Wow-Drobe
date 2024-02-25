package app.wowdrobe.com.maps

import android.Manifest
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.wowdrobe.com.buyClothes.getTimeAgo
import app.wowdrobe.com.firebase.firestore.WasteItem
import app.wowdrobe.com.location.LocationViewModel
import app.wowdrobe.com.ui.theme.appBackground
import app.wowdrobe.com.ui.theme.lightText
import app.wowdrobe.com.ui.theme.textColor
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects
import com.mapbox.geojson.*

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class,
)
@Composable
fun MapScreen(
    viewModel: LocationViewModel,
    paddingValues: PaddingValues,
    mapsSearchViewModel: MapsSearchViewModel,
    navController: NavController
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getPlaces()
    }

    val query = mapsSearchViewModel.query.collectAsState()
    val imageState = mapsSearchViewModel.imageState.collectAsState()

    var allWastes by remember { mutableStateOf<List<WasteItem>?>(null) }

    var point: Point? by remember {
        mutableStateOf(null)
    }
    var relaunch by remember {
        mutableStateOf(false)
    }

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    )
    val latitude = viewModel.latitude
    val longitude = viewModel.longitude
    Log.i("MapScreensssssss", "latitude: $latitude, longitude: $longitude")
    val mapsItems = allWastes?.map {
        MapItem(
            image = it.imagePath,
            location = it.address,
            time = it.timeStamp,
            point = Point.fromLngLat(it.longitude, it.latitude)
        )
    }

    val permissionDrawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
    val gesturesEnabled by remember { derivedStateOf { permissionDrawerState.isOpen } }
    LaunchedEffect(key1 = Unit) {
        viewModel.getPlaces()
    }

    JetFirestore(path = {
        collection("AllWastes")
    }, onRealtimeCollectionFetch = { values, _ ->
        allWastes = values?.getListOfObjects()

    }) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                if (mapsItems != null) {
                    MapBoxMap(
                        onPointChange = { point = it },
                        modifier = Modifier
                            .fillMaxSize(),
                        isClicked = mapsSearchViewModel.isClicked,
                        points = mapsItems,
                        currentPoint = mapsSearchViewModel.currentPoint,
                        isReset = mapsSearchViewModel.isReset,
                        currentLocation = Point.fromLngLat(
                            viewModel.longitude, viewModel.latitude
                        )
                    )
                }
            }
            AnimatedVisibility(
                visible = !mapsSearchViewModel.isClicked.value,
                enter = slideInVertically(tween(1000), initialOffsetY = {
                    it
                }),
                exit = slideOutVertically(tween(1000), targetOffsetY = {
                    it
                })
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(30.dp)
                    ) {
                        items(mapsItems ?: emptyList()) { item ->
                            Card(
                                modifier = Modifier
                                    .width(300.dp)
                                    .height(230.dp)
                                    .padding(
                                        end = 10.dp, bottom =
                                        paddingValues.calculateBottomPadding() + 10.dp
                                    ),
                                shape = RoundedCornerShape(10.dp),
                                elevation = 10.dp,
                                backgroundColor = appBackground
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = item.location,
                                            color = textColor,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            softWrap = true
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(end = 10.dp),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            Text(
                                                text = getTimeAgo(item.time),
                                                color = textColor,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Normal,
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Button(
                                            onClick = {
                                                mapsSearchViewModel.isReset.value = false
                                                mapsSearchViewModel.isClicked.value = true
                                                mapsSearchViewModel.currentPoint.value = item.point
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                backgroundColor = appBackground,
                                            )
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Filled.LocationOn,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(20.dp),
                                                    tint = textColor
                                                )
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Text(
                                                    text = "Navigate",
                                                    color = textColor,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Normal
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                }
                            }
                        }

                    }
                }
            }
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(horizontal = 10.dp, vertical = 25.dp),
//                contentAlignment = Alignment.TopEnd
//            ) {
//                Card(
//                    modifier = Modifier
//                        .padding(end = 10.dp),
//                    shape = RoundedCornerShape(10.dp),
//                    elevation = 10.dp
//                ) {
//                    Button(
//                        onClick = {
//                            isReset.value = true
//                            isClicked.value = false
//
//                        },
//                        shape = RoundedCornerShape(10.dp),
//                        colors = ButtonDefaults.buttonColors(
//                            backgroundColor = CardColor,
//                            contentColor = CardTextColor
//                        )
//                    ) {
//                        Text(
//                            text = "Reset",
//                            color = CardTextColor,
//                            fontSize = 10.sp,
//                            fontWeight = FontWeight.Normal
//                        )
//                    }
//
//                }
//            }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
                Column {
                    MapsSearchBar(
                        mutableText = query.value,
                        onValueChange = {
                            mapsSearchViewModel.setQuery(it)
                        },
                        viewModel = mapsSearchViewModel,
                        onTrailingClick = {
                            mapsSearchViewModel.setQuery(TextFieldValue(""))
                        },
                        navController = navController
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    AnimatedVisibility(
                        visible = imageState.value !is ApiState.NotStarted && imageState.value !is ApiState.ReceivedPhoto,
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            color = lightText
                        )
                    }

                }
            }

        }

    }
}
