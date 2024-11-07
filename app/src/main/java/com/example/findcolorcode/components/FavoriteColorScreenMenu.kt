package com.example.findcolorcode.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import com.example.findcolorcode.data.favoriteColorScreenMenu
import com.example.findcolorcode.model.FavoriteColorDataClass
import com.example.findcolorcode.viewmodel.FavoriteScreenViewModel

@Composable
 fun FavoriteColorListMenu(colorItem:FavoriteColorDataClass,viewModel: FavoriteScreenViewModel){

     //トーストとクリップボードへの保存に使用するContext
    val context = LocalContext.current
     DropdownMenu(
         expanded = viewModel.menuExpand.value?:false,
         onDismissRequest = { viewModel.updateMenuExpand(false) }
     ) {
         favoriteColorScreenMenu.forEachIndexed{index, menu ->
             DropdownMenuItem(
                 text = {Text(text = menu)},
                 onClick = {
                     viewModel.updateMenuExpand(false)
                     when(index){
                         //クリップボードにカラーコードをコピー 
                         0 -> {
                             viewModel.copyToClipBoard(context, colorItem.colorCode)
                         }
                         //色情報を変更するダイアログを表示
                         1 -> viewModel.updateOpenDialog(true)//TODO ダイアログの再利用
                         //色をデータベースから削除する
                         2 -> viewModel.deleteColors(colorItem.id)
                         else -> {}//例外では何も行わない
                     }
                 }
             )
             }
         }
     }
