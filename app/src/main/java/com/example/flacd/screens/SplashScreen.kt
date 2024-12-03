package com.example.flacd.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.flacd.R
import kotlinx.coroutines.delay

/**
 * Splash screen composable
 * @param modifier Modifier to be applied to the layout
 * @param onTimeout Callback to be invoked when the splash screen times out
 */
@Composable
fun SplashScreen(modifier: Modifier, onTimeout: () -> Unit){

    /**
     * Font family for the text
     */
    val dotoFont = FontFamily(
        Font(R.font.doto_variable)
    )

    /**
     * Launches a coroutine to delay the splash screen for 2 seconds before invoking the onTimeout callback
     */
    LaunchedEffect(Unit) {
        delay(2000)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "FLACd",
                fontFamily = dotoFont,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black,
                fontSize = 100.sp
            )
        }
    }
}