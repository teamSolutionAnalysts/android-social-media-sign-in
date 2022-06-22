package com.google_auth.bean

import com.facebook.AccessToken
import com.facebook.AuthenticationToken

data class FacebookLoginResult(
    var isLogin: Boolean=false,
    var accessToken: AccessToken?,
    var errorMessage:String,
    val authenticationToken: AuthenticationToken? = null,
    val recentlyGrantedPermissions: Set<String>?=null,
    val recentlyDeniedPermissions: Set<String>?=null
)