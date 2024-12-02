package com.example.findcolorcode.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.findcolorcode.data.allBasicColorList
import com.example.findcolorcode.model.ColorDataForColorChoice
import com.example.findcolorcode.viewmodel.ColorChoiceViewModel


//ベーシックカラーとカラーパレットを表示するためのタブを設定
@Composable
fun ColorPickerTabs(
    viewModel: ColorChoiceViewModel,
    currentSquareIndex: Int,
    square1ColorData: ColorDataForColorChoice,
    square2ColorData: ColorDataForColorChoice
) {
    //タブコンテンツ
    var isContentsOpen by remember { mutableStateOf(false) }
    //初期状態は基本の色タブを表示する
    var selectedTabIndex by remember { mutableIntStateOf(0) }


        Scaffold(
            topBar = {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.wrapContentHeight(),
                ) {
                    Tab(selected = selectedTabIndex == 0,
                        onClick = {
                            selectedTabIndex = 0
                            isContentsOpen = true
                        },
                        text = {
                            Text(text = "基本の色")
                        }
                    )
                    Tab(selected = selectedTabIndex == 1,
                        onClick = {
                            selectedTabIndex = 1
                            isContentsOpen = true
                        },
                        text = {
                            Text(text = "カラーパレットを作る")
                        }
                    )
                }
            }
        ) { paddingValues -> //Tabの高さ分のパディング
            when (selectedTabIndex) {
                0 -> BasicColorContents(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(top = 10.dp),
                    viewModel = viewModel,
                    currentSquareIndex = currentSquareIndex,
                    // basicColorListはPair(colorCode, name)のリスト
                    // ここでは、colorCodeのみを抽出したリストを引数として渡す
                    allBasicColorList
                )

                1 -> currentColorPalletContent(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .padding(top = 10.dp),
                    viewModel = viewModel,
                    currentSquareIndex = currentSquareIndex,
                    square2ColorData = square2ColorData,
                    square1ColorData = square1ColorData
                )
            }
        }
    }