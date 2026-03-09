package com.example.loversfood.ui.details

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.loversfood.ui.ViewModelFactory

class RecipeDetailsActivity : ComponentActivity() {

    private val viewModel: RecipeDetailsViewModel by viewModels {
        ViewModelFactory(this)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val id = intent.getStringExtra("RECIPE_ID") ?: return
        viewModel.loadRecipe(id)

        setContent {
            val recipe by viewModel.recipe.collectAsState()
            val isLoading by viewModel.isLoading.collectAsState()
            val error by viewModel.error.collectAsState()

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(recipe?.name ?: "Détails") },
                        navigationIcon = {
                            IconButton(onClick = { finish() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                            }
                        }
                    )
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else if (error != null) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = error!!)
                            Button(onClick = { viewModel.loadRecipe(id) }) {
                                Text("Réessayer")
                            }
                        }
                    } else {
                        recipe?.let {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                AsyncImage(
                                    model = it.thumbnailUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(250.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = it.name,
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "${it.category} | ${it.area}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Instructions",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = it.instructions,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    
                                    if (it.ingredients.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text(
                                            text = "Ingrédients",
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        it.ingredients.zip(it.measures).forEach { (ing, meas) ->
                                            Text(
                                                text = "• $ing ($meas)",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
