package com.example.loversfood.data.api

import com.example.loversfood.data.database.RecipeEntity
import com.example.loversfood.domain.model.Recipe
import com.google.gson.annotations.SerializedName

data class MealResponse(
    @SerializedName("meals") val meals: List<MealDto>?
)

data class MealDto(
    @SerializedName("idMeal") val id: String,
    @SerializedName("strMeal") val name: String,
    @SerializedName("strCategory") val category: String?,
    @SerializedName("strArea") val area: String?,
    @SerializedName("strInstructions") val instructions: String?,
    @SerializedName("strMealThumb") val thumbnailUrl: String,
    @SerializedName("strYoutube") val youtubeUrl: String?,
    @SerializedName("strIngredient1") val ingredient1: String?,
    @SerializedName("strIngredient2") val ingredient2: String?,
    @SerializedName("strIngredient3") val ingredient3: String?,
    @SerializedName("strIngredient4") val ingredient4: String?,
    @SerializedName("strIngredient5") val ingredient5: String?,
    @SerializedName("strMeasure1") val measure1: String?,
    @SerializedName("strMeasure2") val measure2: String?,
    @SerializedName("strMeasure3") val measure3: String?,
    @SerializedName("strMeasure4") val measure4: String?,
    @SerializedName("strMeasure5") val measure5: String?
)

data class CategoryResponse(
    @SerializedName("categories") val categories: List<CategoryDto>
)

data class CategoryDto(
    @SerializedName("idCategory") val id: String,
    @SerializedName("strCategory") val name: String,
    @SerializedName("strCategoryThumb") val thumbnailUrl: String
)

fun MealDto.toEntity(): RecipeEntity {
    return RecipeEntity(
        id = id,
        name = name,
        category = category ?: "",
        area = area ?: "",
        instructions = instructions ?: "",
        thumbnailUrl = thumbnailUrl,
        youtubeUrl = youtubeUrl
    )
}

fun MealDto.toDomain(): Recipe {
    return Recipe(
        id = id,
        name = name,
        category = category ?: "",
        area = area ?: "",
        instructions = instructions ?: "",
        thumbnailUrl = thumbnailUrl,
        youtubeUrl = youtubeUrl,
        ingredients = listOfNotNull(ingredient1, ingredient2, ingredient3, ingredient4, ingredient5).filter { it.isNotBlank() },
        measures = listOfNotNull(measure1, measure2, measure3, measure4, measure5).filter { it.isNotBlank() }
    )
}
