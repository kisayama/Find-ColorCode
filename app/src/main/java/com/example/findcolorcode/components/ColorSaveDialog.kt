package com.example.findcolorcode.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import com.example.findcolorcode.ui.theme.FindColorCodeTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.ui.theme.AppColors
import com.example.findcolorcode.model.ColorDataForColorChoice
import com.example.findcolorcode.ui.theme.customTextFieldColors

@ExperimentalMaterial3Api
@Composable

fun ColorSaveDialog(
    currentColorData:ColorDataForColorChoice,//ボタンごとのカラーデータ
    openDialogUpdate:() -> Unit//openDialogをfalseに変更するメソッド
) {
    FindColorCodeTheme {
        val dialogTextStyle = TextStyle(fontSize = 14.sp)
        //Roomのデータベース追加メソッドに引き渡すだけなのでコンポーネント内で状態管理を行う
        val saveName = remember { mutableStateOf("") }
        val saveMemo = remember { mutableStateOf("") }
        BasicAlertDialog(
            modifier = Modifier.wrapContentWidth(),
            onDismissRequest = { openDialogUpdate() },//ダイアログを閉じる
        ) {
            Surface(
                color = Color.White,//ダイアログの背景の色
                shape = RoundedCornerShape(8.dp),//角の形状とその丸みを指定する
                tonalElevation = AlertDialogDefaults.TonalElevation,//奥行きをつけるための影を設定
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //ダイアログのタイトル
                    Text(
                        text = "色を保存",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //ダイアログの左側に,保存する色を背景にしたBOXを配置
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .aspectRatio(1f)
                                    .weight(2f)
                                    .background(
                                        Color(
                                            android.graphics.Color.parseColor(
                                                currentColorData.backgroundColorCode
                                            )
                                        )
                                    )
                                    .border(1.dp, Color.LightGray)
                            )
                        Spacer(modifier = Modifier.width(10.dp))
                        //右側に名前とメモのテキストフィールドを配置する
                        Column(
                            modifier = Modifier
                                .weight(3f)
                                .padding(bottom = 8.dp),
                        ) {
                            //カラーコード表示
                            Text(
                                modifier = Modifier
                                    .border(3.dp, AppColors.gainsboro)
                                    .padding(8.dp),
                                text = currentColorData.backgroundColorCode,
                                style = TextStyle(fontSize = 16.sp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            //色の名前入力フォーム
                            TextField(
                                modifier = Modifier
                                    .background(Color.White)
                                    .height(50.dp)
                                    .border(1.dp, Color.LightGray),
                                value = saveName.value,
                                onValueChange = { newName -> saveName.value = newName },
                                colors = customTextFieldColors(),
                                placeholder = {
                                    Text(
                                        text = "色の名前",
                                        style = dialogTextStyle
                                    )
                                },
                                textStyle = dialogTextStyle,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            //色のメモ入力フォーム
                            TextField(
                                modifier = Modifier
                                    .height(150.dp)
                                    .border(1.dp, Color.LightGray),
                                value = saveMemo.value,
                                onValueChange = { newMemo -> saveMemo.value = newMemo },
                                colors = customTextFieldColors(),
                                placeholder = {
                                    Text(
                                        text = "色のメモを入力してください",
                                        style = dialogTextStyle
                                    )
                                },
                                textStyle = dialogTextStyle,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {

                        //キャンセルボタン
                        TextButton(
                            onClick = { openDialogUpdate() },// ダイアログの状態フラグを変更
                            modifier = Modifier
                                .weight(1f)
                                .background(AppColors.gainsboro)
                        ) {
                            Text(text = "キャンセル", color = Color.Black)
                        }

                        Spacer(modifier = Modifier.width(15.dp))

                        //決定ボタン
                        TextButton(
                            onClick = { openDialogUpdate() },/*TODO　ROOMにデータを保存する*/
                            modifier = Modifier
                                .weight(1f)
                                .background(AppColors.gainsboro)
                        ) {
                            Text(text = "決定", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}
