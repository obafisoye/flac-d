package com.example.flacd

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
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
import com.example.flacd.screens.RelatedAlbumsScreen
import com.example.flacd.screens.SearchScreen
import com.example.flacd.ui.ProfileManager
import com.example.flacd.ui.theme.FLACdTheme
import com.example.flacd.view.Navigation.BottomNav
import com.example.flacd.viewmodel.AlbumViewModel
import com.example.flacd.viewmodel.ProfileViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FLACdTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    /**
                     * Navigation Controller
                     */
                    val navController = rememberNavController()

                    /**
                     * Application context
                     */
                    val context: Context = applicationContext

                    /**
                     * View model for album data
                     */
                    val viewModel: AlbumViewModel = ViewModelProvider(this)[AlbumViewModel::class.java]

                    /**
                     * View model for profile data
                     */
                    val profileViewModel: ProfileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

                    /**
                     * Firebase Firestore database
                     */
                    val db = Firebase.firestore

                    /**
                     * Firebase Authentication instance
                     */
                    val auth = FirebaseAuth.getInstance()

                    /**
                     * Albums Manager
                     */
                    val albumsManager = AlbumsManager(db)

                    /**
                     * Profile Manager
                     */
                    val profileManager = ProfileManager(db)

                    /**
                     * App composable
                     */
                    App(navController = navController, modifier = Modifier.padding(innerPadding),
                        albumsManager, db, viewModel, context, profileViewModel, profileManager,
                        auth)
                }
            }
        }
    }

    /**
     * App composable.
     * @param navController The navigation controller.
     * @param modifier The modifier to be applied to the layout.
     * @param albumsManager The albums manager.
     * @param db The Firebase Firestore database.
     * @param viewModel The album view model.
     * @param context The application context.
     * @param profileViewModel The profile view model.
     * @param profileManager The profile manager.
     * @param auth The Firebase Authentication instance.
     */
    @OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(navController: NavHostController, modifier: Modifier, albumsManager: AlbumsManager,
        db: FirebaseFirestore, viewModel: AlbumViewModel, context: Context,
        profileViewModel: ProfileViewModel, profileManager: ProfileManager, auth: FirebaseAuth) {

        /**
         * Album variable to store album gotten from database
         */
        var album by remember {
            mutableStateOf<Album?>(null)
        }

        /**
         * A font family for the text.
         */
        val dotoFont = FontFamily(
            Font(R.font.doto_variable)
        )



        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.height(85.dp),
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = "FLACd",
                                color = Color.White,
                                fontFamily = dotoFont,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.headlineSmall,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.DarkGray,
                    )
                )
            },
            bottomBar = { BottomNav(navController = navController) }

        ) { paddingValues ->
            paddingValues.calculateBottomPadding()
            Spacer(modifier = Modifier.padding(10.dp))

            /**
             * Navigation Graph
             */
            NavHost(
                navController = navController as NavHostController,
                startDestination = Destination.Home.route
            ) {

                /**
                 * Home Navigation Route
                 */
                composable(Destination.Home.route) {
                    HomeScreen(
                        modifier = Modifier.padding(paddingValues),
                        albumsManager,
                        navController
                    )
                }

                /**
                 * Search Navigation Route
                 */
                composable(Destination.Search.route) {
                    SearchScreen(
                        modifier = Modifier.padding(paddingValues),
                        viewModel = viewModel,
                        db = db,
                        navController
                    )
                }

                /**
                 * Profile Navigation Route
                 */
                composable(Destination.Profile.route) {
                    // User id of current user
                    val user_id = auth.currentUser?.uid

                    LaunchedEffect(user_id) {
                        if(user_id != null) {
                            profileViewModel.loadUser(db, user_id)
                        }
                    }

                    // User state
                    val user by profileViewModel.user.collectAsState()

                    if (user != null) {
                        ProfileScreen(modifier = Modifier.padding(paddingValues),
                            context, user, profileManager, auth)
                    }
                    else{
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color.White)
                        }
                    }

                }

                /**
                 * Album Detail Navigation Route
                 */
                composable(Destination.AlbumDetail.route) { navBackStackEntry ->
                    // Album id obtained from navigation
                    val album_id: String? = navBackStackEntry.arguments?.getString("albumId")

                    LaunchedEffect(album_id) {
                        if (album_id != null) {
                            album = albumsManager.getAlbumById(db, album_id)
                        }
                    }

                    album?.let {
                        AlbumDetailScreen(
                            album = it,
                            modifier = Modifier.padding(paddingValues),
                            viewModel = viewModel,
                            db = db,
                            navController = navController
                        )
                    }
                }

                /**
                 * Profile Detail Navigation Route
                 */
                composable(Destination.ProfileDetail.route) {
                    ProfileDetailScreen(modifier = Modifier.padding(paddingValues))
                }

                /**
                 * Related Albums Navigation Route
                 */
                composable(Destination.RelatedAlbums.route) { navBackStackEntry ->
                    // Style obtained from navigation
                    val style: String? = navBackStackEntry.arguments?.getString("style")

                    style?.let {
                        RelatedAlbumsScreen(
                            style = it,
                            modifier = Modifier.padding(paddingValues),
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
