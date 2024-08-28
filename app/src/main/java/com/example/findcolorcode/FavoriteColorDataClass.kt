package com.example.findcolorcode

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime
import java.util.UUID

@JsonClass(generateAdapter = true)
data class FavoriteColorDataClass(
    val id:String = UUID.randomUUID().toString(),
    val colorCode:String,
    var colorName :String,
    var colorMemo: String,
    var editDate:String)
