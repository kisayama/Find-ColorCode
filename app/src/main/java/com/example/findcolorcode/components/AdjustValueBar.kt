package com.example.findcolorcode.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.findcolorcode.R

@Composable
fun AdjustValueBar(
    adjustValue: (Int) -> Unit
) {
    var value by remember { mutableIntStateOf(5) }

    //マイナスボタン
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        //adjustValueには負の値に変換したvalueを引き渡す
        IconButton(onClick = { adjustValue(-1 * value) }) {
            Icon(
                modifier = Modifier.size(50.dp),
                painter = painterResource(id = R.drawable.ic_minus),
                contentDescription = "Plus"
            )
        }

        //調整メニュー
        AdjustValueChangeMenu(
            value = value,
            updateValue = {newValue -> value = newValue}
        )

        //adjustValueに引き渡すvalueは明示的に正の値に変換している
        IconButton(onClick = { adjustValue(1 * value) }) {
            Icon(
                modifier = Modifier.size(50.dp),
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = "minus"
            )
        }
    }
}
@Preview
@Composable
fun PreviewAdjustValueBar(){
    AdjustValueBar(adjustValue = {})
}
