package com.example.findcolorcode.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRowScopeInstance.align
import androidx.compose.foundation.layout.FlowRowScopeInstance.weight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.InspectableModifier
import androidx.compose.ui.unit.dp
import com.example.findcolorcode.model.ColorDataForColorChoice

@ExperimentalMaterial3Api
@Composable
//,保存する名前とメモ,ダイアログの表示状態を表すフラグ(表示=1)

//TODO 以下引数は多分　関数を渡すことになると思う
fun SaveDialog(currentColorData:ColorDataForColorChoice,//colorSaveBtnを押した時点のnowColorData
               saveName:MutableState<String>,//保存する名前を管理するMutableState
               saveMemo:MutableState<String>,//保存するメモを管理するMutableState
               openDialog:MutableState<Boolean>){//ダイアログを開くOR閉じるためのフラグ
    BasicAlertDialog(
        onDismissRequest = { openDialog.update(false) },//ダイアログを閉じる
    ){
        Surface (
            modifier = Modifier.wrapContentWidth(),
            shape = RoundedCornerShape(8.dp),//角の形状とその丸みを指定する
            tonalElevation = AlertDialogDefaults.TonalElevation,//奥行きをつけるための影を設定
        ){
            Column (modifier = Modifier.padding(16.dp)){
                //ダイアログのタイトル
                Text(text = "色を保存",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Row (modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //ダイアログの左側に保存する色を背景にしたBOXを配置
                    Box(modifier = Modifier
                        .size(50.dp)
                        .aspectRatio(1f)
                        .weight(2f)
                        .background(Color(android.graphics.Color.parseColor(currentColorData.backgroundColorCode)))
                    )
                    //右側に名前とメモのテキストフィールドを配置する
                    Column (modifier = Modifier
                         .weight(3f)
                         .padding(bottom = 8.dp),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        //カラーコード表示
                        Text(text = currentColorData.backgroundColorCode)
                        //色の名前入力フォーム
                        TextField(
                           value = saveName.value,
                           onValueChange = { newName -> saveName.value = newName},
                           placeholder = { Text(text = "色に名前をつけてください") }
                        )
                        //色のメモ入力フォーム
                        TextField(
                           value = saveMemo.value,
                           onValueChange = { newMemo -> saveMemo.value = newMemo},
                           placeholder = { Text(text = "色のメモを入力してください") }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row {

                    //キャンセルボタン
                    TextButton(
                        onClick = { openDialog.update(false) },//ダイアログを閉じる処理
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "キャンセル")
                    }

                    //決定ボタン
                    TextButton(
                        onClick = { openDialog.update(false)/*TODO　ROOMにデータを保存する*/ },//ダイアログを閉じる処理
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "決定")
                     }
                }
            }
        }
    }
}