package com.example.findcolorcode.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//Moshiで使用するデータクラスには＠JsonClassをつける
// (KSPでアダプタ(データ形式を変換する役割)を自動作成するためのアノテーション)
//データクラスの各プロパティ名とJSONのキー名は一致している必要がある
//JsonとDataクラスの変数の名前が異なる時は以下の記入方法
// @Json(name = "hidden_card") val hiddenCard: Card
@JsonClass (generateAdapter = true)
data class ColorSchemeResponse (
    @Json val mode:String,//GET時に指定した色のモード
    @Json val count:Int,//指定したカラーコードの数
    @Json val colors:List<ColorCode>//受け取った色リスト
)
@JsonClass (generateAdapter = true)
data class ColorCode(
    @Json val hex :HexValue//#付きHex、#なしHexの2種類を保持する
)

@JsonClass (generateAdapter = true)
data class HexValue(
    @Json val value :String,//#がついたHex
    @Json val clean: String//#がついていないHex
)