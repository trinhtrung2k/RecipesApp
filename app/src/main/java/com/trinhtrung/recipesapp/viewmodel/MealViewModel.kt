package com.trinhtrung.recipesapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.trinhtrung.recipesapp.db.MealDatabase
import com.trinhtrung.recipesapp.pojo.Meal
import com.trinhtrung.recipesapp.pojo.MealList
import com.trinhtrung.recipesapp.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(val mealDatabase: MealDatabase) :ViewModel() {

  private var mealDetailsLiveData = MutableLiveData<Meal>()

    fun getMealDetail(id:String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
             if (response.body() != null){
                 mealDetailsLiveData.value = response.body()!!.meals[0]

             }else{
                 return

             }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
               Log.d("MealActivity", t.message.toString())
            }

        })
    }

    fun observerMealDetailsLiveData():LiveData<Meal>{
        return mealDetailsLiveData
    }

    fun insertMeal(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }




}