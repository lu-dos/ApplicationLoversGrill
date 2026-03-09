package com.example.loversfood.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loversfood.data.repository.RecipeRepository
import com.example.loversfood.domain.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeDetailsViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadRecipe(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.getRecipeById(id)
            if (result != null) {
                _recipe.value = result
            } else {
                _error.value = "Impossible de charger les détails de la recette."
            }
            _isLoading.value = false
        }
    }
}
