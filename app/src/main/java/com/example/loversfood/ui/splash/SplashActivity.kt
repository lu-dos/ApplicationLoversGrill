package com.example.loversfood.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.loversfood.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Simple splash logic to navigate to MainActivity or RecipeListActivity
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
