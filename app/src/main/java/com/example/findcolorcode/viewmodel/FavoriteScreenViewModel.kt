package com.example.findcolorcode.viewmodel

import androidx.core.graphics.convertTo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findcolorcode.model.FavoriteColorDataClass
import com.example.findcolorcode.repository.FavoriteColorRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FavoriteScreenViewModel(
    private val favoriteColorRepository: FavoriteColorRepository
) : ViewModel() {

    // === プロパティ ===

    //--データベース関連--
    //現在データベースで管理されている全てのデータ
    private val _allColors = MutableLiveData<List<FavoriteColorDataClass>>(emptyList())   // データベースのデータ
    val allColors: LiveData<List<FavoriteColorDataClass>> get() = _allColors

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

    // データベースからデータを全て取得する
    private fun getAllColors() {
        viewModelScope.launch {
            _allColors.value = favoriteColorRepository.getAllColors()
        }
    }

    fun filter() {
        val filterText = _filterText.value ?: ""

        _filteredColors.value = if (filterText.isEmpty()) {
            //filterText（検索欄）がヌルなら全てのデータを表示する
            allColors.value
        } else {
            //全角スペースまたは半角スペース区切りで分割する
            val keywords = filterText.split(" ", "　")
            //フィルタリングする
            keywords.any{keyword ->
                allColors.value?.filter { it ->
                    it.colorCode.contains(keyword, ignoreCase = true) ||
                            it.colorMemo.contains(keyword, ignoreCase = true) ||
                            it.colorName.contains(keyword, ignoreCase = true) ||
                            SimpleDateFormat(
                                "yyyy/MM/dd",
                                Locale.getDefault()
                            ).format(Date(it.editDateTime)).contains(keyword, ignoreCase = true)
                }
            }
        }


    // フィルター用テキストを更新する
    fun updateFilterText(newFilterText: String) {
        _filterText.value = newFilterText
    }

    // ミリ秒を"yyyy/MM/dd"形式の日付に変換する
    fun convertCurrentTimeMillisToYYYYMMDD(millis: Long): String {
        val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val date = Date(millis)
        return formatter.format(date)
    }
}
    }