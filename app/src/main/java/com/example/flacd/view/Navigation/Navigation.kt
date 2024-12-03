package com.example.flacd.view.Navigation

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.flacd.R
import com.example.flacd.destinations.Destination

/**
 * Bottom navigation bar composable
 * @param navController The navigation host controller
 */
@Composable
fun BottomNav(navController: NavHostController) {
    NavigationBar(
        modifier = androidx.compose.ui.Modifier.height(75.dp),
        containerColor = androidx.compose.ui.graphics.Color.DarkGray,
    ) {
        /**
         * Current destination
         */
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry.value?.destination

        /**
         * Icon for the navigation bar
         */
        val ic_home = painterResource(id = R.drawable.ic_home)

        /**
         * Icon for the navigation bar
         */
        val ic_search = painterResource(id = R.drawable.ic_search)

        /**
         * Icon for the navigation bar
         */
        val ic_profile = painterResource(id = R.drawable.ic_profile)

        /**
         * Navigation item for the home screen
         */
        NavigationBarItem(
            selected = currentDestination?.route == "home",
            onClick = { navController.navigate(Destination.Home.route){
                popUpTo(Destination.Home.route)
                    launchSingleTop = true
            } },
            icon = { Icon(painter = ic_home, contentDescription = "Home Screen Button", tint = Color.Black) }
        )

        /**
         * Navigation item for the search screen
         */
        NavigationBarItem(
            selected = currentDestination?.route == "search",
            onClick = { navController.navigate(Destination.Search.route){
                popUpTo(Destination.Search.route)
                    launchSingleTop = true
            } },
            icon = { Icon(painter = ic_search, contentDescription = "Search Screen Button", tint = Color.Black) }
        )

        /**
         * Navigation item for the profile screen
         */
        NavigationBarItem(
            selected = currentDestination?.route == "profile",
            onClick = { navController.navigate(Destination.Profile.route){
                popUpTo(Destination.Profile.route)
                    launchSingleTop = true
            } },
            icon = { Icon(painter = ic_profile, contentDescription = "Profile Screen Button", tint = Color.Black) }
        )

    }
}