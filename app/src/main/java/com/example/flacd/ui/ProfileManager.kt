package com.example.flacd.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.flacd.api.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileManager(private val db: FirebaseFirestore){
    // function to get user data from firebase and return it as a UserProfile object
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

    // function to update user data in firebase
    fun updateUserProfile(context: Context, userProfile: UserProfile){
        val userRef = userProfile.userId?.let { db.collection("users").document(it) }

        val updatedFields = hashMapOf<String, Any?>(
            "username" to userProfile.username,
            "profilePictureUrl" to userProfile.profilePictureUrl,
            "bio" to userProfile.bio
        ) as MutableMap<String, Any?>

        userRef?.update(updatedFields)
            ?.addOnSuccessListener {
                Log.d("Firestore", "User profile updated successfully")
                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            }
            ?.addOnFailureListener { e ->
                Log.e("FirestoreError", "Error updating user profile: ${e.message}")
                Toast.makeText(context, "Error updating profile", Toast.LENGTH_SHORT).show()
            }
    }
}