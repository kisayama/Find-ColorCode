package com.example.findcolorcode.repository

import android.util.Log
import com.example.findcolorcode.api.TheColorApiService
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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
        return try {
            //APIサービスから結果を取得する
            val responce = apiService.getColorScheme(colorCodeWithoutHash, mode, format, count)
            responce.colors.map { it.hex.value }
        }catch (e:UnknownHostException){//インターネット接続エラーなし
            Log.e("RepositoryImpl${e.message}", e.toString())
             throw e
        } catch (e: SocketTimeoutException) {//タイムアウト
            Log.e("RepositoryImpl${e.message}", e.toString())
            throw e
        } catch (e: HttpException) {//サーバーエラー
            Log.e("RepositoryImpl${e.message}", e.toString())
            throw e
        } catch (e:Exception){//それ以外のエラー
            Log.e("RepositoryImpl${e.message}", e.toString())
            throw e
        }
    }
}