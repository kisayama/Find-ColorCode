package com.example.findcolorcode.components

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.findcolorcode.R


//enumでボトムバーに表示するタブのアイコンとラベル,ルートを列挙しておく
//forEachIndexedを使ってタブのインデックスに基づいてアイコンとラベル、ルートを指定する
//列挙型の順序がボトムバーの表示順序に影響する
enum class BottomBarTab (
    val iconId: Int,
    val label:String,
    val route: String
){
    //色選択画面
    ColorChoice(
        iconId = R.drawable.ic_brush_24,
        label = "ColorChoice",
        route = "colorChoice"
    ),
    //色保存画面
    FavoriteList(
        iconId = R.drawable.ic_bookmarks,
        label = "FavoriteList",
        route = "favoriteList"
    )
}

