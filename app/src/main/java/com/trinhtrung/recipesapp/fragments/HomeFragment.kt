package com.trinhtrung.recipesapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.trinhtrung.recipesapp.activites.CategoryMealsActivity
import com.trinhtrung.recipesapp.activites.MainActivity
import com.trinhtrung.recipesapp.activites.MealActivity
import com.trinhtrung.recipesapp.adapter.CategoriesAdapter
import com.trinhtrung.recipesapp.adapter.MostPopularAdapter
import com.trinhtrung.recipesapp.databinding.FragmentHomeBinding
import com.trinhtrung.recipesapp.fragments.bottomsheet.MealBottomSheetFragment
import com.trinhtrung.recipesapp.pojo.MealsByCategory
import com.trinhtrung.recipesapp.pojo.Meal
import com.trinhtrung.recipesapp.viewmodel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.trinhtrung.recipesapp.fragments.idMeal"
        const val MEAL_NAME = "com.trinhtrung.recipesapp.fragments.nameMeal"
        const val MEAL_THUMB = "com.trinhtrung.recipesapp.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.trinhtrung.recipesapp.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  homeMvvm = ViewModelProviders.of(this)[HomeViewModel::class.java]

        viewModel = (activity as MainActivity).viewModel
        popularItemsAdapter = MostPopularAdapter()
        categoriesAdapter = CategoriesAdapter()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preparePopularItemsRecyclerView()

        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observerPopularItemsLiveData()

        onPopularItemClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observerCategoriesLiveData()
        onCategoryClick()

        onPopularItemLongClick()



    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemCkick={meal->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Meal Info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = {category->
            val intent = Intent(activity,CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)

        }

    }

    private fun prepareCategoriesRecyclerView() {
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }

    }

    private fun observerCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories ->
                    categoriesAdapter.setCategoryList(categories)

        })
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter

        }
    }

    private fun observerPopularItemsLiveData() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner,
            { mealList ->
                popularItemsAdapter.setMeals(mealsByCategoryList = mealList as ArrayList<MealsByCategory>)
            })
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner, { meal ->

            Glide.with(this@HomeFragment)
                .load(meal!!.strMealThumb)
                .into(binding.imgRandomMeal)

            this.randomMeal = meal


        })
    }

}