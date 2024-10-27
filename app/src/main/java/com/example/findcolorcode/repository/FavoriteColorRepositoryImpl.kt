package com.example.findcolorcode.repository

import com.example.findcolorcode.data.ColorDao
import com.example.findcolorcode.model.FavoriteColorDataClass

class ColorRepositoryImpl(private val colorDao: ColorDao) {
    override suspend fun insertColor(color: FavoriteColorDataClass){
        ColorDao.
    }
}