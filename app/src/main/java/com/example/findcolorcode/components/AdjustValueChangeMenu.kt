package com.example.findcolorcode.components

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.example.findcolorcode.data.adjustValueChangeMenuList
import com.example.findcolorcode.ui.theme.customTextFieldColors


//調節単位を変更するための入力フォームとドロップダウンメニューを表示する
//開閉状態(isMenuOpen)はAdjustValueChangeMenu内で完結させる
//-理由-
//ExposedDropdownMenuBoxの機能でTextFieldをクリックすると自動で状態が変更されるため
//トリガーとなるTextフィールドも同じコンポーネント内にあるため

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdjustValueChangeMenu(
    modifier: Modifier = Modifier,
    //現在の変更単位を受け取る
    value: Int?,
    updateValue: (Int?) -> Unit
) {
    var isMenuOpen by remember { mutableStateOf(false) }
    //TextFieldの制限文字数
    val limit = 4

    ExposedDropdownMenuBox(
        modifier = modifier.wrapContentHeight(),
        expanded = isMenuOpen,
        //Menu開閉状態を変更する
        onExpandedChange = {
            isMenuOpen = !isMenuOpen
        }
    ) {
        TextField(
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable),
            value = value?.toString() ?: "",
            onValueChange = { newValue: String ->
                //空白の場合はnullでupdateValueを更新する
                if (newValue.isEmpty()) {
                    updateValue(null)
                }
                // プロパティlimit分だけのも字数制限をつける
                // newValueは初期値文字列なのでIntに変換し1以上ならupdateValueに引き渡す
                else if (newValue.length <= limit) {
                    newValue.toInt().takeIf { it > 0 }?.let { updateValue(it) }
                } else {
                    //制限文字数以上なら直近の入力数値そのまま返す
                    value
                }
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                textAlign = TextAlign.Center
            ),
            //数字入力用キーボード
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            maxLines = 1,
            colors = customTextFieldColors()
        )
        ExposedDropdownMenu(
            expanded = isMenuOpen,
            //開閉状態をfalseに変更する
            onDismissRequest = { isMenuOpen = false }
        ) {
            adjustValueChangeMenuList.forEach { menu ->
                DropdownMenuItem(
                    text = { Text(menu.toString()) },
                    onClick = {
                        //調節単位を変更する
                        updateValue(menu)
                        //メニューを閉じる
                        isMenuOpen = false
                    }
                )
            }
        }
    }
}