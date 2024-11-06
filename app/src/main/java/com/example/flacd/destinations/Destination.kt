package com.example.flacd.destinations


//This class is responsible for where the app should navigate to.
//by using sealed class, we are not able to further subclass this class.
sealed class Destination(val route: String) {
    object Home: Destination("home")
    object Search: Destination("search")
    object Profile: Destination("profile")
    object AlbumDetail: Destination("albumDetail/{albumId}"){
        fun createRoute(albumId: Int?) = "albumDetail/$albumId"
    }
    object ProfileDetail: Destination("profileDetail/{userId}"){
        fun createRoute(userId: Int?) = "profileDetail/$userId"
    }

}