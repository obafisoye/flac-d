package com.example.flacd

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flacd.api.AlbumsManager
import com.example.flacd.api.model.Album
import com.example.flacd.destinations.Destination
import com.example.flacd.screens.AlbumDetailScreen
import com.example.flacd.screens.HomeScreen
import com.example.flacd.screens.ProfileDetailScreen
import com.example.flacd.screens.ProfileScreen
import com.example.flacd.screens.SearchScreen
import com.example.flacd.ui.theme.FLACdTheme
import com.example.flacd.view.Navigation.BottomNav
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FLACdTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val navController = rememberNavController()

                    // firebase
                    val db = Firebase.firestore

                    // fetch albums
                    val albumsManager = AlbumsManager(db)

                    App(navController = navController, modifier = Modifier.padding(innerPadding), albumsManager, db)
                }
            }
        }
    }
}

// Main app composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(navController: NavHostController, modifier: Modifier, albumsManager: AlbumsManager, db: FirebaseFirestore) {

    var album by remember {
        mutableStateOf<Album?>(null)
    }

    val interFont = FontFamily(
        Font(R.font.inter_variable)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "FLACd",
                            color = Color.White,
                            fontFamily = interFont
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                )
            )
        },
        bottomBar = { BottomNav(navController = navController)}

    ){ paddingValues ->
        paddingValues.calculateBottomPadding()
        Spacer(modifier = Modifier.padding(10.dp))

        // Navigation Host
        NavHost(navController = navController as NavHostController, startDestination = Destination.Home.route){

            // Home Navigation Route
            composable(Destination.Home.route){
                HomeScreen(modifier = Modifier.padding(paddingValues), albumsManager, navController)
            }

            // Search Navigation Route
            composable(Destination.Search.route){
                SearchScreen(modifier = Modifier.padding(paddingValues))
            }

            // Profile Navigation Route
            composable(Destination.Profile.route){
                ProfileScreen(modifier = Modifier.padding(paddingValues))
            }

            // Album Detail Navigation Route
            composable(Destination.AlbumDetail.route){ navBackStackEntry ->
                val album_id: String? = navBackStackEntry.arguments?.getString("albumId")

                LaunchedEffect(album_id) {
                    if(album_id != null){
                        album = albumsManager.getAlbumById(db, album_id)
                    }
                }

                album?.let { AlbumDetailScreen(album = it, modifier = Modifier.padding(paddingValues)) }
            }

            // Profile Detail Navigation Route
            composable(Destination.ProfileDetail.route){
                ProfileDetailScreen(modifier = Modifier.padding(paddingValues))
            }
        }
    }
}

