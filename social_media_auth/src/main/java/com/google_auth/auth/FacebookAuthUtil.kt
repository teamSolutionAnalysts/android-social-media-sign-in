package com.google_auth.auth

import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.lifecycle.MutableLiveData
import com.facebook.*
import com.facebook.CallbackManager.Factory.create
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google_auth.bean.FacebookLoginResult
import com.google_auth.bean.FacebookProfileResult


open class FacebookAuth {

    companion object {
        fun with(
            activity: ActivityResultRegistryOwner,
            loginResultData: MutableLiveData<FacebookLoginResult>?,
            permissions: MutableList<String>
        ): Builder {
            return Builder(activity, loginResultData, permissions)
        }

        fun with(): BuilderUtil {
            return BuilderUtil()
        }
    }

    class BuilderUtil {

        var accessTokenTracker: AccessTokenTracker? = null

        fun isUserSignIn(): Boolean {
            val accessToken = AccessToken.getCurrentAccessToken()
            return accessToken != null && !accessToken.isExpired
        }

        fun getUpdatedAccessToken(accessTokenResultData: MutableLiveData<AccessToken>) {
            accessTokenTracker = object : AccessTokenTracker() {
                override fun onCurrentAccessTokenChanged(
                    oldAccessToken: AccessToken?,
                    currentAccessToken: AccessToken?
                ) {
                    accessTokenResultData.postValue(currentAccessToken)
                    accessTokenTracker?.stopTracking()
                }
            }
        }

        fun getCurrentAccessToken(): AccessToken? {
            return AccessToken.getCurrentAccessToken()
        }

        fun logout() {
            LoginManager.getInstance().logOut()
        }

        fun getProfileData(
            accessToken: AccessToken,
            fieldParam: String,
            profileResultData: MutableLiveData<FacebookProfileResult>
        ) {

            if (fieldParam.isNotEmpty()) {
                val parameters = Bundle()
                val request = GraphRequest.newMeRequest(accessToken) { profile_data, response ->
                    response?.error?.let {
                        profileResultData.value?.error = it
                    }

                    profile_data?.let {
                        profileResultData.value?.responseJsonObject = it
                    }
                    profileResultData.value = profileResultData.value
                }

                parameters.putString("fields", fieldParam)
                request.parameters = parameters
                request.executeAsync()
            } else {
                profileResultData.value = profileResultData.value
            }
        }
    }

    class Builder(
        private val activity: ActivityResultRegistryOwner,
        val observerResult: MutableLiveData<FacebookLoginResult>?,
        private val permissions: MutableList<String>
    ) {

        private val callbackManager = create()

        fun launch() {

            LoginManager.getInstance().logInWithReadPermissions(
                activity,
                callbackManager,
                permissions
            )

            LoginManager.getInstance()
                .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

                    override fun onSuccess(result: LoginResult) {
                        observerResult?.value = FacebookLoginResult(
                            true,
                            result.accessToken,
                            "",
                            result.authenticationToken,
                            result.recentlyGrantedPermissions,
                            result.recentlyDeniedPermissions
                        )
                        observerResult?.value = observerResult?.value
                    }

                    override fun onCancel() {
                        observerResult?.value =
                            FacebookLoginResult(
                                false,
                                null,
                                "Operation canceled by user",
                                null,
                                null,
                                null
                            )

                        observerResult?.value = observerResult?.value
                    }

                    override fun onError(error: FacebookException) {
                        observerResult?.value = error.message?.let {
                            FacebookLoginResult(
                                false,
                                null, it,
                                null,
                                null,
                                null
                            )
                        }
                        observerResult?.value = observerResult?.value
                    }

                })


        }

    }


}
