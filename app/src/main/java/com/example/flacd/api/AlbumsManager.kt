package com.example.flacd.api

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.flacd.api.model.Album
import com.example.flacd.api.model.AlbumData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumsManager {
    private var _albumsResponse = mutableStateOf<List<Album>>(emptyList())
    private val token = "yKOQpAcVvaYUwqOcJyUYlMVIpdlTjEJIbEQKzrEu"

    val albumsResponse: MutableState<List<Album>>
        @Composable get() = remember {
            _albumsResponse
        }

    init{
        getAlbums()
    }
    private fun getAlbums(){
        val service = Api.retrofitService.getMostWantedAlbums(token)

        service.enqueue(object: Callback<AlbumData>{
            override fun onResponse(
                call: Call<AlbumData>,
                response: Response<AlbumData>
            ){
                if(response.isSuccessful){
                    Log.i("Data", "Data is loaded")

                    _albumsResponse.value = response.body()?.results?: emptyList()
                    // Log.i("DataResponse", _albumsResponse.value.toString())
                }
            }

            override fun onFailure(call: Call<AlbumData>, t: Throwable) {
                Log.d("DataError", "${t.message}")
            }
        })
    }
}