    package com.example.findcolorcode.view

    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.aspectRatio
    import androidx.compose.foundation.layout.fillMaxHeight
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Clear
    import androidx.compose.material.icons.filled.Search
    import androidx.compose.material3.Card
    import androidx.compose.material3.CardDefaults
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.OutlinedTextField
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.livedata.observeAsState
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.navigation.NavController
    import com.example.findcolorcode.R
    import com.example.findcolorcode.model.FavoriteColorDataClass
    import com.example.findcolorcode.ui.theme.Dimensions
    import com.example.findcolorcode.ui.theme.customTextFieldColors
    import com.example.findcolorcode.viewmodel.FavoriteScreenViewModel

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
                Row (modifier = Modifier.height(50.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)){
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(6f),
                        value = filterText,
                        //テキストフィールドの左端に虫眼鏡ボタンを設置
                        //目印なので押した時の処理は無し
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        },
                        //テキストフィールドの右端にクリアボタンを設置
                        trailingIcon = {
                            IconButton(onClick = { viewModel.clearFilterText() }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                )
                            }
                        },
                        colors = customTextFieldColors(),
                        placeholder = { Text(text ="色の名前、メモ、日付など" , fontSize = 12.sp) },
                        //入力された値でfilterTextを更新する
                        //変更後のテキストを使用してフィルタリングを行う
                        onValueChange = {
                            newText -> viewModel.updateFilterText(newText)
                            viewModel.filter()
                        }
                    )
                    //ソートボタン
                    IconButton(onClick = {}/*TODO　並び替えメニューを表示*/,) {
                        Icon(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        painter = painterResource(id = R.drawable.ic_sort_24),
                        contentDescription = "ソート"
                        )
                    }
                }
            }

            //データベースに含まれるカラーデータを表示する
            LazyColumn (modifier = Modifier.fillMaxSize()){
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
        private fun ColorItem(
            colorItem: FavoriteColorDataClass,
            convertCurrentTimeMillisToYyyyMmDd: (Long) -> String
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 13.dp, bottom = 8.dp, start = 10.dp , end =10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp) // 行間を少し開ける
                ) {
                    // 1行目: カラー表示ボックス、色の名前、色のメモ
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        // カラー表示ボックス
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .aspectRatio(1f)
                                .background(Color(android.graphics.Color.parseColor(colorItem.colorCode))),
                        )
                        // 色の名前とメモを縦に並べる
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 10.dp) ,// ボックスとテキストの間にスペースを追加
                        ) {
                            Text(text = colorItem.colorName) // 色の名前
                            Text(text = colorItem.colorMemo) // 色のメモ
                        }
                    }

                    // 2行目: カラーコードと日付
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween // 左右に配置
                    ) {
                        // カラーコード
                        Text(modifier = Modifier.padding(start = 15.dp), text = colorItem.colorCode)
                        // 日付
                        Text(
                            text = convertCurrentTimeMillisToYyyyMmDd(colorItem.editDateTime),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }




