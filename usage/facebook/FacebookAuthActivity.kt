package com.googleauth.facebook.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.google_auth.auth.FacebookAuth
import com.google_auth.bean.FacebookLoginResult
import com.google_auth.bean.FacebookProfileResult
import com.googleauth.R
import com.googleauth.databinding.ActivityFacebookAuthBinding


class FacebookAuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFacebookAuthBinding
    private val loginResultData = MutableLiveData<FacebookLoginResult>()
    private val accessTokenResultData = MutableLiveData<AccessToken>()
    private val profileResultData = MutableLiveData<FacebookProfileResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_facebook_auth)

        initListeners()
    }

    private fun initListeners() {

        binding.clFacebook.setOnClickListener {

            if (FacebookAuth.with().isUserSignIn()) {
                FacebookAuth.with().logout()
                binding.tvLogin.text = getString(R.string.continue_with_facebook)
            } else {
                Log.e("OPOT", "OPOT result = not sign")
                FacebookAuth.with(
                    this, loginResultData, mutableListOf(
                        "email",
                        "public_profile",
                        "user_hometown",
                        "user_birthday",
                        "user_age_range",
                        "user_gender",
                        "user_link"
                    )
                ).launch()
            }
        }

        accessTokenResultData.observe(this) { updatedTokenObject ->
            Log.e("OPOT", "accessTokenResultData = ${updatedTokenObject.token}")
        }

        loginResultData.observe(this) {

            if (it.isLogin) {

                binding.tvLogin.text = getString(R.string.logout)

                it.accessToken?.let { accessToken ->
                    FacebookAuth.with().getProfileData(accessToken,"id,name,link", profileResultData)
                }

                Toast.makeText(this,"Login successfully.",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this,"Please try again.",Toast.LENGTH_SHORT).show()
            }

        }

        profileResultData.observe(this) { profileResponse ->
            val profileJSONObject = profileResponse.responseJsonObject
            val error = profileResponse.error
        }


    }


}