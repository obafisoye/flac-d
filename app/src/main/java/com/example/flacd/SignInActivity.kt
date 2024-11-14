package com.example.flacd

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.flacd.screens.SignInScreen
import com.example.flacd.screens.SplashScreen
import com.example.flacd.ui.theme.FLACdTheme

class SignInActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            FLACdTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // keep track of splash screen state
                    var showSplashScreen by remember { mutableStateOf(true) }

                    AnimatedVisibility(
                        visible = showSplashScreen,
                        exit = fadeOut(animationSpec = tween(durationMillis = 30000))
                    ) {
                        SplashScreen(
                            modifier = Modifier.padding(innerPadding),
                            onTimeout = { showSplashScreen = false })
                    }

                    // when splash screen start sign in logic
                    if(!showSplashScreen){

                        // app context
                        val context: Context = applicationContext

                        // shared preferences
                        val sharedPref =
                            context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                        Log.d(
                            "sharedpref",
                            "${
                                sharedPref.getString(
                                    "email",
                                    ""
                                )
                            }, ${
                                sharedPref.getString(
                                    "password",
                                    ""
                                )
                            }, ${sharedPref.getBoolean("isLoggedIn", false)}"
                        )

                        var loggedIn = sharedPref.getBoolean("isLoggedIn", false)

                        // if user is logged in direct to main activity, if user is not logged in direct to sign in screen
                        if (loggedIn) {
                            var intent = Intent(context, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(intent)
                        } else {
                            SignInScreen(
                                context = context,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}