package com.example.findcolorcode.components

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import com.example.findcolorcode.data.adjustValueChangeMenuList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdjustValueChangeMenu(
    openMenuExpand: Boolean,
    closeMenuExpand: (Boolean) -> Unit,
    //現在の変更単位を受け取る
    value: Int,
    updateValue: (Int) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = openMenuExpand,
        onExpandedChange = closeMenuExpand
    ) {
        TextField(
            value = value.toString(),
            onValueChange = { newValue -> updateValue(newValue.toInt()) },
        )
        adjustValueChangeMenuList.forEachIndexed { index, menu ->
            DropdownMenuItem(
                text = { menu.toString() },
                onClick = {updateValue(menu)}
            )
        }
    }
}