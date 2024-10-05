package com.example.findcolorcode.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.app.ui.theme.AppColors
import com.example.findcolorcode.R
import com.example.findcolorcode.viewmodel.ColorChoiceViewModel

//TODO ランダムカラーパレットの導入
//TODO　枠線の動作
//TODO 時間があればSLiderのthumbを調整するためにカスタムに変更するか検討する

@Composable
fun ColorChoiceScreen(navController: NavController, viewModel: ColorChoiceViewModel) {

    //==squareIndex==
    //選択されたsquareのインデックスを取得
    val selectedSquare by viewModel.selectedSquare.observeAsState(1)
    val square1Index = 1
    val square2Index = 2

    // ====== Square1 Color Data =======
    // square1のRGB値を取得
    val red1 by viewModel.red1.observeAsState(255)
    val green1 by viewModel.green2.observeAsState(255)
    val blue1 by viewModel.blue1.observeAsState(255)
    //square1のカラーコードを取得
    val square1ColorCode = viewModel.colorSquare1.observeAsState("#FFFFFFF").value
    val square1ColorData = ColorData(square1ColorCode, red1, green1, blue1)//RGBとカラーコードをまとめたColorData

    // ===== Square2 Color Data =====
    // square2のRGB値を取得
    val red2 by viewModel.red2.observeAsState(255)
    val green2 by viewModel.green2.observeAsState(255)
    val blue2 by viewModel.blue2.observeAsState(255)
    //square2のカラーコードを取得
    val square2ColorCode = viewModel.colorSquare2.observeAsState("#FFFFFFF").value
    val square2ColorData = ColorData(square2ColorCode, red2, green2, blue2)

    //Boxを横一列に2つ並べる
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.Top,//全体を中央揃え
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //四角を横に二つ並べTextFieldとButtonをその下に2：1の比率で配置
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            //Square1
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start,
            ) {
                ColorColumn(viewModel, selectedSquare, square1ColorData, square1Index)

                ColorColumn(viewModel, selectedSquare, square2ColorData, square2Index)
            }
            Spacer(modifier = Modifier.height(20.dp))//四角とシークバーの間のスペース
            //シークバーを三つ縦に並べるためのColumn
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)//シークバー間に15dpのスペースを入れる
            ) {
                val currentColorData =
                    if (square1Index == selectedSquare) square1ColorData else square2ColorData

                SeekBar(colorDataRGB = currentColorData.red, sliderColor = Color.Red,
                    onValueChange = {newValue -> }
                )
                SeekBar(colorDataRGB = currentColorData.green, sliderColor = Color.Green,
                    onValueChange = {newValue ->currentColorData.green }
                )
                SeekBar(colorDataRGB = currentColorData.blue, sliderColor = Color.Blue,
                    onValueChange = {newValue ->currentColorData.blue }
                )

                }

            }
        }
    }

    data class ColorData(val colorCode: String, val red: Int, val blue: Int, val green: Int)

    @Composable
    fun ColorColumn(
        viewModel: ColorChoiceViewModel,
        selectedSquare: Int,//現在選択中のsquareのIndex
        colorData: ColorData,
        squareIndex: Int//各squareのIndex
    ) {
        val isSelected: Boolean = squareIndex == selectedSquare

        //squareを表示
        ColorSquare(color = colorData.colorCode,
            isSelected = isSelected,
            onSquareSelected = {
                viewModel.changeSelectedSquare(squareIndex)
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            ColorCodeText(
                modifier = Modifier,
                colorCode = colorData.colorCode,
                onValueChanged = { newvalue -> colorData.colorCode })

            ColorSaveBtn(
                modifier = Modifier.weight(1f)
            )
        }
    }

        @Composable
        //シークバーで作成した色を表示するBox
        fun ColorSquare(color: String, isSelected: Boolean, onSquareSelected: () -> Unit) {
            val borderColor = if (isSelected) Color.Black else Color.Gray
            Box(
                modifier = Modifier
                    .size(160.dp)
                    //クリック時にisSelectedをチェックしtrueなら1、falseなら2をonSquareSelectedにセットする
                    .clickable { onSquareSelected() }
                    .background(Color(android.graphics.Color.parseColor(color)))//背景の色を設定
                    .border(2.dp, borderColor)
            )
        }

        @Composable
        fun ColorCodeText(
            modifier: Modifier = Modifier,
            colorCode: String,
            onValueChanged: (String) -> Unit
        ) {
            TextField(
                value = colorCode,
                onValueChange = { new -> onValueChanged(new) },
                label = { Text("カラーコード") },
                modifier = modifier,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = AppColors.White,//フォーカス時の色
                    unfocusedContainerColor = AppColors.White,
                    focusedIndicatorColor = AppColors.Black,
                    focusedLabelColor = AppColors.Gray,
                    unfocusedLabelColor = AppColors.Gray
                )
            )
        }

        @Composable
        //色を保存するためのボタン
        fun ColorSaveBtn(modifier: Modifier = Modifier) {
            IconButton(
                onClick = {
                    //TODO SaveColorDialogを表示する
                }, modifier = modifier
            ) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_save_btn
                    ),
                    contentDescription = "Save Color",
                    modifier = Modifier
                )
            }
        }

        //色を作るためのRGBのシークバー
        @Composable
        fun SeekBar(colorDataRGB: Int,sliderColor: Color, onValueChange: (Int) -> Unit) {
            //TODO onValueChangeの定義
            Slider(
                //あらかじめIf文でselectedSquareに対応するcolorDataを引き渡す
                value = colorDataRGB.toFloat(),//スライダーを滑らかに動かすためにfloatを指定
                colors = SliderDefaults.colors(
                    thumbColor = sliderColor,
                    activeTrackColor = sliderColor,
                    activeTickColor = sliderColor //バーの動作中の色
                ),
                onValueChange = { onValueChange(it.toInt()) },//スライダーの値Float型をIntに変換する
                valueRange = 0f..255f,
                modifier = Modifier.fillMaxWidth(0.9f)//スライダーの横幅は最大値の75%
            )
        }

