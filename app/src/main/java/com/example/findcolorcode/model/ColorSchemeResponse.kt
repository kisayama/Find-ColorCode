package com.example.findcolorcode.model

//変数名とJSONのキー名は一致させている
data class ColorSchemeResponse (

    val mode:String,
    val count:Int,
    val colors:List<ColorCodesList>
)

data class ColorCodesList(
    val value :String,
    val clean: String
)