package com.example.findcolorcode.viewmodel

import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findcolorcode.model.FavoriteColorDataClass
import com.example.findcolorcode.repository.FavoriteColorRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FavoriteScreenViewModel(
    private val favoriteColorRepository: FavoriteColorRepository
) : ViewModel() {

    // === プロパティ ===

    //--データベース関連--
    private val _allColors =
        MutableLiveData<List<FavoriteColorDataClass>>(emptyList())   // データベースのデータ
    private val allColors: LiveData<List<FavoriteColorDataClass>> get() = _allColors

    //--フィルター関連--
    private val _filterText = MutableLiveData("")   // フィルター用テキスト
    val filterText: LiveData<String> get() = _filterText//フィルター用テキスト（読み取り専用）

    // フィルタリング後のリストを保持する
    private val _filteredColors = MutableLiveData<List<FavoriteColorDataClass>>(emptyList())
    val filteredColors: LiveData<List<FavoriteColorDataClass>> get() = _filteredColors

    //色情報の変更ダイアログ
    //===ColorInfoChangeDialogの表示状態を表すフラグ===
    private val _dialogOpen = MutableLiveData(false)
    val isDialogOpen: LiveData<Boolean> get() = _dialogOpen
    //======

    //トースト関連
    //===トーストメッセージ===
    private val _toastMessage = MutableLiveData("")
    val toastMessage: LiveData<String> get() = _toastMessage

    //変更メソッド
    private fun updateToastMessage(message:String){
        _toastMessage.value = message
    }
    fun resetToast(){
        _toastMessage.value = ""
    }
    //======

    // === 初期化処理 ===
    init {
        // データベースからデータを全て取得する
        getAllColors()
    }


    // === メソッド ===

    fun updateDialogOpen(newDialogOpen:Boolean){
        _dialogOpen.value = newDialogOpen
    }

    // データベースからデータを全て取得し_allColorsに格納する

    private fun getAllColors() {
        viewModelScope.launch {
            //collectは.flowで流された各データに{}内の処理を行う
            favoriteColorRepository.getAllColors().collect { colorList ->
                //新しく作成・編集をされた順にリストを並び替える（editDateTimeはUNIXTIMEスタンプ）
                val sortedColorList = colorList.sortedByDescending { it.editDateTime }
                _allColors.value = sortedColorList
                //filteredColorsの初期設定
                _filteredColors.value = _allColors.value
            }

        }
    }

    //=====

    //選択したデータを削除する
    fun deleteColors(id: String) {
        viewModelScope.launch {
            //getColorByIdは条件に一致する<FavoriteColorDataClass>を一つずつ流すが
            // idは一意なのでfirstで最初のデータだけ取得すればOK
            val colorToDelete = favoriteColorRepository.getColorById(id).first()
            //削除する
            favoriteColorRepository.deleteColor(colorToDelete)
        }
    }

    //選択した色を更新する
    //_chosenColorを変更したものを引数colorに渡す
     fun updateColors(color: FavoriteColorDataClass) {
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
         各キーワードすべてに一致した色全てを検索結果として出力する
     */
     fun filter() {
        val filterText = _filterText.value ?: ""
        _filteredColors.value = if (filterText.isEmpty() || filterText == "") {
            //filterText（検索欄）が空なら全てのデータを表示する
            allColors.value
        } else {
            //フィルタリング後に同じ色を保持しないように一時保持用のSetを定義する
            val filteredColorList: MutableList<FavoriteColorDataClass> = mutableListOf()
            //全角スペースまたは半角スペース区切りで分割する
            val keywords = filterText.split(" ", "　")
            val allColorsList = allColors.value ?: emptyList()
            //キーワードごとにフィルタリングしたリスト
            for (color in allColorsList) {
                //各キーワードはAND条件で絞り込みを行う
                //（例　2024　赤　ならどちらも以下の検索対象にのいずれかに当てはまるものを抽出する)
                val filteredAllKeyWords = keywords.all { word ->
                    color.colorCode.contains(word, ignoreCase = true) ||
                            color.colorMemo.contains(word, ignoreCase = true) ||
                            color.colorName.contains(word, ignoreCase = true) ||
                            SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                                .format(Date(color.editDateTime))
                                .contains(word, ignoreCase = true)
                }
                if (filteredAllKeyWords) {
                    filteredColorList.add(color)
                }
            }
            filteredColorList
        }
    }

        // フィルター用テキストを更新する
         fun updateFilterText(newFilterText: String) {
            _filterText.value = newFilterText
        }

        //Filter用テキストを空にするメソッド
         fun clearFilterText() {
            _filterText.value = ""
            // フィルターテキストがクリアされた後、onValueChangeが適用されないため
            // allColorsから全てのデータを取得し、_filteredColorsに設定することで
            // 表示される色のリストを更新する
            _filteredColors.value = allColors.value
        }
    //=====

    //その他

        // ミリ秒を"yyyy/MM/dd"形式の日付に変換する
         fun convertCurrentTimeMillisToYYYYMMDD(millis: Long): String {
            val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            val date = Date(millis)
            return formatter.format(date)
        }

        //クリップボードにカラーコードをコピーする
        fun copyToClipBoard(context:Context,colorCode:String){
            val clipBoard = context.getSystemService(Context.CLIPBOARD_SERVICE)as ClipboardManager
            val clip = android.content.ClipData.newPlainText("カラーコード",colorCode)
            clipBoard.setPrimaryClip(clip)
            updateToastMessage("カラーコードをクリップボードにコピーしました")
        }

}