package com.trinhtrung.recipesapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trinhtrung.recipesapp.db.MealDatabase
import com.trinhtrung.recipesapp.pojo.*
import com.trinhtrung.recipesapp.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val mealDatabase: MealDatabase) : ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favoritesMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private var bottomSheetLiveData = MutableLiveData<Meal>()

   /*

   ======== Cach 1 =====
   //xoay man hinh k lam thay doi gia tri, nen can ta khoi tao khoi init de goi ham geRandomMeal
    //sau do t se loai bo view.getRandomMeal trong HomeFragment
    init {
        getRandomMeal()
    }*/

    //========= Cach 2 ======
    private var saveSateRandomMeal : Meal? = null
    fun getRandomMeal() {
        //kiem tra neu ham da dc goi r thi k goi lai, tranh tinh trang change data khi xoay man hinh
        saveSateRandomMeal?.let {randomMeal->
            randomMealLiveData.postValue(randomMeal)
            return
        }


        RetrofitInstance.api.getRandimMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                    saveSateRandomMeal = randomMeal
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }

        })
    }

    fun getPopularItems() {
        RetrofitInstance.api.getPopularItems("Seafood")
            .enqueue(object : Callback<MealsByCategoryList> {
                override fun onResponse(
                    call: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                    if (response.body() != null) {
                        popularItemsLiveData.value = response.body()!!.meals
                    }
                }

                override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                    Log.d("HomeFragment", t.message.toString())
                }

            })
    }

    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
             //cach 2
                response.body()?.let { categoryList ->
                    categoriesLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }

        })

    }

    fun getMealById(id:String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object:Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
               val meal = response.body()?.meals?.first()
                meal?.let {meal->
                    bottomSheetLiveData.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel",t.message.toString())
            }

        })
    }
    fun deleteMeal(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }
    fun insertMeal(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }


    fun observeRandomMealLiveData(): LiveData<Meal> {
        return randomMealLiveData
    }

    fun observePopularItemsLiveData(): LiveData<List<MealsByCategory>> {
        return popularItemsLiveData
    }
    fun observeCategoriesLiveData():LiveData<List<Category>>{
        return categoriesLiveData
    }

    fun observeFavoritesMealsLiveData():LiveData<List<Meal>>{
        return favoritesMealsLiveData
    }

    fun observeBottomSheetMeal():LiveData<Meal>{
        return bottomSheetLiveData
    }


}