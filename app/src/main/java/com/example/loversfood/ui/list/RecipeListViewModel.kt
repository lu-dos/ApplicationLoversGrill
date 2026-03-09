package com.example.loversfood.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loversfood.data.repository.RecipeRepository
import com.example.loversfood.domain.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeListViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var currentPage = 0
    private var isLastPage = false

    init {
        loadCategories()
        loadNextPage()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _categories.value = repository.getCategories()
        }
    }

    fun loadNextPage() {
        if (_isLoading.value || isLastPage) return

        viewModelScope.launch {
            _isLoading.value = true
            val newRecipes = repository.loadRecipesPage(
                page = currentPage,
                query = _searchQuery.value,
                category = _selectedCategory.value
            )
            
            if (newRecipes.isEmpty()) {
                isLastPage = true
            } else {
                _recipes.value += newRecipes
                currentPage++
            }
            _isLoading.value = false
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        resetAndLoad()
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = if (_selectedCategory.value == category) null else category
        resetAndLoad()
    }

    private fun resetAndLoad() {
        currentPage = 0
        isLastPage = false
        _recipes.value = emptyList()
        loadNextPage()
    }
}
