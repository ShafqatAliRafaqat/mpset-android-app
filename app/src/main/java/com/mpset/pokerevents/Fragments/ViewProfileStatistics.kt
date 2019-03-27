package com.mpset.pokerevents.Fragments


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
import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen
import kotlinx.android.synthetic.main.fragment_view_profile_statistics.view.*
import org.json.JSONObject


class ViewProfileStatistics : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_view_profile_statistics, container, false)

        val id:Int = activity!!.intent.getIntExtra("friend_id",0)
        getViewProfileInformation(SplashScreen.userAccessToken,id)
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
                val playedEvent = meta.getString("played_events")
                val playedTime = meta.getString("played_time")
                view!!.view_profile_Total_time_played.text = playedTime + " min"
                view!!.view_profile_number_of_games_played.text =   playedEvent

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
