package com.example.flacd

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.flacd.screens.SignInScreen
import com.example.flacd.ui.theme.FLACdTheme

class SignInActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            FLACdTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // app context
                    val context: Context = applicationContext

                    // shared preferences
                    val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                    Log.d("sharedpref", "${sharedPref.getString("email", "")}, ${sharedPref.getString("password", "")}, ${sharedPref.getBoolean("isLoggedIn", false)}")

                    var loggedIn = sharedPref.getBoolean("isLoggedIn", false)

                    if(loggedIn){
                        var intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                    else{
                        SignInScreen(context = context, modifier = Modifier.padding(innerPadding))
                    }

                }
            }
        }
    }
}