package com.example.findcolorcode.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.findcolorcode.R

//色調整ボタンと単位調整用ボタンを1行に配置するためのコンポーネント
@Composable
fun AdjustValueBar(
    adjustValue: (Int) -> Unit,
) {
    var value by remember { mutableStateOf<Int?>(5) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(start = 75.dp, end = 75.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        //adjustValueには負の値に変換したvalueを引き渡す
        IconButton(
            onClick = { value?.let { adjustValue(-1 * it) } },
        ) {
            //マイナスボタン
            Icon(
                painter = painterResource(id = R.drawable.ic_minus),
                contentDescription = "Plus"
            )
        }

        //調整メニュー
        AdjustValueChangeMenu(
            modifier = Modifier.width(100.dp),
            value = value,
            updateValue = { newValue -> value = newValue }
        )

        //adjustValueに引き渡すvalueは明示的に正の値に変換している
        IconButton(
            onClick = { value?.let { adjustValue(1 * it) } },
        ) {
            //プラスボタン
            Icon(
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = "minus"
            )
        }
    }
}

@Preview
@Composable
fun PreviewAdjustValueBar() {
    AdjustValueBar(adjustValue = {})
}
