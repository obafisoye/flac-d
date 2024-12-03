package com.example.flacd.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.flacd.R
import com.example.flacd.SignInActivity
import com.example.flacd.api.model.UserProfile
import com.example.flacd.ui.ProfileManager
import com.google.firebase.auth.FirebaseAuth

/**
 * Screen for displaying profile content
 * @param modifier The modifier to be applied to the layout.
 * @param appContext The application context.
 * @param user The user profile.
 * @param profileManager The class for managing profile data.
 * @param auth The Firebase authentication instance.
 */
@Composable
fun ProfileScreen(modifier: Modifier = Modifier, appContext: Context, user: UserProfile?,
                  profileManager: ProfileManager, auth: FirebaseAuth){

    /**
     * A variable to track if the user is editing their profile.
     */
    var isEditing by remember { mutableStateOf(false) }

    /**
     * The activity context.
     */
    val activityContext = LocalContext.current

    /**
     * A variable to track if the user is in their settings.
     */
    var inSettings by remember { mutableStateOf(false) }

    /**
     * The user's username
     */
    var username by remember { mutableStateOf(user?.username ?: "") }

    /**
     * The user's bio
     */
    var bio by remember { mutableStateOf(user?.bio ?: "") }

    /**
     * The user's profile picture URL
     */
    var profilePictureUrl by remember { mutableStateOf(user?.profilePictureUrl ?: "") }

    /**
     * Shared preferences to track login status
     */
    val sharedPref = appContext.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

    /**
     * A scroll state for vertical scrolling.
     */
    val scrollState = rememberScrollState()

    /**
     * A font family for the text.
     */
    val interFont = FontFamily(
        Font(R.font.inter_variable)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        if (user != null) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .verticalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isEditing) {
                        TextField(
                            value = username,
                            onValueChange = { username = it.trim() },
                            label = { Text("Username") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    else{
                        if (username.isNotEmpty()){
                            Text(text = username,
                                color = Color.White,
                                fontFamily = interFont,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 24.sp
                            )
                        }else{
                            Text(text = "No bio available",
                                color = Color.Gray,
                                fontFamily = interFont,
                                fontStyle = FontStyle.Italic,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    if(isEditing){
                        TextField(
                            value = profilePictureUrl,
                            onValueChange = { profilePictureUrl = it.trim() },
                            label = { Text("Profile Picture URL") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }else {
                        if (profilePictureUrl.isNotEmpty()) {
                            AsyncImage(
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(140.dp)
                                    .clip(CircleShape),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(profilePictureUrl)
                                    .build(),
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Crop
                            )
                        }
                        else{
                            Text(text = "No profile picture available",
                                color = Color.Gray,
                                fontFamily = interFont,
                                fontStyle = FontStyle.Italic,
                                fontSize = 20.sp
                            )
                        }
                    }

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isEditing) {
                        TextField(
                            value = bio,
                            onValueChange = { bio = it },
                            label = { Text("Bio") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    else{
                        if (bio.isNotEmpty()){
                            Text(text = bio,
                                color = Color.White,
                                fontFamily = interFont,
                                fontSize = 16.sp
                            )
                        }else{
                            Text(text = "No bio available",
                                color = Color.Gray,
                                fontFamily = interFont,
                                fontStyle = FontStyle.Italic,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(!inSettings){
                        Button(
                            onClick = {
                                // save changes to profile
                                if (isEditing) {
                                    val updatedUserProfile = UserProfile(
                                        user.userId, username, user.email, profilePictureUrl, bio
                                    )

                                    profileManager.updateUserProfile(appContext, updatedUserProfile)
                                }
                                isEditing = !isEditing
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.teal_700),
                            ),
                        ) {
                            Text(text = if (isEditing) "Save" else "Edit Profile")
                        }
                    }

                    if(isEditing){
                        Button(
                            onClick = {
                                isEditing = !isEditing
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.teal_700),
                            ),
                        ){
                            Text(text = "Cancel")
                        }
                    }

                    if(!isEditing) {
                        Button(
                            onClick = {
                                inSettings = !inSettings
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.teal_700),
                            ),
                        ) {
                            Text(text = if (inSettings) "Cancel" else "Settings")
                        }
                    }


                    // if settings button is clicked display options
                    if(inSettings){
                        // if button is clicked delete user
                        Button(
                            onClick = {
                                profileManager.reAuthAndDeleteUser(appContext, activityContext, onSuccess = {
                                    sharedPref.edit().clear().apply()
                                    // restarts app when deletion is successful
                                    restartApp(activityContext)
                                })
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.teal_700),
                            ),
                        ){
                            Text(text = "Delete Account")
                        }

                        // If button is clicked changed logged in status to false
                        if(isLoggedIn){
                            Button(
                                onClick = {
                                    sharedPref.edit().clear().apply()
                                    auth.signOut()
                                    Toast.makeText(appContext, "Logged Out", Toast.LENGTH_SHORT).show()
                                    restartApp(activityContext)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.teal_700),
                                ),
                            ) {
                                Text(text = "Log Out")
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Restarts the app
 * @param activityContext The activity context.
 */
fun restartApp(activityContext: Context){
    val intent = Intent(activityContext, SignInActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    activityContext.startActivity(intent)

    if(activityContext is Activity){
        activityContext.finish()
    }
}
