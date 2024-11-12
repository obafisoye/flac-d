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

class AlbumViewModel: ViewModel(){
    private val token = "yKOQpAcVvaYUwqOcJyUYlMVIpdlTjEJIbEQKzrEu"
    val albums = mutableStateOf<List<Album>>(emptyList())

    var searchTerm = mutableStateOf("")

    // returns a list of searched albums
    fun searchAlbum(query: String, db: FirebaseFirestore){

        // if query is not blank then make api call
        if (query.isNotBlank()){

            //Log.i("Search", "Query: $query")
            val service = Api.retrofitService.searchAlbumByQuery(q = query, token = token)
            service.enqueue(object: Callback<AlbumData>{

                override fun onResponse(call: Call<AlbumData>, response: Response<AlbumData>) {
                    if(response.isSuccessful){
                        //Log.i("Search", "${albums.value}")
                        albums.value = response.body()?.results?: emptyList()

//                        val albumsManager = AlbumsManager(db)
//                        albumsManager.saveAlbumsToFirebase(albums.value, db)
                    }
                }

                override fun onFailure(call: Call<AlbumData>, t: Throwable) {
                    Log.d("SearchError", "${t.message}")
                }

            })
        }
        else{
            albums.value = emptyList()
        }
    }

    // saves search query for use on search screen
    fun saveSearchTerm(term: String){
        searchTerm.value = term
    }
}