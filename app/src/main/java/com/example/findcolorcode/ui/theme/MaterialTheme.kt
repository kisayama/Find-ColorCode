package com.example.findcolorcode.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

private val LightColorPalette = lightColorScheme(
primary = Color(0xFFE3E3E3),//MainColor
    onPrimary = Color.White,//プライマリ色に重ねて表示する色（文字とかアイコン）
    secondary = Color(0xFF2196F3),//Mainに対するSubColor
    onSecondary = Color.Black,
    background = Color.White,//背景色
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)
@Composable
fun JetnewsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorPalette,
        shapes = Shapes(),
        content = content
    )
}

