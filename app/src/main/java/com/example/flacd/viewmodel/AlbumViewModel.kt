package com.example.flacd.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.flacd.api.AlbumsManager
import com.example.flacd.api.Api
import com.example.flacd.api.model.Album
import com.example.flacd.api.model.AlbumData
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Album View Model
 */
class AlbumViewModel: ViewModel(){

    /**
     * The Discogs API token used for authentication.
     */
    private val token = "yKOQpAcVvaYUwqOcJyUYlMVIpdlTjEJIbEQKzrEu"

    /**
     * A mutable state list for holding albums returned from the search API call.
     */
    val albums = mutableStateOf<List<Album>>(emptyList())

    /**
     * A mutable list for holding albums returned from the filtered by style API call.
     */
    val styleAlbums = mutableStateOf<List<Album>>(emptyList())

    /**
     * A mutable state holding the current search term entered by the user
     */
    var searchTerm = mutableStateOf("")

    /**
     * Searches for albums based on the given query and stores them in Firestore.
     * @param query The search query entered by the user.
     * @param db The Firebase Firestore database instance.
     */
    fun searchAlbum(query: String, db: FirebaseFirestore){

        // if query is not blank then make api call
        if (query.isNotBlank()){

            //Log.i("Search", "Query: $query")
            val service = Api.retrofitService.searchAlbumByQuery(q = query, token = token)
            service.enqueue(object: Callback<AlbumData>{

                override fun onResponse(call: Call<AlbumData>, response: Response<AlbumData>) {
                    if(response.isSuccessful){
                        // Update albums list and save to firestore
                        albums.value = response.body()?.results?: emptyList()

                        val albumsManager = AlbumsManager(db)
                        albumsManager.saveAlbumsToFirebase(albums.value, db)
                    }
                }

                override fun onFailure(call: Call<AlbumData>, t: Throwable) {
                    Log.d("SearchError", "${t.message}")
                }

            })
        }
        else{
            // Clear albums list if query is empty
            albums.value = emptyList()
        }
    }

    /**
     * Saves the search term entered by the user for use in the search screen.
     * @param term The search term entered by the user.
     */
    fun saveSearchTerm(term: String){
        searchTerm.value = term
    }

    /**
     * Searches for albums based on the given style and stores them in Firestore.
     * @param style The style of albums to search for.
     * @param db The Firebase Firestore database instance.
     */
    fun getAlbumsByStyle(style:String, db: FirebaseFirestore){

        if(style.isNotBlank()){
            val service = Api.retrofitService.getAlbumsByStyle(style = style, token = token)
            service.enqueue(object: Callback<AlbumData>{
                override fun onResponse(call: Call<AlbumData>, response: Response<AlbumData>) {
                    if(response.isSuccessful){
                        // Update the styleAlbums list and save to firestore
                        styleAlbums.value = response.body()?.results?: emptyList()

                        val albumsManager = AlbumsManager(db)
                        albumsManager.saveAlbumsToFirebase(styleAlbums.value, db)
                    }
                }

                override fun onFailure(call: Call<AlbumData>, t: Throwable) {
                    Log.e("SearchError", "${t.message}")
                }
            })
        }
        else{
            // Clear the styleAlbums list if style is empty
            styleAlbums.value = emptyList()
        }
    }
}