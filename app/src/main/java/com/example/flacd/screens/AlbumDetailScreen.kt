package com.example.flacd.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.flacd.R
import com.example.flacd.api.model.Album

// Screen for displaying album details
@Composable
fun AlbumDetailScreen(modifier: Modifier = Modifier, album: Album){

    val interFont = FontFamily(
        Font(R.font.inter_variable)
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        Column(
            modifier = Modifier.padding(10.dp)
        ) {

            // album title
            album.title?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontFamily = interFont,
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            // album year
            album.year?.let {
                Text(
                    text = "Released in $it",
                    color = Color.White,
                    fontFamily = interFont,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
            // album cover image
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

            Spacer(modifier = Modifier.height(20.dp))

            // album labels
            Text(text = "Labels: ", color = Color.White, fontFamily = interFont, style = MaterialTheme.typography.bodyMedium)
            Row {
                val labels = album.label?.take(3)

                labels?.forEach {
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .size(width = 90.dp, height = 30.dp),
                        contentPadding = PaddingValues(7.dp),
                    ){
                        Text(
                            text = "$it ",
                            color = Color.White,
                            fontFamily = interFont,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            modifier = Modifier.fillMaxSize(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            // albums genres
            Text(text = "Genres: ", color = Color.White, fontFamily = interFont, style = MaterialTheme.typography.bodyMedium)
            Row {
                album.genre?.forEach {
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .size(width = 80.dp, height = 30.dp),
                        contentPadding = PaddingValues(7.dp),
                    ){
                        Text(
                            text = "$it ",
                            color = Color.White,
                            fontFamily = interFont,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            modifier = Modifier.fillMaxSize(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            // album styles
            Text(text = "Styles: ", color = Color.White, fontFamily = interFont, style = MaterialTheme.typography.bodyMedium)
            Row {
                album.style?.forEach {
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .size(width = 80.dp, height = 30.dp),
                        contentPadding = PaddingValues(7.dp),
                    ){
                        Text(
                            text = "$it ",
                            color = Color.White,
                            fontFamily = interFont,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            modifier = Modifier.fillMaxSize(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            // album formats
            Text(text = "Formats: ", color = Color.White, fontFamily = interFont, style = MaterialTheme.typography.bodyMedium)
            Row {
                album.format?.forEach {
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .size(width = 60.dp, height = 30.dp),
                        contentPadding = PaddingValues(7.dp),
                    ){
                        Text(
                            text = "$it ",
                            color = Color.White,
                            fontFamily = interFont,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            modifier = Modifier.fillMaxSize(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

        }
    }
}