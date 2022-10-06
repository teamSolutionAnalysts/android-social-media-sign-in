package com.google_auth.bean

import com.facebook.FacebookRequestError
import org.json.JSONObject

data class FacebookProfileResult(
    var responseJsonObject: JSONObject,
    var error: FacebookRequestError
)