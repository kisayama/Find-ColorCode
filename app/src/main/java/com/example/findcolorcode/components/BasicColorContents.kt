package com.example.findcolorcode.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    modifier: Modifier,
    viewModel: ColorChoiceViewModel,
    currentSquareIndex: Int,
    allColorList: List<List<Pair<String,String>>>
) {
    //親コンポーネントのTabから画面の四割を割り当てられている
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            //BasicSquareが選択された時に呼び出される処理
            val onBasicSquareSelected: (String) -> Unit = { selectedColorCode ->
                //TextFieldの表示を変更
                viewModel.updateColorCode(currentSquareIndex, selectedColorCode)
                //squareの背景色を変更
                viewModel.updateBackgroundColorCode(currentSquareIndex, selectedColorCode)
                //スライダーのRGB値を変更
                viewModel.convertToRGB(currentSquareIndex)
            }
            val colorCodeList:List<List<String>> = allColorList.map { it->it.map { it.first } }
            items(colorCodeList) { list ->

                    BasicColorRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colorList =list,
                        onBasicSquareSelected
                    )
                }
            }
        }

//4つのBoxコンポーネントのBasicColorSquareを一行に並べるコンポーネント
@Composable
private fun BasicColorRow(
    modifier: Modifier = Modifier,
    colorList: List<String>,
    onBasicSquareSelected: (String) -> Unit
) {
    Row(
        modifier = modifier.fillMaxHeight(0.3f),
        //Row内の要素を水平方向に均等に配置する
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        //colorListは常に4つの要素を含む(要素はカラーコード)
        //各カラーコードをBasicColorSquareに引き渡す
        colorList.forEach { colorCode ->
            BasicColorSquare(
                modifier =modifier.weight(1f),
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
            //Boxをクリックすると選択されたカラーコードをonBasicSquareSelectedに引き渡す
            .padding(start = 5.dp, end = 5.dp )
            .clickable { onBasicSquareSelected(colorCode) }
            .background(Color(android.graphics.Color.parseColor(colorCode)))
            .border(1.dp, Color.LightGray)
            .aspectRatio(1f)//1:1の比率で正方形にする
    )
}
