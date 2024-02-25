package app.wowdrobe.com.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import app.wowdrobe.com.UserDatastore
import app.wowdrobe.com.aistudio.FashionAIStudio
import app.wowdrobe.com.buyClothes.CollectWaste
import app.wowdrobe.com.buyClothes.CollectWasteInfo
import app.wowdrobe.com.buyClothes.SuccessfullyCollected
import app.wowdrobe.com.dashboard.NewDashboard
import app.wowdrobe.com.location.LocationViewModel
import app.wowdrobe.com.login.CompleteProfile
import app.wowdrobe.com.login.LoginPage
import app.wowdrobe.com.login.WOnBoardingScreen
import app.wowdrobe.com.login.onboarding.SettingUp
import app.wowdrobe.com.profile.NewProfileScreen
import app.wowdrobe.com.reportwaste.ReportWaste
import app.wowdrobe.com.reportwaste.ReportWasteViewModel
import app.wowdrobe.com.rewards.ClaimedRewardsScreen
import app.wowdrobe.com.rewards.NewRewardsScreen
import app.wowdrobe.com.rewards.RewardDetails
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationController(
    scaffoldState: ScaffoldState,
    locationViewModel: LocationViewModel,
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    val viewModel: LocationViewModel = hiltViewModel()
    val reportWasteViewModel: ReportWasteViewModel = hiltViewModel()
    val context = LocalContext.current
    val store = UserDatastore(context)
    val email = store.getEmail.collectAsState(initial = "")
    val profile = store.getPfp.collectAsState(initial = "")
    val name = store.getName.collectAsState(initial = "")
    AnimatedNavHost(navController = navController, startDestination = Screens.Splash.route) {
        composable(
            route = Screens.LoginScreen.route,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -300 },
                    animationSpec = tween(300)
                ) +
                        fadeOut(animationSpec = tween(durationMillis = 300))
            },
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -300 },
                    animationSpec = tween(durationMillis = 300)
                ) + fadeIn(animationSpec = tween(durationMillis = 300))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -300 },
                    animationSpec = tween(durationMillis = 300)
                ) + fadeIn(animationSpec = tween(durationMillis = 300))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -300 },
                    animationSpec = tween(300)
                ) +
                        fadeOut(animationSpec = tween(durationMillis = 300))
            }
        ) {
            LoginPage(navController = navController, scaffoldState = scaffoldState)
        }
        composable(
            route = Screens.Dashboard.route,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -300 },
                    animationSpec = tween(300)
                ) +
                        fadeOut(animationSpec = tween(durationMillis = 300))
            },
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -300 },
                    animationSpec = tween(durationMillis = 300)
                ) + fadeIn(animationSpec = tween(durationMillis = 300))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -300 },
                    animationSpec = tween(durationMillis = 300)
                ) + fadeIn(animationSpec = tween(durationMillis = 300))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -300 },
                    animationSpec = tween(300)
                ) +
                        fadeOut(animationSpec = tween(durationMillis = 300))
            }
        ) {

            NewDashboard(
                navController = navController,
                email = email.value,
                name = name.value,
                pfp = profile.value,
                viewModel = viewModel
            )
//            DashBoardPage(navHostController = navController, locationViewModel = locationViewModel)
        }
        composable(Screens.Profile.route) {
//            ProfileScreen(navHostController = navController)
            NewProfileScreen(
                navController = navController,
                email = email.value,
                name = name.value,
                pfp = profile.value
            )
        }
        composable(Screens.Onboarding.route) {
            WOnBoardingScreen(navHostController = navController, paddingValues = paddingValues)
        }
        composable(Screens.CompleteProfile.route) {
            CompleteProfile(navHostController = navController)
        }
        composable(Screens.SettingUp.route) {
            SettingUp(navHostController = navController)

        }

        composable(Screens.AIStudio.route) {
            FashionAIStudio(navHostController = navController, viewModel = locationViewModel)
        }

        composable(Screens.Community.route) {
//            CommunitiesSection(
//                paddingValues = paddingValues,
//                email = email.value,
//                name = name.value,
//            )
//            CommunityInfo()
        }
        composable(Screens.ReportWaste.route) {
            ReportWaste(
                navController = navController, viewModel = viewModel,
                email = email.value,
                name = name.value,
                pfp = profile.value,
            )
        }
        composable(Screens.CollectWasteLists.route) {
            CollectWaste(
                navController = navController,
                viewModel = viewModel,
                paddingValues = paddingValues
            )
        }
        composable(Screens.CollectWasteInfo.route) {
            CollectWasteInfo(navController = navController, viewModel = viewModel)
        }
        composable(Screens.CollectedWasteSuccess.route) {
            SuccessfullyCollected(
                navController = navController, viewModel = viewModel,
                email = email.value,
                name = name.value,
                pfp = profile.value
            )
        }
        composable(Screens.Rewards.route) {
            NewRewardsScreen(
                navController = navController,
                email = email.value,
                name = name.value,
                pfp = profile.value,
                viewModel = viewModel
            )
        }
        composable(Screens.RewardsDetails.route) {
            RewardDetails(
                navController = navController,
                email = email.value,
                viewModel = viewModel,
                name = name.value,
            )
        }
        composable(Screens.ClaimedRewards.route) {
            ClaimedRewardsScreen(
                navController = navController,
                email = email.value,
                name = name.value,
                pfp = profile.value,
                viewModel = viewModel
            )
        }
        composable(Screens.Splash.route) {
            SplashScreen(navController = navController, email = email.value)
        }

    }
}


