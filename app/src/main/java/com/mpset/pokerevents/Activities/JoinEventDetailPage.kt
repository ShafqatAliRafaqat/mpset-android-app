package com.mpset.pokerevents.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mpset.pokerevents.MainActivity
import com.mpset.pokerevents.R
import kotlinx.android.synthetic.main.activity_join_event_detail_page.*
import org.json.JSONObject
import java.util.ArrayList
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import com.smarteist.autoimageslider.SliderLayout
import com.smarteist.autoimageslider.SliderView


class JoinEventDetailPage : AppCompatActivity() {
    var imageslist: ArrayList<String> = ArrayList()
    lateinit var id: String
    lateinit var address: String
    lateinit var eventName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_event_detail_page)

        eventName = intent.getStringExtra("event_name")
        id = intent.getStringExtra("id")

        supportActionBar!!.title = eventName
        getSpeciedEvent(userAccessToken)

        imageSlider!!.setIndicatorAnimation(SliderLayout.Animations.FILL) //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider!!.setScrollTimeInSec(3) //set scroll delay in seconds :

        imageslist.add("https://images.pexels.com/photos/547114/pexels-photo-547114.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        imageslist.add("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        imageslist.add("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260")
        imageslist.add("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        join_event.setOnClickListener {
            joinEvent(userAccessToken)
        }

    }

    fun joinEvent(tokenAccess: String) {
        val params = JSONObject()
        params.put("event_id", id)
        val URL = resources.getString(R.string.base_url) +"/user/event/join"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : JsonObjectRequest(Request.Method.POST, URL, params, Response.Listener { s ->
            // Your success code here
            try {

                val message = s.getString("message")
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                startActivity(Intent(this@JoinEventDetailPage, MainActivity::class.java))
            } catch (e: Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            }
        }, Response.ErrorListener { e ->
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)

        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }

    fun getSpeciedEvent(tokenAccess: String) {
        val URL = resources.getString(R.string.base_url) + "/user/event/" + id
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            try {
//                Toast.makeText(context,s,Toast.LENGTH_LONG).show()
                val jsonObject = JSONObject(s)
                val jsonObj = jsonObject.getJSONObject("data")

//                val gameType = jsonObj.getString("game_type_string")
                var gameDateTime = jsonObj.getString("date_string")
//                var rating = jsonObj.getDouble("rating")
                var tableRules = jsonObj.getString("table_rules")
                val maxPlayer = jsonObj.getString("max_players")
                val minPlayer = jsonObj.getString("min_players")
                val minBuyins = jsonObj.getString("min_buyins")
                val maxBuyins = jsonObj.getString("max_buyins")
                val totalPlayers = jsonObj.getString("no_of_players")
                val availSeats= jsonObj.getString("available_seats")
                val jsonObjLocation = jsonObj.getJSONObject("location")
                var address = jsonObjLocation.getString("address")
                val jsonArrayImages = jsonObjLocation.getJSONArray("images")
                for (i in 0 until jsonArrayImages.length()) {
                    val pathObject = jsonArrayImages.getJSONObject(i)
                    val path = pathObject.getString("path")
                    imageslist.add(path)
                }
                event_name_join_event.text = eventName
                location_join_detail.text = address
                date_join_detail.text = gameDateTime
                no_seats_join_detail.text = totalPlayers.toString()
                avail_seats_join_detail.text = availSeats.toString()
                min_max_buyin_join_detail.text = minBuyins.toString() + "/" + maxBuyins.toString()
                min_max_player_join_detail.text = minPlayer.toString() + "/" + maxPlayer.toString()
                table_rules_upcomming_detail.text = tableRules
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
//            sliderView.setOnSliderClickListener { Toast.makeText(this@JoinEventDetailPage, "This is slider " + (i + 1), Toast.LENGTH_SHORT).show() }
            //at last add this view in your layout :
            imageSlider!!.addSliderView(sliderView)
        }
    }
}
