package com.example.findcolorcode.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.app.ui.theme.AppColors

private val DarkColorScheme = darkColorScheme(
    primary = AppColors.Gray,//MainColor
    onPrimary = AppColors.Black,//文字は黒
    secondary = AppColors.lightGray,//Mainに対するSubColor
    onSecondary = AppColors.Black,
    background = AppColors.BgWhite,//背景色
    onBackground = AppColors.Black,
    surface = AppColors.lightGray,
    onSurface = AppColors.Black
)

private val LightColorScheme = lightColorScheme(
    primary = AppColors.Gray,//MainColor
    onPrimary = AppColors.Black,//文字は黒
    secondary = AppColors.lightGray,//Mainに対するSubColor
    onSecondary = AppColors.Black,
    background = AppColors.BgWhite,//背景色
    onBackground = AppColors.Black,
    surface = AppColors.lightGray,
    onSurface = AppColors.Black
)


@Composable
fun FindColorCodeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme =if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}