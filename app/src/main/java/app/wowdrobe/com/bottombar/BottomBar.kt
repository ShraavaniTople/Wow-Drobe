package app.wowdrobe.com.bottombar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import app.wowdrobe.com.navigation.Screens
import app.wowdrobe.com.ui.theme.appBackground
import app.wowdrobe.com.ui.theme.indigo
import app.wowdrobe.com.ui.theme.lightGray
import app.wowdrobe.com.ui.theme.monteSB
import app.wowdrobe.com.ui.theme.orange
import app.wowdrobe.com.ui.theme.textColor
import app.wowdrobe.com.ui.theme.yellow

@Composable
fun BottomBar(
    navController: NavController,
    bottomBarState: MutableState<Boolean> = mutableStateOf(true)
) {
    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                backgroundColor = appBackground,
                elevation = 5.dp,
                shape = RoundedCornerShape(17.dp)
            ) {
                BottomNavigation(
                    modifier = Modifier
                        .height(80.dp),
                    elevation = 0.dp,
                    backgroundColor = lightGray
                ) {
                    items.forEach {
                        val isYellow = currentRoute?.hierarchy?.any { nav ->
                            nav.route == it.route
                        } == true
                        val myTextColor = if (isSystemInDarkTheme()) {
                            if (isYellow) Color.Black else Color.White
                        } else {
                            if (isYellow) Color.White else Color.Black
                        }
                        BottomNavigationItem(
                            icon = {
                                it.icon?.let {
                                    Icon(
                                        painter = painterResource(id = it),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(35.dp)
                                            .padding(bottom = 5.dp),
                                        tint = if (isYellow) indigo else textColor.copy(0.6f)
                                    )
                                }
                            },
                            label = {
                                it.title?.let {
                                    Text(
                                        text = it,
                                        color = if (isYellow) indigo else textColor.copy(0.6f),
                                        softWrap = true,
                                        fontFamily = monteSB,
                                        fontSize = 10.sp
                                    )
                                }
                            },
                            selected = isYellow,
                            selectedContentColor = orange,
                            unselectedContentColor = yellow,
                            modifier = Modifier
                                .background(if (isYellow) lightGray else lightGray)
                                .clip(RoundedCornerShape(17.dp)),
                            onClick = {
                                it.route?.let { it1 ->
                                    navController.navigate(it1) {
                                        popUpTo(Screens.Dashboard.route) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}
