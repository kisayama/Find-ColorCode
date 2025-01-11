package com.example.findcolorcode.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.example.findcolorcode.viewmodel.FavoriteScreenViewModel

//Toastメッセージが変更されたことを検知してToastを表示する
//複数のViewModelで使用予定
@Composable
fun FavoriteColorToast(viewModel: FavoriteScreenViewModel) {
    val context = LocalContext.current
    val currentToastMessage by viewModel.toastMessage.observeAsState("")
    //LaunchedEffectは指定したキーが変更された時に{}内を実行する
    if (currentToastMessage.isNotEmpty()) {
        LaunchedEffect(currentToastMessage) {
            Toast.makeText(context, currentToastMessage, Toast.LENGTH_SHORT).show()
            //トーストメッセージを空にするメソッド
            //これにより同じメッセージを連続で送ることができる
            viewModel.resetToast()
        }
    }
}
