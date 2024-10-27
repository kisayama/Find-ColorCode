package com.example.findcolorcode.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.findcolorcode.components.BottomBar
import com.example.findcolorcode.components.BottomBarTab
import com.example.findcolorcode.data.ColorDatabase
import com.example.findcolorcode.repository.ColorSchemeRepositoryImpl
import com.example.findcolorcode.repository.FavoriteColorRepositoryImpl
import com.example.findcolorcode.ui.theme.FindColorCodeTheme
import com.example.findcolorcode.viewmodel.ColorChoiceViewModel
import com.example.findcolorcode.viewmodel.MainViewModel
import com.squareup.moshi.Moshi


//エントリーポイント
class MainActivity : ComponentActivity(){

    //Moshiをコンパニオンオブジェクトにする TODO　消す予定
    companion object {
        lateinit var moshi: Moshi
            private set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FindColorCodeTheme {
                //ViewModelのインスタンスを取得
                val viewModel: MainViewModel = viewModel()

                //navController（ナビゲーションの操作を管理する）を取得
                val navController:NavHostController = rememberNavController()

                //カラーデータベースのインスタンスを取得する
                //thisはActivityのみのcontext UIコンポーネント関連に使用する
                // applicationContextはアプリ全体のライフサイクルに結びついている
                // アプリが終了したらコンテキストが開放される
                val colorDatabase = ColorDatabase.getDatabase(applicationContext)

                //MainScreenを呼び出し
                MainScreen(navController, viewModel, colorDatabase = colorDatabase)
               }
            }
        }
    }


@Composable
fun MainScreen(navController: NavHostController, viewModel: MainViewModel, colorDatabase: ColorDatabase) {
    //初期選択アイテムを指定する
    var selectedItem by remember { mutableStateOf(0) }
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController,
                selectedItem = selectedItem,
                onItemSelected = { newIndex -> selectedItem = newIndex },
                )
        }
        //paddingはbottombarとかさならないようにscalffoldから提供される
    ) { padding ->
        //画面をタッチしたらキーボードを非表示にする
        val keyboardController = LocalSoftwareKeyboardController.current
        Surface(
            //ポインタの入力を処理する
            modifier = Modifier.pointerInput(Unit) {
                    //入力リスナーの種類を設定する　この場合はタップ
                    detectTapGestures(onTap = {
                        //キーボードを隠す
                        keyboardController?.hide()
                    }
                    )
                },
                color = Color.Transparent
        ) {
            NavHost( //ルートに基づいて他のコンポーサブルのデスティネーション（フラグメント）を表示する
                navController = navController,
                startDestination = BottomBarTab.ColorChoice.route,//初期画面はcolorChoiceFragment
                Modifier.padding(padding)
            ) {

                //MainActivityに表示するのは以下のフラグメント
                //RouteはenumClassのBottomBarTabから取得
                composable(BottomBarTab.ColorChoice.route) {
                    ColorChoiceScreen(
                        navController,
                        ColorChoiceViewModel(
                            apiRepository = ColorSchemeRepositoryImpl(),
                            favoriteColorRepository = FavoriteColorRepositoryImpl(colorDao =colorDatabase.colorDao())
                        )
                    )
                }
                composable(BottomBarTab.FavoriteList.route) {
                    FavoriteListScreen(
                        navController,
                        viewModel
                    )
                }

            }
        }
    }

    }

