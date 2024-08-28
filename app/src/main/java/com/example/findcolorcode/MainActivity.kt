package com.example.findcolorcode

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.findcolorcode.databinding.ActivityMainBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MainActivity : AppCompatActivity(),
    ColorSaveDialog.ColorSaveListener,
    ColorEditDialog.ColorEditListener,
    FavoriteColorAdapter.baseOnColorClickListener{

    private lateinit var binding: ActivityMainBinding
    private lateinit var colorChoiceFragment: ColorChoiceFragment
    private lateinit var favoriteColorFragment: FavoriteColorFragment
    private lateinit var buttons: List<ImageView>

    companion object {
        lateinit var moshi: Moshi
            private set
    }

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
