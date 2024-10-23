package com.example.flacd.view.Navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.flacd.R
import com.example.flacd.destinations.Destination

@Composable
fun BottomNav(navController: NavHostController) {
    NavigationBar {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry.value?.destination

        val ic_home = painterResource(id = R.drawable.ic_home)
        val ic_search = painterResource(id = R.drawable.ic_search)
        val ic_profile = painterResource(id = R.drawable.ic_profile)

        NavigationBarItem(
            selected = currentDestination?.route == "home",
            onClick = { navController.navigate(Destination.Home.route){
                popUpTo(Destination.Home.route)
                    launchSingleTop = true
            } },
            icon = { Icon(painter = ic_home, contentDescription = "Home Screen Button") }
        )

        NavigationBarItem(
            selected = currentDestination?.route == "search",
            onClick = { navController.navigate(Destination.Search.route){
                popUpTo(Destination.Search.route)
                    launchSingleTop = true
            } },
            icon = { Icon(painter = ic_search, contentDescription = "Search Screen Button") }
        )

        NavigationBarItem(
            selected = currentDestination?.route == "profile",
            onClick = { navController.navigate(Destination.Profile.route){
                popUpTo(Destination.Profile.route)
                    launchSingleTop = true
            } },
            icon = { Icon(painter = ic_profile, contentDescription = "Profile Screen Button") }
        )

    }
}