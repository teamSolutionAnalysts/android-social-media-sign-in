Social Media Sign In - Google, Facebook

Easy to use and configurable library for Google Sign In and Facebook Sign In.  It also allows some useful methods for easy code configuration.

Google Sign In

Usage

1. Gradle dependencies:

- Add below code in your app level "build.gradle" file.

dependencies {
    implementation project(path: ':social_media_auth')
}

- Put below code in your "settings.gradle" file

include ':social_media_auth'

2. Google Authentication

Now add below code for start google authentication.

In your activity or fragment xml file add below code for google button either create your own custom button.

<com.google.android.gms.common.SignInButton
    android:id="@+id/sign_in_button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:buttonSize="wide"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toBottomOf="@+id/tv_title">

 <ImageView
  android:layout_width="@dimen/_12sdp"
  android:layout_height="@dimen/_12sdp"
  android:layout_gravity="center_vertical"
  android:layout_marginStart="12dp"
  android:contentDescription="@string/clicked"
  app:srcCompat="@drawable/ic_google" />

</com.google.android.gms.common.SignInButton>

Put below code in your button click listener:

GoogleAuth.with(activity = this).launch { intent ->
    startForSignInResult.launch(intent)
}

3. Handling results

Handle authentication result

private val startForSignInResult =
registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

try { 
 
  val task: Task<GoogleSignInAccount> =
  GoogleSignIn.getSignedInAccountFromIntent(result.data)
  val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
              
  Toast.makeText(this, "Successfully sign in.", Toast.LENGTH_SHORT).show()
  // get user data from the  "account" object and update UI accordingly.

} catch (e: ApiException) {
  Toast.makeText(this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT) .show()
}

}

4. If we want to check if the user is already signed in or not then check using the below method.

if (GoogleAuth.with(this).isUserSignIn()) {
    // User is already sign in to the app.
} else { 
    // User is not sign in
}

5. Fetch user profile data after sign in or if the user is already signed in to the app.

val account = GoogleAuth.with(activity = requireActivity()).getSignInAccountInfo()

Get user data from the  "account" object and update the UI accordingly.

6. If we want to logout and revoke the access of the user use below code.

i.  GoogleAuth.with(this).logout()

ii.  GoogleAuth.with(this).revokeAccess()

All above codes are accessible in "fragment" and "activity". You have to pass the context accordingly.

Firebase Project Configuration

1. Go to the firebase console and add your project.
2. Now register your app into the project.
3. Create a SHA-1 key to add into the app.
   i.   Click on the gradle tab. Top right on the Android Studio.
   ii.  Now click on the gradle icon as seen in the picture.
   iii. Now you can see a new searchable window/screen will open.
   iv. Now type, "gradle signingreport" and press Enter to start generating SHA KEY
   v.  Above process will generate your SHA-1 key in "Build Output" as per your variant and you have to copy and paste into the firebase console.
   xi. Download the "google-services.json" and add it into your project.
4. Now Go to the "Authentication" tab in firebase console and click on "Sign-in method".
5. You can see a "Google" provider enable it and add your email in "Project support email". After that "Save" the configuration and run your project.

Facebook Sign in

Usage

1. Gradle dependencies:

- Add below code in your app level "build.gradle" file.

dependencies {
    implementation project(path: ':social_media_auth')
}

- Put below code in your "settings.gradle" file

include ':social_media_auth'

2. Facebook Authentication

Now add below code for start facebook authentication.

In your activity or fragment xml file add below code for facebook button either create your own custom button.

<androidx.constraintlayout.widget.ConstraintLayout
 android:id="@+id/cl_facebook"
 android:layout_width="wrap_content"
 android:layout_height="wrap_content"
 android:background="@color/color_facebook"
 android:paddingStart="@dimen/_8sdp"
 android:paddingTop="@dimen/_4sdp"
 android:paddingEnd="@dimen/_8sdp"
 android:paddingBottom="@dimen/_4sdp"
 app:layout_constraintBottom_toBottomOf="parent"
 app:layout_constraintEnd_toEndOf="parent"
 app:layout_constraintStart_toStartOf="parent"
 app:layout_constraintTop_toBottomOf="@+id/tv_title">

<ImageView
 android:id="@+id/iv_fb"
 android:layout_width="@dimen/_12sdp"
 android:layout_height="@dimen/_12sdp"
 android:contentDescription="@string/clicked"
 android:src="@drawable/ic_facebook"
 app:layout_constraintBottom_toBottomOf="parent"
 app:layout_constraintStart_toStartOf="parent"
 app:layout_constraintTop_toTopOf="parent" />

<TextView
 android:layout_width="wrap_content"
 android:layout_height="wrap_content"
 android:layout_marginStart="@dimen/_8sdp"
 android:text="@string/continue_with_facebook"
 android:textColor="@color/white"
 android:textStyle="bold"
 android:id="@+id/tv_login"
 app:layout_constraintBottom_toBottomOf="parent"
 app:layout_constraintEnd_toEndOf="parent"
 app:layout_constraintStart_toEndOf="@+id/iv_fb"
 app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>

Put below code in your button click listener:

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

3. Handling results

Now create an observer to receive the success and failure result of sign in. Here you can easily check if the user is successfully signed in with the "isLogin" parameter. Also you get the "Access Token", "Granted and Denied Permission List", "Authentication Token"  and "Error Message".

private val loginResultData = MutableLiveData<FacebookLoginResult>()

loginResultData.observe(this) {

 if (it.isLogin) {
    Toast.makeText(this,"Login successfully.",Toast.LENGTH_SHORT).show()
 } else {
    Toast.makeText(this,"Please try again.",Toast.LENGTH_SHORT).show()
 }

}

4. If we want to check if the user is already signed in or not then check using the below method.

if (FacebookAuth.with().isUserSignIn()) {
    // User is already sign in to the app.
} else {
    // User is not sign in
}

5. When you want to get a current access token put below code to get.

val currentAccessToken = FacebookAuth.with().getCurrentAccessToken()

6. If the user's access token is expired then you can get a new access token from the below method.

private val accessTokenResultData = MutableLiveData<AccessToken>()

FacebookAuth.with().getUpdatedAccessToken(accessTokenResultData)

accessTokenResultData.observe(this) { updatedTokenObject ->
  // Get new token from the "updatedTokenObject.token" and save in your accordingly.
}

7. For logout add below code.

FacebookAuth.with().logout()

8. Fetch user profile data after sign in or if the user is already signed in to the app.

private val profileResultData = MutableLiveData<FacebookProfileResult>()

FacebookAuth.with().getProfileData(accessToken,"id,name,link", profileResultData)

profileResultData.observe(this) { profileResponse ->

  val profileJSONObject = profileResponse.responseJsonObject
  val error = profileResponse.error
  // You have to check jsonobject and error code.
}

9. Open your strings.xml file. Add your "facebook_app_id", "facebook_client_token" and "fb_login_protocol_scheme" in you string.xml file and put related code in "AndroidManifest.xml" file

- strings.xml

  <string name="facebook_app_id">598719211690552</string>
  <string name="fb_login_protocol_scheme">fb598719211690552</string>
  <string name="facebook_client_token">cf74a14496908cfb8990f7c33f488bff</string>

- AndroidManifest.xml

<uses-permission android:name="com.google.android.gms.permission.AD_ID" tools:node="remove"/>

Add below code in between "<application>" tag

<meta-data android:name="com.facebook.sdk.ApplicationId" android:value="@string/facebook_app_id"/>
<meta-data android:name="com.facebook.sdk.ClientToken" android:value="@string/facebook_client_token"/>

<activity
    android:name="com.facebook.CustomTabActivity"
    android:exported="true">
    <intent-filter>
     <action android:name="android.intent.action.VIEW" />
     <category android:name="android.intent.category.DEFAULT" />
     <category android:name="android.intent.category.BROWSABLE" />
     <data android:scheme="@string/fb_login_protocol_scheme" />
    </intent-filter>
</activity>

All above codes are accessible in "fragment" and "activity". You have to pass the context accordingly.

Facebook Project Configuration

1. If you already have "App ID" and "Client Key" then add either following process for creating a key.
   i.   Login in to facebook to create an app.
   ii.  Now select the product of "Facebook Login".
   iii. Choose the "Android" platform and fill all the related details.
   iv. You will find "App Id" at the top of the dashboard. For "Client Key" you have to navigate to Settings > Advanced > Security > Client token.
2. Testing with test account and development mode.
   i. For adding a testing account you have to navigate to Roles > Test Users > Create Test Users.
3. Testing with live user and live mode
   i. You have to add a valid "privacy policy url" in Settings > Basic > Privacy Policy URL to go to the live mode.

For Check all the existing configuration go through using the below account in facebook.

Email Address: keyur.zala.sa@gmail.com
Password: keyur@1994









