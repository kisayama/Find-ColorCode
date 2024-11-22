package com.example.findcolorcode.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
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
    //現在の変更単位を受け取る
    value: Int,
    updateValue: (Int) -> Unit
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isMenuOpen,
        //Menu開閉状態を変更する
        onExpandedChange = {new -> isMenuOpen = new}
    ) {
        TextField(
            value = value.toString(),
            onValueChange = {newValue ->
                //newValueは初期値文字列なのでIntかNullに変換し
                // Nullじゃないかつ0以上なら処理する。
               newValue.toIntOrNull()?.takeIf { it > 0 }?.let{updateValue(it)}
            },
            colors = customTextFieldColors(),
            //数字入力用キーボード
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            maxLines = 1
        )
        ExposedDropdownMenu(
            expanded = isMenuOpen,
            //開閉状態をfalseに変更する
            onDismissRequest = { isMenuOpen = false }
        ) {
            adjustValueChangeMenuList.forEach{ menu ->
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