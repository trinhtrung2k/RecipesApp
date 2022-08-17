package com.trinhtrung.recipesapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.trinhtrung.recipesapp.pojo.Meal

@Dao
interface MealDao {
    /*  OnConflictStrategy.REPLACE dieu nay co nghia neu ban chen vao 1 meal
    co san trong csdl thi no se cap nhat thay vi chen , su dung cai nay vua Insert vua update*/
    //upsert co nghia la chuc nay nay k chen

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(meal:Meal)

    @Delete
    suspend fun delete(meal:Meal)

    @Insert
    suspend fun insertFavorite(meal: Meal)


    /*
    //return ve mot livedata
    // LiveData là một lớp lưu trữ dữ liệu có thể được quan sát
     trong một vòng đời nhất định. Luôn giữ / lưu trữ phiên bản mới nhất của dữ liệu.
      Thông báo cho những người quan sát tích cực của nó khi dữ liệu đã thay đổi.
       Vì chúng tôi đang nhận được tất cả nội dung của cơ sở dữ liệu, chúng tôi sẽ
     được thông báo bất cứ khi nào bất kỳ nội dung cơ sở dữ liệu nào thay đổi.
     */
    @Query("SELECT * FROM mealInformation")
    fun  getAllMeals():LiveData<List<Meal>>

}