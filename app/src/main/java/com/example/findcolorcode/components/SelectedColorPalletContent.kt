package com.example.findcolorcode.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

    @Composable
    fun SelectedColorPalletContent(modifier: Modifier, selectedSquare: Int) {
        Column(modifier = modifier.fillMaxSize()) {
            Text(text = "選択している色のカラーパレット")
        }
    }
