package com.example.findcolorcode.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.findcolorcode.data.favoriteColorScreenSortMenuList

@Composable
 fun FavoriteColorScreenSortMenu(
    //昇順または降順の選択をViewに渡すためのメソッド
    openSortMenuExpand:Boolean,
    closeSortMenuCallBack:()-> Unit,
    sortTypeCallBack:(Int) -> Unit,
    currentSortOrder:Int//現在のソート
 ){
     DropdownMenu(
         expanded = openSortMenuExpand,
         onDismissRequest = {closeSortMenuCallBack()}//メニューを閉じる
     ) {
         favoriteColorScreenSortMenuList.forEachIndexed{index, menu ->
             DropdownMenuItem(
                 text = {
                     Row(verticalAlignment = Alignment.CenterVertically,
                         horizontalArrangement = Arrangement.End) {

                         //現在選択中のメニューの丸は青、そうでなければ透明
                             Box(
                                 modifier = Modifier
                                     .size(10.dp)
                                     .background(
                                         if (currentSortOrder == index) Color.Blue else Color.Transparent,
                                         CircleShape
                                     ),
                             )
                         // 丸とテキストの間に余白を設定
                         Spacer(modifier = Modifier.width(5.dp))

                         Text(text = menu, textAlign = TextAlign.End)
                         }
                 },
                 onClick = {
                          //1.日付が古い順,2.日付が新しい順
                          sortTypeCallBack(index)
                          closeSortMenuCallBack()
                      }
             )
             }
         }
     }
