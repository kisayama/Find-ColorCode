package com.example.findcolorcode.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.app.ui.theme.AppColors
import com.example.findcolorcode.viewmodel.ColorViewModel
import androidx.compose.ui.graphics.Color as ComposeColor

//TODO MaterialDesignでテーマカラーの色を調整する
//TODO 時間があればSLiderのthumbを調整するためにカスタムに変更するか検討する
@Composable
fun ColorChoiceScreen(navController: NavController, viewModel: ColorViewModel) {
    //rememberはデバイスの回転などのアクティビティの破棄をされると状態が保存されないことに注意
    //青赤緑のシークバーの初期値を保存しておく
        val red = remember { mutableStateOf(255) }
        val green = remember { mutableStateOf(255) }
        val blue = remember { mutableStateOf(255) }

        //LiveDataが更新された時に自動的にComposableを再描写するためにobserveAsStateを使用する
        val color1Code = viewModel.colorSquare1.observeAsState("#FFFFFFF").value
        val color2Code = viewModel.colorSquare2.observeAsState("#FFFFFFF").value

    val currentColor = ComposeColor(red.value,green.value,blue.value)//シークバーの位置によって色を計算する

        //Boxを横一列に2つ並べる
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp),
            verticalArrangement = Arrangement.Top,//全体を中央揃え
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //四角を横に二つ並べるためのRow
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Square2
                Column {
                    ColorSquare(color = color1Code,onColorselected = { selectedColor ->
                        viewModel.updateColorSquare1(selectedColor)
                    })
                    ColorCodeText(colorCode = color1Code, onValueChanged = {new ->
                        viewModel.updateColorSquare1(new)
                    })
                }
                //Square1とSquare2　の間のスペース
                Spacer(modifier = Modifier.width(40.dp))//間にスペース

                //Square2
                Column {
                    ColorSquare(color = color2Code, onColorselected = { selectedColor ->
                        viewModel.updateColorSquare2(selectedColor)
                    })
                    ColorCodeText(colorCode = color2Code, onValueChanged = {new ->
                        viewModel.updateColorSquare2(new)})
                }
            }
            Spacer(modifier = Modifier.height(20.dp))//四角とシークバーの間のスペース
            //シークバーを三つ縦に並べるためのColumn
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)//シークバー間に15dpのスペースを入れる
            ) {
                Slider(
                    value = red.value.toFloat(),
                    onValueChange = {
                        red.value = it.toInt() //カラー変更の設定,
                    },
                    valueRange = 0f..255f,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    colors = SliderDefaults.colors(
                        activeTickColor = AppColors.Red,
                        activeTrackColor = AppColors.Red,
                        thumbColor = AppColors.Red, //バーの動作中の色
                    )
                )
                Slider(
                    value = blue.value.toFloat(),
                    onValueChange = { blue.value = it.toInt()
                    },
                    valueRange = 0f..255f,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    colors = SliderDefaults.colors(
                        thumbColor = AppColors.Green,
                        activeTrackColor = AppColors.Green,
                        activeTickColor = AppColors.Green//バーの動作中の色
                    )
                )
                Slider(
                    value = green.value.toFloat(),
                    onValueChange = { green.value = it.toInt()
                    },
                    valueRange = 0f..255f,
                    modifier = Modifier.fillMaxWidth(0.9f),//スライダーの横幅は最大値の75%
                    colors = SliderDefaults.colors(
                        thumbColor = AppColors.Blue,
                        activeTrackColor = AppColors.Blue,
                        activeTickColor = AppColors.Blue//バーの動作中の色
                    ),
                )
            }


        }
    }

    @Composable
    //シークバーで作成した色を表示するBox
    fun ColorSquare(color:String,onColorselected:(String)-> Unit) {
        Box (
            modifier = Modifier
                .size(150.dp)
                .clickable { onColorselected(color) }//clickableでクリック時の挙動を設定する
                .background(Color(android.graphics.Color.parseColor(color)))//背景の色を設定
                .border(2.dp,AppColors.Gray03)
        )
    }

    @Composable
    fun ColorCodeText(colorCode:String,onValueChanged:(String)-> Unit) {
        TextField(value = colorCode,
            onValueChange = {new -> onValueChanged(new)},
            label = { Text("カラーコードを入力")},
            modifier = Modifier
                .padding(top = 16.dp)
                .width(150.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = AppColors.White,//フォーカス時の色
                unfocusedContainerColor = AppColors.White,
                focusedIndicatorColor = AppColors.Black,
                focusedLabelColor = AppColors.Gray03,
                unfocusedLabelColor = AppColors.Gray03
        ))
    }

//色を作るためのRGBのシークバー
@Composable
fun SeekBar(value:Int,onValueChange: (Int)-> Unit) {
    Slider(
        value = value.toFloat(),//スライダーを滑らかに動かすためにfloatを指定
        onValueChange = { onValueChange(it.toInt()) },//スライダーの値Float型をIntに変換する
        valueRange = 0f..255f,
        modifier = Modifier.fillMaxWidth(0.75f)//スライダーの横幅は最大値の75%
    )
}



//<?xml version="1.0" encoding="utf-8"?>
//<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
//    xmlns:app="http://schemas.android.com/apk/res-auto"
//    xmlns:tools="http://schemas.android.com/tools"
//    android:id="@+id/main"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:background="@color/bg_white"
//    android:orientation="vertical"
//    android:padding="@dimen/default_padding"
//    tools:context=".view.MainActivity">
//
//    <LinearLayout
//        android:layout_width="wrap_content"
//        android:layout_height="wrap_content"
//        android:orientation="horizontal"
//        android:layout_gravity="center">
//
//        <LinearLayout
//            android:id="@+id/colorSquare1Block"
//            android:layout_width="wrap_content"
//            android:layout_height="wrap_content"
//            android:orientation="vertical"
//            android:layout_marginRight="15dp">
//
//            <View
//                android:id="@+id/colorSquare1"
//                android:layout_width="150dp"
//                android:layout_height="150dp"
//                android:background="@drawable/square1" />
//
//            <LinearLayout
//                android:layout_width="match_parent"
//                android:layout_height="wrap_content"
//                android:orientation="horizontal"
//                android:layout_marginTop="@dimen/default_margin"
//                android:layout_gravity="center"
//                android:padding="5dp">
//
//                <EditText
//                    android:id="@+id/colorSquare1Code"
//                    android:layout_width="0dp"
//                    android:layout_height="wrap_content"
//                    android:layout_weight="1"
//                    android:background="@null"
//                    android:textAlignment="center"
//                    android:maxLines="1"
//                    android:selectAllOnFocus="true"
//                    android:textIsSelectable="true"
//                    android:hint="カラーコード" />
//
//                <!-- ボタンを配置 -->
//                <ImageView
//                    android:id="@+id/color1SaveBtn"
//                    android:layout_width="wrap_content"
//                    android:layout_height="match_parent"
//                    android:src="@drawable/ic_save_btn"
//                    android:layout_gravity="center"
//                    android:text="ボタン" />
//            </LinearLayout>
//
//
//
//        </LinearLayout>
//
//        <LinearLayout
//            android:id="@+id/colorSquare2Block"
//            android:layout_width="wrap_content"
//            android:layout_height="wrap_content"
//            android:orientation="vertical">
//
//            <View
//                android:id="@+id/colorSquare2"
//                android:layout_width="150dp"
//                android:layout_height="150dp"
//                android:background="@drawable/square1" />
//
//            <LinearLayout
//                android:layout_width="match_parent"
//                android:layout_height="wrap_content"
//                android:orientation="horizontal"
//                android:layout_marginTop="@dimen/default_margin"
//                android:layout_gravity="center"
//                android:padding="5dp">
//
//                <EditText
//                    android:id="@+id/colorSquare2Code"
//                    android:layout_width="0dp"
//                    android:layout_height="wrap_content"
//                    android:layout_weight="1"
//                    android:background="@null"
//                    android:textAlignment="center"
//                    android:maxLines="1"
//                    android:selectAllOnFocus="true"
//                    android:textIsSelectable="true"
//                    android:hint="カラーコード" />
//
//                <!-- ボタンを配置 -->
//                <ImageView
//                    android:id="@+id/color2SaveBtn"
//                    android:layout_width="wrap_content"
//                    android:layout_height="match_parent"
//                    android:src="@drawable/ic_save_btn"
//                    android:layout_gravity="center"
//                    android:text="ボタン" />
//            </LinearLayout>
//
//        </LinearLayout>
//
//    </LinearLayout>
//
//    <SeekBar
//        android:id="@+id/seekBarRed"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:layout_marginTop="@dimen/default_margin"
//        android:max="255"
//        android:progress="255"
//        android:thumbTint="@color/red" />
//
//    <SeekBar
//        android:id="@+id/seekBarBlue"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:max="255"
//        android:thumbTint="@color/blue"
//        android:progress="255"
//        android:layout_marginTop="@dimen/default_margin" />
//
//    <SeekBar
//        android:id="@+id/seekBarGreen"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:max="255"
//        android:thumbTint="@color/green"
//        android:progress="255"
//        android:layout_marginTop="@dimen/default_margin" />
//
//
//
//</LinearLayout>
//package com.example.findcolorcode
//
//import android.graphics.Color
//import android.graphics.drawable.ColorDrawable
//import android.os.Build
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.EditText
//import android.widget.LinearLayout
//import android.widget.SeekBar
//import android.widget.TextView
//import android.widget.Toast
//import androidx.annotation.RequiresApi
//import androidx.compose.runtime.savedinstancestate.savedInstanceState
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import com.example.findcolorcode.databinding.FragmentColorChoiceBinding
//
//class ColorChoiceFragment : Fragment(){
////ColorChoiceScreenを実装する
//    private lateinit var seekBarRed: SeekBar
//    private lateinit var seekBarBlue: SeekBar
//    private lateinit var seekBarGreen: SeekBar
//    private lateinit var selectedColorSquare: View
//    private lateinit var selectedColorCode: TextView
//
//    private var _binding: FragmentColorChoiceBinding? = null
//    private val binding get() = _binding!!
//    private var redPoint = 255
//    private var bluePoint = 255
//    private var greenPoint = 255
//    private val TAG = "ColorChoiceFragment"
//
//    private var selectedSquare = 1
//    private var lastSelectedColorCode: TextView? = null
//
//    // 再起的ループを防ぐためのフラグ
//    private var isUpdating = false
//
//    companion object {
//        fun newInstance() = ColorChoiceFragment()
//    }
//
//
//    //ColorviewModelをインスタンス化
//    private val viewModel: ColorViewModel by viewModels()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentColorChoiceBinding.inflate(inflater, container, false)
//
//        viewModel.colorSquare1.observe(viewLifecycleOwner) { color ->
//            binding.colorSquare1.setBackgroundColor(Color.parseColor(color))
//        }
//        viewModel.colorSquare2.observe(viewLifecycleOwner) { color ->
//            binding.colorSquare2.setBackgroundColor(Color.parseColor(color))
//        }
//
//        return binding.root
//    }
//
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        Log.d(TAG,"onViewCreated")
//        super.onViewCreated(view, savedInstanceState)
//
//        seekBarRed = binding.seekBarRed
//        seekBarBlue = binding.seekBarBlue
//        seekBarGreen = binding.seekBarGreen
//
//        //色の初期状態をセット
//        setInitialColorSelection()
//
//        // SeekBarChangedListener
//        val colorSeekBarChangedListener = object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                when (seekBar?.id) {
//                    binding.seekBarRed.id -> redPoint = progress
//                    binding.seekBarBlue.id -> bluePoint = progress
//                    binding.seekBarGreen.id -> greenPoint = progress
//                }
//                updateColor()
//            }
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
//        }
//
//        // setEventListener
//        listOf(seekBarRed, seekBarBlue, seekBarGreen).forEach { seekBar ->
//            seekBar.setOnSeekBarChangeListener(colorSeekBarChangedListener)
//        }
//
//        binding.colorSquare1Block.setOnClickListener {
//            selectSquare(binding.colorSquare1, binding.colorSquare1Code)
//        }
//
//        binding.colorSquare2Block.setOnClickListener {
//            selectSquare(binding.colorSquare2, binding.colorSquare2Code)
//        }
//
//        val colorSaveBtns = listOf(binding.color1SaveBtn, binding.color2SaveBtn)
//
//        colorSaveBtns.forEach { button ->
//            button.setOnClickListener {
//                val dialog = ColorSaveDialog().apply {
//                    arguments = Bundle().apply {
//                        putString("colorCode", selectedColorCode.text.toString())
//                    }
//                }
//                dialog.show(childFragmentManager, "ColorSaveDialog")
//            }
//        }
//
//        // EditTextの設定
//        setEditTextSettings(binding.colorSquare1Code, binding.colorSquare1Block)
//        setEditTextSettings(binding.colorSquare2Code, binding.colorSquare2Block)
//    }
//
//
//    //画面の中断時にViewModelの値が初期化されるのでviewModel再現
//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        Log.d(TAG,"onViewRestored")
//        super.onViewStateRestored(savedInstanceState)
//        savedInstanceState?.let {
//            val restoredColor1 = it.getString("colorSquare1", "#FFFFFF")
//            val restoredColor2 = it.getString("colorSquare2", "#FFFFFF")
//            viewModel.colorSquare1.value = restoredColor1
//            viewModel.colorSquare2.value = restoredColor2
//        }
//    }
//
//    //画面の再会時にviewModelを保存する
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//            outState?.putString("colorSquare1", viewModel.colorSquare1.value)
//            outState?.putString("colorSquare2", viewModel.colorSquare2.value)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.d(TAG,referColorViewModel())
//        Log.d(TAG,"OnPause")
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Log.d(TAG, "Resume")
//       Log.d(TAG, "a:${referColorViewModel()}")
//    }
//
//    private fun setEditTextSettings(editText: EditText, parentView: LinearLayout) {
//
//        editText.setOnFocusChangeListener { _, hasFocus ->
//            if (hasFocus) {
//                parentView.performClick()
//                editText.selectAll()
//            }
//        }
//
//        // TextChangeListener
//        editText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//            override fun afterTextChanged(s: Editable?) {
//                if (isUpdating) return
//                isUpdating = true
//
//                Log.d("ColorChoiceFragment", "AfterTextChanged")
//                val colorCode = s.toString()
//                if (isValidColorCode(colorCode)) {
//                    Log.d("ColorChoiceFragment", "Color Code OK $$colorCode")
//                    val color = Color.parseColor(colorCode)
//                    selectedColorSquare.setBackgroundColor(color)
//                    selectedColorCode.text = String.format("#%06X", color)
//                    changeColorViewModel(selectedSquare,colorCode)
//                    changeColorPoint(seekBarRed, seekBarBlue, seekBarGreen)
//                } else {
//                    Log.d("ColorChoiceFragment", "Color Code Error")
//                    Toast.makeText(
//                        context,
//                        "色が見つかりません。色の名前, #RRGGBB, #AARRGGBB のみ有効です。",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//
//                isUpdating = false
//            }
//        })
//
//    }
//
//    private fun isValidColorCode(colorCode: String): Boolean {
//        //#RRGGBB
//        val rRGGBB = Regex("^#([0-9a-fA-F]{6})$", RegexOption.IGNORE_CASE)
//
//        //#AARRGGBB
//        val aARRGGBB = Regex("^#[0-9A-Fa-f]{8}$", RegexOption.IGNORE_CASE)
//
//        // colorName
//        val colorNames = setOf(
//            "red", "blue", "green", "black", "white", "gray", "cyan", "magenta",
//            "yellow", "lightgray", "darkgray", "grey", "lightgrey", "darkgrey",
//            "aqua", "fuchsia", "lime", "maroon", "navy", "olive", "purple", "silver", "teal"
//        )
//
//        return colorCode.let {
//            it.matches(rRGGBB) || it.matches(aARRGGBB) || it.lowercase() in colorNames
//        }
//
//    }
//
//    private fun setInitialColorSelection() {
//        //初期のSquareを指定　初期色はビューモデルで指定済み
//        selectedColorSquare = binding.colorSquare1
//        selectedColorCode = binding.colorSquare1Code
//
//        //初期色で塗りつぶす際に枠線ごと塗りつぶされないよう色と共に枠も指定しておく
//        selectedColorSquare.setBackgroundColor(Color.parseColor(referColorViewModel()))
//        selectedColorSquare.setBackgroundResource(R.drawable.square1)
//        selectedColorCode.text = viewModel.colorSquare1.value
//        selectedColorCode.setBackgroundResource(R.drawable.selected_square)
//    }
//
//    private fun updateColor() {
//        //色の選択をした時に呼び出される
//        val color = Color.rgb(redPoint, greenPoint, bluePoint)
//        val colorHex = String.format("#%06X", color)
//        selectedColorSquare.setBackgroundColor(color)
//
//        // 背景色を変更
//        changeColorViewModel(selectedSquare,colorHex)
//        selectedColorCode.text = referColorViewModel()
//        viewModel.colorSquare1.value?.let { Log.d(TAG, it) }
//    }
//
//    private fun updateColorFromCode(colorCode: String){
//        if (isValidColorCode(colorCode)){
//            //カラーコードを入力した時に呼び出される
//            val color = Color.parseColor(colorCode)
//
//            if(selectedSquare == 1){
//                viewModel.colorSquare1.value =colorCode
//            }else{
//                viewModel.colorSquare2.value = colorCode
//            }
//            selectedColorSquare.setBackgroundColor(color)
//            changeColorPoint(seekBarRed,seekBarBlue,seekBarGreen)
//        }
//    }
//
//    private fun selectSquare(square: View, code: TextView) {
//        lastSelectedColorCode = selectedColorCode
//        selectedColorSquare = square
//        selectedColorCode = code
//
//        if (square == binding.colorSquare1){
//            selectedSquare = 1
//        }else selectedSquare =2
//
//        //選択枠の設定
//        lastSelectedColorCode?.setBackgroundResource(android.R.color.transparent)
//        selectedColorCode.setBackgroundResource(R.drawable.selected_square)
//
//        //シークバーを動かす
//        changeColorPoint(seekBarRed, seekBarBlue, seekBarGreen)
//    }
//
//    private fun changeColorPoint(seekBarRed: SeekBar, seekBarBlue: SeekBar, seekBarGreen: SeekBar) {
//        val color = Color.parseColor(referColorViewModel())
//        Log.d("ColorChoiceFragment", color.toString())
//        redPoint = Color.red(color)
//        bluePoint = Color.blue(color)
//        greenPoint = Color.green(color)
//        seekBarRed.progress = redPoint
//        seekBarBlue.progress = bluePoint
//        seekBarGreen.progress = greenPoint
//    }
//
//    private fun referColorViewModel(): String {
//        if (selectedSquare == 1) {
//            return  viewModel.colorSquare1.value ?: "#FFFFFF"
//        } else {
//            return  viewModel.colorSquare2.value ?: "#FFFFFF"
//        }
//    }
//    fun changeColorViewModel(selectedSquare: Int,colorCode:String){
//        if(selectedSquare == 1){
//            viewModel.colorSquare1.value =colorCode
//        }else{
//            viewModel.colorSquare2.value = colorCode
//        }
//
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}