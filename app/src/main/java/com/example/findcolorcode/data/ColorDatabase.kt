package com.example.findcolorcode.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.findcolorcode.FavoriteColorDataClass

@Database(entities =[FavoriteColorDataClass::class], version =1)
abstract class ColorDatabase: RoomDatabase() {
    abstract fun colorDao(): ColorDao

    companion object{
        @Volatile
        private var INSTANCE:ColorDatabase? = null

        fun getDatabase(context: Context): ColorDatabase {
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