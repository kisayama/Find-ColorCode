package com.example.findcolorcode.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GetDynamicTypography(): Typography {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    return when {
        screenWidth < 600.dp -> smallDeviceTypography
        screenWidth > 600.dp -> largeDeviceTypography
        else -> largeDeviceTypography
    }
}
val smallDeviceTypography = Typography(
    bodyLarge = TextStyle(//通常のコンテンツで使うフォント設定
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(//タイトルのフォント設定
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(//小見出し
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
val largeDeviceTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,  // 大きめの本文フォント
        lineHeight = 28.sp,  // 行間も広く設定
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,  // タイトルはさらに大きめ
        lineHeight = 30.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,  // ラベルや小見出しはやや大きく
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp
    )
)


