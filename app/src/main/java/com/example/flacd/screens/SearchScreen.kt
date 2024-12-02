package com.example.flacd.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flacd.R
import com.example.flacd.viewmodel.AlbumViewModel
import com.google.firebase.firestore.FirebaseFirestore

// Screen for displaying and searching for content
@Composable
fun SearchScreen(modifier: Modifier = Modifier, viewModel: AlbumViewModel, db: FirebaseFirestore, navController: NavController){

    // album to be searched
    var query by rememberSaveable { viewModel.searchTerm }

    val keyboardController = LocalSoftwareKeyboardController.current

    val interFont = FontFamily(
        Font(R.font.inter_variable)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        Column(
            // modifier = Modifier.
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row {
                OutlinedTextField(
                    value = viewModel.searchTerm.value,
                    onValueChange = {
                        viewModel.searchTerm.value = it
                        if (query.isBlank()) {
                            query = ""
                            viewModel.searchAlbum(query, db)
                        }
                    },
                    label = { Text("Search for an album/artist", color = Color.White, fontSize = 14.sp) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        keyboardController?.hide()
                    }),
                    modifier = Modifier
                        .padding(16.dp),
                    textStyle = TextStyle(color = Color.White)
                )
                Button(
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .size(85.dp, 40.dp),
                    onClick = {
                        viewModel.searchAlbum(query, db)
                        keyboardController?.hide()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                ){
                    Text("Search", fontFamily = interFont, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            for(movie in viewModel.albums.value){
                LazyColumn {
                    items(viewModel.albums.value) { album ->
                        Album(album, navController = navController)
                    }
                }
            }

        }

    }
}