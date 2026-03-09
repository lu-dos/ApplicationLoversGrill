package com.example.loversfood.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {
    @GET("search.php")
    suspend fun searchRecipes(@Query("s") query: String): MealResponse

    @GET("lookup.php")
    suspend fun getRecipeDetails(@Query("i") id: String): MealResponse

    @GET("categories.php")
    suspend fun getCategories(): CategoryResponse

    @GET("filter.php")
    suspend fun filterByCategory(@Query("c") category: String): MealResponse
}
