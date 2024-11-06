package com.example.flacd.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

// Screen for displaying profile details
@Composable
fun ProfileDetailScreen(modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ){
        Text(text = "Profile Detail Screen", color = Color.White)
    }
}