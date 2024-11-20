package com.example.findcolorcode.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.findcolorcode.data.favoriteColorActionsMenuList
import com.example.findcolorcode.model.FavoriteColorDataClass
import com.example.findcolorcode.viewmodel.FavoriteScreenViewModel
import java.net.URLEncoder


//FavoriteListScreen内のリストのmoreボタンを押すと表示されるメニュー
//選択しているお気に入りの色に関連する操作を行う
@Composable
 fun FavoriteColorActionsMenu(
    //ColorChoiceScreenに遷移する
    navController: NavHostController,
    //現在選択している色
    colorItem:FavoriteColorDataClass,
    viewModel: FavoriteScreenViewModel,
    //メニュー開閉フラグ
    openMenuExpand:Boolean,
    //menuを閉じるメソッド
    closeMenuExpand:()->Unit
 ){
     //トーストとクリップボードへの保存に使用するContext
    val context = LocalContext.current
     DropdownMenu(
         expanded = openMenuExpand,
         onDismissRequest = {closeMenuExpand()}//メニューを閉じる
     ) {
         favoriteColorActionsMenuList.forEachIndexed{ index, menu ->
             DropdownMenuItem(
                 text = {Text(text = menu)},
                 onClick = {
                     closeMenuExpand()//メニューを閉じる
                     when(index){
                         //ColorChoiceScreenに遷移する
                         //同時に左フラグ、色コードを引き渡す
                         0 -> moveToColorChoice(
                             navController,
                             "left",
                             colorItem.colorCode
                         )
                         //ColorChoiceScreenに遷移する
                         //同時に右フラグ、色コードを引き渡す
                         1 -> moveToColorChoice(
                             navController,
                             "right",
                             colorItem.colorCode)
                         //クリップボードにカラーコードをコピー
                         2 -> viewModel.copyToClipBoard(context, colorItem.colorCode)
                         //色情報を変更するダイアログを表示
                         3 -> viewModel.updateDialogOpen(true)
                         //色をデータベースから削除する
                         4 -> viewModel.deleteColors(colorItem.id)
                         else -> {}//その他は何もしない
                     }
                 }
             )
             }
         }
     }
fun moveToColorChoice(navController:NavHostController, direction:String, colorCode:String){
    //色コードに含まれる＃がURLに正しく変換できないからエンコードする
    val encodedColorCode = URLEncoder.encode(colorCode,"UTF-8")
    //URL形式で出力し指定したViewに遷移する（移動先：route colorChoice（ColorChoiceScreen））
    //directionとencodedColorCodeパラメータを引き渡す
     navController.navigate("colorChoice?direction=${direction}&colorCode=${encodedColorCode}")
}




