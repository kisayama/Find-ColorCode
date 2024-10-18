package com.example.findcolorcode.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//変数名とJSONのキー名は一致させている

//Moshiで使用するデータクラスには＠JsonClassをつける
// (KSPでアダプタ(データ形式を変換する役割)を自動作成するためのアノテーション)
//JsonとDataクラスの変数の名前が異なる時は以下の記入方法
// @Json(name = "hidden_card") val hiddenCard: Card
@JsonClass (generateAdapter = true)
data class ColorSchemeResponse (
    @Json val mode:String,
    @Json val count:Int,
    @Json val colors:List<ColorCodesList>
)

@JsonClass (generateAdapter = true)
data class ColorCodesList(
    @Json val value :String,
    @Json val clean: String
)