    package com.example.findcolorcode.view

    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
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
                        modifier = Modifier.weight(6f),
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
                        placeholder = { Text(text ="色の名前、メモ、日付など" , fontSize = 14.sp) },
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


    //カラー表示のレイアウトを子コンポーネントで定義する
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