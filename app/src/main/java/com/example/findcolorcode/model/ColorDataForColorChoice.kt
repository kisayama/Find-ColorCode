package com.example.findcolorcode.model

//ColorChoiceScreenで色の作成中に使用する一時的なデータクラス
    data class ColorDataForColorChoice(
        //ユーザーが編集しTextFieldに表示するカラーコード
        val colorCode: String,
        //プレビュー用Boxの背景色に指定するカラーコード
        val backgroundColorCode:String,
        //RGBスライダーの値
        val red: Int,
        val green: Int,
        val blue: Int
    )