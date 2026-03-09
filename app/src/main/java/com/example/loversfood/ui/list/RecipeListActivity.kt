package com.example.loversfood.ui.list

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.loversfood.domain.model.Recipe
import com.example.loversfood.ui.ViewModelFactory
import com.example.loversfood.ui.components.CategoryFilter
import com.example.loversfood.ui.components.SearchBar
import com.example.loversfood.ui.details.RecipeDetailsActivity
import com.example.loversfood.ui.theme.LoversFoodTheme

class RecipeListActivity : ComponentActivity() {

    private val viewModel: RecipeListViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoversFoodTheme {
                val recipes by viewModel.recipes.collectAsState()
                val categories by viewModel.categories.collectAsState()
                val selectedCategory by viewModel.selectedCategory.collectAsState()
                val searchQuery by viewModel.searchQuery.collectAsState()
                val isLoading by viewModel.isLoading.collectAsState()

                Scaffold(
                    topBar = {
                        Surface(shadowElevation = 4.dp) {
                            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                                SearchBar(
                                    query = searchQuery,
                                    onQueryChange = { viewModel.onSearchQueryChanged(it) }
                                )
                                CategoryFilter(
                                    categories = categories,
                                    selectedCategory = selectedCategory,
                                    onCategorySelected = { viewModel.onCategorySelected(it) }
                                )
                            }
                        }
                    }
                ) { padding ->
                    RecipeListContent(
                        modifier = Modifier.padding(padding),
                        recipes = recipes,
                        isLoading = isLoading,
                        onRecipeClick = { recipe ->
                            val intent = Intent(this, RecipeDetailsActivity::class.java).apply {
                                putExtra("RECIPE_ID", recipe.id)
                            }
                            startActivity(intent)
                        },
                        onLoadMore = { viewModel.loadNextPage() }
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeListContent(
    modifier: Modifier = Modifier,
    recipes: List<Recipe>,
    isLoading: Boolean,
    onRecipeClick: (Recipe) -> Unit,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != null && lastVisibleItem.index >= recipes.size - 5
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && !isLoading) {
            onLoadMore()
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(recipes, key = { _, recipe -> recipe.id }) { _, recipe ->
            RecipeItem(recipe = recipe, onClick = { onRecipeClick(recipe) })
        }

        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun RecipeItem(recipe: Recipe, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = recipe.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .padding(4.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = recipe.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
