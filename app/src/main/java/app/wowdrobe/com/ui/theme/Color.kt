package app.wowdrobe.com.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material.lightColors


val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
//val backGround = Color(0xFF21c998)
val CardBackground = Color(0xFF122754)
val lightText = Color(0xFF3f68a8)


@SuppressLint("ConflictingOnColor")
val LightColors = lightColors(
    primary = Palette.md_theme_light_primary,
    onPrimary = Palette.md_theme_light_onPrimary,
    secondary = Palette.md_theme_light_secondary,
    onSecondary = Palette.md_theme_light_onSecondary,
    error = Palette.md_theme_light_error,
    onError = Palette.md_theme_light_onError,
    background = Palette.md_theme_light_background,
    onBackground = Palette.md_theme_light_onBackground,
    surface = Palette.md_theme_light_surface,
    onSurface = Palette.md_theme_light_onSurface,
)


val DarkColors = darkColors(
    primary = Palette.md_theme_dark_primary,
    onPrimary = Palette.md_theme_dark_onPrimary,
    secondary = Palette.md_theme_dark_secondary,
    onSecondary = Palette.md_theme_dark_onSecondary,
    error = Palette.md_theme_dark_error,
    onError = Palette.md_theme_dark_onError,
    background = Palette.md_theme_dark_background,
    onBackground = Palette.md_theme_dark_onBackground,
    surface = Palette.md_theme_dark_surface,
    onSurface = Palette.md_theme_dark_onSurface,
)


val backGround = Color(0xFF1a1b1b)
val yellow = Color(0xFFfabf31)
val indigo = Color(0xFFdcc0fe)
val orange = Color(0xFFf7653c)
val blue = Color(0xFF38abf2)
val green = Color(0xFF98d65c)
val lightGray = Color(0xFF2f2f2f)
val buttonBackground = Color(0xFF6d6c6d)
val textColor = Color(0xFFFCF9FC)


val appGradient: Brush
    @Composable
    get() = Brush.horizontalGradient(
        0.0f to Color(0xFF927DFF).copy(alpha = 0.87f),
        500.0f to Color(0xFF1A1A1A),
    )


val isDarkThemeEnabled: Boolean
    @Composable
    get() = isSystemInDarkTheme()

val appBackground: Color
    @Composable
    get() = backGround

val CardColor: Color
    @Composable
    get() = lightGray

val CardTextColor: Color
    @Composable
    get() = lightText

//val textColor: Color
//    @Composable
//    get() = MaterialTheme.colors.onSurface
