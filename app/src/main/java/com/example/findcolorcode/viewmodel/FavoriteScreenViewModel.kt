package com.example.findcolorcode.viewmodel

import androidx.compose.ui.res.colorResource
import androidx.core.graphics.convertTo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findcolorcode.model.FavoriteColorDataClass
import com.example.findcolorcode.repository.FavoriteColorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
class FavoriteScreenViewModel(
    private val favoriteColorRepository: FavoriteColorRepository
) : ViewModel() {

    // === プロパティ ===

    //--データベース関連--
    private val _allColors = MutableLiveData<List<FavoriteColorDataClass>>(emptyList())   // データベースのデータ
    val allColors: LiveData<List<FavoriteColorDataClass>> get() = _allColors


    //選択した色のデータ
    private val _chosenColor = MutableLiveData<FavoriteColorDataClass>()
    val chosenColor :LiveData<FavoriteColorDataClass> get() = _chosenColor

    //--フィルター関連--
    private val _filterText = MutableLiveData<String>("")   // フィルター用テキスト
    val filterText: LiveData<String> get() = _filterText//フィルター用テキスト（読み取り専用）

    // フィルタリング後のリストを保持する
    private val _filteredColors = MutableLiveData<List<FavoriteColorDataClass>>(emptyList())
    val filteredColors:LiveData<List<FavoriteColorDataClass>> get() = _filteredColors


    // === 初期化処理 ===
    init {
        // データベースからデータを全て取得する
        getAllColors()
    }


    // === メソッド ===

    // データベースからデータを全て取得し_allColorsに格納する
    private fun getAllColors() {
        viewModelScope.launch {
            //collectは.flowで流された各データに{}内の処理を行う
            favoriteColorRepository.getAllColors().collect{ colorList ->
                _allColors.value = colorList
            }
        }
    }

    //選択したデータを削除する
    fun deleteColors(id: String){
        viewModelScope.launch {
            //getColorByIdは条件に一致する<FavoriteColorDataClass>を一つずつ流すが
            // idは一意なのでfirstで最初のデータだけ取得すればOK
            val colorToDelete = favoriteColorRepository.getColorById(id).first()
            //削除する
            favoriteColorRepository.deleteColor(colorToDelete)
        }
    }

    //IDから色を特定しFavoriteColorDataClass型のデータを返す
    fun searchColorById(id:String){
        viewModelScope.launch {
            _chosenColor.value = favoriteColorRepository.getColorById(id).first()
        }
    }

    //選択した色を更新する
    //_chosenColorを変更したものを引数colorに渡す
    fun updateColors(color: FavoriteColorDataClass){
        viewModelScope.launch {
            //カラーデータの更新を行う　
            favoriteColorRepository.updateColor(color)
            //データベースの最新のデータを取得し,_allColorsに格納する
            getAllColors()
        }
    }

    //フィルターメソッド
    /*Filterのルール
      1.検索対象はallColorsの以下の項目
      colorCode,colorMemo,colorName,editDateTime(yyyy/mm/dd形式の文字列)
      2.[赤　2024]といったスペースで区切られた検索ワードの場合
      　　キーワードごとに個別にフィルタリングを行い、
         各キーワードに一致した色全てを検索結果として出力する
      3.同じ色が複数の検索条件に一致した場合は、重複なく出力する
     */
    fun filter() {
        val filterText = _filterText.value ?: ""
        _filteredColors.value = if (filterText.isEmpty()) {
            //filterText（検索欄）が空なら全てのデータを表示する
            allColors.value
        } else {
            //フィルタリング後に同じ色を保持しないように一時保持用のSetを定義する
            val currentSet :MutableSet<FavoriteColorDataClass> = mutableSetOf()
            //全角スペースまたは半角スペース区切りで分割する
            val keywords = filterText.split(" ", "　")
            //各キーワードでフィルタリングを行う
            keywords.forEach { keyword ->
                //キーワードごとにフィルタリングしたリスト
                val filteredByKeywords = allColors.value?.filter { color ->
                            color.colorCode.contains(keyword, ignoreCase = true) ||
                            color.colorMemo.contains(keyword, ignoreCase = true) ||
                            color.colorName.contains(keyword, ignoreCase = true) ||
                            SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                                .format(Date(color.editDateTime))
                                .contains(keyword, ignoreCase = true)
                }
                //currentSetに追加する（重複したcolorを削除することができる）
                filteredByKeywords?.let { currentSet.addAll(it) }
            }
            //SetをList型に変換し _filteredColors.valueに格納
             currentSet.toList()
        }
        }

    // フィルター用テキストを更新する
    fun updateFilterText(newFilterText: String) {
        _filterText.value = newFilterText
    }

    //Filter用テキストを空にするメソッド
    fun clearFilterText() {
        _filterText.value = ""
    }



    // ミリ秒を"yyyy/MM/dd"形式の日付に変換する
    fun convertCurrentTimeMillisToYYYYMMDD(millis: Long): String {
        val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val date = Date(millis)
        return formatter.format(date)
    }
}