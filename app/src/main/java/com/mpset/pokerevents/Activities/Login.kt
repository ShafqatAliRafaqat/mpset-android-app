package com.mpset.pokerevents.Activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
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

import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import org.json.JSONString
import android.R.attr.data
import com.android.volley.VolleyError
import com.mpset.pokerevents.SplashScreen.Companion.notificationStatus
import java.nio.charset.Charset


class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    lateinit var gender:String
    lateinit var token:String
    lateinit var id:String
    lateinit var nickName:String
    lateinit var firstName:String
    lateinit var lastName:String
    lateinit var email:String
    lateinit var avatar:String
    lateinit var phone:String
    lateinit var address:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()
        auth = FirebaseAuth.getInstance()
       window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)


        text_register.setOnClickListener {
            var intent = Intent(applicationContext, Register::class.java)
            startActivity(intent)
        }

        btn_signin.setOnClickListener {
            progresbar_login.visibility = View.VISIBLE
            serverCallLogin()
//                                        startActivity(Intent(applicationContext,MainActivity::class.java))

//            if(check_remember.isChecked){
//                val sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
//                var editor = sharedPreference.edit()
//                editor.putString("username",edittext_user.text.toString())
//                editor.putString("password",edittext_pass.text.toString())
//                editor.commit()
//                Toast.makeText(applicationContext,"Yes remember",Toast.LENGTH_LONG).show()
//            }
//
//
//            auth.signInWithEmailAndPassword(edittext_user.text.toString(), edittext_pass.text.toString())
//                    .addOnCompleteListener(this) { task ->
//                        if (task.isSuccessful) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Toast.makeText(baseContext, "Authentication Success.",
//                                    Toast.LENGTH_SHORT).show()
//                            val user = auth.currentUser
//                        } else {
//                            // If sign in fails, display a message to the user.
//
//                            Toast.makeText(baseContext, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show()
//                        }
//                    }
        }

        forgot_password.setOnClickListener {
//            val auth = FirebaseAuth.getInstance()
////            val emailAddress = "m.faisal2745@gmail.com"
////
////            auth.sendPasswordResetEmail(emailAddress)
////                    .addOnCompleteListener { task ->
////                        if (task.isSuccessful) {
////                          Toast.makeText(applicationContext, "Email sent.",Toast.LENGTH_LONG).show()
////                        }
////                    }
            startActivity(Intent(this@Login, ForgotPassword::class.java))

        }
        btn_facebook_signin.setOnClickListener {
            val intent = Intent(this@Login,FacebookDetailPage::class.java)
//            intent.putExtra("id","1")
            startActivity(intent)
//            startActivity(Intent(this@Login, GoogleDetailPage::class.java))
        }
        btn_google_signin.setOnClickListener {
            val intent = Intent(this@Login,GoogleDetailPage::class.java)
            intent.putExtra("id","2")
            startActivity(intent)
        }

    }
    fun   serverCallLogin(){
        if(edittext_user.text.isEmpty()){
            Toast.makeText(applicationContext,"Please enter email",Toast.LENGTH_LONG).show()
            return
        }
        if(edittext_pass.text.isEmpty()){
            Toast.makeText(applicationContext,"Please enter password",Toast.LENGTH_LONG).show()
            return
        }
        val URL = resources.getString(R.string.base_url) +"/login"
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(Request.Method.POST, URL, Response.Listener { s ->
            // Your success code here
            Toast.makeText(applicationContext,"Login Successfully",Toast.LENGTH_LONG).show()
            val obj = JSONObject(s)
            val userObject = obj.getJSONObject("user")
             token = obj.getJSONObject("auth").getString("access_token")
             id = userObject.getString("id")
             nickName = userObject.getString("nick_name")
             firstName = userObject.getString("first_name")
             lastName = userObject.getString("last_name")
             email = userObject.getString("email")
             avatar = userObject.getString("avatar")
             phone = userObject.getString("phone")
             gender = userObject.getString("gender")
             address = userObject.getString("address")
             notificationStatus = userObject.getString("notification_status")

//            Toast.makeText(applicationContext, obj.getJSONObject("user").getString("id"), Toast.LENGTH_LONG).show()
            saveUserToShareprefrences()
            progresbar_login.visibility = View.GONE
//            Toast.makeText(applicationContext, obj.toString(), Toast.LENGTH_LONG).show()
            startActivity(Intent(this@Login,MainActivity::class.java))

        }, Response.ErrorListener { e ->
            // Your error code here
            progresbar_login.visibility = View.GONE
//            if(e.networkResponse.statusCode==400){
//                val vari =
//            Log.e("sss", e.networkResponse.headers.get("Content-Length"))
//            val jsonstring = String(e.message)
//            val dataJson  = JSONObject(jsonstring)
//            val value = dataJson.getString("message")
                Toast.makeText(applicationContext,e.message ,Toast.LENGTH_LONG).show()
//            }
        }) {
            override fun getParams(): Map<String, String> = mapOf("email" to edittext_user.text.toString(),"password" to edittext_pass.text.toString() )
//            override fun parseNetworkError(volleyError: VolleyError?): VolleyError {
////
////                    val error = VolleyError(String(volleyError!!.networkResponse.data))
//////                                 Log.e("sss",volleyError.message)
//                return super.parseNetworkError(volleyError)
//            }
        }

        queue.add(stringRequest)
    }

    private  fun saveUserToShareprefrences( ){
        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString("token",token)
        editor.putString("id",id)
        editor.putString("nick_name",nickName)
        editor.putString("first_name",firstName)
        editor.putString("last_name",lastName)
        editor.putString("avatar",avatar)
        editor.putString("email",email)
        editor.putString("phone",phone)
        editor.putString("gender",gender)
        editor.putString("address",address)
        editor.putString("not_status",notificationStatus)
        editor.commit()
        userAccessToken = token
        userId = id
        userNickName = nickName
        userFirstName = firstName
        userLastName = lastName
        userEmail = email
        userAvatar = avatar
        userPhone = phone
        userGender = gender
        userAddress = address

    }


}
