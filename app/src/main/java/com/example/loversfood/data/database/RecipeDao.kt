package com.example.loversfood.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes LIMIT :limit OFFSET :offset")
    suspend fun getRecipesPaged(limit: Int, offset: Int): List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE category = :category LIMIT :limit OFFSET :offset")
    suspend fun getRecipesByCategoryPaged(category: String, limit: Int, offset: Int): List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE name LIKE '%' || :query || '%' LIMIT :limit OFFSET :offset")
    suspend fun searchRecipesPaged(query: String, limit: Int, offset: Int): List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE category = :category AND name LIKE '%' || :query || '%' LIMIT :limit OFFSET :offset")
    suspend fun searchRecipesByCategoryPaged(category: String, query: String, limit: Int, offset: Int): List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeById(id: String): RecipeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Query("DELETE FROM recipes")
    suspend fun deleteAllRecipes()
    
    @Query("SELECT COUNT(*) FROM recipes")
    suspend fun getCount(): Int
}
