package com.example.findcolorcode.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.example.findcolorcode.viewmodel.ColorChoiceViewModel
import com.example.findcolorcode.viewmodel.MainViewModel

//Toastメッセージが変更されたことを検知してToastを表示する
//複数のViewModelで使用予定
@Composable
fun ShowToast(toastMessage: String,resetMessage:() -> Unit) {
    val context = LocalContext.current
    //LaunchedEffectは指定したキーが変更された時に{}内を実行する
    if (toastMessage.isNotEmpty()) {
        LaunchedEffect(toastMessage) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            resetMessage()
        }
    }
}
