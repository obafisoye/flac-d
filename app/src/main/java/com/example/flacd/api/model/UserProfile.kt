package com.example.flacd.api.model

/**
 * Represents a user profile
 */
data class UserProfile (
    val userId: String? = null,
    val username: String? = null,
    val email: String? = null,
    val profilePictureUrl: String? = null,
    val bio: String? = null,
)
