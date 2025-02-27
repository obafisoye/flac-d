package com.example.flacd.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.request.Disposable
import com.example.flacd.viewmodel.AlbumViewModel

/**
 * Screen for displaying related albums based on the selected style.
 * @param style The selected style.
 * @param modifier The modifier to be applied to the layout.
 * @param viewModel The AlbumViewModel instance.
 * @param navController The navigation controller for navigating between screens.
 */
@Composable
fun RelatedAlbumsScreen(style: String, modifier: Modifier = Modifier, viewModel: AlbumViewModel, navController: NavController){

    /**
     * A scroll state for vertical scrolling.
     */
    val scrollState = rememberScrollState()

    /**
     * A list of albums related to the selected styles gotten from the view model.
     */
    val albums = viewModel.styleAlbums.value

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        Column(
            modifier = Modifier
                .padding(10.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "$style Music", color = Color.White, style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.height(10.dp))

            albums.forEach {
                Album(album = it, navController = navController)
            }
        }
    }
}