package com.example.loversfood.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loversfood.data.api.MealApiService
import com.example.loversfood.data.database.RecipeDatabase
import com.example.loversfood.data.repository.RecipeRepository
import com.example.loversfood.ui.details.RecipeDetailsViewModel
import com.example.loversfood.ui.list.RecipeListViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(MealApiService::class.java)
    private val database = RecipeDatabase.getDatabase(context)
    private val repository = RecipeRepository(apiService, database.recipeDao())

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RecipeListViewModel::class.java) -> {
                RecipeListViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RecipeDetailsViewModel::class.java) -> {
                RecipeDetailsViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
