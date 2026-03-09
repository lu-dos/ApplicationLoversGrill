package com.example.loversfood.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loversfood.R
import com.example.loversfood.ui.list.RecipeListActivity
import com.example.loversfood.ui.theme.LoversFoodTheme
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoversFoodTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // App Logo
                        // Important: Make sure you rename your file to logo_app.png in res/drawable
                        Image(
                            painter = painterResource(id = R.drawable.logo_app),
                            contentDescription = "Logo",
                            modifier = Modifier.size(200.dp),
                            contentScale = ContentScale.Fit
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // App Name
                        Text(
                            text = stringResource(id = R.string.app_name),
                            color = Color.White,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        
                        Spacer(modifier = Modifier.height(48.dp))
                        
                        // Loading Indicator
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 4.dp
                        )
                    }
                }
            }

            LaunchedEffect(Unit) {
                delay(2500)
                startActivity(Intent(this@SplashActivity, RecipeListActivity::class.java))
                finish()
            }
        }
    }
}
