package com.example.findcolorcode.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.app.ui.theme.AppColors
import com.example.findcolorcode.model.FavoriteColorDataClass
import com.example.findcolorcode.ui.theme.FindColorCodeTheme
import com.example.findcolorcode.ui.theme.getDynamicTypography
import com.example.findcolorcode.ui.theme.customTextFieldColors

@ExperimentalMaterial3Api
@Composable

//色情報（名前、メモ）を変更するときに表示するダイアログ
fun ColorUpdateDialog(
    currentColorData: FavoriteColorDataClass,//現在のカラーデータ
    updateFavoriteColor: (FavoriteColorDataClass) -> Unit,//データベースを変更するメソッド
    //viewModelのopenDialogプロパティをfalseに変更するメソッド
    dismissDialog: () -> Unit,
) {
    //データベース変更メソッドに引き渡すためにcurrentColorDataの名前とメモを保持する
    val saveName = remember { mutableStateOf(currentColorData.colorName) }
    val saveMemo = remember { mutableStateOf(currentColorData.colorMemo) }

    FindColorCodeTheme {
        val dialogTextStyle = TextStyle(fontSize = getDynamicTypography().bodyLarge.fontSize)
        BasicAlertDialog(
            modifier = Modifier.wrapContentWidth(),
            // ダイアログが閉じられたときに、フラグの更新処理を実行する
            onDismissRequest = { dismissDialog() },
        ) {
            //画面をタッチしたらキーボードを非表示にする
            val keyboardController = LocalSoftwareKeyboardController.current
            //画面をタッチした時にフォーカスを解除する
            val focusManager = LocalFocusManager.current
            Surface(
                color = Color.White,//ダイアログの背景の色
                shape = RoundedCornerShape(8.dp),//角の形状とその丸みを指定する
                tonalElevation = AlertDialogDefaults.TonalElevation,//奥行きをつけるための影を設定
                modifier = Modifier
                    .pointerInput(Unit) {
                        //入力リスナーの種類を設定する　この場合はタップ
                        detectTapGestures(onTap = {
                            //キーボードを隠す
                            keyboardController?.hide()
                            //フォーカスを解除する
                            focusManager.clearFocus()
                        }
                        )
                    }
                    .onKeyEvent { keyEvent ->
                        //KeyBoardを離した時かつそのキーボードがエンターキーの時にキーボードを閉じる
                        if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Enter) {
                            focusManager.clearFocus()
                            true
                        } else {
                            false
                        }
                    }
            )
             {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //ダイアログのタイトル
                    Text(
                        text = "色情報を変更",
                        style = getDynamicTypography().titleLarge,
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
                                    Color(android.graphics.Color.parseColor(currentColorData.colorCode))
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
                                text = currentColorData.colorCode,
                                style = TextStyle(fontSize = getDynamicTypography().bodyLarge.fontSize)
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
                                singleLine = true
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
                    //左側にキャンセルボタン、右側に決定ボタンを配置する
                    Row {
                        //キャンセルボタン
                        TextButton(
                            onClick = { dismissDialog() },// ダイアログの状態フラグを変更
                            modifier = Modifier
                                .weight(1f)
                                .background(AppColors.gainsboro)
                        ) {
                            Text(text = "キャンセル", color = Color.Black)
                        }

                        Spacer(modifier = Modifier.width(15.dp))

                        //決定ボタン
                        TextButton(
                            onClick = {
                                dismissDialog()//openDialogをfalseに変更する（閉じた状態）
                                val currentTimeMillis = System.currentTimeMillis()//現在の日時

                                val saveData = FavoriteColorDataClass(
                                    id = currentColorData.id,
                                    colorCode = currentColorData.colorCode,
                                    colorName = saveName.value,
                                    colorMemo = saveMemo.value,
                                    editDateTime = currentTimeMillis//1970/1/1からの経過時間をミリビョウで表す
                                )
                                updateFavoriteColor(saveData)
                            },
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
