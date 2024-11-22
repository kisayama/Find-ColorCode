package com.example.findcolorcode.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.findcolorcode.R
import com.example.findcolorcode.components.AdjustValueBar
import com.example.findcolorcode.components.BasicColorContents
import com.example.findcolorcode.components.SelectedColorPalletContent
import com.example.findcolorcode.components.ShowToast
import com.example.findcolorcode.data.basicColorsList1
import com.example.findcolorcode.data.basicColorsList2
import com.example.findcolorcode.data.basicColorsList3
import com.example.findcolorcode.model.ColorDataForColorChoice
import com.example.findcolorcode.ui.theme.Dimensions
import com.example.findcolorcode.ui.theme.customTextFieldColors
import com.example.findcolorcode.viewmodel.ColorChoiceViewModel

//スライダーを調節することによって色を作成、保存するためのダイアログを呼び出すView
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorChoiceScreen(
    navController: NavController,
    viewModel: ColorChoiceViewModel
) {
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
    val square1ColorData =
        ColorDataForColorChoice(square1ColorCode, square1BackgroundColorCode, red1, green1, blue1)
    //========

    // ===== Square2 Color Data =====
    // square2のRGB値を取得
    val red2 by viewModel.red2.observeAsState(255)
    val green2 by viewModel.green2.observeAsState(255)
    val blue2 by viewModel.blue2.observeAsState(255)
    //square2のカラーコードを取得
    val square2ColorCode by viewModel.square2ColorCode.observeAsState("#FFFFFF")
    val square2BackgroundColorCode by viewModel.square2BackgroundColorCode.observeAsState("#FFFFFF")
    val square2ColorData =
        ColorDataForColorChoice(square2ColorCode, square2BackgroundColorCode, red2, green2, blue2)
    //========

    //初回のみ実行する
    LaunchedEffect(navController.currentBackStackEntry) {
        //ColorFavoriteScreenから遷移した時にnavHostに保存されてるデータを取得する
        val receiveDirection =
            navController.currentBackStackEntry?.arguments?.getString("direction") ?: "left"
        val receiveColorCode =
            navController.currentBackStackEntry?.arguments?.getString("colorCode") ?: "#FFFFFF"
        if (receiveDirection == "left" && receiveColorCode == "#FFFFFF") {
            Log.d("RECEIVE", "exceptionalData")
        } else {
            // 必要な処理
            //receiveSquareIndexを宣言する(selectedSquareのルールに従い左のSquareに1、右に2)
            val receiveSquareIndex = if (receiveDirection == "left") 1 else 2
            //選択Squareを変更する
            viewModel.changeSelectedSquare(receiveSquareIndex)
            //BackgroundColorCodeを変更する（Squareの背景色を変更）
            viewModel.updateBackgroundColorCode(receiveSquareIndex, receiveColorCode)
            //ColorCodeを表示しているTextFieldの文字を変更する
            viewModel.updateColorCode(receiveSquareIndex, receiveColorCode)
            //シークバーの値を変更する
            viewModel.convertToRGB(selectedSquare)
        }
    }

    //トーストメッセージを取得
    val toastMessage by viewModel.toastMessage.observeAsState("")

    //selectedSquareに応じて使用するColorDataを決定する
    val currentColorData =
        if (square1Index == selectedSquare) square1ColorData else square2ColorData

    //ダイアログ開閉状態(trueなら開く falseなら閉じる)
    val isSaveDialogOpen by viewModel.isSaveDialogOpen.observeAsState(false)

    if (isSaveDialogOpen) {
        com.example.findcolorcode.components.ColorSaveDialog(
            currentColorData = currentColorData,
            saveFavoriteColor = {
                //データベースインサート用メソッドを引き渡す
                    favoriteColorData ->
                viewModel.insertColor(favoriteColorData)
            },
            dismissDialog = { viewModel.updateDialogOpen(false) })
    }
    // 全体をColumnで囲んでレイアウトを縦方向に
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.screenPadding),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
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
                ColorColumn(
                    viewModel,
                    selectedSquare,
                    square1ColorData,
                    square1Index,
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ColorColumn(
                    viewModel,
                    selectedSquare,
                    square2ColorData,
                    square2Index,
                )
            }
        }

        // シークバーを表示
        SeekBars(
            currentColorData = currentColorData,
            selectedSquare = selectedSquare,
            viewModel = viewModel,
        )
        //±ボタンと調節単位変更用ボタンを表示する
        //TODO シークバー選択ボタンを追加し現在選択中のシークバーを管理する
        AdjustValueBar(adjustValue = { value ->
            viewModel.setSquareRGB(
                selectedSquare = selectedSquare,
                rgbColorType = {},//TODO ここを指定
                value = value,
                isAdjustment = true
            )
        }
        )

        //基本の色、カラーパレットをまとめたタブ
        ColorPalletTab(viewModel, selectedSquare, square1ColorData, square2ColorData)

        //メッセージを変更するとトーストが表示される
        ShowToast(toastMessage = toastMessage, resetMessage = { viewModel.resetToast() })
    }

}

@Composable
fun ColorColumn(
    viewModel: ColorChoiceViewModel,
    selectedSquare: Int,//現在選択中のsquareのIndex
    colorData: ColorDataForColorChoice,
    squareIndex: Int,//各squareのIndex
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)//シークバー間に15dpのスペースを入れる
    ) {
        val isSelected: Boolean = squareIndex == selectedSquare

        //squareを表示
        ColorSquare(
            backgroundColor = colorData.backgroundColorCode,
            isSelected = isSelected,
            //クリック時にselectedSquareをColorSquareの
            //親コンポーネントのColorColumnの引数squareIndexの値に変更する
            //同様の処理をColorCodeText,ColorSaveBtnでも行う
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
                colorCode = colorData.colorCode,
                //ColorSquare,ColorSaveBtnと同様に
                onSquareSelected = { viewModel.changeSelectedSquare(squareIndex) },
                onValueChanged = { newValue ->
                    viewModel.updateColorCode(squareIndex, newValue)
                    val colorCode = viewModel.convertToHexColorCode(newValue)
                    if (colorCode != null) {
                        //背景の色の変更とSeekBarの値の変更を行う
                        viewModel.updateBackgroundColorCode(squareIndex, colorCode)
                        viewModel.convertToRGB(selectedSquare)
                    } else {
                        //nullの場合(colorCodeに誤った値が入力されている時)は処理を行わない
                    }
                },
            )

            ColorSaveBtn(
                modifier = Modifier.weight(1f),
                onClicked = {
                    viewModel.changeSelectedSquare(squareIndex)
                    viewModel.updateDialogOpen(true)
                }
            )
        }
    }
}

@Composable
//TODO Sliderの操作についての簡便化　数値ラベルの表示、+-ボタンの追加
fun SeekBars(
    selectedSquare: Int, viewModel: ColorChoiceViewModel, currentColorData: ColorDataForColorChoice
) {
    Column(
        modifier = Modifier.fillMaxWidth(), // 幅を親コンポーネントに合わせる
        verticalArrangement = Arrangement.spacedBy(16.dp), // シークバー間に16dpの間隔
        horizontalAlignment = Alignment.CenterHorizontally //水平方向に中央揃え
    ) {
        SeekBar(
            //現在選択しているsquare
            selectedSquare = selectedSquare,
            //selectedSquareに応じて決定されたColorDataの中のRGB値のいずれかを値とする
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
        SeekBar(
            colorDataRGB = currentColorData.green,
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
        SeekBar(
            colorDataRGB = currentColorData.blue,
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

@Composable
//色を表示するBox
fun ColorSquare(
    backgroundColor: String,
    isSelected: Boolean,
    onSquareSelected: () -> Unit
) {
    Log.d("RECEIVE", "COLORsquare COmposed")
    Log.d("RECEIVE", "${backgroundColor}ColorSquareComposedColor")
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
    onSquareSelected: () -> Unit,
    onValueChanged: (String) -> Unit,
) {
    TextField(
        value = colorCode,
        onValueChange = { newValue ->
            onValueChanged(newValue)
        },
        label = { Text("カラーコード") },
        modifier = modifier
            //clickableだとvalueChangeが優先され正しく処理されないのでonFocusChangeを使用する
            //textFieldにフォーカスしたらselectedSquareを変更する
            .onFocusChanged { focusState ->
                if (focusState.isFocused) onSquareSelected()
            },
        colors = customTextFieldColors(),
        maxLines = 1
    )
}

@Composable
//色を保存するためのボタン
fun ColorSaveBtn(
    modifier: Modifier = Modifier,
    onClicked: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = {
            onClicked()
        },
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
fun SeekBar(
    colorDataRGB: Int,
    sliderColor: Color,
    selectedSquare: Int,
    //ViewModelのRGB値を変更
    onValueChange: (Int) -> Unit,
    //シークバーの値をカラーコードに変換
    onConvertToColorCode: (Int) -> (Unit),
) {
    Slider(
        //あらかじめIf文でselectedSquareに対応するcolorDataを引き渡す
        value = colorDataRGB.toFloat(),//スライダーを滑らかに動かすためにfloatを指定
        colors = SliderDefaults.colors(
            thumbColor = sliderColor,
            activeTrackColor = sliderColor,
            activeTickColor = sliderColor //バーの動作中の色
        ),
        onValueChange = { newValue ->
            onValueChange(newValue.toInt())
            onConvertToColorCode(selectedSquare)
        },
        //スライダーの値Float型をIntに変換する
        valueRange = 0f..255f,
        modifier = Modifier.fillMaxWidth(0.9f)//スライダーの横幅は最大値の75%
    )
}

//ベーシックカラーとカラーパレットを表示するためのタブを設定
@Composable
fun ColorPalletTab(
    viewModel: ColorChoiceViewModel, selectedSquare: Int,
    square1ColorData: ColorDataForColorChoice, square2ColorData: ColorDataForColorChoice
) {
    //初期状態は基本の色タブを表示する
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    Scaffold(
        topBar = {
            TabRow(
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
        when (selectedTabIndex) {
            0 -> BasicColorContents(
                viewModel = viewModel,
                selectedSquare = selectedSquare,
                // basicColorListはPair(colorCode, name)のリスト
                // ここでは、colorCodeのみを抽出したリストを引数として渡す
                colorList1 = basicColorsList1.map { it.first },
                colorList2 = basicColorsList2.map { it.first },
                colorList3 = basicColorsList3.map { it.first }
            )

            1 -> SelectedColorPalletContent(
                Modifier.padding(paddingValues),
                viewModel = viewModel,
                selectedSquare = selectedSquare,
                square1ColorData = square1ColorData,
                square2ColorData = square2ColorData
            )
        }
    }
}
