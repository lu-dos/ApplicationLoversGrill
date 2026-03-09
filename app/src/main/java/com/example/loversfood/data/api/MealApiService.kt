package com.example.loversfood.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {
    @GET("search.php")
    suspend fun searchRecipes(@Query("s") query: String): Any // Placeholder response type

    @GET("lookup.php")
    suspend fun getRecipeDetails(@Query("i") id: String): Any // Placeholder response type
}
