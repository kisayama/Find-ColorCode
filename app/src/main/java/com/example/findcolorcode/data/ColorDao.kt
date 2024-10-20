package com.example.findcolorcode.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.findcolorcode.model.FavoriteColorDataClass
import kotlinx.coroutines.flow.Flow

@Dao
interface ColorDao {
    //色をデータベースに挿入
    //onConflictで同じ主キーが存在している場合の処理を決める（この場合は置き換える）
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertColor(color: FavoriteColorDataClass)

    //全ての色を取得するメソッド
    @Query("SELECT * from FavoriteColorDataClass")
    suspend fun getAllColors(): List<FavoriteColorDataClass>

    //指定したIDで色を取得するメソッド
    @Query("SELECT * from FavoriteColorDataClass WHERE id = :colorId")
    fun getColorById(colorId:String) : Flow<FavoriteColorDataClass>//Flowはデータの変更を監視する

    //色を更新するメソッド
    @Update
    suspend fun updateColor(color: FavoriteColorDataClass)

    //色を削除するメソッド
    @Delete
    suspend fun deleteColor(color: FavoriteColorDataClass)
}