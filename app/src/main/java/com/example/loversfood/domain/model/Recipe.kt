package com.example.loversfood.domain.model

data class Recipe(
    val id: String,
    val name: String,
    val category: String,
    val area: String,
    val instructions: String,
    val thumbnailUrl: String,
    val youtubeUrl: String? = null,
    val ingredients: List<String> = emptyList(),
    val measures: List<String> = emptyList()
)
