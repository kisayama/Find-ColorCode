package com.example.findcolorcode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ColorViewModel: ViewModel() {

    val colorSquare1 :MutableLiveData<String> by lazy {
        MutableLiveData("#FFFFFF")
    }
    val colorSquare2 :MutableLiveData<String> by lazy {
        MutableLiveData("#FFFFFF")
    }
}