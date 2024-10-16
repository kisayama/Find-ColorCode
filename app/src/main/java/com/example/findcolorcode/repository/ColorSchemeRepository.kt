package com.example.findcolorcode.repository

import com.example.findcolorcode.model.ColorSchemeResponse
import retrofit2.http.Query

//RepositoryはAPIデータ取得を目的とするクラスに実装され、
//クライアントからの指示でデータの取得を行う(窓口になる)
// 実際にデータの取得を行うのはAPIサービス。
// RepositoryはAPIサービスの動作をクライアントから隠す役割がある。

interface ColorSchemeRepository {
    suspend fun getColorScheme(
        colorCodeWithoutHash :String,
        mode :String,
        format :String,
        count : Int
    ):ColorSchemeResponse //戻り値のデータ型
}