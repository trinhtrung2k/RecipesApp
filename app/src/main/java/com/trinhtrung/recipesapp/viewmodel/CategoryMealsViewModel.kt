package com.trinhtrung.recipesapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trinhtrung.recipesapp.pojo.MealsByCategory
import com.trinhtrung.recipesapp.pojo.MealsByCategoryList
import com.trinhtrung.recipesapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel : ViewModel() {
    val mealsLiveData = MutableLiveData<List<MealsByCategory>>()

    fun getMealsCategory(categoryName: String) {
        RetrofitInstance.api.getMealsByCategory(categoryName)
            .enqueue(object : Callback<MealsByCategoryList> {
                override fun onResponse(
                    call: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                   response.body()?.let { mealsList ->
                       mealsLiveData.postValue(mealsList.meals)
                   }
                }

                override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                   Log.e("CategoryMealsViewModel", t.message.toString())
                }

            })
    }

    fun observerMealsLiveData():LiveData<List<MealsByCategory>>{
        return mealsLiveData
    }
}