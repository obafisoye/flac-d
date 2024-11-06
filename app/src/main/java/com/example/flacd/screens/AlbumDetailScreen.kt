package com.example.flacd.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.flacd.api.model.Album

// Screen for displaying album details
@Composable
fun AlbumDetailScreen(modifier: Modifier = Modifier, album: Album){
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ){
        album.title?.let { Text(text = it, color = Color.Black) }
    }
}