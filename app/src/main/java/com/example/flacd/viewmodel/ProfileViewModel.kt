package com.example.flacd.viewmodel

import android.view.View
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flacd.api.model.UserProfile
import com.example.flacd.ui.ProfileManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    private val _user = MutableStateFlow<UserProfile?>(null)
    val user: StateFlow<UserProfile?> = _user

    fun loadUser(db: FirebaseFirestore, userId: String){
        viewModelScope.launch {
            val profileManager = ProfileManager(db)
            val fetchedUser = profileManager.getUser(userId)
            _user.value = fetchedUser
        }
    }
}