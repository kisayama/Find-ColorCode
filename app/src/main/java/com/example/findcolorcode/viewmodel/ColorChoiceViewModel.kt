package com.example.findcolorcode.viewmodel


import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findcolorcode.model.FavoriteColorDataClass
import com.example.findcolorcode.repository.ColorSchemeRepository
import com.example.findcolorcode.repository.FavoriteColorRepository
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ColorChoiceViewModel(
    //APIとデータベースのRepositoryの依存性を注入
    private val apiRepository: ColorSchemeRepository,
    private val favoriteColorRepository: FavoriteColorRepository
) : ViewModel() {

    //==プロパティ==

    //選択squareのインデックス
    private val _currentSquareIndex = MutableLiveData(1)
    val currentSquareIndex: LiveData<Int> = _currentSquareIndex

    //直近で操作したスライダーの色 初期値はred (red,blue,green)
    private val _currentSliderColorName = MutableLiveData("red")
    val currentSliderColorName: LiveData<String> = _currentSliderColorName

    //===colorCodeプロパティ====
    //・テキストフィールドに表示するカラーコード
    //・ユーザーがテキストフィールドに入力した文字列
    //上記両方を管理するプロパティ
    //Square1
    private val _square1ColorCode = MutableLiveData("#FFFFFF")//デフォルトのカラーコード
    val square1ColorCode: LiveData<String> get() = _square1ColorCode

    //Square2
    private val _square2ColorCode = MutableLiveData("#FFFFFF")//デフォルトのカラーコード
    val square2ColorCode: LiveData<String> get() = _square2ColorCode

    //背景色を管理するプロパティ
    //Square1
    private val _square1BackgroundColorCode = MutableLiveData("#FFFFFF")//デフォルトのカラーコード
    val square1BackgroundColorCode: LiveData<String> get() = _square1BackgroundColorCode

    //Square2
    private val _square2BackgroundColorCode = MutableLiveData("#FFFFFF")//デフォルトのカラーコード
    val square2BackgroundColorCode: LiveData<String> get() = _square2BackgroundColorCode
    //=====

    //====スライダーのRGB値を保存する変数square1,square2====
    //rememberはデバイスの回転などのアクティビティの破棄をされると状態が保存されないことに注意
    val red1 = MutableLiveData(255)//square1の各スライダーの値を保存する変数とその初期値
    val green1 = MutableLiveData(255)
    val blue1 = MutableLiveData(255)

    val red2 = MutableLiveData(255)//square2の各スライダーの値を保存する変数とその初期値
    val green2 = MutableLiveData(255)
    val blue2 = MutableLiveData(255)
    //===================

    //===ColorPalletContentに表示するカラーパレットのリスト===
    // カラーコードは#付きのHex形式、リストサイズは5
    //カラーパレットに表示するカラーコードの初期値のリスト
    private val initialColorPalletList = listOf("#FFFFFF", "#FFFFFF", "#FFFFFF", "#FFFFFF", "#FFFFFF")
    private val _colorPalletList = MutableLiveData(initialColorPalletList)
    val colorPalletList: LiveData<List<String>> get() = _colorPalletList

    //======

    //getColorSchemeに引き渡すpalletMode
    private val _palletMode = MutableLiveData("analogic")
    val palletMode:LiveData<String> get() = _palletMode

    //======

    //===お気に入りの色リスト===
    private val _favoriteColors = MutableLiveData<List<FavoriteColorDataClass>>(emptyList())
    val favoriteColors: LiveData<List<FavoriteColorDataClass>> get() = _favoriteColors
    //======

    //===トーストメッセージ===
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage
    //======

    //===ColorSveDialogの表示状態を表すフラグ===
    private val _isSaveDialogOpen = MutableLiveData(false)
    val isSaveDialogOpen: LiveData<Boolean> get() = _isSaveDialogOpen

    //初期化処理
    init {
        getAllFavoriteColors()
    }

    //保存済みのお気に入りの色を全て取得する
    private fun getAllFavoriteColors() {
        viewModelScope.launch {
            favoriteColorRepository.getAllColors().collect { colorList ->
                //新しく保存された順に並び替える
                _favoriteColors.value = colorList.sortedByDescending { it.editDateTime }
            }
        }
    }

    //変更メソッド
    fun updateDialogOpen(newDialogOpen: Boolean) {
        _isSaveDialogOpen.value = newDialogOpen
    }
    //======

    //==メソッド==

    //選択squareを変更する
    fun changeCurrentSquareIndex(newNumber: Int) {
        _currentSquareIndex.value = newNumber
        //操作スクエアを変更したら操作スライダーを赤色に変更する
        changeCurrentRGBSeekBar("red")
    }

    //選択スライダーを変更する
    fun changeCurrentRGBSeekBar(rgb: String) {
        _currentSliderColorName.value = rgb
    }

    //テキストフィールド表示用のカラーコードを変更するメソッド
    //TextFieldのリアルタイムな値（手入力した値も）を兼ねるのでここでカラーコードの検証は行わない
    fun updateColorCode(squareIndex: Int, newValue: String) {
        when (squareIndex) {
            1 -> _square1ColorCode.value = newValue
            2 -> _square2ColorCode.value = newValue
        }
    }

    //背景色のカラーコードを変更するメソッド
    fun updateBackgroundColorCode(squareIndex: Int, validColorCode: String) {
        when (squareIndex) {
            1 -> _square1BackgroundColorCode.value = validColorCode
            2 -> _square2BackgroundColorCode.value = validColorCode
        }
    }

    //現在選択しているRGB値を変更するために値の検査を行い
    //メソッド内で上記変更メソッドを呼び出す
    //ユーザーがRGB値をTextFieldに直接入力したときやボタンで調整を行った時に使用するメソッド
    fun validAndUpdateRGBValue(
        inputValue: String?,
        currentSquareIndex: Int,
        rgbColorType: String,
        //falseなら値引き渡し,trueなら増減値を引き渡し
        isAdjustment: Boolean= false
    ){
        val value  = inputValue?.toIntOrNull()?:0
        currentRGBValueChange(currentSquareIndex,rgbColorType, value,isAdjustment)
    }
    //currentSquareIndex別のスライダー3種類の値を更新する
    fun currentRGBValueChange(
        currentSquareIndex: Int,
        rgbColorType: String,
        value: Int,
        isAdjustment: Boolean = false
    ) {
        val updateValue: (Int) -> (Int) = { currentValue ->
            if (isAdjustment) {
                //isAdjustmentがtrueなら増減値が渡される(マイナスの時は負の値)
                (currentValue + value).coerceIn(0, 255)
            } else {
                //falseならvalueにはRGB値自体が渡される
                value.coerceIn(0, 255)
            }
        }
        when (currentSquareIndex) {
            1 -> {
                when (rgbColorType) {
                    "red" -> {
                        red1.value = red1.value?.let { updateValue(it) }
                    }
                    "green" -> {
                        green1.value = green1.value?.let { updateValue(it) }
                    }
                    "blue" -> {
                        blue1.value = blue1.value?.let { updateValue(it) }
                    }
                }
            }
            2 -> {
                when (rgbColorType) {
                    "red" -> {
                        red2.value = red2.value?.let { updateValue(it) }
                    }
                    "green" -> {
                        green2.value = green2.value?.let { updateValue(it) }
                    }
                    "blue" -> {
                        blue2.value = blue2.value?.let { updateValue(it) }
                    }
                }
            }
        }
        //rgb値からカラーコードを更新する
        convertToColorCode(currentSquareIndex)
    }

    //色名→Hex、Hex検証
    //ユーザーが入力した色名をHexに変換する
    //Hexが正しい値か検証する際にも使用する
    fun convertToHexColorCode(text: String): String? {
        //TextFieldが空の時はnullを返し呼び出し元で処理を行わないようにする
        val trimText = text.trim()//スペースを削除
        if (trimText.isEmpty()) {
            return null
        }
        return try {
            //parseColorで変換した値
            val intColorCode = Color.parseColor(text)
            //colorInt(上2桁は透明度、下4桁はRGB)から透明度を無視するAND演算を行いRGB部分だけ取得する
            //0xはプレフィックスで数値が16進数であることを示す
            val rgbColorCode = intColorCode and 0x00FFFFFF
            String.format("#%06X", rgbColorCode)
        } catch (e: IllegalArgumentException) {
            //入力されたtextからColorCodeが見つからない場合nullを返す
            null
        }
    }

    //====カラーコード→RGB=====

    fun convertToRGB(currentSquareIndex: Int) {
        when (currentSquareIndex) {
            //currentSquareIndexに応じて現在のcolorCodeを取得しRGBを計算する

            1 -> {
                val colorCode = _square1ColorCode.value
                val (red, green, blue) = calConvertToRGB(colorCode.toString())
                red1.value = red
                green1.value = green
                blue1.value = blue
            }

            2 -> {
                val colorCode = _square2ColorCode.value
                val (red, green, blue) = calConvertToRGB(colorCode.toString())
                red2.value = red
                green2.value = green
                blue2.value = blue
            }
        }
    }

    //ColorCodeを受け取り10進数に変換しRGB値を返す
    private fun calConvertToRGB(colorCode: String): Triple<Int, Int, Int> {
        return try {
            val adjustColorCode = Color.parseColor(colorCode)
            val red = Color.red(adjustColorCode)
            val green = Color.green(adjustColorCode)
            val blue = Color.blue(adjustColorCode)
            Triple(red, green, blue)// R,G,BをTripleで返す
        } catch (e: IllegalStateException) {
            //エラーが起きた場合デフォルトのRGB値を返す
            Triple(255, 255, 255)
        }
    }
    //=============

    //====RGB→カラーコード=====
    private fun convertToColorCode(currentSquareIndex: Int) {
        when (currentSquareIndex) {
            //currentSquareIndexに応じて現在のcolorCodeを取得しRGBを計算する
            1 -> {
                val red = red1.value ?: 0
                val green = green1.value ?: 0
                val blue = blue1.value ?: 0
                val colorCode = calConvertToColorCode(red, green, blue)
                //TextFieldとsquareの背景色を変更する
                _square1ColorCode.value = colorCode
                _square1BackgroundColorCode.value = colorCode
            }

            2 -> {
                val red = red2.value ?: 0
                val green = green2.value ?: 0
                val blue = blue2.value ?: 0
                val colorCode = calConvertToColorCode(red, green, blue)
                _square2ColorCode.value = colorCode
                _square2BackgroundColorCode.value = colorCode
            }
        }
    }

    //RGBを受け取りColorCodeを返す
    private fun calConvertToColorCode(red: Int, green: Int, blue: Int): String {
        return String.format("#%02X%02X%02X", red, green, blue)
    }
    //=============

    //====Toast関連====
    //変更メソッド
    fun updateToastMessage(message: String) {
        _toastMessage.value = message
    }

    fun resetToast() {
        _toastMessage.value = ""
    }
    //=====


    //====API関連====

    //getColorSchemeに引き渡すpalletModeを変更
    fun changePalletMode(mode:String){
        _palletMode.value = mode
    }

    //selectedColorPalletContentに表示するpalletColorListを取得するためのAPI通信を行う
    fun fetchColorScheme(colorCode: String) {
        viewModelScope.launch {
            try {
                val response = apiRepository.getColorScheme(
                    colorCode.removePrefix("#"),//#を取り除いたHex値を引き渡す,
                    mode = _palletMode.value ?:"analogic"
                )
                //リストのサイズが5かつ全てのカラーコードが正しい形式であることを確認
                if (response.size == 5 &&
                    response.all { convertToHexColorCode(it) != null }
                ) {
                    _colorPalletList.value = response //APIから取得したレスポンスをカラーパレットリストに保存
                    _toastMessage.value = "カラーパレットが作成できました！"
                } else {
                    //RepositoryImplからのレスポンスが誤った形式で他の通信エラーと同様のタグを設定する
                    Log.e(
                        "RepositoryImpl",
                        "リストサイズ:${response.size},colorCodeHex:${response}　サイズが5以外,colorCodeHexの形式エラー"
                    )
                    _toastMessage.value = "無効なレスポンスが含まれています。再度お試しください"
                }
            } catch (e: SocketTimeoutException) {
                _toastMessage.value =
                    "通信がタイムアウトしました。ネットワーク接続を確認してください"
            } catch (e: UnknownHostException) {
                _toastMessage.value = "インターネット接続エラー　ネットワーク接続を確認してください"
            } catch (e: Exception) {
                _toastMessage.value =
                    "予期しないエラーが発生しました。ネットワーク接続を確認してください"
            }

        }
    }

    //=======

    //データベース挿入メソッド
    fun insertColor(color: FavoriteColorDataClass) {
        viewModelScope.launch {
            favoriteColorRepository.insertColor(color)
        }
    }

    //==========
}