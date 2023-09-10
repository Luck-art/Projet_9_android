package com.openclassrooms.realestatemanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.databinding.CreateAccountActivityBinding

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var binding: CreateAccountActivityBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CreateAccountActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.createAccountButton.setOnClickListener {
            val email = binding.createEmailEditText.text.toString()
            val password = binding.createPasswordEditText.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful)
                        else {
                        Snackbar.make(binding.createAccountButton, "Account creation failed.", Snackbar.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
