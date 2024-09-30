package com.example.findcolorcode.model

import androidx.room.PrimaryKey
import androidx.room.Entity
import com.squareup.moshi.JsonClass
import java.util.UUID

@JsonClass(generateAdapter = true)
@Entity(tableName = "FavoriteColorDataClass")//エンティティとして指定
data class FavoriteColorDataClass(
    @PrimaryKey val id:String = UUID.randomUUID().toString(),
    val colorCode:String,
    var colorName :String,
    var colorMemo: String,
    var editDate:String)
