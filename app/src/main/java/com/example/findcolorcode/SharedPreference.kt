package com.example.findcolorcode

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

fun Context.getPreferences():SharedPreferences = this.getSharedPreferences("favorite_colors",Context.MODE_PRIVATE)

fun SharedPreferences.saveFavoriteColors(moshi: Moshi,colors: List<FavoriteColorDataClass>) {
    // List<FavoriteColorDataClass> の型を指定
    val type: Type = Types.newParameterizedType(List::class.java,FavoriteColorDataClass::class.java)
    val jsonAdapter = moshi.adapter<List<FavoriteColorDataClass>>(type)
    val json = jsonAdapter.toJson(colors)
    this.edit().putString("Colors",json).apply()
}

fun SharedPreferences.loadFavoriteColors(moshi: Moshi):List<FavoriteColorDataClass>{
    val json = this.getString("Colors","[]")
    val type:Type = Types.newParameterizedType(List::class.java,FavoriteColorDataClass::class.java)
    val jsonAdapter= moshi.adapter<List<FavoriteColorDataClass>>(type)
    //変換に失敗した時はemptyListを返す
   val data = jsonAdapter.fromJson(json) ?: emptyList()
    Log.d("loadFavoriteColors", "Loaded data: $data")
    return data
}