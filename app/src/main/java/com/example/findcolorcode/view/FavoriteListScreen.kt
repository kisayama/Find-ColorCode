package com.example.findcolorcode.view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.findcolorcode.model.FavoriteColorDataClass
import com.example.findcolorcode.repository.FavoriteColorRepository
import com.example.findcolorcode.ui.theme.Dimensions
import com.example.findcolorcode.viewmodel.FavoriteScreenViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FavoriteColorList(
    navController: NavController,
    viewModel: FavoriteScreenViewModel
) {
    // データベースから取得したお気に入りの色リスト
    val allColors by viewModel.allColors.observeAsState(emptyList())

    //フィルター用のテキスト
    val filterText by viewModel.filterText.observeAsState("")

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(Dimensions.screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
    ){
        //フィルター用の入力フォーム
        Column{
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = filterText,
                //入力された値でfilterTextを更新する
                onValueChange = {newText -> viewModel.updateFilterText(newText)}

            )
        }

        LazyColumn {
            items(allColors) { color ->
                ColorItem(
                    colorItem = color,
                    //currentTimeMillisの変換メソッドを引き渡す
                    convertCurrentTimeMillisToYyyyMmDd ={ millis ->
                        viewModel.convertCurrentTimeMillisToYYYYMMDD(millis)
                    }
                )
            }
        }
    }
}


@Composable
private fun ColorItem (
    colorItem:FavoriteColorDataClass,
    convertCurrentTimeMillisToYyyyMmDd:(Long) -> String)
{
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ){
            Row (
                modifier = Modifier.padding(10.dp),
                ){
                Column {
                    
                    //左右に１：２の比率で分け左側には色を視覚的に表示するためのボックス、カラーコード
                    //右側には色の詳細データ（色の名前,メモ,日付など）
                    Row (modifier = Modifier.weight(1f)){
                        //ボックス
                        Box (modifier = Modifier
                            .size(100.dp)
                            .background(Color(android.graphics.Color.parseColor(colorItem.colorCode)))
                        )
                        //カラーコード
                        Text(text = colorItem.colorCode)
                    }
                    Column(modifier=Modifier.weight(1f)) {
                        //色の詳細データ
                        Text(text = colorItem.colorName)//名前
                        Text(text = colorItem.colorMemo)//メモ
                        //currentTimeMillisからyyyy/mm/dd形式に変換する　
                        Text(text = convertCurrentTimeMillisToYyyyMmDd(colorItem.editDateTime)) //日付
                    }
                }
            }
        }
}