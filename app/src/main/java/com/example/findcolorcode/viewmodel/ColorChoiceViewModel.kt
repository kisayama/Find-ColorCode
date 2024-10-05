package com.example.findcolorcode.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ColorChoiceViewModel :ViewModel(){

    //選択squareのインデックス
    private val _selectedSquare = MutableLiveData(1)
    val selectedSquare: LiveData<Int>  = _selectedSquare

    //選択squareを変更する
    fun changeSelectedSquare(newNumber : Int){
        _selectedSquare.value =   newNumber
    }

    //Square１の状態
    private val _colorSquare1 = MutableLiveData("#FFFFFF")//デフォルトのカラーコード
    val colorSquare1 : LiveData<String> get() =_colorSquare1

    //Square２の状態
    private val _colorSquare2 = MutableLiveData("#FFFFFF")//デフォルトのカラーコード
    val colorSquare2 : LiveData<String> get() =_colorSquare2

    //====シークバーのRGB値を保存する変数square1,square2====
    //rememberはデバイスの回転などのアクティビティの破棄をされると状態が保存されないことに注意
    val red1 = MutableLiveData(255)//square1の各シークバーの値を保存する変数とその初期値
    val green1 = MutableLiveData(255)
    val blue1= MutableLiveData(255)

    val red2 = MutableLiveData(255)//square2の各シークバーの値を保存する変数とその初期値
    val green2 = MutableLiveData(255)
    val blue2 = MutableLiveData(255)

    /*可読性を高めるためにDataClassにまとめてもいいかも。
     データクラス内のデータに一つでも変更があるとデータクラス内を全て再描写しないといけないから注意（RGBならいいかも）
     //square1の各シークバーの値とその初期値
    val square1ColorDataValues = MutableLiveData(ColorRGBValues(255,255,255))

    //square2の各シークバーの値とその初期値
    val square2ColorDataValues = MutableLiveData(ColorRGBValues(255,255,255))

    data class ColorRGBValues(
    val red:Int,
    val green :Int,
    val blue :Int
)
    */



    //カラーを更新する関数
    fun updateColorSquare1(color: String){
        _colorSquare1.value = color
    }

    fun updateColorSquare2(color:String){
        _colorSquare2.value = color
    }


}

//import android.widget.TextView
//import android.widget.Toast
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