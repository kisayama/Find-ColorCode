package com.example.findcolorcode.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.findcolorcode.model.FavoriteColorDataClass

//FavoriteColorDataClassをエンティティに指定
@Database(entities =[FavoriteColorDataClass::class], version =1)
abstract class ColorDatabase: RoomDatabase() {
    //ColorDaoを取得するための抽象メソッド
    abstract fun colorDao(): ColorDao

    companion object{
        @Volatile
        //INSTANCE変数が変更されたら他のスレッドにも反映が変更されるようにするアノテーション
        private var INSTANCE:ColorDatabase? = null

        //データベースのインスタンスを取得するメソッド
        fun getDatabase(context: Context): ColorDatabase {
            //複数のスレッドが同時にインスタンスを生成しないようsynchronizedを使用する
            return  INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ColorDatabase::class.java,
                    "color_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }


}