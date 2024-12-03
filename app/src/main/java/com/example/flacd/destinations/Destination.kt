package com.example.flacd.destinations


/**
 * This class is responsible for where the app should navigate to.
 */
sealed class Destination(val route: String) {
    /**
     * Home destination
     */
    object Home: Destination("home")

    /**
     * Search destination
     */
    object Search: Destination("search")

    /**
     * Profile destination
     */
    object Profile: Destination("profile/{userId}"){
        fun createRoute(userId: String?) = "profile/$userId"
    }

    /**
     * Album detail destination
     */
    object AlbumDetail: Destination("albumDetail/{albumId}"){
        fun createRoute(albumId: Int?) = "albumDetail/$albumId"
    }

    /**
     * Profile detail destination
     */
    object ProfileDetail: Destination("profileDetail/{userId}"){
        fun createRoute(userId: String?) = "profileDetail/$userId"
    }

    /**
     * Related albums destination
     */
    object RelatedAlbums: Destination("relatedAlbums/{style}"){
        fun createRoute(style: String?) = "relatedAlbums/$style"
    }
}