package com.example.findcolorcode.ui.theme

import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import com.example.app.ui.theme.AppColors


@Composable
fun customTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.colors(
        focusedContainerColor = AppColors.White,//フォーカス時の色
        unfocusedContainerColor = AppColors.White,//非フォーカス時の色
        focusedIndicatorColor = AppColors.Black,//フォーカス時のインジケーター
        focusedLabelColor = AppColors.Gray,// フォーカス時のラベル
        unfocusedLabelColor = AppColors.Gray //非フォーカス時のラベル
    )
}

