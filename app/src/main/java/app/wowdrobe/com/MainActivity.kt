package app.wowdrobe.com

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.currentBackStackEntryAsState
import app.wowdrobe.com.bottombar.BottomBar
import app.wowdrobe.com.components.permissions.PermissionDrawer
import app.wowdrobe.com.location.LocationViewModel
import app.wowdrobe.com.login.onboarding.SmsBroadcastReceiver
import app.wowdrobe.com.login.onboarding.SmsBroadcastReceiver.SmsBroadcastReceiverListener
import app.wowdrobe.com.navigation.NavigationController
import app.wowdrobe.com.navigation.Screens
import app.wowdrobe.com.ui.theme.Waste2WealthTheme
import app.wowdrobe.com.ui.theme.appBackground
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.auth.api.phone.SmsRetriever
import dagger.hilt.android.AndroidEntryPoint
import javax.annotation.Nullable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var smsBroadcastReceiver: SmsBroadcastReceiver
    private lateinit var viewModel: LocationViewModel

    @OptIn(
        ExperimentalAnimationApi::class, ExperimentalPermissionsApi::class,
        ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Waste2WealthTheme {
                val navController = rememberAnimatedNavController()
                val permissionState = rememberMultiplePermissionsState(
                    permissions = listOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CAMERA,
                    )
                )
                val permissionDrawerState = rememberBottomDrawerState(
                    if (permissionState.allPermissionsGranted) BottomDrawerValue.Closed else BottomDrawerValue.Open
                )
                val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                when (navBackStackEntry?.destination?.route) {
                    Screens.Dashboard.route -> {
                        bottomBarState.value = true
                    }
                    Screens.Community.route -> {
                        bottomBarState.value = true
                    }
                    else -> {
                        bottomBarState.value = false
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
                        BottomBar(
                            navController = navController,
                            bottomBarState = bottomBarState
                        )
                    }) {

                        val systemUiController = rememberSystemUiController()
                        systemUiController.setSystemBarsColor(appBackground)
                        systemUiController.setNavigationBarColor(appBackground)

                        viewModel = ViewModelProvider(this)[LocationViewModel::class.java]

                        val locationViewModel: LocationViewModel = hiltViewModel()
                        val scaffoldState = rememberScaffoldState()
                        val client = SmsRetriever.getClient(this)
                        client.startSmsUserConsent(null)
                        println(it)

                        NavigationController(
                            scaffoldState,
                            locationViewModel,
                            navController,
                            it
                        )

                    }
                }
            }
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver.smsBroadcastReceiverListener =
            object : SmsBroadcastReceiverListener {
                override fun onSuccess(intent: Intent?) {
                    intent?.let { startActivityForResult(it, 200) }
                }

                override fun onFailure() {}
            }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)
    }


    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadcastReceiver)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            if (resultCode == RESULT_OK && data != null) {
                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                println("Message $message")
                viewModel.result.value = message
            }
        }
    }

}



