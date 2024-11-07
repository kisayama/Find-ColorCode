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
    private val favoriteColorRepository :FavoriteColorRepository
) :ViewModel() {

    //==選択squareについて==
    //選択squareのインデックス
    private val _selectedSquare = MutableLiveData(1)
    val selectedSquare: LiveData<Int> = _selectedSquare

    //選択squareを変更する
    fun changeSelectedSquare(newNumber: Int) {
        _selectedSquare.value = newNumber
    }
    //===================

    //==squareColorCodeの初期値の定義と更新メソッド==
    //Square1
    private val _square1ColorCode = MutableLiveData("#FFFFFF")//デフォルトのカラーコード
    val square1ColorCode: LiveData<String> get() = _square1ColorCode

    //Square2
    private val _square2ColorCode = MutableLiveData("#FFFFFF")//デフォルトのカラーコード
    val square2ColorCode: LiveData<String> get() = _square2ColorCode

    //入力されたカラーコードをViewModelのカラーコードに入力する
    //TextFieldのリアルタイムな値（手入力した値も）を兼ねるのでここでカラーコードの検証は行わない
    fun updateColorCode(squareIndex: Int, newValue: String) {
        when (squareIndex) {
            1 -> _square1ColorCode.value = newValue
            2 -> _square2ColorCode.value = newValue
        }
        Log.d("BasicColorCodeContent","changedColorCode${_square1ColorCode.value}")

    }
    //===================

    //==backgroundColorの背景色==
    //ユーザーがテキスト入力中に背景色が変わらないように
    // squareColorCodeとは別に背景色を管理する変数を用意しておく

    //Square1
    private val _square1BackgroundColorCode = MutableLiveData("#FFFFFF")//デフォルトのカラーコード
    val square1BackgroundColorCode: LiveData<String> get() = _square1BackgroundColorCode

    //Square2
    private val _square2BackgroundColorCode = MutableLiveData("#FFFFFF")//デフォルトのカラーコード
    val square2BackgroundColorCode: LiveData<String> get() = _square2BackgroundColorCode


    fun updateBackgroundColorCode(squareIndex: Int, validColorCode: String) {
        when (squareIndex) {
            1 -> _square1BackgroundColorCode.value = validColorCode
            2 -> _square2BackgroundColorCode.value = validColorCode
        }
        Log.d("BasicColorCodeContent","changedBackgroundColorCode${_square1BackgroundColorCode.value}")
    }
    //===================

    //====シークバーのRGB値を保存する変数square1,square2====
    //rememberはデバイスの回転などのアクティビティの破棄をされると状態が保存されないことに注意
    val red1 = MutableLiveData(255)//square1の各シークバーの値を保存する変数とその初期値
    val green1 = MutableLiveData(255)
    val blue1 = MutableLiveData(255)

    val red2 = MutableLiveData(255)//square2の各シークバーの値を保存する変数とその初期値
    val green2 = MutableLiveData(255)
    val blue2 = MutableLiveData(255)
    //===================

    //===ColorPalletContentに表示するカラーパレットのリスト===
    // カラーコードは#付きのHex形式、リストサイズは5
    private val initialColorPalletList = listOf("#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF")//初期値のリスト
    private val _colorPalletList = MutableLiveData(initialColorPalletList)
    val colorPalletList:LiveData<List<String>> get() = _colorPalletList

    //======

f
    //======

    //===ColorSveDialogの表示状態を表すフラグ===
    private val _openDialog = MutableLiveData<Boolean>(false)
    val openDialog: LiveData<Boolean> get() = _openDialog
    //変更メソッド
    fun updateOpenDialog(newOpenDialog:Boolean){
        _openDialog.value = newOpenDialog
    }
    //======

    //選択しているsquareに応じたシークバーの値を取得する関数
    fun setSquareRGB(selectedSquare: Int, rgbColorType: String, value: Int) {
        when (selectedSquare) {
            1 -> {
                when (rgbColorType) {
                    "red" -> red1.value = value
                    "green" -> green1.value = value
                    "blue" -> blue1.value = value
                }
            }

            2 -> {
                when (rgbColorType) {
                    "red" -> red2.value = value
                    "green" -> green2.value = value
                    "blue" -> blue2.value = value
                }
            }
        }
    }

    //====カラーコード→RGB=====

    //TextFieldに入力された値を16進数のカラーコードに変換する
    //Hexが正しい形式か検証するメソッドとしても利用する
    fun convertToHexColorCode(text:String):String?{
        //TextFieldが空の時はnullを返し呼び出し元で処理を行わないようにする
        val trimText = text.trim()//スペースを削除
        if (trimText.isEmpty()){
            return null
        }
        return try {
            //parseColorで変換した値
            val intColorCode = Color.parseColor(text)
            //colorInt(上2桁は透明度、下4桁はRGB)から透明度を無視するAND演算を行いRGB部分だけ取得する
            //0xはプレフィックスで数値が16進数であることを示す
            val rgbColorCode = Color.parseColor(text) and 0x00FFFFFF
            Log.d("convertToHexColorCode","return $rgbColorCode")
            String.format("#%06X", rgbColorCode)
        }catch (e:IllegalArgumentException){
            //入力されたtextからColorCodeが見つからない場合nullを返す
            null
    }
    }

    fun convertToRGB(selectedSquare: Int) {
        when (selectedSquare) {
            //selectedSquareに応じて現在のcolorCodeを取得しRGBを計算する
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
            Log.d("ColorChoiceScreen",colorCode)
            val adjustColorCode = Color.parseColor(colorCode)
            val red = Color.red(adjustColorCode)
            val green = Color.green(adjustColorCode)
            val blue = Color.blue(adjustColorCode)
            Log.d("convertToRGB","convertToRGB")
        Triple(red, green, blue)// R,G,BをTripleで返す
        } catch (e:IllegalStateException){
            //エラーが起きた場合デフォルトのRGB値を返す
            Log.d("convertToRGB","convertToRGB")
            Triple(255,255,255)
        }
    }
    //=============

    //====RGB→カラーコード=====
    fun convertToColorCode(selectedSquare: Int) {
        when (selectedSquare) {
            //selectedSquareに応じて現在のcolorCodeを取得しRGBを計算する
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

    //====API関連====

    //selectedColorPalletContentに表示するpalletColorListを取得するためのAPI通信を行う
    fun fetchColorScheme(colorCode: String){
        viewModelScope.launch {
            try {
                val response = apiRepository.getColorScheme(
                    colorCode.removePrefix("#")//#を取り除いたHex値を引き渡す
                )
                //リストのサイズが5かつ全てのカラーコードが正しい形式であることを確認
                if (response.size == 5&&
                    response.all {convertToHexColorCode(it) != null}
                    ){
                    _colorPalletList.value = response //APIから取得したレスポンスをカラーパレットリストに保存
                    .value = "カラーパレットが作成できました！"
                }else{
                    //RepositoryImplからのレスポンスが誤った形式で他の通信エラーと同様のタグを設定する
                    Log.e("RepositoryImpl","リストサイズ:${response.size},colorCodeHex:${response}　サイズが5以外,colorCodeHexの形式エラー")
                    _toastMessage.value = "無効なレスポンスが含まれています。再度お試しください"
                }
            }
            catch (e: SocketTimeoutException){
                _toastMessage.value = "通信がタイムアウトしました。ネットワーク接続を確認してください"
            }
            catch (e: UnknownHostException){
                _toastMessage.value = "インターネット接続エラー　ネットワーク接続を確認してください"
            }
            catch (e:Exception){
                _toastMessage.value = "予期しないエラーが発生しました。ネットワーク接続を確認してください"
            }

        }
    }

    //=======

    //データベース関連のメソッド
    fun insertColor(color:FavoriteColorDataClass) {
        viewModelScope.launch {
            favoriteColorRepository.insertColor(color)
        }
    }
}
//廃止OR今後実装するかもしれないコード置き場

//今後
    /*可読性を高めるためにDataClassにまとめてもいいかも。
     データクラス内のデータに一つでも変更があるとデータクラス内を全て再描写しないといけないから注意（RGBならいいかも）
     //square1の各シークバーの値とその初期値
    val square1ColorDataValues = MutableLiveData(ColorRGBValues(255,255,255))

    //square2の各シークバーの値とその初期値
    val square2ColorDataValues = MutableLiveData(ColorRGBValues(255,255,255))

    data class ColorRGBValues(
    val red:Int,
    val green :Int,
    val blue :Int
    )
    */

//廃止
    /*　TextField入力についてToastを表示しない方針
    //ColorCode検証時に表示するエラーコード
    private val _colorCodeErrorMessage = MutableLiveData<String>()//デフォルトのカラーコード
    val colorCodeErrorMessage: LiveData<String> get() = _colorCodeErrorMessage
    */

    /*入力された値をParseできるかどうかで判断する方がシンプルだから検証機能を廃止した
    //ColorCodeが手入力された時に検証するメソッド
    fun isValidColorCode(colorCode: String): Boolean {
    //パースできるColorCodeの形式は以下の3種類
    Log.d("ValidColorCode",colorCode)

    //正規表現　#RRGGBB 6桁の16進数
    val RRGGBB = Regex("^#([0-9a-fA-F]{6})$", RegexOption.IGNORE_CASE)

    //正規表現　#AARRGGBB 8桁の16進数
    val AARRGGBB = Regex("^#[0-9A-Fa-f]{8}$", RegexOption.IGNORE_CASE)

    // 認識できる色名をセットにする
    val colorNames = setOf(
        "red", "blue", "green", "black", "white", "gray", "cyan", "magenta",
        "yellow", "lightgray", "darkgray", "grey", "lightgrey", "darkgrey",
        "aqua", "fuchsia", "lime", "maroon", "navy", "olive", "purple", "silver", "teal"
    )

    //入力された値が上記3種類に当てはまるか検証
    return colorCode.let {
        it.matches(RRGGBB) || it.matches(AARRGGBB) || it.lowercase() in colorNames
    }
}
    */
