package com.example.flacd.ui

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.flacd.api.model.UserProfile
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
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

    // re-authenticate user with credentials then delete said user
    fun reAuthAndDeleteUser(appContext: Context, activityContext: Context, onSuccess: () -> Unit){
        val user = FirebaseAuth.getInstance().currentUser

        if(user != null){
            promptPassword(activityContext) { password ->
                val credential = EmailAuthProvider.getCredential(user.email!!, password)

                user.reauthenticate(credential)
                    .addOnCompleteListener { reAuthTask ->
                        if (reAuthTask.isSuccessful) {
                            deleteUser(appContext, onSuccess)
                        } else {
                            Log.e("FirestoreError", "Error reauthenticating user: ${reAuthTask.exception?.message}"
                            )
                            Toast.makeText(appContext, "Error reauthenticating user", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    // delete user from auth
    fun deleteUser(context: Context, onSuccess: () -> Unit){
        val user = FirebaseAuth.getInstance().currentUser
        if(user != null){
            val userId = user.uid

            user.delete()
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        deleteUserData(context, userId, onSuccess)
                    }
                    else{
                        Log.e("FirestoreError", "Error deleting user: ${task.exception?.message}")
                        Toast.makeText(context, "Error deleting user", Toast.LENGTH_SHORT).show()
                    }
                }
        }else{
            Log.e("FirestoreError", "No user is logged in")
        }
    }

    // delete user data from firestore
    private fun deleteUserData(context: Context, userId: String, onSuccess: () -> Unit){
        db.collection("users").document(userId)
            .delete()
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Log.d("Firestore", "User data deleted successfully")
                    Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show()
                    onSuccess() // call onSuccess if delete is successful
                }
                else {
                    Log.e("FirestoreError", "Error deleting user data: ${task.exception?.message}")
                    Toast.makeText(context, "Error deleting user", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // prompts user for password to delete account
    private fun promptPassword(context: Context, callback: (String) -> Unit){
        val inputField = EditText(context)
        inputField.hint = "Enter your password"
        inputField.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD

        AlertDialog.Builder(context)
            .setTitle("Re-Authentication Required")
            .setMessage("Please enter password to confirm")
            .setView(inputField)
            .setPositiveButton("Confirm") { _, _ ->
                val password = inputField.text.toString()

                if(password.isNotEmpty()){
                    callback(password)
                } else{
                    Toast.makeText(context, "Password is required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}