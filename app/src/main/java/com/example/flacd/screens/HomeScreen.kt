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

// Screen for displaying home content
@Composable
fun HomeScreen(modifier: Modifier = Modifier, albumsManager: AlbumsManager, navController: NavController){

    val interFont = FontFamily(
        Font(R.font.inter_variable)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        //Text(text = "Home Screen", color = Color.White, fontFamily = interFont)

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

// Album item composable
@Composable
fun Album(album: Album, navController: NavController){

    Column(
        modifier = Modifier
            //.border(1.dp, Color.White, shape = RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                Log.i("AlbumClicked", "Album ${album.id} clicked")
                navController.navigate("albumDetail/${album.id}")
            }
    ) {
//        Row(
//            modifier = Modifier
//                .background(Color.Black)
//                .fillMaxWidth()
//                .padding(5.dp)
//        ) {
//            Text(text = album.title, color = Color.White)
//        }
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
