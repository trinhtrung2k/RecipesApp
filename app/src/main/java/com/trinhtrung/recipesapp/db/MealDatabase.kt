package com.trinhtrung.recipesapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.trinhtrung.recipesapp.pojo.Meal

//exportSchema dieu nay co nghia la neu ban muon export CSDL or schema duoi dang JSON
//@Database(entities = [Meal::class], version = 1, exportSchema = false)
@Database(entities = [Meal::class], version = 1)
@TypeConverters(MealTypeConvertor::class)
abstract class MealDatabase:RoomDatabase() {

    abstract fun  mealDao():MealDao

    companion object{
        /* Volatile  thay đổi trên phiên bản này từ bất kỳ thread nào
        sẽ hiển thị với bất kỳ thread nào khác */
        @Volatile
        var INSTANCE:MealDatabase? = null


      //  only one thread có thể có 1 INSTANCE từ roomdatabase
        @Synchronized
        fun getInstance(context:Context):MealDatabase{
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, MealDatabase::class.java
                ,"meal.db")
                        /* fallbackToDestructiveMigration()
                        có nghĩa tôi muốn rebuild lại database but i want to keep data
                        inside database va sau đó chỉ cần gọi build
                         */
                    .fallbackToDestructiveMigration()
                    .build()
            }
        //    return INSTANCE as MealDatabase
            return INSTANCE!!
        }

    }
}