package com.example.flacd.ui

import android.util.Log
import com.example.flacd.api.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileManager(private val db: FirebaseFirestore){
    suspend fun getUser(userId: String): UserProfile? {
        return try{
            val userDoc = db.collection("users").document(userId).get().await()
            if(userDoc.exists()){
                userDoc.toObject(UserProfile::class.java)
            }else{
                null
            }
        }catch(e: Exception){
          Log.e("FirestoreError", "Error getting user: ${e.message}")
            null
        }
    }
}