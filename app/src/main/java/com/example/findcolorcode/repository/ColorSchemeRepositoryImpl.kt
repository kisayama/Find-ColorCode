package com.example.findcolorcode.repository

import com.example.findcolorcode.api.TheColorApiService
import com.example.findcolorcode.model.ColorSchemeResponse
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

//APIと通信を行うクラス
//プリマリコンストラクタで引数にtheColorApiServiceインスタンスを引き渡し依存性注入を行う
class ColorSchemeRepositoryImpl :ColorSchemeRepository {//Repository(InterFace)の実装

    private val moshi : Moshi = Moshi.Builder().build()//Moshiのインスタンスを作成

    //TODO　今後DIを使用する
    //Retrofitのインスタンスを作成
    //addConvertFactoryでJSONをObjectに変換するように設定する(Moshiを使用)
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://www.thecolorapi.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))//Moshi
        .build()

    //TODO　今後DIを使用する
    private val apiService :TheColorApiService = retrofit.create(TheColorApiService::class.java)

    override suspend fun getColorScheme(
        colorCodeWithoutHash: String,
        mode: String,
        format: String,
        count: Int
    ): List<String> {//ColorSchemeRepositoryと同じ戻り値の型を使用する
        //APIサービスから結果を取得する
        val responce = apiService.getColorScheme(colorCodeWithoutHash,mode,format,count)
        return responce.colors.map { it.hex.value }
    }

}