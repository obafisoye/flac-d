package com.example.flacd.api

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.flacd.api.model.Album
import com.example.flacd.api.model.AlbumData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AlbumsManager(private val db: FirebaseFirestore) {
    private var _albumsResponse = mutableStateOf<List<Album>>(emptyList())
    private val token = "yKOQpAcVvaYUwqOcJyUYlMVIpdlTjEJIbEQKzrEu"


    // public state variable for album data
    val albumsResponse: MutableState<List<Album>>
        @Composable get() = remember {
            _albumsResponse
        }

    // async function to get album data
    init {
        getAlbums()
    }

    // fetches a list of most wanted albums from the Discogs API and updates the albumsResponse state
    // updated to update database with unique albums and then fetch albums from database
    private fun getAlbums() {
        val service = Api.retrofitService.getMostWantedAlbums(token)

        service.enqueue(object : Callback<AlbumData> {
            override fun onResponse(
                call: Call<AlbumData>,
                response: Response<AlbumData>
            ) {
                if (response.isSuccessful) {
                    Log.i("Data", "Data is loaded")
                    val albumList = response.body()?.results ?: emptyList()

                    // Store in firebase firestore
                    saveAlbumsToFirebase(albumList, db = db)

                    // Updates the albumsResponse state with the fetched data
                    getAlbumsFromFirestore(db = db)
                }
            }

            override fun onFailure(call: Call<AlbumData>, t: Throwable) {
                // Logs the error if the API call fails
                Log.d("DataError", "${t.message}")
            }
        })
    }

    // iterates through the album list and saves each album to a firestore collection named "albums"
    fun saveAlbumsToFirebase(albumList: List<Album>, db: FirebaseFirestore) {
        val collection: CollectionReference = db.collection("albums")

        CoroutineScope(Dispatchers.IO).launch {
            for (album in albumList) {
                try {
                    // check if album already exists in database
                    val querySnapshot = collection.whereEqualTo("title", album.title).get().await()

                    if (querySnapshot.isEmpty) {
                        val albumData = hashMapOf(
                            "id" to album.id,
                            "country" to album.country,
                            "coverImage" to album.coverImage,
                            "format" to album.format,
                            "genre" to album.genre,
                            "label" to album.label,
                            "style" to album.style,
                            "thumb" to album.thumb,
                            "title" to album.title,
                            "type" to album.type,
                            "year" to album.year
                        )

                        // add album to firestore
                        collection.add(albumData).await()
                        Log.i("Firestore", "Album ${album.title} added successfully")
                    }
                }
                catch (e: Exception){
                    Log.e("FirestoreError", "Error processing album ${album.title}: ${e.message}")
                }
            }
        }
    }

    // function to get albums from firestore
    fun getAlbumsFromFirestore(db: FirebaseFirestore) {
        CoroutineScope(Dispatchers.IO).launch {
            val collection: CollectionReference = db.collection("albums")

            collection.get()
                .addOnSuccessListener { documents ->
                    val albumList = mutableListOf<Album>()
                    for (document in documents) {
                        val album = document.toObject(Album::class.java)
                        albumList.add(album)
                    }

                    albumList.shuffle()
                    _albumsResponse.value = albumList
                    Log.i("Firestore", "Albums fetched successfully")
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreError", "Error getting albums: ${e.message}")
                }
        }
    }

    // function to get a single album from firestore
    suspend fun getAlbumById(db: FirebaseFirestore, movieId: String): Album? {
        return try {
            // query firestore with specific movie id
            val documentSnapshot =
                db.collection("albums").whereEqualTo("id", movieId.toInt()).get().await()

            if (!documentSnapshot.isEmpty) {
                documentSnapshot.documents[0].toObject(Album::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error getting album: ${e.message}")
            null
        }
    }
}
