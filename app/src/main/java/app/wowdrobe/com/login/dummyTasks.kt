package app.wowdrobe.com.login

import androidx.compose.ui.graphics.Color
import app.wowdrobe.com.R
import app.wowdrobe.com.ui.theme.blue
import app.wowdrobe.com.ui.theme.green
import app.wowdrobe.com.ui.theme.indigo
import app.wowdrobe.com.ui.theme.orange
import app.wowdrobe.com.ui.theme.yellow

data class Tasks(
    val name: String,
    val color: Color,
    val icon: Int? = null,
)

val dummyTasks = listOf(
    Tasks(
        name = "Shopping",
        color = orange,
        icon = R.drawable.shoppping
    ),
    Tasks(
        name = "Cart",
        color = indigo,
        icon = R.drawable.cart
    ),
    Tasks(
        name = "Orders",
        color = yellow,
        icon = R.drawable.orders
    ),
    Tasks(
        name = "Outwear",
        color = blue,
        icon = R.drawable.outwear
    ),
    Tasks(
        name = "Sustainable",
        color = green,
        icon = R.drawable.sustainable_fashion
    ),

    Tasks(
        name = "Wowdrobe",
        color = Color.White,
        icon = R.drawable.wowdrobe
    ),

    )