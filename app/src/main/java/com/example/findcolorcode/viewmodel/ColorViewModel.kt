package com.example.findcolorcode.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ColorViewModel :ViewModel(){


    //スクエア1の状態
    private val _colorSquare1 = MutableLiveData("#FFFFFF")//デフォルトのカラーコード
    val colorSquare1 : LiveData<String> get() =_colorSquare1

    //スクエア2の状態
    private val _colorSquare2 = MutableLiveData("#FFFFFF")//デフォルトのカラーコード
    val colorSquare2 : LiveData<String> get() =_colorSquare2

    //カラーを更新する関数
    fun updateColorSquare1(color: String){
        _colorSquare1.value = color
    }

    fun updateColorSquare2(color:String){
        _colorSquare2.value = color
    }


}