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

/**
 * A manager class for managing album data.
 * @param db The Firebase Firestore database instance.
 */
class AlbumsManager(private val db: FirebaseFirestore) {
    /**
     * A mutable state list for holding album response data.
     */
    private var _albumsResponse = mutableStateOf<List<Album>>(emptyList())

    /**
     * The Discogs API token used for authentication.
     */
    private val token = "yKOQpAcVvaYUwqOcJyUYlMVIpdlTjEJIbEQKzrEu"


    /**
     * A public state variable for holding album data.
     */
    val albumsResponse: MutableState<List<Album>>
        @Composable get() = remember {
            _albumsResponse
        }

    /**
     * Initializes the manager by fetching album data from the Discogs API.
     */
    init {
        getAlbums()
    }

    /**
     * Fetches album data from the Discogs API and updates the albumsResponse state,
     * saves the albums to the database and then fetches the albums from the database.
     */
    private fun getAlbums() {
        /**
         * Enqueues a Retrofit call to fetch album data from the Discogs API.
         */
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

    /**
     * Iterates through the album list and saves each album to a firestore collection named "albums".
     * @param albumList The list of albums to be saved.
     * @param db The Firebase Firestore database instance.
     * @throws Exception if an error occurs while saving the albums.
     */
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
                            "year" to album.year,
                            "createdAt" to System.currentTimeMillis()
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

    /**
     * Retrieves albums from the Firebase Firestore database and updates the albumsResponse state.
     * @param db The Firebase Firestore database instance.
     */
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

    /**
     * Retrieves a single album from the Firebase Firestore database by its ID.
     * @param db The Firebase Firestore database instance.
     * @param albumId The ID of the album to retrieve.
     * @return The album if found, else null.
     * @throws Exception if an error occurs while retrieving the album.
     */
    suspend fun getAlbumById(db: FirebaseFirestore, albumId: String): Album? {
        return try {
            // query firestore with specific movie id
            val documentSnapshot =
                db.collection("albums").whereEqualTo("id", albumId.toInt()).get().await()

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
