package com.example.loversfood.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.loversfood.domain.model.Recipe

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val area: String,
    val instructions: String,
    val thumbnailUrl: String,
    val youtubeUrl: String?,
    val ingredients: String = "", // Stored as comma separated string or JSON
    val measures: String = ""
)

fun RecipeEntity.toDomain(): Recipe {
    return Recipe(
        id = id,
        name = name,
        category = category,
        area = area,
        instructions = instructions,
        thumbnailUrl = thumbnailUrl,
        youtubeUrl = youtubeUrl,
        ingredients = if (ingredients.isBlank()) emptyList() else ingredients.split("||"),
        measures = if (measures.isBlank()) emptyList() else measures.split("||")
    )
}

fun Recipe.toEntity(): RecipeEntity {
    return RecipeEntity(
        id = id,
        name = name,
        category = category,
        area = area,
        instructions = instructions,
        thumbnailUrl = thumbnailUrl,
        youtubeUrl = youtubeUrl,
        ingredients = ingredients.joinToString("||"),
        measures = measures.joinToString("||")
    )
}
