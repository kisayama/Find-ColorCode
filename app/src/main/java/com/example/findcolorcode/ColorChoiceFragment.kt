package com.example.findcolorcode

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ActionMode
import android.view.ActionMode.Callback
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.findcolorcode.databinding.FragmentColorChoiceBinding
class ColorChoiceFragment : Fragment(){

    private lateinit var seekBarRed: SeekBar
    private lateinit var seekBarBlue: SeekBar
    private lateinit var seekBarGreen: SeekBar
    private lateinit var selectedColorSquare: View
    private lateinit var selectedColorCode: TextView

    private var _binding: FragmentColorChoiceBinding? = null
    private val binding get() = _binding!!
    private var lastSelectedColorCode: TextView? = null
    private var redPoint = 255
    private var bluePoint = 255
    private var greenPoint = 255
    private val TAG = "ColorChoiceFragment"
    private var lastColorCode ="#FFFFFFFF"

    // 再起的ループを防ぐためのフラグ
    private var isUpdating = false

    companion object {
        fun newInstance() = ColorChoiceFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentColorChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG,"onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        seekBarRed = binding.seekBarRed
        seekBarBlue = binding.seekBarBlue
        seekBarGreen = binding.seekBarGreen

        //色の初期状態をセット
        setInitialColorSelection()

        // SeekBarChangedListener
        val colorSeekBarChangedListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                when (seekBar?.id) {
                    binding.seekBarRed.id -> redPoint = progress
                    binding.seekBarBlue.id -> bluePoint = progress
                    binding.seekBarGreen.id -> greenPoint = progress
                }
                updateColor()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }

        // setEventListener
        listOf(seekBarRed, seekBarBlue, seekBarGreen).forEach { seekBar ->
            seekBar.setOnSeekBarChangeListener(colorSeekBarChangedListener)
        }

        binding.colorSquare1Block.setOnClickListener {
            selectSquare(binding.colorSquare1, binding.colorSquare1Code)
        }

        binding.colorSquare2Block.setOnClickListener {
            selectSquare(binding.colorSquare2, binding.colorSquare2Code)
        }

        val colorSaveBtns = listOf(binding.color1SaveBtn, binding.color2SaveBtn)

        colorSaveBtns.forEach { button ->
            button.setOnClickListener {
                val dialog = ColorSaveDialog().apply {
                    arguments = Bundle().apply {
                        putString("colorCode", selectedColorCode.text.toString())
                    }
                }
                dialog.show(childFragmentManager, "ColorSaveDialog")
            }
        }

        // EditTextの設定
        setEditTextSettings(binding.colorSquare1Code, binding.colorSquare1Block)
        setEditTextSettings(binding.colorSquare2Code, binding.colorSquare2Block)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("lastColorCode",selectedColorCode.text.toString())
        Log.d(TAG,"onSaveInstance:$selectedColorCode")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.d(TAG,"onViewRestored")
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            lastColorCode = it.getString("lastColorCode","#FFFFFFF")
            selectedColorCode.text = lastColorCode
            updateColorFromCode(lastColorCode)
        }?:run {
            if(arguments?.containsKey("colorCode")==true) {
                val colorCode = arguments?.getString("colorCode")
                if(colorCode != null){
                    lastColorCode = colorCode
                    selectedColorCode.text = lastColorCode
                    updateColorFromCode(lastColorCode)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        isUpdating =true
       lastColorCode = selectedColorCode.text.toString()
        Log.d(TAG,"3:$lastColorCode")
        Log.d(TAG,"OnPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "Resume")
    }

    private fun setEditTextSettings(editText: EditText, parentView: LinearLayout) {

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                parentView.performClick()
                editText.selectAll()
            }
        }

        // TextChangeListener
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                isUpdating = true

                Log.d("ColorChoiceFragment", "AfterTextChanged")
                val colorCode = s.toString()
                if (isValidColorCode(colorCode)) {
                    Log.d("ColorChoiceFragment", "Color Code OK $$colorCode")
                    val color = Color.parseColor(colorCode)
                    selectedColorSquare.setBackgroundColor(color)
                    selectedColorCode.text = String.format("#%06X", color)
                    changeColorPoint(seekBarRed, seekBarBlue, seekBarGreen)
                } else {
                    Log.d("ColorChoiceFragment", "Color Code Error")
                    Toast.makeText(
                        context,
                        "色が見つかりません。色の名前, #RRGGBB, #AARRGGBB のみ有効です。",
                        Toast.LENGTH_LONG
                    ).show()
                }

                isUpdating = false
            }


        })

    }

    private fun isValidColorCode(colorCode: String): Boolean {
        //#RRGGBB
        val rRGGBB = Regex("^#([0-9a-fA-F]{6})$", RegexOption.IGNORE_CASE)

        //#AARRGGBB
        val aARRGGBB = Regex("^#[0-9A-Fa-f]{8}$", RegexOption.IGNORE_CASE)

        // colorName
        val colorNames = setOf(
            "red", "blue", "green", "black", "white", "gray", "cyan", "magenta",
            "yellow", "lightgray", "darkgray", "grey", "lightgrey", "darkgrey",
            "aqua", "fuchsia", "lime", "maroon", "navy", "olive", "purple", "silver", "teal"
        )

        return colorCode.let {
            it.matches(rRGGBB) || it.matches(aARRGGBB) || it.lowercase() in colorNames
        }

    }

    private fun setInitialColorSelection() {
        // 初期状態
        //初期のSquareを指定
        selectedColorSquare = binding.colorSquare1
        selectedColorCode = binding.colorSquare1Code

        //初期背景色の指定
        val initialColor = Color.parseColor("white")
        //初期色で塗りつぶす際に枠線ごと塗りつぶされないよう色と共に枠も指定しておく
        selectedColorSquare.setBackgroundColor(Color.parseColor(lastColorCode))
        selectedColorSquare.setBackgroundResource(R.drawable.square1)
        selectedColorCode.text = String.format("#%06X",initialColor)
        selectedColorCode.setBackgroundResource(R.drawable.selected_square)
    }

    private fun updateColor() {
        val color = Color.rgb(redPoint, greenPoint, bluePoint)
        // 背景色を変更
        selectedColorSquare.setBackgroundColor(color)
        selectedColorCode.text = String.format("#%06X", color)
    }

    private fun updateColorFromCode(colorCode: String){
        if (isValidColorCode(colorCode)){
            val color = Color.parseColor(colorCode)
            selectedColorSquare.setBackgroundColor(color)
            selectedColorCode.text = String.format("#%06X", color)
            changeColorPoint(seekBarRed,seekBarBlue,seekBarGreen)
        }
    }

    private fun selectSquare(square: View, code: TextView) {
        lastSelectedColorCode = selectedColorCode
        selectedColorSquare = square
        selectedColorCode = code

        lastSelectedColorCode?.setBackgroundResource(android.R.color.transparent)
        selectedColorCode.setBackgroundResource(R.drawable.selected_square)

        changeColorPoint(seekBarRed, seekBarBlue, seekBarGreen)
    }

    private fun changeColorPoint(seekBarRed: SeekBar, seekBarBlue: SeekBar, seekBarGreen: SeekBar) {
        val color = (selectedColorSquare.background as? ColorDrawable)?.color ?: Color.WHITE
        Log.d("ColorChoiceFragment", color.toString())
        redPoint = Color.red(color)
        bluePoint = Color.blue(color)
        greenPoint = Color.green(color)
        seekBarRed.progress = redPoint
        seekBarBlue.progress = bluePoint
        seekBarGreen.progress = greenPoint
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}