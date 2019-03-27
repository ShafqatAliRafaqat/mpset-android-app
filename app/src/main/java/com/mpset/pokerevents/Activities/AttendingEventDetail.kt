package com.mpset.pokerevents.Activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mpset.pokerevents.Adapters.ArchiveDetailHostListAdapter
import com.mpset.pokerevents.Adapters.ArchiveStatisticsHostListAdapter
import com.mpset.pokerevents.Adapters.AttendingDetailListAdapter
import com.mpset.pokerevents.MainActivity
import com.mpset.pokerevents.Model.ArchiveDetailsHostModel
import com.mpset.pokerevents.Model.ArchiveStatisticsHostModel
import com.mpset.pokerevents.R
import com.mpset.pokerevents.R.id.location_attending_detail
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import com.smarteist.autoimageslider.SliderLayout
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_attending_event_detail.*
import kotlinx.android.synthetic.main.activity_join_event_detail_page.*
import org.json.JSONObject
import java.util.ArrayList

class AttendingEventDetail : AppCompatActivity() {
     var id =""
     var eventName =""
    var imageslist: ArrayList<String> = ArrayList()
    val usersAttendingDetails = ArrayList<ArchiveDetailsHostModel>()
    lateinit var adapterAttendingDetail : AttendingDetailListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attending_event_detail)
        supportActionBar!!.hide()
        id = intent.getStringExtra("event_id")
        eventName = intent.getStringExtra("event_name")
        title_attending_detail.text = eventName
        image_slider_attending_detail!!.setIndicatorAnimation(SliderLayout.Animations.FILL) //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        image_slider_attending_detail!!.setScrollTimeInSec(3)
        rcyclerview_playerlist_attending_detail.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        getAttendingEventDetail(userAccessToken)

        adapterAttendingDetail = AttendingDetailListAdapter(this,usersAttendingDetails, object : AttendingDetailListAdapter.MyClickListener {
            override fun onClick(position: Int) {
//                Toast.makeText(applicationContext,"ssss", Toast.LENGTH_LONG).show()
            }

            override fun onLongClick(position: Int) {
//                Toast.makeText(applicationContext, "You got a long click it", Toast.LENGTH_LONG).show()
            }

        })

        rcyclerview_playerlist_attending_detail.adapter = adapterAttendingDetail
        image_attending_back.setOnClickListener {
            startActivity(Intent(this@AttendingEventDetail,MainActivity::class.java))
        }
        btn_cancel_attending_detail.setOnClickListener {
            showDialog()
        }

    }
    fun getAttendingEventDetail(tokenAccess: String) {
        val URL = resources.getString(R.string.base_url) + "/user/event/" + id
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            try {
//                Toast.makeText(context,s,Toast.LENGTH_LONG).show()
                val jsonObject = JSONObject(s)
                val jsonObj = jsonObject.getJSONObject("data")

                val eventType = jsonObj.getString("PublicStatus")
                var gameDateTime = jsonObj.getString("date_string")
////                var rating = jsonObj.getDouble("rating")
                var address = jsonObj.getString("address")
                var tableRules = jsonObj.getString("table_rules")
                val maxPlayer = jsonObj.getString("max_players")
                val minPlayer = jsonObj.getString("min_players")
                val minBuyins = jsonObj.getString("min_buyins")
                val maxBuyins = jsonObj.getString("max_buyins")
                val totalPlayers = jsonObj.getString("no_of_players")
                val jsonObjLocation = jsonObj.getJSONObject("location")

                val jsonArrayImages = jsonObjLocation.getJSONArray("images")
                for (i in 0 until jsonArrayImages.length()) {
                    val pathObject = jsonArrayImages.getJSONObject(i)
                    val path = pathObject.getString("path")
                    imageslist.add(path)
                }
                val users = jsonObj.getJSONArray("users")
                for (i in 0 until users.length()) {
                    val userObject = users.getJSONObject(i)
                    val id = userObject.getInt("id")
                    val firstName = userObject.getString("first_name")
                    val lastName = userObject.getString("last_name")
                    val nickName = userObject.getString("nick_name")
                    usersAttendingDetails.add(ArchiveDetailsHostModel(id,firstName +" "+ lastName, nickName))
                }
                location_attending_detail.text = address
                date_attending_detail.text = gameDateTime
                no_seats_attending_detail.text = totalPlayers.toString()
                avail_seats_attending_detail.text = totalPlayers.toString()
                min_max_buyin_attending_detail.text = minBuyins.toString() + "/" + maxBuyins.toString()
                min_max_player_attending_detail.text = minPlayer.toString() + "/" + maxPlayer.toString()
                table_rules_attending_detail.text = tableRules
                if(eventType.equals("Private")){
                    text_private_public_attending_detail.text = "Private"
                    image_private_public_attending_detail.setImageDrawable(resources.getDrawable(R.drawable.eyeprivate))
                }else{
                    image_private_public_attending_detail.setImageDrawable(resources.getDrawable(R.drawable.eyewhite))
                    text_private_public_attending_detail.text = "Public"
                }
                setSliderViews()
            } catch (e: Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            }
        }, Response.ErrorListener { e ->

            //          Toast.makeText(applicationContext,"Please fill your information",Toast.LENGTH_LONG).show()
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)

        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }
    private fun setSliderViews() {

        for (i in 0 until imageslist.size) {
            val sliderView = SliderView(this)
            when (i) {
                i -> sliderView.imageUrl = imageslist.get(i)
            }
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
            image_slider_attending_detail!!.addSliderView(sliderView)
        }
    }
    fun cancelAttendingEvent(tokenAccess: String) {
        val URL = resources.getString(R.string.base_url) + "/user/event/cancel/" + id
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            // Your success code here
            try {
                val jsonObject = JSONObject(s)
                val message = jsonObject.getString("message")
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                startActivity(Intent(this@AttendingEventDetail,MainActivity::class.java))
            } catch (e: Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            }
        }, Response.ErrorListener { e ->

            //          Toast.makeText(applicationContext,"Please fill your information",Toast.LENGTH_LONG).show()
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)

        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }
    private fun showDialog() {
        lateinit var dialog: AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cancel Attending Event")
        builder.setMessage("Do you really want to cancel this event")

        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
//                    Toast.makeText(context, "Positive/Yes button clicked.", Toast.LENGTH_LONG).show()
                    cancelAttendingEvent(userAccessToken)
                }
                DialogInterface.BUTTON_NEGATIVE -> dialog.cancel()
                DialogInterface.BUTTON_NEUTRAL -> dialog.cancel()
            }
        }
        builder.setPositiveButton("YES", dialogClickListener)
        builder.setNegativeButton("NO", dialogClickListener)
        builder.setNeutralButton("CANCEL", dialogClickListener)
        dialog = builder.create()
        dialog.show()
    }
}
