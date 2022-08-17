package com.trinhtrung.recipesapp.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.trinhtrung.recipesapp.R
import com.trinhtrung.recipesapp.db.MealDatabase
import com.trinhtrung.recipesapp.viewmodel.HomeViewModel
import com.trinhtrung.recipesapp.viewmodel.HomeViewModelFactory

class MainActivity : AppCompatActivity() {
     val viewModel:HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this)
        val homeViewModelProviderFactory = HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this,homeViewModelProviderFactory)[HomeViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //navigation
        //inflate the bottom
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.btm_nav)
        //navigation controller
        val navController = Navigation.findNavController(this, R.id.host_fragment)

        NavigationUI.setupWithNavController(bottomNavigation,navController)
    }
}