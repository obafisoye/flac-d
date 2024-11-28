package com.example.flacd.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.flacd.api.model.UserProfile

// Screen for displaying profile content
@Composable
fun ProfileScreen(modifier: Modifier = Modifier, context: Context, user: UserProfile?){
    val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ){
        if (user != null) {
            if(isLoggedIn){
                Button(
                    onClick = {
                        sharedPref.edit()
                            .putBoolean("isLoggedIn", false)
                            .apply()

                        Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show()

                    }
                ) {
                    Text(text = "Log Out")
                }
            }
             user.username?.let { Text(text = it, color = Color.White) }
        }
    }
}