package com.example.findcolorcode.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
//selectedItemは選択中のタブのインデックスを示す
fun BottomBar(
    navController: NavController,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit) {
    NavigationBar {
        //表示項目をenumClassで列挙する
        BottomBarTab.entries.forEachIndexed { index, item ->
            NavigationBarItem(
                //タブが選択されているかを判定
                selected = selectedItem == index,
                //クリック時の動作を定義
                onClick = {
                    onItemSelected(index)//indexを呼び出し元コンポーネントに渡す
                    navController.navigate(item.route)
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