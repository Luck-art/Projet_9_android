package com.openclassrooms.realestatemanager

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LogInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.log_in_activity)

        // Initialize Firebase Auth and GoogleSignInClient here
        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.app_name)) // app_name provisional
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

            callbackManager = CallbackManager.Factory.create()

        val googleLoginButton = findViewById<Button>(R.id.googleLoginButton)
        val facebookLoginButton = findViewById<Button>(R.id.facebookLoginButton)
        val emailLoginButton = findViewById<Button>(R.id.emailLoginButton)

        googleLoginButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        facebookLoginButton.setReadPermissions("email", "public_profile")
        facebookLoginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val credential = FacebookAuthProvider.getCredential(loginResult.accessToken.token)
                auth.signInWithCredential(credential)
            }

            override fun onCancel() {
                // Handle cancel case
            }

            override fun onError(error: FacebookException) {
                // Handle error case
            }
        })

        emailLoginButton.setOnClickListener {
            val email = // Get this from EditText
            val password = // Get this from EditText
                signInWithEmail(email, password)
        }
    }

    private fun signInWithEmail(email: String, password: String) {
        // Your existing implementation
    }
}

