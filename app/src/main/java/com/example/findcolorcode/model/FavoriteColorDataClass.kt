package com.example.findcolorcode.model

import androidx.room.PrimaryKey
import androidx.room.Entity
import com.squareup.moshi.JsonClass
import java.util.UUID

//データベースに保存する形式のデータクラス
@JsonClass(generateAdapter = true)
@Entity(tableName = "FavoriteColorDataClass")//エンティティとして指定
data class FavoriteColorDataClass(
    @PrimaryKey val id:String = UUID.randomUUID().toString(),
    //backgroundColorCode
    val colorCode:String,
    //ユーザーがダイアログ上に入力した名前
    var colorName:String,
    //ユーザーがダイアログ上に入力したメモ
    var colorMemo: String,
    //作成・変更した日時どちらか新しい方
    var editDateTime:Long
)
