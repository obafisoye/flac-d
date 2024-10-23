package com.example.flacd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flacd.destinations.Destination
import com.example.flacd.screens.AlbumDetailScreen
import com.example.flacd.screens.HomeScreen
import com.example.flacd.screens.ProfileDetailScreen
import com.example.flacd.screens.ProfileScreen
import com.example.flacd.screens.SearchScreen
import com.example.flacd.ui.theme.FLACdTheme
import com.example.flacd.view.Navigation.BottomNav

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FLACdTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val navController = rememberNavController()

                    App(navController = navController, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(navController: NavHostController, modifier: Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "FLACd")}
            )
        },
        bottomBar = { BottomNav(navController = navController)}

    ){ paddingValues ->
        paddingValues.calculateBottomPadding()
        Spacer(modifier = Modifier.padding(10.dp))

        NavHost(navController = navController as NavHostController, startDestination = Destination.Home.route){

            composable(Destination.Home.route){
                HomeScreen(modifier = Modifier.padding(paddingValues))
            }

            composable(Destination.Search.route){
                SearchScreen(modifier = Modifier.padding(paddingValues))
            }

            composable(Destination.Profile.route){
                ProfileScreen(modifier = Modifier.padding(paddingValues))
            }

            composable(Destination.AlbumDetail.route){
                AlbumDetailScreen(modifier = Modifier.padding(paddingValues))
            }

            composable(Destination.ProfileDetail.route){
                ProfileDetailScreen(modifier = Modifier.padding(paddingValues))
            }
        }
    }
}

