package com.charityright.bd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.charityright.bd.databinding.ActivityAuthenticateMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.lang.Exception

class AuthenticateMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticateMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticateMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findNavController(R.id.fragment)

    }
}