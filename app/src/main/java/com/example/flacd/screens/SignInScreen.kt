package com.example.flacd.screens

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.EditText
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.flacd.MainActivity
import com.example.flacd.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import android.app.AlertDialog

/**
 * Screen for signing in users.
 * @param context The application context.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun SignInScreen(context: Context, modifier: Modifier = Modifier) {

    /**
     * A Firebase Authentication instance.
     */
    val auth = FirebaseAuth.getInstance()

    /**
     * The email of the user.
     */
    var email by remember { mutableStateOf("") }

    /**
     * The password of the user.
     */
    var password by remember { mutableStateOf("") }

    /**
     * A keyboard controller for hiding the keyboard.
     */
    val keyboardController = LocalSoftwareKeyboardController.current

    /**
     * The activity context.
     */
    val activityContext = LocalContext.current

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
                value = email,
                onValueChange = { email = it.trim() },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
            )

            TextField(
                value = password,
                onValueChange = { password = it.trim() },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
            )

            Button(
                onClick = {
                    if(email.isEmpty() || password.isEmpty()){
                        Toast.makeText(context, "Email and password are required", Toast.LENGTH_SHORT).show()
                    }else{
                        performSignIn(auth, email, password, context, keyboardController)
                    } },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Text("Sign In")
            }

            Button(
                onClick = { sendResetPasswordEmail(auth, activityContext) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Text("Forgotten?")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = "New user?", color = Color.White)

                Button(
                    onClick = {
                        // if register is clicked direct to register screen
                        val intent = Intent(context, RegisterActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Text("Register")
                }
            }

        }
    }
}

/**
 * Performs the sign in process.
 * @param email The email of the user.
 * @param password The password of the user.
 * @param context The application context.
 * @param keyboardController A keyboard controller for hiding the keyboard.
 */
fun performSignIn(auth: FirebaseAuth, email: String, password: String, context: Context, keyboardController: SoftwareKeyboardController?){
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                keyboardController?.hide()

                // Save email and password to shared preferences
                val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                sharedPref.edit()
                    .putString("email", email)
                    .putString("password", password)
                    .putString("userId", auth.currentUser?.uid)
                    .putBoolean("isLoggedIn", true)
                    .apply()

                Log.d("sharedpref", "${sharedPref.getString("email", "")}, ${sharedPref.getString("password", "")}, ${sharedPref.getString("userId", "")}, ${sharedPref.getBoolean("isLoggedIn", false)}")

                Toast.makeText(context, "Sign In Successful", Toast.LENGTH_SHORT).show()

                // Redirect to main activity after sign in is successful
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("userId", auth.currentUser?.uid)
                context.startActivity(intent)
            } else {
                keyboardController?.hide()
                Toast.makeText(context, "Sign In Failed", Toast.LENGTH_SHORT).show()
            }
            keyboardController?.hide()
        }
}

/**
 * Sends a password reset email to the user.
 * @param auth The Firebase Authentication instance.
 * @param activityContext The activity context.
 */
fun sendResetPasswordEmail(auth: FirebaseAuth, activityContext: Context){
    promptEmail(activityContext){ email ->
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(activityContext, "Password reset email sent", Toast.LENGTH_SHORT).show()
                }else{
                    val errorMessage = task.exception?.message ?: "Failed to send password reset email"
                    Toast.makeText(activityContext, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

}

/**
 * Prompts the user for an email to reset their password.
 * @param context The application context.
 * @param callback A callback function to be executed with the entered email.
 */
private fun promptEmail(context: Context, callback: (String) -> Unit){
    val inputField = EditText(context)
    inputField.hint = "Enter your email"
    inputField.inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

    AlertDialog.Builder(context)
        .setTitle("Reset Password")
        .setMessage("Please enter your email to reset your password")
        .setView(inputField)
        .setPositiveButton("Confirm") { _, _ ->
            val email = inputField.text.toString()

            if(email.isNotEmpty()){
                callback(email)
            }else{
                Toast.makeText(context, "Email is required", Toast.LENGTH_SHORT).show()
            }
        }
        .setNegativeButton("Cancel", null)
        .show()
}