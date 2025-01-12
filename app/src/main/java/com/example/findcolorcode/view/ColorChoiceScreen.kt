package com.example.findcolorcode.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.findcolorcode.components.AdjustValueBar
import com.example.findcolorcode.components.ColorPickerTabs
import com.example.findcolorcode.model.ColorDataForColorChoice
import com.example.findcolorcode.ui.theme.Dimensions
import com.example.findcolorcode.ui.theme.getDynamicTypography
import com.example.findcolorcode.viewmodel.ColorChoiceViewModel
import com.kisayama.findcolorcode.R

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

    //トーストメッセージを取得
    val toastMessage by viewModel.toastMessage.observeAsState("")

    val context = LocalContext.current

    //currentSquareIndexに応じて使用するColorDataを決定する
    val currentColorData =
        if (square1Index == currentSquareIndex) square1ColorData else square2ColorData

    //ダイアログ開閉状態(trueなら開く falseなら閉じる)
    val isSaveDialogOpen by viewModel.isSaveDialogOpen.observeAsState(false)
    val systemBarsPadding =  WindowInsets.systemBars.asPaddingValues()
    val isSystemBarsPaddingZero = systemBarsPadding.run {
            calculateTopPadding() == 0.dp &&
            calculateBottomPadding() == 0.dp &&
            //LTRはstart,endの方向が国によって違うのでLtrまたはRtlで指定する(Ltrは左がstart,右がend)
            calculateStartPadding(LayoutDirection.Ltr) == 0.dp &&
            calculateEndPadding(LayoutDirection.Ltr) == 0.dp
    }
    val topPadding = if (isSystemBarsPaddingZero)Dimensions.screenVerticalPadding else 0.dp

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

    //toastMessageが変更されたらトーストを表示する
    LaunchedEffect(toastMessage) {
        if (toastMessage.isNotEmpty()) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        }
    }

    //FavoriteListScreenでユーザーが選択した色をColorChoiceScreenのカラーパレット(左右いずれか)に表示する
    //View間の移動を行った初回のみ実行する
    LaunchedEffect(navController.currentBackStackEntry) {
        //ColorFavoriteScreenから遷移した時にnavHostに保存されてるデータを取得する
        val receiveDirection =
            navController.currentBackStackEntry?.arguments?.getString("direction")
        val receiveColorCode =
            navController.currentBackStackEntry?.arguments?.getString("colorCode")

        if (receiveDirection == null || receiveColorCode == null) {
            //処理を行わない
        } else {
            // 必要な処理
            //receiveSquareIndexを宣言する(selectedSquareのルールに従い左のSquareに1、右に2)
            val receiveSquareIndex = if (receiveDirection == "left") 1 else 2
            //選択Squareを変更する
            viewModel.changeCurrentSquareIndex(receiveSquareIndex)
            //BackgroundColorCodeを変更する（Squareの背景色を変更）
            viewModel.updateBackgroundColorCode(receiveSquareIndex, receiveColorCode)
            //ColorCodeを表示しているTextFieldの文字を変更する
            viewModel.updateColorCode(receiveSquareIndex, receiveColorCode)
            //シークバーの値を変更する
            viewModel.convertToRGB(currentSquareIndex)
        }
    }

    // 全体をColumnで囲んでレイアウトを縦方向に
    Column(
        modifier = Modifier
            .padding(systemBarsPadding)
            //実機のナビゲーションバーなどの高さ分パディングを入れる
            .padding(
                top = topPadding,
                start = Dimensions.screenHorizontalPadding,
                end = Dimensions.screenHorizontalPadding
            )
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
//[ColorColumn、RGBSlidersGroup,AdjustValueBar]をColumnで
// 囲み画面全体の7割の高さを与える
    Column(
        modifier = Modifier
            .weight(0.65f)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        Spacer(modifier = Modifier.weight(0.04f))

        // Boxを横一列に2つ並べる
        Row(
            modifier = Modifier
                .weight(0.4f),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            ColorColumn(
                Modifier.weight(1f),
                viewModel,
                currentSquareIndex,
                square1ColorData,
                square1Index,
            )
            ColorColumn(
                Modifier.weight(1f),
                viewModel,
                currentSquareIndex,
                square2ColorData,
                square2Index,
            )
        }

        // スライダーを表示
        RGBSlidersGroup(
            modifier = Modifier
                .weight(0.34f)
                .fillMaxWidth(),
            currentColorData = currentColorData,
            currentSquareIndex = currentSquareIndex,
            selectedColor = currentSliderColorName,
            viewModel = viewModel,
        )
        //±ボタンと調節単位変更用ボタンを含むコンポーネント
        AdjustValueBar(
            modifier = Modifier
                .weight(0.08f)
                .fillMaxWidth(),
            adjustValue = { value ->
                viewModel.validAndUpdateRGBValue(
                    inputValue = value.toString(),
                    currentSquareIndex = currentSquareIndex,
                    rgbColorType = currentSliderColorName, //現在選択中のrgb
                    isAdjustment = true
                )
            }
        )
    }
        //ColorPickerTabsには画面の3割
        Column(
            modifier = Modifier.weight(0.35f)
        ) {
            //基本の色、カラーパレットをまとめたタブ
            ColorPickerTabs(viewModel, currentSquareIndex, square1ColorData, square2ColorData)
        }
    }

}

@Composable
fun ColorColumn(
    modifier: Modifier = Modifier,
    viewModel: ColorChoiceViewModel,
    currentSquareIndex: Int,//現在選択中のsquareのIndex
    colorData: ColorDataForColorChoice,//各squareのColorData
    squareIndex: Int,//各squareのIndex
) {
    Column(
        modifier = modifier
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val isSelected: Boolean = squareIndex == currentSquareIndex

        //squareを表示
        ColorSquare(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxWidth(),
            backgroundColor = colorData.backgroundColorCode,
            isSelected = isSelected,
            //クリック時にcurrentSquareIndexをColorSquareの
            //親コンポーネントのColorColumnの引数squareIndexの値に変更する
            onSquareSelected = {
                viewModel.changeCurrentSquareIndex(squareIndex)
            },
        )
        Row(
            modifier = Modifier
                .weight(0.45f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            //ユーザーに表示するカラーコードテキスト
            //ユーザーが値を入力するなどしてvalueが変更されるとcolorCodeの変更を行い、
            // 検査後にバッグラウンドカラーコードとRGB値の更新を行う
            ColorCodeText(
                modifier = Modifier
                    .weight(2f)
                    .padding(top = 5.dp),
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
    modifier: Modifier = Modifier,
    currentSquareIndex: Int,
    selectedColor: String,
    viewModel: ColorChoiceViewModel,
    currentColorData: ColorDataForColorChoice
) {
    Column(
        modifier = modifier.fillMaxWidth(), // 幅を親コンポーネントに合わせる
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally //水平方向に中央揃え
    ) {
        RGBSlier(
            modifier = modifier.weight(1f),
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
            modifier = modifier.weight(1f),
            currentSquareRGB = currentColorData.green,
            viewModel = viewModel,
            sliderColor = Color.Green,
            sliderColorName = "green",
            currentSliderColorName = selectedColor,
            currentSquareIndex = currentSquareIndex
        )
        RGBSlier(
            modifier = modifier.weight(1f),
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
    modifier: Modifier = Modifier,
    backgroundColor: String,
    isSelected: Boolean,
    onSquareSelected: () -> Unit
) {
    val borderColor = if (isSelected) Color.Black else Color.Gray
    Box(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .aspectRatio(1f)
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
    BasicTextField(
        modifier = modifier
            .onFocusChanged { focusState ->
                if (focusState.isFocused) onSquareSelected()
            },
        value = colorCode,
        onValueChange = { newValue -> onValueChanged(newValue) },
        singleLine = true,
        textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
        decorationBox = { innerTextField ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                ) {
                    Text(
                        text = "カラーコードを入力",
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = getDynamicTypography().labelSmall.fontSize
                        )
                    )
                }
                innerTextField()

                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color.Gray) // 下線の色
                )
            }
            }
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
                modifier = modifier
            )
        }
    }

    // RGBの色を調整するスライダー、選択ボタン、値表示用のTextField
    @Composable
    fun RGBSlier(
        modifier: Modifier = Modifier,
        currentSquareRGB: Int,
        viewModel: ColorChoiceViewModel,
        sliderColor: Color,
        sliderColorName: String,
        currentSliderColorName: String,
        currentSquareIndex: Int
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            //現在選択中のメニューの丸はグレー、そうでなければ透明
            Box(
                modifier = Modifier
                    .size(20.dp)
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
            var value by remember { mutableStateOf<String?>("255") }

            val editValue by remember(currentSquareRGB) { mutableStateOf(currentSquareRGB.toString()) }

            //スライダーの横に設置するRGB表示用のテキストフィールド
            BasicTextField(
                modifier = Modifier
                    .weight(0.15f)
                    .padding(3.dp)
                    .focusable(),
                value = editValue,
                onValueChange = { newValue: String? ->
                    //選択しているスライダーの色を変更しボタンの色を変更する
                    value = if (newValue.isNullOrEmpty()) {
                        "0"
                    } else {
                        viewModel.changeCurrentRGBSeekBar(sliderColorName)
                        newValue
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = getDynamicTypography().bodyLarge.fontSize,
                    textAlign = TextAlign.Center
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .border(1.dp, Color.Black, RectangleShape)
                            .padding(4.dp)
                    ) {
                        innerTextField()
                    }
                }
            )
            //再コンポーネントのされた時に現在のRGB値を初期化しないようにフラグを設定する
            val isInitialLoad = remember { mutableStateOf(true) }

            //初回の255は無視する
            //valueが255より大きい場合は無視して競合を防ぐ
            LaunchedEffect(value) {
                if (isInitialLoad.value){
                    if (value == "255") {
                        isInitialLoad.value = false//初回だけ無視(再コンポーズの際に初期値の競合を防ぐ)
                        return@LaunchedEffect //初回の場合処理終了
                    }
                }
                if (value?.toIntOrNull() in 0..255) {
                    viewModel.validAndUpdateRGBValue(
                        value,
                        currentSquareIndex,
                        sliderColorName,
                        false
                    )
                }
            }

            Spacer(modifier = Modifier.width(3.dp))

            //色調節のためのスライダー
            Slider(
                modifier = Modifier.weight(0.85f),//スライダーの横幅は最大値の75%
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
                valueRange = 0f..255f
            )
        }
    }

