package com.example.flacd.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.flacd.R
import com.example.flacd.api.model.Album
import com.example.flacd.viewmodel.AlbumViewModel
import com.google.firebase.firestore.FirebaseFirestore

// Screen for displaying album details
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AlbumDetailScreen(modifier: Modifier = Modifier, album: Album, viewModel: AlbumViewModel, db: FirebaseFirestore, navController: NavController){

    val scrollState = rememberScrollState()

    val interFont = FontFamily(
        Font(R.font.inter_variable)
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        Column(
            modifier = Modifier
                .padding(10.dp)
                .verticalScroll(scrollState)
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
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ){
                val labels = album.label?.take(4)

                labels?.forEach {
                    val buttonWidth = (it.length * 10).dp
                    Button(
                        onClick = {},
                        enabled = false,
                        colors = ButtonDefaults.buttonColors(
                            disabledContainerColor = Color.DarkGray,
                        ),
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .size(width = buttonWidth.coerceIn(55.dp, 150.dp), height = 30.dp),
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
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                album.genre?.forEach {
                    val buttonWidth = (it.length * 10).dp
                    Button(
                        onClick = {},
                        enabled = false,
                        colors = ButtonDefaults.buttonColors(
                            disabledContainerColor = Color.DarkGray,
                        ),
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .size(width = buttonWidth.coerceIn(55.dp, 150.dp), height = 30.dp),
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
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                album.style?.forEach {
                    val buttonWidth = (it.length * 10).dp
                    Button(
                        onClick = {
                            viewModel.getAlbumsByStyle(style = it, db = db)
                            navController.navigate("relatedAlbums/${it}")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.teal_700),
                        ),
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .size(width = buttonWidth.coerceIn(55.dp, 150.dp), height = 30.dp),
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
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                album.format?.forEach {
                    val buttonWidth = (it.length * 10).dp
                    Button(
                        onClick = {},
                        enabled = false,
                        colors = ButtonDefaults.buttonColors(
                            disabledContainerColor = Color.DarkGray,
                        ),
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .size(width = buttonWidth.coerceIn(55.dp, 150.dp), height = 30.dp),
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