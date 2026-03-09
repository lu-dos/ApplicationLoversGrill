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
        
        // Always try to fetch fresh data for the first page
        if (page == 0) {
            try {
                // If we have a category, we fetch that category's meals
                if (category != null) {
                    val response = apiService.filterByCategory(category)
                    response.meals?.let { dtos ->
                        val entities = dtos.map { dto ->
                            dto.toEntity().copy(category = category)
                        }
                        recipeDao.insertRecipes(entities)
                    }
                } 
                
                // If we have a search query, we also fetch search results
                if (query.isNotEmpty()) {
                    val response = apiService.searchRecipes(query)
                    response.meals?.let { dtos ->
                        recipeDao.insertRecipes(dtos.map { it.toEntity() })
                    }
                }

                // If both are empty, just get some default ones
                if (category == null && query.isEmpty()) {
                    val response = apiService.searchRecipes("")
                    response.meals?.let { dtos ->
                        recipeDao.insertRecipes(dtos.map { it.toEntity() })
                    }
                }
            } catch (e: Exception) {
                // Network error, will use what's in the DB
            }
        }

        // Return from local DB with combined filtering
        val entities = when {
            category != null && query.isNotEmpty() -> 
                recipeDao.searchRecipesByCategoryPaged(category, query, pageSize, offset)
            category != null -> 
                recipeDao.getRecipesByCategoryPaged(category, pageSize, offset)
            query.isNotEmpty() -> 
                recipeDao.searchRecipesPaged(query, pageSize, offset)
            else -> 
                recipeDao.getRecipesPaged(pageSize, offset)
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
