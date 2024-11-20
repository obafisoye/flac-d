package com.example.flacd.screens

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.flacd.MainActivity
import com.example.flacd.RegisterActivity
import com.example.flacd.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RegisterScreen(context: Context, modifier: Modifier = Modifier){

    var username by remember { mutableStateOf("")}
    var email by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}
    val keyboardController = LocalSoftwareKeyboardController.current

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.DarkGray),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                label = { Text("Username") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
            )

            TextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
            )

            Button(
                onClick = {
                    registerUser(username, email, password, context, keyboardController, auth, firestore)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Register")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Already have an account?", color = Color.White)
                
                Button(
                    onClick = {
                        val intent = Intent(context, SignInActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text("Sign In")
                }
            }

        }
    }
}

fun registerUser(username: String,email: String, password: String, context: Context, keyboardController: SoftwareKeyboardController?, auth: FirebaseAuth, firestore: FirebaseFirestore) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                keyboardController?.hide()
                // save email and password to shared preferences
                val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                sharedPref.edit()
                    .putString("email", email)
                    .putString("password", password)
                    .putBoolean("isLoggedIn", true)
                    .apply()

                val user = auth.currentUser
                user?.let{
                    val userId = it.uid
                    saveUser(username, userId, firestore)
                }
                Log.d("sharedpref", "${sharedPref.getString("email", "")}, ${sharedPref.getString("password", "")}, ${sharedPref.getBoolean("isLoggedIn", false)}")

                Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()

                var intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("userId", auth.currentUser?.uid)
                context.startActivity(intent)

            } else {
                keyboardController?.hide()
                Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show()
            }
        }
}

fun saveUser(username: String, userId: String, firestore: FirebaseFirestore){

    val user = hashMapOf(
        "username" to username
    )

    firestore.collection("users")
        .document(userId)
        .set(user)
        .addOnSuccessListener {
            Log.d("firestore", "User saved successfully")
        }
        .addOnFailureListener{
            Log.d("firestore", "Error saving user")
        }
}


