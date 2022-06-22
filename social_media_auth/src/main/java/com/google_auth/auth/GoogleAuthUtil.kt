package com.google_auth.auth

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


open class GoogleAuth {

    companion object {
        const val GOOGLE_AUTH_REQUEST_CODE = 100

        fun with(activity: Activity): Builder {
            return Builder(activity)
        }
    }

    class Builder(private val activity: Activity) {

        private val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        private val googleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions)

        fun launch(onResult: (Intent) -> Unit) {
            onResult(googleSignInClient.signInIntent)
            activity.startActivityForResult(
                googleSignInClient.signInIntent,
                GOOGLE_AUTH_REQUEST_CODE
            )
        }

        fun isUserSignIn(): Boolean {
            val account = GoogleSignIn.getLastSignedInAccount(activity)
            return account != null
        }

        fun getSignInAccountInfo(): GoogleSignInAccount? {
            return GoogleSignIn.getLastSignedInAccount(activity)
        }

        fun logout() {
            googleSignInClient.signOut()
                .addOnCompleteListener(activity) {

                }
        }

        fun revokeAccess() {
            googleSignInClient.revokeAccess()
                .addOnCompleteListener(activity) {

                }
        }

    }

}


