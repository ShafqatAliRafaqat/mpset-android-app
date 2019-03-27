package com.mpset.pokerevents.Activities

import android.app.ProgressDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mpset.pokerevents.R
import com.google.android.gms.common.util.IOUtils.toByteArray
import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.view.View
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.facebook.GraphResponse
import org.json.JSONObject
import com.facebook.GraphRequest
import com.mpset.pokerevents.MainActivity
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
import kotlinx.android.synthetic.main.activity_facebook_detail_page.*
import kotlinx.android.synthetic.main.activity_facebook_google_detail_page.*
import java.net.URL


class FacebookDetailPage : AppCompatActivity() {
   private var callbackManager:CallbackManager?= null
    var name:String = ""
    var id:String = ""
    var email:String = ""
    var accessToken:String = ""
    var genderInt:String = "female"
    var nickName:String = "female"
    var phone:String = "female"
    lateinit var address:String
    lateinit var dialog:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook_detail_page)
        supportActionBar!!.title = "More Details"
        dialog =  ProgressDialog(this)
        male_facebook.setOnClickListener {
            male_facebook.setBackgroundResource(R.drawable.blackbar)
            female_facebook.setBackgroundResource(R.drawable.outlinebar)
            male_facebook.setTextColor(Color.parseColor("#ffffff"))
            female_facebook.setTextColor(Color.parseColor("#9f9f9e"))
            genderInt = "male"
        }
        female_facebook.setOnClickListener {
            male_facebook.setBackgroundResource(R.drawable.outlinebar)
            female_facebook.setBackgroundResource(R.drawable.blackbar)
            male_facebook.setTextColor(Color.parseColor("#9f9f9e"))
            female_facebook.setTextColor(Color.parseColor("#ffffff"))
            genderInt = "female"
        }

        btn_facebook_register.setOnClickListener {
            dialog.show()
            serverCallCheckUserData(accessToken)
        }

        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(    this, Arrays.asList("public_profile","email"))
        LoginManager.getInstance().registerCallback(callbackManager,object :FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
//                Toast.makeText(applicationContext,result?.accessToken?.userId.toString(),Toast.LENGTH_LONG).show()
                val request = GraphRequest.newMeRequest(
                        result?.accessToken
                ) { `object`, response ->
                    // Application code
                    val json = response.jsonObject
                    name = json.getString("name")
                    id = json.getString("id")
                    email = json.getString("email")
                    dialog.setMessage("Checking Your Availability...")
                    dialog.show()
//                    Toast.makeText(applicationContext,name + id +email,Toast.LENGTH_LONG).show()
                    serverCallRegister()
                }
                val parameters = Bundle()
                parameters.putString("fields", "id,name,email")
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException?) {
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
    fun serverCallRegister() {
//        email = edittext_email_reg.text.toString()
//        firstName = edittext_fname.text.toString()
//        lastName = edittext_lname.text.toString()

        val URL = resources.getString(R.string.base_url) +"/socialLogin"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.POST, URL, Response.Listener { s ->
            // Your success code here
            val json = JSONObject(s)
            accessToken = json.getString("access_token")
            serverCallCheckUserValidation(accessToken)
        }, Response.ErrorListener { e ->
            // Your error code here
            Log.e("TAG", "Received an exception", e)
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json")

            override fun getParams(): Map<String, String> = mapOf(
                    "name" to name,
                    "email" to email,
                    "provider_name" to "Facebook"
                    , "provider_id" to id
            )
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)
    }

    fun validation(view: View) {

        if (edittext_nickname_google.text.isEmpty()) {
            Snackbar.make(view, "Please enter nickname", Snackbar.LENGTH_LONG).show()
            return
        }

        if (edittext_phone_google.text.isEmpty()) {
            Snackbar.make(view, "Please enter number", Snackbar.LENGTH_LONG).show()
            return
        }


    }
    fun serverCallCheckUserValidation(tokenAccess:String) {

        val URL = resources.getString(R.string.base_url) +"/user/details"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            val json = JSONObject(s)
            val objectData = JSONObject(s).getJSONObject("data")
//            id = objectData.getString("id")
//            nickName = objectData.getString("nick_name")
//            firstName = objectData.getString("first_name")
//            lastName = objectData.getString("last_name")
//            email = objectData.getString("email")
//            photoUrl = objectData.getString("avatar")
//            phone = objectData.getString("phone")
//            gender = objectData.getString("gender")
//            getUserDate()
                dialog.cancel()
                startActivity(Intent(this@FacebookDetailPage,MainActivity::class.java))
                finish()

//            Toast.makeText(applicationContext, obj.getJSONObject("user").getString("id"), Toast.LENGTH_LONG).show()
        }, Response.ErrorListener { e ->
            // Your error code here
            dialog.cancel()
            Log.e("TAG", "Received an exception", e)
//            serverCallCheckUserData(accessToken)
            if(e.networkResponse.statusCode==400){

//                startActivity(Intent(this@FacebookDetailPage,FacebookDetailPage::class.java))
                Toast.makeText(applicationContext, "Please fill these information", Toast.LENGTH_LONG).show()
            }
//            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json" , "Authorization" to "Bearer " +tokenAccess)
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }
    fun serverCallCheckUserData(tokenAccess:String) {

        val URL = resources.getString(R.string.base_url) +"/user/details"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.POST, URL, Response.Listener { s ->
            // Your success code here
            val json = JSONObject(s)
            val objectData = JSONObject(s).getJSONObject("data")
//            id = objectData.getString("id")
//            nickName = objectData.getString("nick_name")
//            firstName = objectData.getString("first_name")
//            lastName = objectData.getString("last_name")
//            email = objectData.getString("email")
//            photoUrl = objectData.getString("avatar")
//            phone = objectData.getString("phone")
//            gender = objectData.getString("gender")
//            address = userObject.getString("address")
//            getUserDate()
//            Toast.makeText(applicationContext, s, Toast.LENGTH_LONG).show()
            dialog.cancel()
            startActivity(Intent(this@FacebookDetailPage,MainActivity::class.java))
             finish()
//            val obj = JSONObject(s)
//            Toast.makeText(applicationContext, obj.getJSONObject("user").getString("id"), Toast.LENGTH_LONG).show()
        }, Response.ErrorListener { e ->
            dialog.cancel()
            // Your error code here
//            Log.e("TAG", "Received an exception", e)
//            if(e.networkResponse.statusCode==400){
//          Toast.makeText(applicationContext,"Please fill your information",Toast.LENGTH_LONG).show()
//            }
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json" , "Authorization" to "Bearer " +tokenAccess)

            override fun getParams(): Map<String, String> = mapOf(
                    "nick_name" to edittext_nickname_facebook.text.toString(),
                    "phone" to edittext_phone_facebook.text.toString(),
                    "avatar" to "https://graph.facebook.com/" + id + "/picture?type=large",
                    "gender" to genderInt,
                    "address" to ""
            )
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }
    fun getUserDate() {
        val url = "https://graph.facebook.com/" + id + "/picture?type=large"
        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString("token", accessToken)
        editor.putString("id", id)
        editor.putString("nick_name", nickName)
        editor.putString("first_name", name)
        editor.putString("last_name", name)
        editor.putString("avatar", url )
        editor.putString("email", email)
        editor.putString("phone", phone)
        editor.putString("gender", genderInt)
        editor.putString("address",address)
        editor.commit()
        userAccessToken = accessToken
        userId = id
        userNickName = nickName
        userFirstName = name
        userLastName = name
        userEmail = email
        userAvatar = url
        userPhone = phone
        userGender = genderInt
        userAddress = address
    }

}
