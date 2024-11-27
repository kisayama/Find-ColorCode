package com.example.findcolorcode.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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
    val currentSquareIndex by viewModel.currentSquareIndex.observeAsState(1)
    val square1Index = 1
    val square2Index = 2

    //直近で操作したスライダーの色 初期値はred (red,blue,green)
    val currentSliderColorName by viewModel.currentSliderColorName.observeAsState("red")

    // ====== Square1 Color Data =======
    // square1のRGB値を取得
    val red1 by viewModel.red1.observeAsState(255)
    val green1 by viewModel.green1.observeAsState(255)
    val blue1 by viewModel.blue1.observeAsState(255)
    //square1のカラーコードを取得(TextFieldに表示する値として使用する)
    val square1ColorCode by viewModel.square1ColorCode.observeAsState("#FFFFFF")
    //ユーザーがテキスト入力中に背景色が変わらないように 上記カラーコードとは別に背景色を管理する変数を用意しておく
    val square1BackgroundColorCode by viewModel.square1BackgroundColorCode.observeAsState("#FFFFFF")
    //ユーザー入力反映用カラーコード、内部管理用カラーコード、内管カラーコードのRGB値
    val square1ColorData =
        ColorDataForColorChoice(square1ColorCode, square1BackgroundColorCode, red1, green1, blue1)
    //========

    // ===== Square2 Color Data =====
    val red2 by viewModel.red2.observeAsState(255)
    val green2 by viewModel.green2.observeAsState(255)
    val blue2 by viewModel.blue2.observeAsState(255)
    val square2ColorCode by viewModel.square2ColorCode.observeAsState("#FFFFFF")
    val square2BackgroundColorCode by viewModel.square2BackgroundColorCode.observeAsState("#FFFFFF")
    val square2ColorData =
        ColorDataForColorChoice(square2ColorCode, square2BackgroundColorCode, red2, green2, blue2)
    //========

    //FavoriteListScreenでユーザーが選択した色をColorChoiceScreenのカラーパレット(左右いずれか)に表示する
    //View間の移動を行った初回のみ実行する
    LaunchedEffect(navController.currentBackStackEntry) {
        Log.d("ColorChoiceScreen","LaunchedEffect発火${navController.currentBackStackEntry}")
        Log.d("ColorChoiceScreen","square1ColorCode${square1ColorCode}")
        Log.d("ColorChoiceScreen","square2ColorCode${square2ColorCode}")

        //ColorFavoriteScreenから遷移した時にnavHostに保存されてるデータを取得する
        val receiveDirection =
            navController.currentBackStackEntry?.arguments?.getString("direction") ?: "left"
        val receiveColorCode =
            navController.currentBackStackEntry?.arguments?.getString("colorCode") ?: "#FFFFFF"

            Log.d("ColorChoiceScreen","処理開始${navController.currentBackStackEntry}")
            Log.d("ColorChoiceScreen","処理開始${receiveDirection}")
            Log.d("ColorChoiceScreen","処理開始${receiveColorCode}")
            //ユーザーが選択したパレットに保存した色を表示する
            //receiveSquareIndexを宣言する(ColorChoiceScreenの左のSquareに1、右に2が割り振られている)
            val receiveSquareIndex = if (receiveDirection == "left") 1 else 2
            //選択Squareを変更する
            viewModel.changeCurrentSquareIndex(receiveSquareIndex)
            //BackgroundColorCodeを変更する（Squareの背景色を変更）
            viewModel.updateBackgroundColorCode(receiveSquareIndex, receiveColorCode)
            //ColorCodeを表示しているTextFieldの文字を変更する
            viewModel.updateColorCode(receiveSquareIndex, receiveColorCode)
            //スライダーの値を変更する
            viewModel.convertToRGB(currentSquareIndex)
            Log.d("ColorChoiceScreen","処理終了${navController.currentBackStackEntry}")
            Log.d("ColorChoiceScreen","square1ColorCode${square1ColorCode}")
            Log.d("ColorChoiceScreen","square2ColorCode${square2ColorCode}")
            Log.d("ColorChoiceScreen","sqreenSideVewModel${viewModel}")
    }

    //トーストメッセージを取得
    val toastMessage by viewModel.toastMessage.observeAsState("")

    //currentSquareIndexに応じて使用するColorDataを決定する
    val currentColorData =
        if (square1Index == currentSquareIndex) square1ColorData else square2ColorData

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
        verticalArrangement = Arrangement.spacedBy(10.dp),
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
            ) {
                ColorColumn(
                    viewModel,
                    currentSquareIndex,
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
                    currentSquareIndex,
                    square2ColorData,
                    square2Index,
                )
            }
        }

        // スライダーを表示
        RGBSlidersGroup(
            currentColorData = currentColorData,
            currentSquareIndex = currentSquareIndex,
            selectedColor = currentSliderColorName,
            viewModel = viewModel,
        )
        //±ボタンと調節単位変更用ボタンを含むコンポーネント
        AdjustValueBar(
            adjustValue = { value ->
                viewModel.validAndUpdateRGBValue(
                    inputValue = value.toString(),
                    currentSquareIndex = currentSquareIndex,
                    rgbColorType = currentSliderColorName, //現在選択中のrgb
                    isAdjustment = true
                )
            }
        )

        //基本の色、カラーパレットをまとめたタブ
        ColorPalletTab(viewModel, currentSquareIndex, square1ColorData, square2ColorData)

        //メッセージを変更するとトーストが表示される
        ShowToast(toastMessage = toastMessage, resetMessage = { viewModel.resetToast() })
    }

}

@Composable
fun ColorColumn(
    viewModel: ColorChoiceViewModel,
    currentSquareIndex: Int,//現在選択中のsquareのIndex
    colorData: ColorDataForColorChoice,//各squareのColorData
    squareIndex: Int,//各squareのIndex
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val isSelected: Boolean = squareIndex == currentSquareIndex

        //squareを表示
        ColorSquare(
            backgroundColor = colorData.backgroundColorCode,
            isSelected = isSelected,
            //クリック時にcurrentSquareIndexをColorSquareの
            //親コンポーネントのColorColumnの引数squareIndexの値に変更する
            onSquareSelected = {
                viewModel.changeCurrentSquareIndex(squareIndex)
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
                onSquareSelected = { viewModel.changeCurrentSquareIndex(squareIndex) },
                onValueChanged = { newValue ->
                    viewModel.updateColorCode(squareIndex, newValue)
                    val colorCode = viewModel.convertToHexColorCode(newValue)
                    if (colorCode != null) {
                        //背景の色の変更とSeekBarの値の変更を行う
                        viewModel.updateBackgroundColorCode(squareIndex, colorCode)
                        viewModel.convertToRGB(currentSquareIndex)
                    } else {
                        //nullの場合(colorCodeに誤った値が入力されている時)は処理を行わない
                    }
                },
            )

            ColorSaveBtn(
                modifier = Modifier.weight(1f),
                onClicked = {
                    viewModel.changeCurrentSquareIndex(squareIndex)
                    viewModel.updateDialogOpen(true)
                }
            )
        }
    }
}

@Composable
fun RGBSlidersGroup(
    currentSquareIndex: Int,
    selectedColor: String,
    viewModel: ColorChoiceViewModel,
    currentColorData: ColorDataForColorChoice
) {
    Column(
        modifier = Modifier.fillMaxWidth(), // 幅を親コンポーネントに合わせる
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally //水平方向に中央揃え
    ) {
        RGBSlier(
            //現在選択しているsquare
            currentSquareRGB = currentColorData.red,
            viewModel = viewModel,
            //currentSquareIndexに応じて決定されたColorDataの中のRGB値のいずれかを値とする
            sliderColor = Color.Red,
            //スライダーの色を指定する
            sliderColorName = "red",
            //直近で操作しているスライダーの色
            currentSliderColorName = selectedColor,
            currentSquareIndex = currentSquareIndex
        )
        RGBSlier(
            currentSquareRGB = currentColorData.green,
            viewModel = viewModel,
            sliderColor = Color.Green,
            sliderColorName = "green",
            currentSliderColorName = selectedColor,
            currentSquareIndex = currentSquareIndex
        )
        RGBSlier(
            currentSquareRGB = currentColorData.blue,
            viewModel = viewModel,
            sliderColor = Color.Blue,
            sliderColorName = "blue",
            currentSliderColorName = selectedColor,
            currentSquareIndex = currentSquareIndex
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
    Log.d("ColorChoiceScreen","colorSquare${isSelected}${backgroundColor}")
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
            //textFieldにフォーカスしたらcurrentSquareIndexを変更する
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

// RGBの色を調整するスライダー、選択ボタン、値表示用のTextField
@Composable
fun RGBSlier(
    currentSquareRGB: Int,
    viewModel: ColorChoiceViewModel,
    sliderColor: Color,
    sliderColorName: String,
    currentSliderColorName: String,
    currentSquareIndex: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {

        //現在選択中のメニューの丸はグレー、そうでなければ透明
        Box(
            modifier = Modifier
                .size(25.dp)
                .background(
                    if (sliderColorName == currentSliderColorName) Color.Gray else Color.Transparent,
                    CircleShape
                )
                .border(
                    width = 3.dp,
                    color = Color.Gray,
                    shape = CircleShape
                )
                //CircleShapeをユーザーがクリックしたときに現在選択しているスライダーの色を変更する(ボタンの状態を変更）
                .clickable { viewModel.changeCurrentRGBSeekBar(sliderColorName) },
        )
        
        Spacer(modifier = Modifier.width(3.dp))

        //BasicTextFieldとSliderで使用する
        var value by remember { mutableStateOf<String?>(currentSquareRGB.toString()) }

        //Sliderコンポーネント内でvalueが変更された時にviewModelで保持しているRGB値の更新を行う
        LaunchedEffect(value) {
            viewModel.validAndUpdateRGBValue(value,currentSquareIndex,sliderColorName, false)
        }


        //RGB値表記・編集用のTextField
        BasicTextField(
            modifier = Modifier
                .width(50.dp)
                .padding(3.dp)
                .focusable(),
            value = currentSquareRGB.toString(),
            onValueChange = { newValue:String? ->
                /*viewModel.validAndUpdateRGBValue(
                    newValue,currentSquareIndex, colorName,false
                )*/
                    //選択しているスライダーの色を変更しボタンの色を変更する
                    viewModel.changeCurrentRGBSeekBar(sliderColorName)
                    value = newValue
                }
        ,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            maxLines = 1,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,// テキストのサイズ
                textAlign = TextAlign.Center
                ),
            decorationBox = { TextField ->
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .border(1.dp, Color.Black, RectangleShape)
                        .padding(4.dp)
                ) {
                    TextField()
                }
            }
        )

        Spacer(modifier = Modifier.width(3.dp))

        //色調節のためのスライダー
        Slider(
            //あらかじめIf文でcurrentSquareIndexに対応するcolorDataを引き渡す
            value = currentSquareRGB.toFloat(),//スライダーを滑らかに動かすためにfloatを指定
            colors = SliderDefaults.colors(
                thumbColor = sliderColor,
                activeTrackColor = sliderColor,
                activeTickColor = sliderColor //バーの動作中の色
            ),
            onValueChange = { newValue ->
                //選択しているスライダーの色を変更しボタンの色を変更する
                viewModel.changeCurrentRGBSeekBar(sliderColorName)
                value = newValue.toInt().toString()
            },

            //スライダーの値Float型をIntに変換する
            valueRange = 0f..255f,
            modifier = Modifier.fillMaxWidth(0.9f)//スライダーの横幅は最大値の75%
        )
    }
}

//ベーシックカラーとカラーパレットを表示するためのタブを設定
@Composable
fun ColorPalletTab(
    viewModel: ColorChoiceViewModel, currentSquareIndex: Int,
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
                currentSquareIndex = currentSquareIndex,
                // basicColorListはPair(colorCode, name)のリスト
                // ここでは、colorCodeのみを抽出したリストを引数として渡す
                colorList1 = basicColorsList1.map { it.first },
                colorList2 = basicColorsList2.map { it.first },
                colorList3 = basicColorsList3.map { it.first }
            )

            1 -> SelectedColorPalletContent(
                Modifier.padding(paddingValues),
                viewModel = viewModel,
                currentSquareIndex = currentSquareIndex,
                square2ColorData = square2ColorData,
                square1ColorData = square1ColorData
            )
        }
    }
}
