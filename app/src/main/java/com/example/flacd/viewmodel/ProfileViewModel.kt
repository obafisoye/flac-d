package com.example.flacd.viewmodel

import android.view.View
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flacd.api.model.UserProfile
import com.example.flacd.ui.ProfileManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    var user = mutableStateOf<UserProfile?>(null)

    fun loadUser(db: FirebaseFirestore, userId: String){
        viewModelScope.launch {
            val profileManager = ProfileManager(db)
            val fetchedUser = profileManager.getUser(userId)
            user.value = fetchedUser
        }
    }
}