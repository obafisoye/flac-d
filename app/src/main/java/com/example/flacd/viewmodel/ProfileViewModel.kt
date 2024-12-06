package com.example.flacd.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flacd.api.model.UserProfile
import com.example.flacd.ui.ProfileManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * A ViewModel class for managing user profiles.
 */
class ProfileViewModel: ViewModel() {
    /**
     * A mutable state flow for holding the user profile.
     */
    private val _user = MutableStateFlow<UserProfile?>(null)

    /**
     * A public state flow for accessing the user profile.
     */
    val user: StateFlow<UserProfile?> = _user

    /**
     * Loads a user profile from the database.
     * @param db The Firebase Firestore database instance.
     * @param userId The ID of the user to load.
     */
    fun loadUser(db: FirebaseFirestore, userId: String){
        viewModelScope.launch {
            // Fetch user then update view model with fetched user
            val profileManager = ProfileManager(db)
            val fetchedUser = profileManager.getUser(userId)
            _user.value = fetchedUser
        }
    }
}