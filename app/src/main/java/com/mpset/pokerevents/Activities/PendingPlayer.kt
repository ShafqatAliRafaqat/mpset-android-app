package com.mpset.pokerevents.Activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mpset.pokerevents.MainActivity
import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import com.mpset.pokerevents.SplashScreen.Companion.userId
import com.smarteist.autoimageslider.SliderLayout
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_attending_event_detail.*
import kotlinx.android.synthetic.main.activity_join_event_detail_page.*
import kotlinx.android.synthetic.main.activity_update_event.*
import kotlinx.android.synthetic.main.activity_pending_player.*
import org.json.JSONObject
import java.util.ArrayList

class PendingPlayer : AppCompatActivity() {
    lateinit var id: String
    lateinit var eventName: String
    var imageslist: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending_player)
        supportActionBar!!.hide()
        imageSlider_pending_detail!!.setIndicatorAnimation(SliderLayout.Animations.FILL)
        imageSlider_pending_detail!!.setScrollTimeInSec(3)
        id = intent.getStringExtra("event_id")
        eventName = intent.getStringExtra("event_name")
        event_name_pending.text = eventName
        getPendingEvent(userAccessToken)
        image_player_pending_back.setOnClickListener {
           finish()
        }
        btn_cancel_pending_detail.setOnClickListener {
            showDialogCancelRequest(id.toInt())
        }
    }
    fun getPendingEvent(tokenAccess: String) {
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
                val maxPlayer = jsonObj.getInt("max_players")
                val minPlayer = jsonObj.getInt("min_players")
                val minBuyins = jsonObj.getInt("min_buyins")
                val maxBuyins = jsonObj.getInt("max_buyins")
                val publicStatus = jsonObj.getString("PublicStatus")
                val totalPlayers = jsonObj.getInt("no_of_players")
                val availSeats= jsonObj.getInt("available_seats")
                val jsonObjLocation = jsonObj.getJSONObject("location")
                var address = jsonObjLocation.getString("address")
                val jsonArrayImages = jsonObjLocation.getJSONArray("images")
                for (i in 0 until jsonArrayImages.length()) {
                    val pathObject = jsonArrayImages.getJSONObject(i)
                    val path = pathObject.getString("path")
                    imageslist.add(path)
                }
                if(publicStatus.equals("Private")){
                    game_type_title.text = "Private"
                    game_type_icon.setImageDrawable(resources.getDrawable(R.drawable.eyeprivate))
                    cart_private_pending.visibility = View.VISIBLE
                }else{
                    game_type_icon.setImageDrawable(resources.getDrawable(R.drawable.eyewhite))
                    game_type_title.text = "Public"
                    cart_private_pending.visibility = View.GONE
                }

                location_pending_detail.text = address
                date_pending_detail.text = gameDateTime
                no_seats_pending_detail.text = totalPlayers.toString()
                avail_seats_pending_detail.text = availSeats.toString()
                min_max_buyin_pending_detail.text = minBuyins.toString() + "/" + maxBuyins.toString()
                min_max_player_pending_detail.text = minPlayer.toString() + "/" + maxPlayer.toString()
                table_rules_pending_detail.text = tableRules
                setSliderViews()
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
    private fun setSliderViews() {
        for (i in 0 until imageslist.size) {
            val sliderView = SliderView(this)
            when (i) {
                i -> sliderView.imageUrl = imageslist.get(i)
            }
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
            imageSlider_pending_detail!!.addSliderView(sliderView)
        }
    }
    private fun showDialogCancelRequest(id: Int) {
        lateinit var dialog: AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cancel Pending Event")
        builder.setMessage("Do you really want to cancel this event")

        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
//                    Toast.makeText(context, "Positive/Yes button clicked.", Toast.LENGTH_LONG).show()
                    cancelAttendingEvent(userAccessToken, id)
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
    fun cancelAttendingEvent(tokenAccess: String, id: Int) {
        val URL = resources.getString(R.string.base_url) + "/user/event/cancel/" + id
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            // Your success code here
            try {
//                Toast.makeText(context, s, Toast.LENGTH_LONG).show()
                val jsonObject = JSONObject(s)
                val message = jsonObject.getString("message")
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()

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
}
