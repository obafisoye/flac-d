package com.example.flacd.api

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.flacd.api.model.Album
import com.example.flacd.api.model.AlbumData
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AlbumsManager(db: FirebaseFirestore) {
    private var _albumsResponse = mutableStateOf<List<Album>>(emptyList())
    private val token = "yKOQpAcVvaYUwqOcJyUYlMVIpdlTjEJIbEQKzrEu"

    // public state variable for album data
    val albumsResponse: MutableState<List<Album>>
        @Composable get() = remember {
            _albumsResponse
        }

    // async function to get album data
    init{
        getAlbums()
    }

    // fetches a list of most wanted albums from the Discogs API and updates the albumsResponse state
    private fun getAlbums(){
        val service = Api.retrofitService.getMostWantedAlbums(token)

        service.enqueue(object: Callback<AlbumData>{
            override fun onResponse(
                call: Call<AlbumData>,
                response: Response<AlbumData>
            ){
                if(response.isSuccessful){
                    Log.i("Data", "Data is loaded")

                    // Updates the albumsResponse state with the fetched data
                    _albumsResponse.value = response.body()?.results?: emptyList()
                }
            }

            override fun onFailure(call: Call<AlbumData>, t: Throwable) {
                // Logs the error if the API call fails
                Log.d("DataError", "${t.message}")
            }
        })
    }
}