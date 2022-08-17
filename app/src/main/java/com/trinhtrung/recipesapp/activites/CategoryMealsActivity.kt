package com.trinhtrung.recipesapp.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.trinhtrung.recipesapp.R
import com.trinhtrung.recipesapp.adapter.CategoryMealsAdapter
import com.trinhtrung.recipesapp.databinding.ActivityCategoryMealsBinding
import com.trinhtrung.recipesapp.fragments.HomeFragment
import com.trinhtrung.recipesapp.viewmodel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {

    lateinit var  binding : ActivityCategoryMealsBinding
    lateinit var categoryMealsViewModel: CategoryMealsViewModel
    lateinit var categoryMealsAdapter: CategoryMealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        categoryMealsViewModel = ViewModelProviders.of(this)[CategoryMealsViewModel::class.java]


        var categoryName = intent.getStringExtra(HomeFragment.CATEGORY_NAME)
        categoryMealsViewModel.getMealsCategory(categoryName!!)
        categoryMealsViewModel.observerMealsLiveData().observe(this, Observer { mealsList->
            binding.tvCategoryCount.text = categoryName + ": " + mealsList.size.toString()
            categoryMealsAdapter.setMealsList(mealsList)
        })
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rcvMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL,false)
            adapter = categoryMealsAdapter
        }
    }
}