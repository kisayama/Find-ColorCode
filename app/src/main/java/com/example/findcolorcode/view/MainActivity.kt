package com.example.findcolorcode

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.findcolorcode.ViewModel.MainViewModel
import com.example.findcolorcode.components.BottomBar
import com.example.findcolorcode.components.BottomBarTab
import com.example.findcolorcode.ui.theme.FindColorCodeTheme
import com.squareup.moshi.Moshi
import okhttp3.Route


//エントリーポイント
class MainActivity : ComponentActivity(){

    //MoshiをコンパニオンオブジェクトにするTODO　消す予定
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

                //MainScreenを呼び出し
                MainScreen(navController, viewModel)
               }
            }
        }
    }


@Composable
fun MainScreen(navController: NavHostController, viewModel: MainViewModel) {
    //初期選択アイテムを指定する
    var selectedItem by remember { mutableIntStateOf(0) }
    Scaffold(
        bottomBar = { BottomBar(navController = navController,selectedItem)
        }
        //paddingはbottombarとかさならないようにscalffoldから提供される
    ) {padding ->
    NavHost( //ルートに基づいて他のコンポーサブルのデスティネーション（フラグメント）を表示する
        navController = navController,
        startDestination =  BottomBarTab.ColorChoice.route,//初期画面はcolorChoiceFragment
        Modifier.padding(padding)
    ){
        //MainActivityに表示するのは以下のフラグメント
        //RouteはenumClassのBottomBarTabから取得
        composable(BottomBarTab.ColorChoice.route){MainViewModel.Colorc(navController,viewModel) }
        composable(BottomBarTab.FavoriteList.route){FavoriteListScreen(navController,viewModel)}}
    } 
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview(){
    FindColorCodeTheme {
        val navController = rememberNavController() // NavControllerのスタブ
        val viewModel: MainViewModel = viewModel() // ViewModelのスタブ
        MainScreen(navController,viewModel)
    }
}



    /*
    //各リスナーはMainActivityに実装している
    ColorSaveDialog.ColorSaveListener,
    ColorEditDialog.ColorEditListener,
    FavoriteColorAdapter.baseOnColorClickListener{


    private lateinit var binding: ActivityMainBinding
    private lateinit var colorChoiceFragment: ColorChoiceFragment
    private lateinit var favoriteColorFragment: FavoriteColorFragment
    private lateinit var buttons: List<ImageView>





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttons = listOf(
            binding.colorChoiceFragmentBtn,
            binding.FavoriteColorFragmentBtn
        )

        colorChoiceFragment = ColorChoiceFragment.newInstance()
        favoriteColorFragment = FavoriteColorFragment.newInstance()

        //最初に表示する
        val fragmentContainerId = R.id.fragmentContainer
        supportFragmentManager.beginTransaction()
            .replace(fragmentContainerId, ColorChoiceFragment())
            .commit()
        updateButtonColor(0,buttons)

        //ボタンを押してフラグメント表示する
        binding.colorChoiceFragmentBtn.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(fragmentContainerId, colorChoiceFragment)
                .commit()
                updateButtonColor(0,buttons)
        }

        binding.FavoriteColorFragmentBtn.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(fragmentContainerId, favoriteColorFragment)
                .commit()
                updateButtonColor(1,buttons)
        }
    }

      override fun onColorSaved(newColorIndex: Int, newColor: FavoriteColorDataClass) {
          favoriteColorFragment.addNewColor(newColorIndex,newColor)
        Log.d("MainActivity","$favoriteColorFragment")
      }

        override fun onColorEdit(editColorIndex: Int,editedColor:FavoriteColorDataClass) {
            favoriteColorFragment.editColor(editColorIndex,editedColor)
        }

    //Viewがタッチされる時に処理を実行するメソッド
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val inputMethodManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val focusView = currentFocus ?:return false

        inputMethodManager.hideSoftInputFromWindow(
            focusView.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
        return false
    }

    //favoriteColorAdapterから通知を受け取ってColorChoiceFragmentを表示
    override fun deliveryColorChoiceFragment(colorCode: String){
        val bundle = Bundle()
        bundle.putString("colorCode",colorCode)

        val fragment = ColorChoiceFragment()
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer,fragment)
            .commit()
        updateButtonColor(0, buttons)
    }

    private fun updateButtonColor(selectedIndex: Int, buttons: List<ImageView>) {
        val selectColor = getColor(R.color.blue02)
        val defaultColor = getColor(R.color.gray)

        buttons.forEachIndexed { index, buttonImageView ->
            if (index == selectedIndex) {
                buttonImageView.setColorFilter(selectColor)
            } else {
                buttonImageView.setColorFilter(defaultColor)
            }
        }
    }
}
*/
