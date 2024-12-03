package com.example.flacd

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.flacd.screens.RegisterScreen
import com.example.flacd.ui.theme.FLACdTheme

class RegisterActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            FLACdTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // Application context
                    val context: Context = applicationContext

                    // Register screen
                    RegisterScreen(context = context, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}