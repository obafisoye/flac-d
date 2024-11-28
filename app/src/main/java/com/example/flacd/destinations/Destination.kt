package com.example.flacd.destinations


//This class is responsible for where the app should navigate to.
//by using sealed class, we are not able to further subclass this class.
sealed class Destination(val route: String) {
    object Home: Destination("home")
    object Search: Destination("search")
    object Profile: Destination("profile/{userId}"){
        fun createRoute(userId: String?) = "profile/$userId"
    }
    object AlbumDetail: Destination("albumDetail/{albumId}"){
        fun createRoute(albumId: Int?) = "albumDetail/$albumId"
    }
    object ProfileDetail: Destination("profileDetail/{userId}"){
        fun createRoute(userId: String?) = "profileDetail/$userId"
    }
    object RelatedAlbums: Destination("relatedAlbums/{style}"){
        fun createRoute(style: String?) = "relatedAlbums/$style"
    }


}