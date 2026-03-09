package com.example.loversfood.data.repository

import com.example.loversfood.data.api.MealApiService
import com.example.loversfood.data.api.toEntity
import com.example.loversfood.data.database.RecipeDao
import com.example.loversfood.data.database.toDomain
import com.example.loversfood.domain.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepository(
    private val apiService: MealApiService,
    private val recipeDao: RecipeDao
) {
    private val pageSize = 30

    suspend fun getCategories(): List<String> = withContext(Dispatchers.IO) {
        try {
            apiService.getCategories().categories.map { it.name }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun loadRecipesPage(
        page: Int,
        query: String = "",
        category: String? = null
    ): List<Recipe> = withContext(Dispatchers.IO) {
        val offset = page * pageSize
        
        // 1. Try to fetch from network if it's the first page or we are searching/filtering
        // to ensure we have fresh data.
        if (page == 0) {
            try {
                val response = when {
                    category != null -> apiService.filterByCategory(category)
                    query.isNotEmpty() -> apiService.searchRecipes(query)
                    else -> apiService.searchRecipes("") // Get some default recipes
                }
                
                response.meals?.let { dtos ->
                    recipeDao.insertRecipes(dtos.map { it.toEntity() })
                }
            } catch (e: Exception) {
                // Ignore network errors, fall back to DB
            }
        }

        // 2. Return from local DB with pagination
        val entities = when {
            category != null -> recipeDao.getRecipesByCategoryPaged(category, pageSize, offset)
            query.isNotEmpty() -> recipeDao.searchRecipesPaged(query, pageSize, offset)
            else -> recipeDao.getRecipesPaged(pageSize, offset)
        }
        
        entities.map { it.toDomain() }
    }

    suspend fun getRecipeById(id: String): Recipe? = withContext(Dispatchers.IO) {
        val cached = recipeDao.getRecipeById(id)
        if (cached != null && cached.instructions.isNotEmpty()) {
            return@withContext cached.toDomain()
        }

        try {
            val response = apiService.getRecipeDetails(id)
            response.meals?.firstOrNull()?.let { dto ->
                val entity = dto.toEntity()
                recipeDao.insertRecipes(listOf(entity))
                return@withContext entity.toDomain()
            }
        } catch (e: Exception) {
            // Error handling
        }
        cached?.toDomain()
    }
}
