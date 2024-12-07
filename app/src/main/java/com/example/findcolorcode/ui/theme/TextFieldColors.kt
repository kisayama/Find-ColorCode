package com.example.findcolorcode.ui.theme

import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.app.ui.theme.AppColors


@Composable
fun customTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,//フォーカス時の色
        unfocusedContainerColor = Color.Transparent,//非フォーカス時の色
        focusedIndicatorColor = AppColors.Black,//フォーカス時のインジケーター
        focusedLabelColor = AppColors.Gray,// フォーカス時のラベル
        unfocusedLabelColor = AppColors.Gray //非フォーカス時のラベル
    )
}

