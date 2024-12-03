package com.example.flacd.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.flacd.R
import com.example.flacd.api.AlbumsManager
import com.example.flacd.api.model.Album

/**
 * Screen for displaying home content
 * @param modifier The modifier to be applied to the layout.
 * @param albumsManager The class for managing album data.
 * @param navController The navigation controller for navigating between screens.
 */
@Composable
fun HomeScreen(modifier: Modifier = Modifier, albumsManager: AlbumsManager, navController: NavController){

    /**
     * A font family for the text.
     */
    val interFont = FontFamily(
        Font(R.font.inter_variable)
    )


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        //Text(text = "Home Screen", color = Color.White, fontFamily = interFont)

        /**
         * Albums obtained from the AlbumsManager class.
         */
        val albums = albumsManager.albumsResponse.value

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            items(albums){album ->
                Album(album, navController)
            }
        }

    }
}

/**
 * A composable function that displays an album with its cover image.
 * @param album The album object to be displayed.
 * @param navController The navigation controller for navigating between screens.
 */
@Composable
fun Album(album: Album, navController: NavController){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                Log.i("AlbumClicked", "Album ${album.id} clicked")
                navController.navigate("albumDetail/${album.id}")
            }
    ) {
        Row {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                model = ImageRequest.Builder(
                    LocalContext.current
                ).data(album.coverImage)
                    .build(),
                contentDescription = album.title,
                contentScale = ContentScale.FillWidth
            )
        }
    }
}
