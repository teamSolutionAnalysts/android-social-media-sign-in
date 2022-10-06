package com.googleauth.google.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google_auth.auth.GoogleAuth
import com.googleauth.R
import com.googleauth.databinding.ActivityMainBinding


class GoogleAuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initComponents()
        initListeners()
    }

    private fun initComponents() {
        if (GoogleAuth.with(this).isUserSignIn()) {
            updateUI(true)
            val account = GoogleAuth.with(activity = this).getSignInAccountInfo()
            account?.let { updateUserInfo(it) }
        } else {
            updateUI(false)
        }
    }

    private fun updateUI(isUserSignIn: Boolean) {
        binding.signInButton.visibility = if (isUserSignIn) View.GONE else View.VISIBLE
        binding.cardUserData.visibility = if (isUserSignIn) View.VISIBLE else View.GONE
        binding.cardLogout.visibility = if (isUserSignIn) View.VISIBLE else View.GONE
        binding.cardDisconnectAccount.visibility = if (isUserSignIn) View.VISIBLE else View.GONE
    }

    private fun initListeners() {

        binding.signInButton.setOnClickListener {

            GoogleAuth.with(activity = this).launch { intent ->
                startForSignInResult.launch(intent)
            }

        }

        binding.cardLogout.setOnClickListener {
            GoogleAuth.with(activity = this).logout()
            updateUI(false)
        }

        binding.cardDisconnectAccount.setOnClickListener {
            GoogleAuth.with(activity = this).revokeAccess()
            updateUI(false)
        }


    }

    private val startForSignInResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            try {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                Log.e("OPOT", "OPOT = ${account.displayName}")
                Toast.makeText(this, "Successfully sign in.", Toast.LENGTH_SHORT).show()
                updateUI(true)
                updateUserInfo(account)
            } catch (e: ApiException) {
                Log.e("OPOT", "OPOT = ${e.statusCode} ${e.status}")
                Toast.makeText(this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT)
                    .show()
            }

        }

    private fun updateUserInfo(account: GoogleSignInAccount) {

        Glide.with(this).load(account.photoUrl).into(binding.ivUser)

        binding.tvUser.text =
            "ID: ${account.id} \n\nName: ${account.displayName} \n\nEmail: ${account.email}"

    }


}