package com.example.findcolorcode.api

import com.example.findcolorcode.model.ColorSchemeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TheColorApiService {
    //エンドポイントを指定
    @GET("scheme")
    suspend fun getColorScheme(
        // #を除いたcolorCode　例: 0047AB
        @Query("hex") colorCodeWithoutHash: String,
        //スキームを生成するためのモード デフォルトはanalogic　他にもいくつかある
        @Query("mode") mode: String = "analogic",
        //結果を受け取る形式を指定する。デフォルトはjson 他にhtml,svg
        @Query("format") format: String = "json",
        //取得するスキームカラーの数　デフォルトは5
        @Query("count") count: Int = 5,
    ): ColorSchemeResponse
}