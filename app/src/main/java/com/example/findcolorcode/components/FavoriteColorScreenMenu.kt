package com.example.findcolorcode.components

import android.util.Log
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.findcolorcode.data.favoriteColorScreenMenuList
import com.example.findcolorcode.model.FavoriteColorDataClass
import com.example.findcolorcode.viewmodel.FavoriteScreenViewModel

@Composable
 fun favoriteColorScreenMenu(
    colorItem:FavoriteColorDataClass,
    viewModel: FavoriteScreenViewModel,
    openMenuExpand:Boolean,
    closeMenuExpand:()->Unit
 ){
     //トーストとクリップボードへの保存に使用するContext
    val context = LocalContext.current
     DropdownMenu(
         expanded = openMenuExpand,
         onDismissRequest = {closeMenuExpand()}//メニューを閉じる
     ) {
         favoriteColorScreenMenuList.forEachIndexed{index, menu ->
             DropdownMenuItem(
                 text = {Text(text = menu)},
                 onClick = {
                     closeMenuExpand()//メニューを閉じる
                     when(index){
                         //クリップボードにカラーコードをコピー 
                         0 -> viewModel.copyToClipBoard(context, colorItem.colorCode)
                         //色情報を変更するダイアログを表示
                         1 -> viewModel.updateOpenDialog(true)
                         //色をデータベースから削除する
                         2 -> viewModel.deleteColors(colorItem.id)
                         else -> {}//その他は何もしない
                     }
                 }
             )
             }
         }
     }
