package com.example.findcolorcode.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.findcolorcode.model.FavoriteColorDataClass
import com.example.findcolorcode.ui.theme.getDynamicTypography
import com.example.findcolorcode.viewmodel.ColorChoiceViewModel

@Composable
fun FavoriteColorContents(
    modifier: Modifier,
    viewModel: ColorChoiceViewModel,
    currentSquareIndex: Int
) {
    val favoriteColors by viewModel.favoriteColors.observeAsState(emptyList())

    if (favoriteColors.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "保存された色はありません",
                fontSize = getDynamicTypography().bodyLarge.fontSize,
                color = Color.Gray
            )
        }
    } else {
        // 4つずつに分割してグリッド風にする
        val chunkedColors = favoriteColors.chunked(4)

        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // 色が選択された時の処理
            val onFavoriteSquareSelected: (String) -> Unit = { selectedColorCode ->
                viewModel.updateColorCode(currentSquareIndex, selectedColorCode)
                viewModel.updateBackgroundColorCode(currentSquareIndex, selectedColorCode)
                viewModel.convertToRGB(currentSquareIndex)
            }

            items(chunkedColors) { rowColors ->
                FavoriteColorRow(
                    modifier = Modifier.fillMaxWidth(),
                    colorList = rowColors,
                    onFavoriteSquareSelected = onFavoriteSquareSelected
                )
            }
        }
    }
}

@Composable
private fun FavoriteColorRow(
    modifier: Modifier = Modifier,
    colorList: List<FavoriteColorDataClass>,
    onFavoriteSquareSelected: (String) -> Unit
) {
    Row(
        modifier = modifier.fillMaxHeight(0.3f),
        horizontalArrangement = Arrangement.Start,
    ) {
        colorList.forEach { colorData ->
            FavoriteColorSquare(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                colorData = colorData,
                onFavoriteSquareSelected = onFavoriteSquareSelected
            )
        }
        // 1行に満たない場合はSpacerで埋める
        if (colorList.size < 4) {
            repeat(4 - colorList.size) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun FavoriteColorSquare(
    modifier: Modifier = Modifier,
    colorData: FavoriteColorDataClass,
    onFavoriteSquareSelected: (String) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable { onFavoriteSquareSelected(colorData.colorCode) }
                .background(Color(android.graphics.Color.parseColor(colorData.colorCode)))
                .border(1.dp, Color.LightGray)
        )
        Text(
            text = colorData.colorName,
            fontSize = getDynamicTypography().labelSmall.fontSize,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}
