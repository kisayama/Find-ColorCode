package com.example.findcolorcode.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.findcolorcode.viewmodel.ColorChoiceViewModel

//1行に4つのBOXを配置するBasicColorRowコンポーネントを
//縦に3つ並べるコンポーネント
@Composable
fun BasicColorContents(
    viewModel: ColorChoiceViewModel,
    selectedSquare: Int,
    colorList1: List<String>,
    colorList2: List<String>,
    colorList3: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 72.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            //各オブジェクト(BasicRow)間に垂直方向の間隔を設定
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            //BasicSquareが選択された時に呼び出される処理
            val onBasicSquareSelected: (String) -> Unit = { selectedColorCode ->
                //TextFieldの表示を変更
                viewModel.updateColorCode(selectedSquare, selectedColorCode)
                //squareの背景色を変更
                viewModel.updateBackgroundColorCode(selectedSquare, selectedColorCode)
                //シークバーのRGB値を変更
                viewModel.convertToRGB(selectedSquare)
            }
            //縦に3つ並べる
            BasicColorRow(colorList = colorList1, onBasicSquareSelected)
            BasicColorRow(colorList = colorList2, onBasicSquareSelected)
            BasicColorRow(colorList = colorList3, onBasicSquareSelected)
        }
    }
}

//4つのBoxコンポーネントのBasicColorSquareを一行に並べるコンポーネント
@Composable
private fun BasicColorRow(
    colorList: List<String>,
    onBasicSquareSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        //Row内の要素を水平方向に均等に配置する
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        //colorListは常に4つの要素を含む(要素はカラーコード)
        //各カラーコードをBasicColorSquareに引き渡す
        colorList.forEach { colorCode ->
            BasicColorSquare(
                colorCode = colorCode,
                onBasicSquareSelected = onBasicSquareSelected
            )
        }
    }
}


@Composable
private fun BasicColorSquare(
    modifier: Modifier = Modifier,
    colorCode: String,
    onBasicSquareSelected: (String) -> Unit
) {
    Box(
        modifier = modifier
            .padding(end = 8.dp)
            .size(70.dp)
            //Boxをクリックすると選択されたカラーコードをonBasicSquareSelectedに引き渡す
            .clickable { onBasicSquareSelected(colorCode) }
            .background(Color(android.graphics.Color.parseColor(colorCode)))
            .border(1.dp, Color.LightGray)
            .aspectRatio(1f)//1:1の比率で正方形にする
    )
}
