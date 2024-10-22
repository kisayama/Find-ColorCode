package com.example.findcolorcode.repository

import com.example.findcolorcode.model.ColorSchemeResponse

//RepositoryはAPIデータ取得を目的とするクラスに実装され、
//クライアントからの指示でデータの取得を行う(窓口になる)
// 実際にデータの取得を行うのはAPIサービス。
// RepositoryはAPIサービスの動作をクライアントから隠す役割がある。

interface ColorSchemeRepository {
    suspend fun getColorScheme(
        colorCodeWithoutHash :String,
        mode :String = "analogic",
        format :String = "json",
        count : Int = 5
    ):List<String> //戻り値のデータ型
}