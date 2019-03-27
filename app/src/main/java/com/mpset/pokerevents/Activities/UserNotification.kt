package com.mpset.pokerevents.Activities

import android.app.DownloadManager
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mpset.pokerevents.Adapters.HeaderListAdapter
import com.mpset.pokerevents.Adapters.MyPagerAdapter
import com.mpset.pokerevents.Adapters.MyPagerAdapterJoinEvent
import com.mpset.pokerevents.Adapters.NotificationListAdapter
import com.mpset.pokerevents.MainActivity
import com.mpset.pokerevents.Model.HeaderModel
import com.mpset.pokerevents.Model.NotificationModel
import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen.Companion.notificationStatus
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import kotlinx.android.synthetic.main.activity_edit_location.*
import kotlinx.android.synthetic.main.activity_join_event.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.content_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

class UserNotification : AppCompatActivity() {
    val usersNotification = ArrayList<NotificationModel>()
    lateinit var adapterNotification:NotificationListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        supportActionBar!!.title = "Notifications"
        if(notificationStatus==null || notificationStatus.equals("") || notificationStatus.equals("null"))
            user_notification.isChecked = false
        else
            user_notification.isChecked = true
        recycler_notification.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        getNotifications(userAccessToken)
        user_notification.setOnCheckedChangeListener { buttonView, isChecked ->
            serverCallLogin(isChecked, userAccessToken)
        }

        adapterNotification = NotificationListAdapter(applicationContext,usersNotification, object : NotificationListAdapter.MyClickListener {
            override fun onClick(position: Int) {
//                Toast.makeText(applicationContext,"ssss", Toast.LENGTH_LONG).show()
            }
            override fun onLongClick(position: Int) {
//                Toast.makeText(applicationContext, "You got a long click it", Toast.LENGTH_LONG).show()
            }
        })
        recycler_notification.adapter = adapterNotification
    }


    fun getNotifications(tokenAccess: String) {

        val URL = resources.getString(R.string.base_url) + "/user/notifications"
        val queue = Volley.newRequestQueue(applicationContext)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            // Your success code here
            try {
                val jsonObject = JSONObject(s)
                val data = jsonObject.getJSONArray("data")
                for (i in 0 until data.length()){
                    val jsonObject = data.getJSONObject(i)
                    val title = jsonObject.getString("title")
                    val body = jsonObject.getString("body")
                    val time = jsonObject.getString("created_at")
                    usersNotification.add(NotificationModel(title, time,body))
                }
//                val
//                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()

            } catch (e: Exception) {
                Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
            }
        }, Response.ErrorListener { e ->
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)

        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }
    fun   serverCallLogin(isCheked:Boolean, tokenAccess: String){

        val params = JSONObject()
        params.put("notification_status", isCheked)

        val URL = resources.getString(R.string.base_url) +"/user/settings"

        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : JsonObjectRequest(Request.Method.PATCH, URL, params, Response.Listener { s ->
            if (isCheked == true)
            Toast.makeText(applicationContext,"Notification On Successfully",Toast.LENGTH_LONG).show()
            else
                Toast.makeText(applicationContext,"Notification Off Successfully",Toast.LENGTH_LONG).show()

        }, Response.ErrorListener { e ->
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json" , "Authorization" to "Bearer " + tokenAccess)
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)
    }
}
