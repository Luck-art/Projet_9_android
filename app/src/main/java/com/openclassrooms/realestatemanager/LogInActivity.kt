package com.openclassrooms.realestatemanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.estate_manager.RealEstateManagerActivity


class LogInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LogInActivity", "Before setContentView")
        setContentView(R.layout.log_in_activity)
        Log.d("LogInActivity", "After setContentView")


        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("426097529879-2ln72eo1n4g4ajuppmufojdahmhn8tbm.apps.googleusercontent.com") // Remplacez "YOUR_CLIENT_ID_HERE" par votre ID client OAuth 2.0
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)


        callbackManager = CallbackManager.Factory.create()

        val googleSignInButton = findViewById<SignInButton>(R.id.googleSignInButton)
        val facebookLoginButton = findViewById<LoginButton>(R.id.facebookLoginButton)
        val emailLoginButton = findViewById<Button>(R.id.emailLoginButton)


        if (googleSignInButton != null) {
            googleSignInButton.setOnClickListener {
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        } else {
            Log.e("LogInActivity", "googleSignInButton is null")
        }


        facebookLoginButton.setReadPermissions("email", "public_profile")
        FacebookSdk.setApplicationId("1231913224148121");
        FacebookSdk.sdkInitialize(getApplicationContext());
        facebookLoginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val credential = FacebookAuthProvider.getCredential(loginResult.accessToken.token)
                auth.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this@LogInActivity, RealEstateManagerActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

            override fun onCancel() {
                Snackbar.make(
                    findViewById(R.id.logIn),
                    "Facebook sign in cancelled.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }


            override fun onError(error: FacebookException) {
                Snackbar.make(
                    findViewById(R.id.logIn),
                    "Facebook sign in failed.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

        })

        // Email & password

        emailLoginButton.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.sign_in_mail_pop_up, null)

            val emailEditText = dialogView.findViewById<EditText>(R.id.emailEditText)
            val passwordEditText = dialogView.findViewById<EditText>(R.id.passwordEditText)

            val alertDialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("Sign in with Email")
                .setPositiveButton("Sign In") { dialog, which ->
                    val email = emailEditText.text.toString()
                    val password = passwordEditText.text.toString()

                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val intent = Intent(this@LogInActivity, RealEstateManagerActivity::class.java)
                                startActivity(intent)
                            } else {
                                Snackbar.make(
                                    findViewById(R.id.logIn),
                                    "Authentication Failed.",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
                .setNegativeButton("Cancel", null)
                .create()

            alertDialog.show()
        }

        val createAccountButton = findViewById<Button>(R.id.createAccountButton)
        createAccountButton.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)

        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = task.getResult(ApiException::class.java)

        if (requestCode == RC_SIGN_IN) {
            try {
                val intent = Intent(this, RealEstateManagerActivity::class.java)
                startActivity(intent)
            } catch (e: ApiException) {
                Log.w("LogInActivity", "Google sign in failed", e)
            }
        }
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val intent = Intent(this@LogInActivity, RealEstateManagerActivity::class.java)
                startActivity(intent)
            } else {
                Snackbar.make(
                    findViewById(R.id.logIn),
                    "Authentication Failed.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    }
}

