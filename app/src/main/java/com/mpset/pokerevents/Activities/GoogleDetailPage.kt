package com.mpset.pokerevents.Activities

import android.app.PendingIntent.getActivity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.mpset.pokerevents.MainActivity
import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import com.mpset.pokerevents.SplashScreen.Companion.userAddress
import com.mpset.pokerevents.SplashScreen.Companion.userAvatar
import com.mpset.pokerevents.SplashScreen.Companion.userEmail
import com.mpset.pokerevents.SplashScreen.Companion.userFirstName
import com.mpset.pokerevents.SplashScreen.Companion.userGender
import com.mpset.pokerevents.SplashScreen.Companion.userId
import com.mpset.pokerevents.SplashScreen.Companion.userLastName
import com.mpset.pokerevents.SplashScreen.Companion.userNickName
import com.mpset.pokerevents.SplashScreen.Companion.userPhone
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_facebook_google_detail_page.*
import kotlinx.android.synthetic.main.activity_update_event.*
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class GoogleDetailPage : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(applicationContext, p0.errorMessage, Toast.LENGTH_LONG).show()
    }

    lateinit var gso: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleApiClient
    val RC_SIGN_IN: Int = 1
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var email: String
    lateinit var photoUrl: String
    lateinit var serverId: String
    lateinit var idGoogle: String
    lateinit var id: String
    lateinit var name: String
    lateinit var tokenAccess: String
    lateinit var nickName: String
    lateinit var phone: String
    var gender: String = "female"
    lateinit var address:String
    lateinit var dialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook_google_detail_page)
        supportActionBar!!.title = "More Details"
        dialog = ProgressDialog(this)

        val ss: String = intent.getStringExtra("id")
//        val serverClientId ="669317931495-en79bhq0ugoch00kteguf178bs4m70ep.apps.googleusercontent.com"
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestScopes( Scope(Scopes.DRIVE_APPFOLDER))
//                .requestServerAuthCode("516394471224-r7gu78u9u784tku02d9212fgm5gc4rda.apps.googleusercontent.com")
                .requestProfile()
                .requestEmail()
                .build()
//        mGoogleSignInClient =  GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build()
        mGoogleSignInClient = GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build()
        signIn()
//        Toast.makeText(applicationContext,ss,Toast.LENGTH_LONG).show()
//        if(ss.equals("1")){
//            image_logo_login.setBackgroundResource(R.drawable.facebookbtn)
//            image_logo_login.visibility = View.VISIBLE
//            text_title_fb_google.setText("Login With Facebook")
//        }
        if (ss.equals("2")) {
            image_logo_login.setBackgroundResource(R.drawable.googlebtn)
            image_logo_login.visibility = View.VISIBLE
            text_title_fb_google.setText("Login With Google")
            text_title_fb_google.setTextColor(Color.parseColor("#ff0000"))
        }


        male_google.setOnClickListener {
            male_google.setBackgroundResource(R.drawable.blackbar)
            female_google.setBackgroundResource(R.drawable.outlinebar)
            male_google.setTextColor(Color.parseColor("#ffffff"))
            female_google.setTextColor(Color.parseColor("#9f9f9e"))
            gender = "male"
        }
        female_google.setOnClickListener {
            male_google.setBackgroundResource(R.drawable.outlinebar)
            female_google.setBackgroundResource(R.drawable.blackbar)
            male_google.setTextColor(Color.parseColor("#9f9f9e"))
            female_google.setTextColor(Color.parseColor("#ffffff"))
            gender = "female"
        }
        btn_google_register.setOnClickListener {
            dialog.show()
            validation(it)
//            Auth.GoogleSignInApi.signOut(mGoogleSignInClient)
        }
    }

    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        Toast.makeText(this, "result", Toast.LENGTH_LONG).show()

        if (requestCode == RC_SIGN_IN) {
//            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            val result: GoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleResult(result)
        } else {
            Toast.makeText(this, "Problem in execution order :(", Toast.LENGTH_LONG).show()
        }
    }

    private fun handleResult(completedTask: GoogleSignInResult) {
//        try {
        if (completedTask.isSuccess) {
            val account: GoogleSignInAccount = completedTask.signInAccount!!
            updateUI(account)
        }
//        } catch (e: ApiException) {
//            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
//        }
    }

    private fun updateUI(account: GoogleSignInAccount): GoogleSignInAccount {
//        return account
//        val dispTxt = findViewById<View>(R.id.dispTxt) as TextView
//        val acct = GoogleSignIn.getLastSignedInAccount(this)
        text_title_fb_google.text = account.displayName
        name = account.givenName.toString()
        email = account.email.toString()
        idGoogle = account.id.toString()
//        serverCallRegister()
//        val url = "https://www.googleapis.com/plus/v1/people/"+id+"?fields=image&key=AIzaSyDX1zNf-ByFGw1nIivu3DdJZnZVlaCUSTc"

        photoUrl = account.photoUrl.toString()
        dialog.setMessage("Checking Your Availability...")
        dialog.show()

        Picasso.get().load(photoUrl).into(image_logo_login)
//        Toast.makeText(applicationContext,account.displayName.toString(),Toast.LENGTH_LONG).show()
//        Toast.makeText(applicationContext,account.givenName.toString(),Toast.LENGTH_LONG).show()
//        Toast.makeText(applicationContext,account.familyName.toString(),Toast.LENGTH_LONG).show()
//        Toast.makeText(applicationContext,photoUrl,Toast.LENGTH_LONG).show()
//        text_title_fb_google.setText("Login With" +account.displayName.toString())
        serverCallRegister()
//        val authCode = account.getServerAuthCode()
//        email = account.email.toString()
//        firstName = account.givenName.toString()
//        lastName = account.idToken.toString()
//        photoUrl = account.photoUrl.toString()

//        signOut.setOnClickListener {
//            view: View? ->  mGoogleSignInClient.signOut().addOnCompleteListener {
//            task: Task<Void> -> if (task.isSuccessful) {
//            dispTxt.text = " "
//            signOut.visibility = View.INVISIBLE
//            signOut.isClickable = false
//        }
//        }
//        }
        return account
    }

    fun serverCallRegister() {
//        email = edittext_email_reg.text.toString()
//        firstName = edittext_fname.text.toString()
//        lastName = edittext_lname.text.toString()

        val URL = resources.getString(R.string.base_url) + "/socialLogin"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.POST, URL, Response.Listener { s ->
            // Your success code here
            val json = JSONObject(s)
            tokenAccess = json.getString("access_token")
//            Toast.makeText(applicationContext, json.getString("access_token"), Toast.LENGTH_LONG).show()
//            val obj = JSONObject(s)
            serverCallCheckUserValidation(tokenAccess)
//            Toast.makeText(applicationContext, obj.getJSONObject("user").getString("id"), Toast.LENGTH_LONG).show()
        }, Response.ErrorListener { e ->
            // Your error code here
            Log.e("TAG", "Received an exception", e)
//            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json")

            override fun getParams(): Map<String, String> = mapOf(
                    "name" to name,
                    "email" to email,
                    "provider_name" to "Google"
                    , "provider_id" to idGoogle

            )
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)
    }

    fun validation(view: View) {

        if (edittext_nickname_google.text.isEmpty()) {
//             Snackbar.make(this@Register, "www.journaldev.com", Snackbar.LENGTH_LONG).show()
            Snackbar.make(view, "Please enter nickname", Snackbar.LENGTH_LONG).show()
            return
        }

        if (edittext_phone_google.text.isEmpty()) {
//             Snackbar.make(this@Register, "www.journaldev.com", Snackbar.LENGTH_LONG).show()
            Snackbar.make(view, "Please enter number", Snackbar.LENGTH_LONG).show()
            return
        }

        serverCallCheckUserData(tokenAccess)
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        val authcode = account?.serverAuthCode

//        Toast.makeText(applicationContext,account?.serverAuthCode.toString(),Toast.LENGTH_LONG).show()
//                    Auth.GoogleSignInApi.signOut(mGoogleSignInClient)

    }

    fun serverCallCheckUserValidation(tokenAccess: String) {

        val URL = resources.getString(R.string.base_url) + "/user/details"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            val json = JSONObject(s)
            val objectData = JSONObject(s).getJSONObject("data")
            id = objectData.getString("id")
            nickName = objectData.getString("nick_name")
            firstName = objectData.getString("first_name")
            lastName = objectData.getString("last_name")
            email = objectData.getString("email")
            photoUrl = objectData.getString("avatar")
            phone = objectData.getString("phone")
            gender = objectData.getString("gender")
            getUserDate()
            dialog.cancel()
            startActivity(Intent(this@GoogleDetailPage, MainActivity::class.java))
            finish()
//            Toast.makeText(applicationContxext, json.getString("access_token"), Toast.LENGTH_LONG).show()
//            val obj = JSONObject(s)
//            Toast.makeText(applicationContext, obj.getJSONObject("user").getString("id"), Toast.LENGTH_LONG).show()
        }, Response.ErrorListener { e ->
            // Your error code here
            dialog.cancel()
            Log.e("TAG", "Received an exception", e.cause)
            if (e.networkResponse.statusCode == 400) {
                Toast.makeText(applicationContext, "Please fill these information", Toast.LENGTH_LONG).show()
            }
//            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }

    fun serverCallCheckUserData(tokenAccess: String) {

        val URL = resources.getString(R.string.base_url) + "/user/details"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.POST, URL, Response.Listener { s ->
            // Your success code here
            val json = JSONObject(s)
            val objectData = json.getJSONObject("data")
            id = objectData.getString("id")
            nickName = objectData.getString("nick_name")
            firstName = objectData.getString("first_name")
            lastName = objectData.getString("last_name")
            email = objectData.getString("email")
            photoUrl = objectData.getString("avatar")
            phone = objectData.getString("phone")
            gender = objectData.getString("gender")
            address = objectData.getString("address")
            getUserDate()
//            Toast.makeText(applicationContext, s, Toast.LENGTH_LONG).show()
            dialog.cancel()
            startActivity(Intent(this@GoogleDetailPage, MainActivity::class.java))
            finish()

//            val obj = JSONObject(s)
//            Toast.makeText(applicationContext, obj.getJSONObject("user").getString("id"), Toast.LENGTH_LONG).show()
        }, Response.ErrorListener { e ->
            dialog.cancel()
            // Your error code here
            Log.e("TAG", "Received an exception", e)
            if (e.networkResponse.statusCode == 400) {

            }
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)

            override fun getParams(): Map<String, String> = mapOf(
                    "nick_name" to edittext_nickname_google.text.toString(),
                    "phone" to edittext_phone_google.text.toString(),
                    "avatar" to photoUrl,
                    "gender" to gender,
                    "address" to ""
            )
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }

    fun getUserDate() {
        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString("token", tokenAccess)
        editor.putString("id", id)
        editor.putString("nick_name", nickName)
        editor.putString("first_name", firstName)
        editor.putString("last_name", lastName)
        editor.putString("avatar", photoUrl)
        editor.putString("email", email)
        editor.putString("phone", phone)
        editor.putString("gender", gender)
//        editor.putString("address",address)
        editor.commit()
        userAccessToken = tokenAccess
        userId = id
        userNickName = nickName
        userFirstName = firstName
        userLastName = lastName
        userEmail = email
        userAvatar = photoUrl
        userPhone = phone
        userGender = gender
//        userAddress = address
    }
}
