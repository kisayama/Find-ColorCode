package com.example.findcolorcode.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.app.ui.theme.AppColors
import com.example.findcolorcode.R
import com.example.findcolorcode.components.BasicColorContents
import com.example.findcolorcode.data.basicColorsList1
import com.example.findcolorcode.data.basicColorsList2
import com.example.findcolorcode.data.basicColorsList3
import com.example.findcolorcode.model.ColorDataForColorChoice
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
    val green1 by viewModel.green1.observeAsState(255)
    val blue1 by viewModel.blue1.observeAsState(255)
    //square1のカラーコードを取得(TextFieldに表示する値として使用する)
    val square1ColorCode by viewModel.square1ColorCode.observeAsState("#FFFFFF")
    //ユーザーがテキスト入力中に背景色が変わらないように squareColorCodeとは別に背景色を管理する変数を用意しておく
    val square1BackgroundColorCode by viewModel.square1BackgroundColorCode.observeAsState("#FFFFFF")
    val square1ColorData = ColorDataForColorChoice(square1ColorCode, square1BackgroundColorCode,red1, green1, blue1)//RGBとカラーコードをまとめたColorData

    // ===== Square2 Color Data =====
    // square2のRGB値を取得
    val red2 by viewModel.red2.observeAsState(255)
    val green2 by viewModel.green2.observeAsState(255)
    val blue2 by viewModel.blue2.observeAsState(255)
    //square2のカラーコードを取得
    val square2ColorCode by viewModel.square2ColorCode.observeAsState("#FFFFFF")
    val square2BackgroundColorCode by viewModel.square2BackgroundColorCode.observeAsState("#FFFFFF")
    val square2ColorData = ColorDataForColorChoice(square2ColorCode, square2BackgroundColorCode,red2, green2, blue2)


    val toastMessage = remember { mutableStateOf("") }
    @Composable
    fun ShowToast(toastMessage: String) {
        val context = LocalContext.current
        //LaunchedEffectは指定したキーが変更された時に{}内を実行する
        LaunchedEffect(toastMessage) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        }
    }

    // 全体をColumnで囲んでレイアウトを縦方向に
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp), // 四角とシークバーの間のスペース
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Boxを横一列に2つ並べる
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ColorColumn(viewModel, selectedSquare, square1ColorData, square1Index,toastMessage)
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ColorColumn(viewModel, selectedSquare, square2ColorData, square2Index,toastMessage)
            }
        }

        // シークバーを表示
        SeekBars(
            selectedSquare = selectedSquare,
            square1Index = square1Index,
            viewModel = viewModel,
            square1ColorData = square1ColorData,
            square2ColorData = square2ColorData
        )
        ColorPalletTab(selectedSquare)
    }

}

    @SuppressLint("SuspiciousIndentation")
    @Composable
    fun ColorColumn(
        viewModel: ColorChoiceViewModel,
        selectedSquare: Int,//現在選択中のsquareのIndex
        colorData: ColorDataForColorChoice,
        squareIndex: Int,//各squareのIndex
        toastMessage:MutableState<String>
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)//シークバー間に15dpのスペースを入れる
        ) {
            val isSelected: Boolean = squareIndex == selectedSquare

            //squareを表示
            ColorSquare(backgroundColor = colorData.backgroundColorCode,
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
                    modifier = Modifier.weight(2f),
                    colorCode = colorData.colorCode
                ) { newvalue ->
                    viewModel.updateColorCode(squareIndex, newvalue)
                    val colorCode = viewModel.convertToHexColorCode(newvalue)
                    if (colorCode == null) {
                    }//nullの場合処理は行わない
                    else {
                        viewModel.updateBackgroundColorCode(squareIndex, colorCode)
                        viewModel.convertToRGB(selectedSquare)
                        toastMessage.value = "カラーコードが更新されました"//TODO　キーボードに隠されてトーストが見えない。取捨確認する。
                    }
                }
                ColorSaveBtn(
                    modifier = Modifier.weight(1f)
                )
            }
            }

            //TODO 不要になったのでコメントアウト　後から取捨を確認しよう
//            //エラーメッセージの表示
//            //カラーコードに無効な値が入力された時に表示するエラーメッセージ(初期値は"")
//            val colorCodeErrorMessage by viewModel.colorCodeErrorMessage.observeAsState("")
//            if (colorCodeErrorMessage.isNotEmpty()){ //colorCodeErrorMessageが空白の時
//                ShowToast(message = colorCodeErrorMessage)
//            }

        }


        @Composable
        //TODO Sliderの操作についての簡便化　数値ラベルの表示、+-ボタンの追加
        fun SeekBars(selectedSquare: Int,square1Index: Int,viewModel: ColorChoiceViewModel,
                     square1ColorData: ColorDataForColorChoice,square2ColorData: ColorDataForColorChoice) {
            //selectedSquareに応じて使用するColorDataを決定する
            val currentColorData =
                if (square1Index == selectedSquare) square1ColorData else square2ColorData

            Column(
                modifier = Modifier.fillMaxWidth(), // 幅を親コンポーネントに合わせる
                verticalArrangement = Arrangement.spacedBy(16.dp), // シークバー間に16dpの間隔
                horizontalAlignment = Alignment.CenterHorizontally //水平方向に中央揃え
            ) {
                SeekBar(
                    //現在選択しているsquare
                    selectedSquare = selectedSquare,
                    //selectedSquareに応じて決定されたColorDaの中のRGB値のいずれかを値とする
                    colorDataRGB = currentColorData.red,
                    //スライダーの色を指定する
                    sliderColor = Color.Red,
                    //==スライダーが動かされ値が変更された時の処理==
                    //setSquareRGBメソッドを呼び出しviewModelの指定されたRGB値の更新を行う
                    onValueChange = { newValue ->
                        viewModel.setSquareRGB(
                            selectedSquare,
                            "red",
                            newValue,
                        )
                    },
                    //rgb値をカラーコードに変換し、selectedSquareのcolorCodeを更新する
                    onConvertToColorCode = {
                        viewModel.convertToColorCode(selectedSquare)
                    }
                )
                SeekBar(colorDataRGB = currentColorData.green,
                    sliderColor = Color.Green,
                    selectedSquare = selectedSquare,
                    onValueChange = { newValue ->
                        viewModel.setSquareRGB(
                            selectedSquare,
                            "green",
                            newValue,
                        )
                    },
                    onConvertToColorCode = {
                        viewModel.convertToColorCode(selectedSquare)
                    }
                )
                SeekBar(colorDataRGB = currentColorData.blue,
                    sliderColor = Color.Blue,
                    selectedSquare = selectedSquare,
                    onValueChange = { newValue ->
                        viewModel.setSquareRGB(
                            selectedSquare,
                            "blue",
                            newValue,
                        )
                    },
                    onConvertToColorCode = {
                        viewModel.convertToColorCode(selectedSquare)
                    }
                )
            }
        }

//TODO　ボタン、テキストコードを触った時もonSquareSelectedを動作させる
//TODO カラーコード入力時に判定を行う
        @Composable
        //色を表示するBox
        fun ColorSquare(backgroundColor: String, isSelected: Boolean, onSquareSelected: () -> Unit) {
            val borderColor = if (isSelected) Color.Black else Color.Gray
            Box(
                modifier = Modifier
                    .size(160.dp)
                    //クリック時にisSelectedをチェックしtrueなら1、falseなら2をonSquareSelectedにセットする
                    .clickable { onSquareSelected() }
                    .background(Color(android.graphics.Color.parseColor(backgroundColor)))//背景の色を設定
                    .border(2.dp, borderColor)
            )
        }

        @Composable
        fun ColorCodeText(
            modifier: Modifier = Modifier,
            colorCode: String,
            onValueChanged: (String) -> Unit,
        ) {
            TextField(
                value = colorCode,
                onValueChange = { newValue ->
                    onValueChanged(newValue)
                },
                label = { Text("カラーコード") },
                modifier = modifier,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = AppColors.White,//フォーカス時の色
                    unfocusedContainerColor = AppColors.White,
                    focusedIndicatorColor = AppColors.Black,
                    focusedLabelColor = AppColors.Gray,
                    unfocusedLabelColor = AppColors.Gray
                ),
                maxLines = 1
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
        fun SeekBar(colorDataRGB: Int,
                    sliderColor: Color,
                    selectedSquare:Int,
                    //ViewModelのRGB値を変更
                    onValueChange: (Int) -> Unit,
                    //シークバーの値をカラーコードに変換
                    onConvertToColorCode: (Int) -> (Unit),
                    ){
            Slider(
                //あらかじめIf文でselectedSquareに対応するcolorDataを引き渡す
                value = colorDataRGB.toFloat(),//スライダーを滑らかに動かすためにfloatを指定
                colors = SliderDefaults.colors(
                    thumbColor = sliderColor,
                    activeTrackColor = sliderColor,
                    activeTickColor = sliderColor //バーの動作中の色
                ),
                onValueChange = {newValue->
                    onValueChange(newValue.toInt())
                    onConvertToColorCode(selectedSquare)
                },
                //スライダーの値Float型をIntに変換する
                valueRange = 0f..255f,
                modifier = Modifier.fillMaxWidth(0.9f)//スライダーの横幅は最大値の75%
            )
        }
            //TODO　カラーパレット
            @Composable
            fun ColorPalletTab(selectedSquare: Int){
                var selectedTabIndex by remember { mutableStateOf(0) }
                Scaffold (
                    topBar = {
                        TabRow (
                            selectedTabIndex = selectedTabIndex,
                            modifier = Modifier.height(56.dp)
                        ) {
                            Tab(selected = selectedTabIndex == 0,
                                onClick = { selectedTabIndex = 0 },
                                text = {
                                    Text(text = "基本の色")
                                }
                            )
                            Tab(selected = selectedTabIndex == 1,
                                onClick = { selectedTabIndex = 1 },
                                text = {
                                    Text(text = "カラーパレットを作る")
                                }
                            )
                        }
                    }
                ) { paddingValues ->
                    when (selectedTabIndex){
                        0-> BasicColorContents(
                            Modifier.padding(paddingValues),
                            viewModel = ColorChoiceViewModel(),
                            selectedSquare = selectedSquare,
                            // basicColorListはPair(colorCode, name)のリスト
                            // ここでは、colorCodeのみを抽出したリストを引数として渡す
                            colorList1 = basicColorsList1.map { it.first },
                            colorList2 = basicColorsList2.map { it.first },
                            colorList3 = basicColorsList3.map { it.first }
                            )
                        1-> SelectedColorPalletContent(Modifier.padding(paddingValues))
                    }
                }
            }

    @Composable
    fun SelectedColorPalletContent(modifier: Modifier) {
        Column(modifier = modifier.fillMaxSize()){
            Text(text = "選択している色のカラーパレット")
        }
    }



