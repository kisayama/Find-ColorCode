package com.example.findcolorcode.api

import com.example.findcolorcode.model.ColorSchemeResponce
import retrofit2.http.GET
import retrofit2.http.Query

interface TheColorApiService {
    @GET("https://www.thecolorapi.com/scheme?")
    suspend fun getColorScheme(
        @Query("hex") colorCodeWithoutHash :String,// #を除いたcolorCode　例: 0047AB
        @Query("mode") mode :String = "analogic",//スキームを生成するためのモード デフォルトはanalogic　他にもいくつかある
        @Query("format") format :String = "json",//結果を受け取る形式を指定する。デフォルトはjson 他にhtml,svg
        @Query("count") count : Int = 5,//取得するスキームカラーの数　デフォルトは5
        ): ColorSchemeResponce
}