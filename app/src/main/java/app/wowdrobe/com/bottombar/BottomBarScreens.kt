package app.wowdrobe.com.bottombar

import app.wowdrobe.com.R
import app.wowdrobe.com.navigation.Screens

sealed class BottomBarScreens(val route: String?, val title: String?, val icon: Int?) {
    object HomeScreen : BottomBarScreens(Screens.Dashboard.route, "Home", R.drawable.homei)

    object BuyScreen : BottomBarScreens(Screens.CollectWasteLists.route, "Buy", R.drawable.buy)

    object SellScreen : BottomBarScreens(Screens.ReportWaste.route, "Sell", R.drawable.sell)

}

val items = listOf(
    BottomBarScreens.HomeScreen,
    BottomBarScreens.BuyScreen,
    BottomBarScreens.SellScreen
)
