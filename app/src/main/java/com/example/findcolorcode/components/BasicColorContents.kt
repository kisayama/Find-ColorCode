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
   .padding(top = 72.dp) ,
  verticalArrangement = Arrangement.Center,
  horizontalAlignment = Alignment.CenterHorizontally
 ) {
  Column(
   verticalArrangement = Arrangement.spacedBy(12.dp),//オブジェクトごとに垂直方向に間隔を入れる
  ) {
   //BasicSquareが選択された時の処理
   val onBasicSquareSelected: (String) -> Unit = {
    //選択されたBasicSquareのカラーコードを取得する。
    // updateColorメソッドを使用して現在選択しているsquareの背景の色を書き換える
     selectedColorCode ->
    //TextFieldの表示を変更
    viewModel.updateColorCode(selectedSquare, selectedColorCode)
    //squareの背景色を変更
    viewModel.updateBackgroundColorCode(selectedSquare, selectedColorCode)
    //RGB値を変更
    viewModel.convertToRGB(selectedSquare)
   }
   //縦に3つ並べる
   BasicColorRow(modifier = Modifier.weight(1f), colorList = colorList1, onBasicSquareSelected)
   BasicColorRow(modifier = Modifier.weight(1f), colorList = colorList2, onBasicSquareSelected)
   BasicColorRow(modifier = Modifier.weight(1f), colorList = colorList3, onBasicSquareSelected)
  }
 }
}

@Composable
private fun BasicColorRow(
 modifier: Modifier = Modifier,
 colorList: List<String>,
 onBasicSquareSelected: (String) -> Unit
) {
 Row(
  modifier = Modifier
   .fillMaxWidth()
   .padding(start = 8.dp, end = 8.dp),
  horizontalArrangement = Arrangement.SpaceEvenly,
 ) {
  BasicColorSquare(
   colorCode = colorList[0],
   onBasicSquareSelected = onBasicSquareSelected
  )
  BasicColorSquare(
   colorCode = colorList[1],
   onBasicSquareSelected = onBasicSquareSelected
  )
  BasicColorSquare(
   colorCode = colorList[2],
   onBasicSquareSelected = onBasicSquareSelected
  )
  BasicColorSquare(
   colorCode = colorList[3],
   onBasicSquareSelected = onBasicSquareSelected
  )
 }
}



@Composable
 private fun BasicColorSquare(
 modifier: Modifier = Modifier,
 colorCode:String,
 onBasicSquareSelected: (String)->Unit){
  Box (
   modifier = modifier
    .padding(end = 8.dp)
    .size(70.dp)
    .clickable { onBasicSquareSelected(colorCode) }
    .background(Color(android.graphics.Color.parseColor(colorCode)))
    .border(2.dp, Color.LightGray)
    .aspectRatio(1f)//1:1比率
  )
 }
