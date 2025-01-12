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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.findcolorcode.model.ColorDataForColorChoice
import com.example.findcolorcode.ui.theme.getDynamicTypography
import com.example.findcolorcode.viewmodel.ColorChoiceViewModel

// 選択した色のカラーコードを基に、APIからカラーパレットを取得するView
// ViewModelで処理した結果を受け取り、取得したカラーパレットを表示する
@Composable
fun ColorPalletContents(
    modifier: Modifier = Modifier,
    viewModel: ColorChoiceViewModel,
    currentSquareIndex: Int,
    square2ColorData: ColorDataForColorChoice,
    square1ColorData: ColorDataForColorChoice
) {

    val initialColorPalletList =
        //初期値のリスト
        listOf("#FFFFFF", "#FFFFFF", "#FFFFFF", "#FFFFFF", "#FFFFFF")

    //viewModelのcolorPalletListをobserveAsStateで取得しUIに反映する
    //APIからのレスポンスで取得したカラーコードを保持する
    val colorPalletList by viewModel.colorPalletList.observeAsState(initialColorPalletList)

    //currentSquareIndexに応じて使用するColorDataを決定する
    val currentColorData = if (1 == currentSquareIndex) square1ColorData else square2ColorData

    //Pallet選択時の処理
    val palletSquareSelected: (String) -> Unit = { palletColorCode ->
        //TextFieldの表示を変更
        viewModel.updateColorCode(currentSquareIndex, palletColorCode)
        //squareの背景色を変更
        viewModel.updateBackgroundColorCode(currentSquareIndex, palletColorCode)
        //RGB値を変更
        viewModel.convertToRGB(currentSquareIndex)
    }
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        
        //カラーパレットプレビュー用の5つの四角
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            colorPalletList.forEach { colorCode ->
                PalletColorSquare(
                    colorCode = colorCode,
                    onPalletSquareSelected = palletSquareSelected
                )
            }
         }
        Spacer(modifier = Modifier.height(30.dp))
        //モード変更ボタンと作成ボタン
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center
        ){
            ChangePalletModeButton(
                currentMode = viewModel.palletMode.value ?: "analogic"
            ) {
                viewModel.changePalletMode(it)
            }
             Spacer(modifier = Modifier.width(20.dp))
             //API通信を行うトリガーボタン
                PalletCreateButton(
                    //ボタン押下時の処理
                    onButtonClicked = {
                        //現在背景色に使用されている色をAPIに引き渡す
                        //念の為正しいHEXが入力されているか検証する
                        //パースできない値が入力されていない場合はトーストを表示する
                        //API通信を行う　ViewModel自身の動作でcolorPalletListを更新するのでここでは操作を行わない
                        val currentColorCode = currentColorData.backgroundColorCode
                        val colorCode = viewModel.convertToHexColorCode(currentColorCode)
                        if (colorCode != null) {
                            viewModel.updateToastMessage("カラーパレット作成中...")
                            viewModel.fetchColorScheme(colorCode)
                        } else {
                            viewModel.updateToastMessage("正しい色を入力してください。（例: #FFFFFF または whiteなど）")
                        }
                    }
                )
            }
        }
    }

//API通信を行うトリガーボタン
@Composable
private fun PalletCreateButton(
    onButtonClicked: () -> Unit
) {
    OutlinedButton(
        onClick = { onButtonClicked() }
    ) {
        Text("カラーパレット作成",
            fontSize = getDynamicTypography().bodyLarge.fontSize
        )
    }
}

//APIから受け取ったカラーコードを表示するSquare
@Composable
private fun PalletColorSquare(
    colorCode: String,
    onPalletSquareSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .size(65.dp)
            .background(
                try {
                    Color(android.graphics.Color.parseColor(colorCode))
                } catch (e: IllegalArgumentException) {
                    Color.White
                }
            )
            .border(1.dp, Color.LightGray)
            .aspectRatio(1f) //1:1比率
            .clickable { onPalletSquareSelected(colorCode) }
    )
}

