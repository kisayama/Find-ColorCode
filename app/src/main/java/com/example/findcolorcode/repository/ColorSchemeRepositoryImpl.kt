package com.example.findcolorcode.repository

import com.example.findcolorcode.api.TheColorApiService
import com.example.findcolorcode.model.ColorSchemeResponse

//APIと通信を行うクラス
//プリマリコンストラクタで引数にtheColorApiServiceインスタンスを引き渡し依存性注入を行う
//Diの使用頻度が高くなればHilt,Daggerを検討する
class ColorSchemeRepositoryImpl(private val theColorApiService: TheColorApiService)
    :ColorSchemeRepository {//Repository(InterFace)の実装
    override suspend fun getColorScheme(
        colorCodeWithoutHash: String,
        mode: String,
        format: String,
        count: Int
    ): ColorSchemeResponse {//ColorSchemeRepositoryで使用している戻り値の型を使用する
        //APIサービスから結果を取得する
        return theColorApiService.getColorScheme(colorCodeWithoutHash,mode,format,count)
    }
}