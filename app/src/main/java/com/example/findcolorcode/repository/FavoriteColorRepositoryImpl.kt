package com.example.findcolorcode.repository

import com.example.findcolorcode.data.ColorDao
import com.example.findcolorcode.model.FavoriteColorDataClass
import kotlinx.coroutines.flow.Flow

//repositoryの実装クラス
//ColorDaoの5つのメソッドをオーバーライドする
class FavoriteColorRepositoryImpl(private val colorDao: ColorDao):FavoriteColorRepository {
    override suspend fun insertColor(color: FavoriteColorDataClass){
        colorDao.insertColor(color)
    }

    override suspend fun getAllColors(): List<FavoriteColorDataClass> {
        return colorDao.getAllColors()
    }

    override fun getColorById(colorId: String): Flow<FavoriteColorDataClass> {
        return colorDao.getColorById(colorId)
    }


    override suspend fun updateColor(color: FavoriteColorDataClass) {
        colorDao.updateColor(color)
    }

    override suspend fun deleteColor(color: FavoriteColorDataClass) {
        colorDao.deleteColor(color)
    }
}