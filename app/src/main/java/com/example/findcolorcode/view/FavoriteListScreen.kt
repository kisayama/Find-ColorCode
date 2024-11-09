    package com.example.findcolorcode.view

    import android.util.Log
    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.aspectRatio
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
    import androidx.compose.material3.CardColors
    import androidx.compose.material3.CardDefaults
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.OutlinedTextField
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.livedata.observeAsState
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.TextStyle
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.unit.dp
    import androidx.navigation.NavController
    import com.example.app.ui.theme.AppColors
    import com.example.findcolorcode.R
    import com.example.findcolorcode.components.ShowToast
    import com.example.findcolorcode.components.favoriteColorScreenMenu
    import com.example.findcolorcode.model.FavoriteColorDataClass
    import com.example.findcolorcode.ui.theme.Dimensions
    import com.example.findcolorcode.ui.theme.customTextFieldColors
    import com.example.findcolorcode.viewmodel.FavoriteScreenViewModel

    @Composable
    fun FavoriteColorList(
        navController: NavController,
        viewModel: FavoriteScreenViewModel
    ) {
        // filter後のリストを取得する　filterTextが空ならデータベースの全てのデータ
        val displhayColors by viewModel.filteredColors.observeAsState(emptyList())

        //フィルター用のテキスト
        val filterText by viewModel.filterText.observeAsState("")

        //トーストメッセージを取得
        val toastMessage by viewModel.toastMessage.observeAsState("")

        //LazyColumnで選択したアイテム
        var selectedColorItem:FavoriteColorDataClass? by remember { mutableStateOf(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.White)
                .padding(Dimensions.screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //フィルター用の入力フォーム
            Column {
                Row(
                    modifier = Modifier.height(50.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {

                    OutlinedTextField(
                        modifier = Modifier
                            .weight(6f),
                        value = filterText,
                        //TextFieldの入力文字の大きさを設定
                        textStyle = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
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
                            IconButton(onClick = {
                                viewModel.clearFilterText()
                                Log.d("FavoriteScreen", "clearFilter${filterText}")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                )
                            }
                        },
                        colors = customTextFieldColors(),
                        placeholder = {
                            Text(
                                text = "色の名前、メモ、日付など",
                                fontSize = MaterialTheme.typography.labelSmall.fontSize
                            )
                        },
                        //入力された値でfilterTextを更新する
                        //変更後のテキストを使用してフィルタリングを行う
                        onValueChange = { newText ->
                            viewModel.updateFilterText(newText)
                            viewModel.filter()
                        },
                        maxLines = 1
                    )
                    //ソートボタン
                    IconButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        onClick = {}/*TODO　並び替えメニューを表示*/,
                    ) {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(id = R.drawable.ic_sort_24),
                            contentDescription = "ソート",
                            tint = AppColors.Black
                        )
                    }
                }
            }
            //データベースに含まれるカラーデータを表示する
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(displayColors) { color ->

                        //メニューの開閉状態と、現在選択されているアイテムのIDを管理する
                        var isMenuExpand:Boolean by remember { mutableStateOf(false) }
                        var expandedItemId:String by remember { mutableStateOf("")}
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            colors = CardColors(
                                //カード内の背景や文字の色を指定する
                                containerColor = AppColors.White,
                                contentColor = AppColors.Black,
                                disabledContainerColor = AppColors.Black,
                                disabledContentColor = AppColors.White
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 6.dp
                            )
                            , onClick = {
                                selectedColorItem = color
                                expandedItemId = color.id
                            }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 13.dp, bottom = 8.dp, start = 10.dp, end = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp) // Box等のRowとカラーコード、日付Rowの間にスペース
                            ) {
                                /*TODO 本当はROW内に２Column　カラーボックスとカラーコード　名前、メモ、日付　のに列を１Rowとして表示したかったけど
                                        日付のCard下辺右寄せができなかったから２Row　カラーボックス、名前、メモ　カラーコード、日付としている
                                        Cardないの2つ目のColumnのサイズに下限がいてしまうことが原因なので解決方法が見つかれば上記配置に修正する*/

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
                                            .background(
                                                Color(
                                                    android.graphics.Color.parseColor(
                                                        color.colorCode
                                                    )
                                                )
                                            ),
                                    )
                                    // 色の名前とメモを縦に並べる
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(start = 15.dp) ,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)// 名前とメモの間にスペースを追加
                                    ) {
                                        Row {
                                            Text(modifier = Modifier.weight(1f),// 色の名前
                                                text = color.colorName,
                                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                            )
                                            IconButton(
                                                onClick = {
                                                    selectedColorItem = color
                                                    expandedItemId = color.id
                                                    isMenuExpand = true
                                                    //メニューが開かれている且つ選択されている色のIDと一致している

                                                          },
                                                modifier = Modifier
                                                    .padding(0.dp)
                                                    .size(24.dp))
                                            {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_more),
                                                    contentDescription = "その他のメニュー",
                                                    tint = AppColors.Black
                                                )
                                                if (isMenuExpand && selectedColorItem == color ) {
                                                    favoriteColorScreenMenu(
                                                        colorItem = selectedColorItem!!,
                                                        viewModel = viewModel,
                                                        openMenuExpand = true,
                                                        closeMenuExpand = {isMenuExpand = false}
                                                    )
                                                    Log.d("FavoriteScreen","${selectedColorItem}colorItem:${viewModel}")
                                                }
                                            }

                                        }
                                        Text(text = color.colorMemo, fontSize = MaterialTheme.typography.bodyLarge.fontSize) // 色のメモ
                                    }
                                }

                                // 2行目: カラーコードと日付
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween // 左右に配置
                                ) {
                                    // カラーコード
                                    Text(
                                        modifier = Modifier.padding(start = 15.dp),
                                        text = color.colorCode,
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                                    )
                                    // 日付
                                    Text(
                                        text = viewModel.convertCurrentTimeMillisToYYYYMMDD(color.editDateTime),
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                    }
                }

            ShowToast(toastMessage = viewModel.toastMessage.value.toString(), resetMessage = { viewModel.resetToast() })
        }
    }



