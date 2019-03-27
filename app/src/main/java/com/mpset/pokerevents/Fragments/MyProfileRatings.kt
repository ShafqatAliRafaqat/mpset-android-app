package com.mpset.pokerevents.Fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mpset.pokerevents.Activities.ReviewDetailsPage

import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import com.mpset.pokerevents.SplashScreen.Companion.userId
import kotlinx.android.synthetic.main.fragment_my_profile_ratings.view.*
import kotlinx.android.synthetic.main.fragment_view_profile_rating.view.*
import org.json.JSONObject


class MyProfileRatings : Fragment() {
    var locationId = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_my_profile_ratings, container, false)
        getViewProfileInformation(userAccessToken, userId.toInt())
        view.player_reviews_my_profile.setOnClickListener {
            val intent = Intent(context, ReviewDetailsPage::class.java)
            intent.putExtra("review_id", userId.toInt())
            intent.putExtra("type", "player")
            startActivity(intent)
        }
        view.host_reviews_my_profile.setOnClickListener {
            val intent = Intent(context, ReviewDetailsPage::class.java)
            intent.putExtra("review_id", userId.toInt())
            intent.putExtra("type", "host")
            startActivity(intent)
        }
        view.location_reviews_my_profile.setOnClickListener {
            val intent = Intent(context, ReviewDetailsPage::class.java)
            intent.putExtra("review_id", locationId)
            intent.putExtra("type", "location")
            startActivity(intent)
        }
        return  view
    }
    fun getViewProfileInformation(tokenAccess: String, id: Int) {

        val URL = resources.getString(R.string.base_url) + "/user/friends/" + id
        val queue = Volley.newRequestQueue(context)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            // Your success code here
            try {
                val jsonObject = JSONObject(s)
                val meta = jsonObject.getJSONObject("meta")
                val  avgPlayerRating = meta.getDouble("player_rating")
                val avgHostRating = meta.getDouble("host_rating")
                val avgLocationRating = meta.getDouble("location_rating")
                val jsonObjectLocation = meta.getJSONObject("location")
                locationId = jsonObjectLocation.getInt("id")
                view!!.player_rating_my_profile.rating = avgPlayerRating.toFloat()
                view!!.host_rating_my_profile.rating = avgHostRating.toFloat()
                view!!.location_rating_my_profile.rating = avgLocationRating.toFloat()

            } catch (e: Exception) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }, Response.ErrorListener { e ->
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }

}
