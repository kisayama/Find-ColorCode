package com.example.findcolorcode.repository

import com.example.findcolorcode.model.FavoriteColorDataClass
import kotlinx.coroutines.flow.Flow


interface FavoriteColorRepository {
    suspend fun insertColor(color:FavoriteColorDataClass)
    fun getAllColors():Flow<List<FavoriteColorDataClass>>
    fun getColorById(colorId:String):Flow<FavoriteColorDataClass>
    suspend fun updateColor(color:FavoriteColorDataClass)
    suspend fun deleteColor(color:FavoriteColorDataClass)

}