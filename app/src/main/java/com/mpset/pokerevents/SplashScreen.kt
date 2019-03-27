package com.mpset.pokerevents

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import bolts.Bolts
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.mpset.pokerevents.Activities.Login
import org.json.JSONObject
import com.google.android.gms.common.util.IOUtils.toByteArray
import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import android.util.Base64
import android.util.Log
import com.mpset.pokerevents.Helper.DBHelper
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SplashScreen : AppCompatActivity() {
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds
    var  internetNotRespose:Boolean=false
    internal lateinit var dbHelper: DBHelper
//    private  lateinit  var features: ArrayList<String>()
    var features: ArrayList<String> = ArrayList()
    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            getvalues()

//            if(!internetNotRespose) {
//                if (allFeature.equals("false")) {
                    if (!userAccessToken.equals("") && !userAccessToken.equals("defaultName")) {
                        startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                        finish()
                    } else {
                        clearValues()
                        val intent = Intent(applicationContext, Login::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Toast.makeText(applicationContext, "This Application Is Not Allow In Your Country...", Toast.LENGTH_LONG).show()
                    finish()
                }
//            }else{
//                Toast.makeText(applicationContext, "Your Internet Is Not Responding...", Toast.LENGTH_LONG).show()
//                finish()
//            }
//        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar!!.hide()

        blackList()

    }

    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }

    fun getvalues(){
    val sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
    userAccessToken = sharedPreference.getString("token","defaultName")
    userNickName = sharedPreference.getString("nick_name","defaultName")
    userFirstName = sharedPreference.getString("first_name","defaultName")
    userLastName = sharedPreference.getString("last_name","defaultName")
    userAvatar = sharedPreference.getString("avatar","defaultName")
    userEmail = sharedPreference.getString("email","defaultName")
    userPhone = sharedPreference.getString("phone","defaultName")
    userGender = sharedPreference.getString("gender","defaultName")
    userId = sharedPreference.getString("id","defaultName")
    notificationStatus = sharedPreference.getString("not_status","defaultName")
}

    fun clearValues(){
        val sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString("token","")
        editor.putString("nick_name","")
        editor.putString("first_name","")
        editor.putString("last_name","")
        editor.putString("avatar","")
        editor.putString("email","")
        editor.putString("phone","")
        editor.putString("gender","")
        editor.commit()
    }
 companion object {
    lateinit var userAccessToken:String
    lateinit var userNickName:String
    lateinit var userFirstName:String
    lateinit var userLastName:String
    lateinit var userAvatar:String
    lateinit var userEmail:String
    lateinit var userPhone:String
    lateinit var userGender:String
    lateinit var userId:String
    lateinit var userAddress:String
    lateinit var allFeature:String
    lateinit var notificationStatus:String
 }
    fun blackList() {

        val URL = resources.getString(R.string.base_url) + "/blackLists"
        val queue = Volley.newRequestQueue(applicationContext)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            // Your success code here
            try {
                val jsonObject = JSONObject(s)
                val data  = jsonObject.getJSONObject("data")
                val jsonArray = data.getJSONArray("features")
                 allFeature = data.getString("all_features")
                for (i in 0 until jsonArray.length()){
                    val jsonObject = jsonArray.getString(i)
                    features.add(jsonObject)
                }
                mDelayHandler = Handler()

                //Navigate with delay
                mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "check your server or internet", Toast.LENGTH_LONG).show()
                internetNotRespose = true
            }
        }, Response.ErrorListener { e ->
            Toast.makeText(applicationContext, "check your server or internet", Toast.LENGTH_LONG).show()
            internetNotRespose = true
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json")
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }
}
