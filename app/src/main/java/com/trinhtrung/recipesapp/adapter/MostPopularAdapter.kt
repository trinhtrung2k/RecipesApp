package com.trinhtrung.recipesapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trinhtrung.recipesapp.databinding.PopularItemsBinding
import com.trinhtrung.recipesapp.pojo.MealsByCategory

class MostPopularAdapter():RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>() {

    lateinit var onItemClick:((MealsByCategory) -> Unit)
    private var mealsList = ArrayList<MealsByCategory>()
     var onLongItemCkick:((MealsByCategory) -> Unit)? =   null

    fun setMeals(mealsByCategoryList:ArrayList<MealsByCategory>){
        this.mealsList = mealsByCategoryList
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
       return PopularMealViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgPopularMealItem)

        holder.itemView.setOnClickListener{
            onItemClick.invoke(mealsList[position])
        }

        holder.itemView.setOnLongClickListener{
            onLongItemCkick?.invoke(mealsList[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    class PopularMealViewHolder( val binding:PopularItemsBinding):RecyclerView.ViewHolder(binding.root)

}