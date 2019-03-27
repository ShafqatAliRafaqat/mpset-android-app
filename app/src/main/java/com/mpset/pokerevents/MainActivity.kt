package com.mpset.pokerevents

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessaging
import com.mpset.pokerevents.Activities.*
import com.mpset.pokerevents.Adapters.HeaderListAdapter
import com.mpset.pokerevents.Adapters.MyPagerAdapter
import com.mpset.pokerevents.ContactDetails.About
import com.mpset.pokerevents.ContactDetails.TermsAndConditions
import com.mpset.pokerevents.Model.HeaderModel
import com.mpset.pokerevents.SplashScreen.Companion.userAvatar
import com.mpset.pokerevents.SplashScreen.Companion.userNickName
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import java.util.ArrayList
import com.google.firebase.iid.InstanceIdResult
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId
import com.mpset.pokerevents.Activities.Chat.Companion.getCurrentTime
import com.mpset.pokerevents.ContactDetails.PrivacyPolicy
import com.mpset.pokerevents.Helper.DBHelper
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import com.mpset.pokerevents.SplashScreen.Companion.userId
import org.json.JSONObject


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var newToken = ""
    lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbHelper = DBHelper(this)
        if (dbHelper.checkIsUserAlreadyInDBorNot(userId.toInt()))

        else
            dbHelper.insertDataUser(userId.toInt(), userNickName, userAvatar, getCurrentTime())




        setSupportActionBar(toolbar)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        val hView = nav_view.getHeaderView(0)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this@MainActivity, OnSuccessListener<InstanceIdResult> { instanceIdResult ->
            newToken = instanceIdResult.token
//            Toast.makeText(applicationContext,newToken,Toast.LENGTH_LONG).show()
            servertokenUpdate(userAccessToken,newToken)
//            Log.e("newToken", newToken)
        })
//        Toast.makeText(applicationContext,newToken,Toast.LENGTH_LONG).show()

        Glide.with(applicationContext).load(userAvatar).into(profile_pic)
        Glide.with(applicationContext).load(userAvatar).into(hView.imageView_nav_profile)
        nick_name_main.text = userNickName
        val imageviewNavProfile = findViewById<ImageView>(R.id.profile_pic)
        imageviewNavProfile.setOnClickListener {
            startActivity(Intent(this@MainActivity, MyProfile::class.java))
        }
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Sn ackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        viewpager_main_slider.adapter = fragmentAdapter

        tabs_main.setupWithViewPager(viewpager_main_slider)

        ///////////////////////////////////////////
        val recyclerview = findViewById(R.id.recycler) as RecyclerView

        recyclerview.layoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false) as RecyclerView.LayoutManager?

        val users = ArrayList<HeaderModel>()
        users.add(HeaderModel("New Event", R.drawable.neweventn))
        users.add(HeaderModel("Join Event", R.drawable.joineventn))
        users.add(HeaderModel("My Friends", R.drawable.myfriendsn))
        users.add(HeaderModel("Chat", R.drawable.chatn))
        users.add(HeaderModel("Notifications", R.drawable.notificationsn))


        val adapter = HeaderListAdapter(applicationContext, users, object : HeaderListAdapter.MyClickListener {
            override fun onClick(position: Int) {
                Toast.makeText(applicationContext, users[position].eventName, Toast.LENGTH_LONG).show()

                if (users[position].eventName.equals("Notifications")) {
                    val intent = Intent(applicationContext, UserNotification::class.java)
//                intent.putExtra("event_id", users[position].id)
                    startActivity(intent)
                }
                if (users[position].eventName.equals("Join Event")) {

                    val intent = Intent(applicationContext, JoinEvent::class.java)
                    startActivity(intent)
                }
                if (users[position].eventName.equals("My Friends")) {

                    val intent = Intent(applicationContext, MyFriends::class.java)
                    startActivity(intent)
                }
                if (users[position].eventName.equals("New Event")) {

                    val intent = Intent(applicationContext, NewEventCashTour::class.java)
                    startActivity(intent)
                }
                if (users[position].eventName.equals("Chat")) {

                    val intent = Intent(applicationContext, Inbox::class.java)
                    startActivity(intent)
                }


            }

            override fun onLongClick(position: Int) {
                Toast.makeText(applicationContext, "You got a long click it", Toast.LENGTH_LONG).show()
            }

        })
        recyclerview.adapter = adapter
        ///////////////////////////////////////////////////
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.filtermenu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        when (item.itemId) {
//            R.id.action_filter ->{
//                startActivity(Intent(this@MainActivity,FilterPage::class.java))
//                return true}
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_lobby -> {
                // Handle the camera action
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
            R.id.nav_terms -> {
                // Handle the camera action
                startActivity(Intent(applicationContext, TermsAndConditions::class.java))
            }
            R.id.nav_about -> {
                startActivity(Intent(applicationContext, About::class.java))

            }
            R.id.nav_privacy -> {
                startActivity(Intent(applicationContext, PrivacyPolicy::class.java))

            }
            R.id.nav_contact -> {
//                startActivity(Intent(applicationContext,ContactUs::class.java))
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_EMAIL, "support@mpset.co")
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on MPSET app from user ")
                intent.putExtra(Intent.EXTRA_TEXT, "Please share with us any feedback you might have\n" +
                        "Username id: <Username id>\n" +
                        "Device type: <device>\n" +
                        "System version: <sys ver.>\n" +
                        "Application version: <app ver.>\n")
                intent.setType("text/plain")
                startActivity(Intent.createChooser(intent, "Send Email using:"))
            }
            R.id.nav_app_rate -> {

            }
            R.id.nav_logout -> {
                logoutShareprefrences()
                Toast.makeText(applicationContext, "Logout Successfully", Toast.LENGTH_LONG).show()
                startActivity(Intent(this@MainActivity, Login::class.java))
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun logoutShareprefrences() {
        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.clear().commit()
    }
    fun servertokenUpdate(tokenAccess:String,token:String) {
        val params = JSONObject()
        params.put("token", token)
        val URL = resources.getString(R.string.base_url) +"/user/pushNotifications/register"
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : JsonObjectRequest(Request.Method.POST, URL, params, Response.Listener { s ->
        }, Response.ErrorListener { e ->
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json" , "Authorization" to "Bearer " +tokenAccess)
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)
    }
}
