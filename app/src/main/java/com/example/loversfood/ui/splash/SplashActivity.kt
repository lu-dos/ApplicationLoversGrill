package com.example.loversfood.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.loversfood.R
import com.example.loversfood.ui.list.RecipeListActivity
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                // Using foreground launcher icon as a placeholder for the logo
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Logo",
                    modifier = Modifier.size(120.dp)
                )
            }

            LaunchedEffect(Unit) {
                delay(2000)
                startActivity(Intent(this@SplashActivity, RecipeListActivity::class.java))
                finish()
            }
        }
    }
}
