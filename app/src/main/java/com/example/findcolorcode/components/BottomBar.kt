package com.example.findcolorcode.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.navOptions

@Composable
//selectedItemは選択中のタブのインデックスを示す
fun BottomBar(
    navController: NavController,
    currentBottomBarItem: Int
) {
    NavigationBar(modifier = Modifier.height(60.dp)) {
        //表示項目をenumClassで列挙する
        BottomBarTab.entries.forEachIndexed { index, item ->
            NavigationBarItem(
                //タブが選択されているかを判定
                //trueの時自動でバー上に強調表示される
                selected = currentBottomBarItem == index,
                //クリック時の動作を定義
                onClick = {
                    navController.navigate(
                        route = item.route,
                        navOptions = navOptions {
                            //現在開いているViewのrouteをpopUpToに渡しそのビューをバックスタックから削除する
                            // 削除Viewの状態保存をしておくことで再度表示した時に前回の状態の復元する
                            popUpTo(BottomBarTab.entries[currentBottomBarItem].route){ saveState= true }
                        }
                    )
                },
                //Iconとlabelを指定
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconId),
                        contentDescription = item.label,
                        modifier = Modifier.size(40.dp)
                    )
                }
            )
        }
    }
}