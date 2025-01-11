package com.example.findcolorcode.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.findcolorcode.data.colorSchemeModeList
import com.example.findcolorcode.ui.theme.getDynamicTypography
import com.kisayama.findcolorcode.R

@Composable
fun ChangePalletModeButton(
    currentMode:String,
    updateValue: (String) -> Unit
){
    val mode by remember { mutableStateOf(currentMode) }
    var modeDescription by remember(mode) {
        mutableStateOf(when(mode){
            "analogic" -> "調和的、穏やか"
            "complement" -> "コントラスト"
            "analogic-complement" -> "バランス"
            "triad" -> "鮮やか"
            "quad" -> "とても鮮やか"
            "monochrome" -> "モノクロ"
            "monochrome-light" -> "モノクロライト"
            "monochrome-dark" -> "モノクロダーク"
            else -> "未設定"
         }
        )
    }

    var isMenuOpen by remember { mutableStateOf(false) }
    val fontSize = getDynamicTypography().bodyLarge.fontSize

    OutlinedButton(
        onClick = {isMenuOpen = true},
        modifier = Modifier
            .width(190.dp)
    ){
        Row {

            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.ic_changed),
                contentDescription = "モード変更",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(start = 3.dp)
                    .weight(1f),//余ったスペースをテキストが全て使う
                text = modeDescription,
                fontSize = fontSize
            )
        }

        DropdownMenu(
            expanded = isMenuOpen,
            onDismissRequest = { isMenuOpen = false }) {
            colorSchemeModeList.forEach { menu ->
                DropdownMenuItem(
                    text = { Text(text = menu.first)},
                    onClick = {
                        updateValue(menu.second)
                        modeDescription = menu.first
                        isMenuOpen = false
                    }
                )
            }
        }
    }
}
