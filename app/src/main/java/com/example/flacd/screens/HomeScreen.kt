package com.example.flacd.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.flacd.R
import com.example.flacd.api.AlbumsManager

@Composable
fun HomeScreen(modifier: Modifier = Modifier, albumsManager: AlbumsManager){

    val interFont = FontFamily(
        Font(R.font.inter_variable)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ){
        Text(text = "Home Screen", color = Color.White, fontFamily = interFont)
    }
}