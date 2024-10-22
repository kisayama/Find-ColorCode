package com.example.findcolorcode.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.findcolorcode.model.ColorDataForColorChoice
import com.example.findcolorcode.repository.ColorSchemeRepositoryImpl
import com.example.findcolorcode.viewmodel.ColorChoiceViewModel

@Composable
    fun SelectedColorPalletContent(
    modifier: Modifier,
    viewModel:ColorChoiceViewModel,
    square1ColorData:ColorDataForColorChoice,
    square2ColorData:ColorDataForColorChoice,
    selectedSquare:Int,
    colorList: List<String>?) {

    //selectedSquareに応じて使用するColorDataを決定する
    val currentColorData = if (1 == selectedSquare) square1ColorData else square2ColorData

    // colorListがnullならデフォルト値を使用する
    val adjustList = colorList ?: listOf("#FFFFFF", "#FFFFFF", "#FFFFFF", "#FFFFFF", "#FFFFFF")

    //Pallet選択時の処理
    val palletsquareSellected : (String) -> Unit = { palletColorCode ->
        //TextFieldの表示を変更
        viewModel.updateColorCode(selectedSquare, palletColorCode)
        //squareの背景色を変更
        viewModel.updateBackgroundColorCode(selectedSquare, palletColorCode)
        //RGB値を変更
        viewModel.convertToRGB(selectedSquare)
    }
        Column(modifier = modifier.fillMaxSize()
            .padding(top = 66.dp, bottom = 10.dp)
        ){
            PalletCreateButton(
                //次はここから
                onButtonClicked = {viewModel.fetchColorScheme(currentColorData.colorCode)}
            )
            Row(modifier.padding(start = 10.dp, end = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly){
                PalletColorSquare(
                    colorCode = adjustList[0],
                    onPalletSquareSelected = palletsquareSellected
                )
                PalletColorSquare(
                    colorCode = adjustList[0],
                    onPalletSquareSelected = palletsquareSellected
                )
                PalletColorSquare(
                    colorCode = adjustList[0],
                    onPalletSquareSelected = palletsquareSellected
                )
                PalletColorSquare(
                    colorCode = adjustList[0],
                    onPalletSquareSelected = palletsquareSellected
                )
                PalletColorSquare(
                    colorCode = adjustList[0],
                    onPalletSquareSelected = palletsquareSellected
                )
            }
        }
        }

@Composable
    private fun PalletCreateButton(onButtonClicked:() -> Unit) {
    OutlinedButton(onClick = {onButtonClicked() }){
            Text("選択した色からカラーパレットを作成します")
        }
}

//APIから受け取ったカラーコードを表示するSquare
@Composable
    private fun PalletColorSquare(
    modifier: Modifier = Modifier,
    colorCode:String,
    onPalletSquareSelected: (String) -> Unit){
    Box(
        modifier = modifier
            .padding(end = 8.dp)
            .size(70.dp)
            .background(
                try {
                    Color(android.graphics.Color.parseColor(colorCode))
                } catch (e: IllegalArgumentException) {
                    Color.White
                }
            )
            .border(1.dp, androidx.compose.ui.graphics.Color.LightGray)
            .aspectRatio(1f) //1:1比率
            .clickable { onPalletSquareSelected(colorCode) }
    )
    }

@Preview
@Composable
fun PreviewPallet(){
    PalletColorSquare(modifier = Modifier,colorCode = "#FFFFFF", onPalletSquareSelected = {})
}

