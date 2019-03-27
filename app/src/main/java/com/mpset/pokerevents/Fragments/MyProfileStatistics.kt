package com.mpset.pokerevents.Fragments


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mpset.pokerevents.Adapters.ViewProfileEventStatisticsListAdapter
import com.mpset.pokerevents.Adapters.ViewProfileLocationStatisticsListAdapter
import com.mpset.pokerevents.Model.ViewProfileEventStatisticsModel
import com.mpset.pokerevents.Model.ViewProfileLocationStatisticsModel
import com.mpset.pokerevents.R
import com.mpset.pokerevents.R.drawable.cashout
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import kotlinx.android.synthetic.main.fragment_my_profile_ratings.view.*
import kotlinx.android.synthetic.main.fragment_my_profile_statistics.*
import kotlinx.android.synthetic.main.fragment_my_profile_statistics.view.*
import org.json.JSONObject
import java.util.ArrayList

class MyProfileStatistics : Fragment() {
    val eventStat = ArrayList<ViewProfileEventStatisticsModel>()
    val locationStat = ArrayList<ViewProfileLocationStatisticsModel>()
    lateinit var adapterEvent:ViewProfileEventStatisticsListAdapter
    lateinit var  adapterLocation:ViewProfileLocationStatisticsListAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_profile_statistics, container, false)
//        val recyclerview = view.findViewById(R.id.recyclerview__my_profile_stat_events) as RecyclerView
//        val recyclerview2 = view.findViewById(R.id.recyclerview__my_profile_stat_location) as RecyclerView
        getViewProfileStatistics(userAccessToken)
        view.recyclerview__my_profile_stat_events.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL,false)
        view.recyclerview__my_profile_stat_location.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL,false)


//        eventStat.add(ViewProfileEventStatisticsModel(1, "Private","Honda Point Lahore",200,30,2000.0,250000.6767,"12/12/2018 12:40 Am"))
//        eventStat.add(ViewProfileEventStatisticsModel(1, "Private","Center Point Lahore",200,30,2000.0,250000.6767,"12/12/2018 12:40 Am"))
//        eventStat.add(ViewProfileEventStatisticsModel(1, "Private","Main Market Lahore",200,30,2000.0,250000.6767,"12/12/2018 12:40 Am"))
//        eventStat.add(ViewProfileEventStatisticsModel(1, "Private","Ferdous Market Lahore",200,30,2000.0,250000.6767,"12/12/2018 12:40 Am"))
//        users.add(ViewProfileEventStatisticsModel(1, "Private","Calvery Ground Lahore",200,30,2000.0,250000.6767,"12/12/2018 12:40 Am"))
//        users.add(ViewProfileEventStatisticsModel(1, "Private","Bhatta Chowk Lahore",200,30,2000.0,250000.6767,"12/12/2018 12:40 Am"))
//        users.add(ViewProfileEventStatisticsModel(1, "Private","Yateen Khana Lahore",200,30,2000.0,250000.6767,"12/12/2018 12:40 Am"))
//        users.add(ViewProfileEventStatisticsModel(1, "Private","Divine center 2",200,30,2000.0,250000.6767,"12/12/2018 12:40 Am"))


//        locationStat.add(ViewProfileLocationStatisticsModel(1, 15,"Honda Point Lahore",200,30,2000.0,250000.6767))
//        users2.add(ViewProfileLocationStatisticsModel(1, 15,"Center Point Lahore",200,30,2000.0,250000.6767))
//        users2.add(ViewProfileLocationStatisticsModel(1, 15,"Main Market Lahore",200,30,2000.0,250000.6767))
//        users2.add(ViewProfileLocationStatisticsModel(1, 15,"Ferdous Market Lahore",200,30,2000.0,250000.6767))
//        users2.add(ViewProfileLocationStatisticsModel(1, 15,"Calvery Ground Lahore",200,30,2000.0,250000.6767))
//        users2.add(ViewProfileLocationStatisticsModel(1, 15,"Bhatta Chowk Lahore",200,30,2000.0,250000.6767))
//        users2.add(ViewProfileLocationStatisticsModel(1, 15,"Yateen Khana Lahore",200,30,2000.0,250000.6767))
//        users2.add(ViewProfileLocationStatisticsModel(1, 15,"Divine center 2",200,30,2000.0,250000.6767))






         adapterEvent = ViewProfileEventStatisticsListAdapter(context,eventStat, object : ViewProfileEventStatisticsListAdapter.MyClickListener {
            override fun onClick(position: Int) {
                Toast.makeText(context,"ssss", Toast.LENGTH_LONG).show()



            }

            override fun onLongClick(position: Int) {
                Toast.makeText(context, "You got a long click it", Toast.LENGTH_LONG).show()
            }

        })

         adapterLocation = ViewProfileLocationStatisticsListAdapter(context,locationStat, object : ViewProfileLocationStatisticsListAdapter.MyClickListener {
            override fun onClick(position: Int) {
                Toast.makeText(context,"ssss", Toast.LENGTH_LONG).show()



            }

            override fun onLongClick(position: Int) {
                Toast.makeText(context, "You got a long click it", Toast.LENGTH_LONG).show()
            }

        })
        view.recyclerview__my_profile_stat_events.adapter = adapterEvent
        view.recyclerview__my_profile_stat_location.adapter = adapterLocation



        /////////////////////////////
        val myProfileEventSta = view.findViewById<TextView>(R.id.my_profile_event_sta)
        val myProfileLocationSta = view.findViewById<TextView>(R.id.my_profile_location_sta)
        myProfileEventSta.setOnClickListener {
            view.recyclerview__my_profile_stat_events.visibility = View.VISIBLE
            view.recyclerview__my_profile_stat_location.visibility = View.GONE
            myProfileEventSta.background = resources.getDrawable(R.drawable.goldtoggleleft)
            myProfileLocationSta.background = resources.getDrawable(R.drawable.whitetoggleright)
            myProfileEventSta.setTextColor(Color.parseColor("#ffffff"))
            myProfileLocationSta.setTextColor(Color.parseColor("#000000"))
        }
        myProfileLocationSta.setOnClickListener {
            view.recyclerview__my_profile_stat_events.visibility = View.GONE
            view.recyclerview__my_profile_stat_location.visibility = View.VISIBLE
            myProfileEventSta.background = resources.getDrawable(R.drawable.whitetoggleleft)
            myProfileLocationSta.background = resources.getDrawable(R.drawable.goldtoggleright)
            myProfileLocationSta.setTextColor(Color.parseColor("#ffffff"))
            myProfileEventSta.setTextColor(Color.parseColor("#000000"))
        }
        return  view
    }

    fun getViewProfileStatistics(tokenAccess: String) {
        eventStat.clear()
        locationStat.clear()
        val URL = resources.getString(R.string.base_url) + "/user/event/status"
        val queue = Volley.newRequestQueue(context)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            try {
                val jsonObject = JSONObject(s)
              val data = jsonObject.getJSONObject("data")
                val min = data.getInt("minutes")
                val totalEvents = data.getInt("no_of_events")
                val buyins = data.getInt("buyins")
                val checkout = data.getInt("checkout")
                val balance = data.getInt("balance")
                val cashGameBal = data.getLong("cashGame_balance")
                val tourGameBal = data.getLong("tournament_balance")
                val locationArray = data.getJSONArray("location_events")
                val eventArray = data.getJSONArray("events")
                if(locationArray.length()==0){
                    txt_event_location.visibility = View.VISIBLE
                }
                for (i in 0 until locationArray.length()){
                    val jsonLocationData = locationArray.getJSONObject(i)
                    val address = jsonLocationData.getString("address")
//                    val minutes = jsonLocationData.getString("minutes")
                    val no_of_events = jsonLocationData.getLong("no_of_events")
                    var buyins = jsonLocationData.getLong("buyins")
                    if (buyins.equals("null") || buyins==null)
                        buyins = 0
                    var checkout = jsonLocationData.getLong("checkout")
                    if (checkout.equals("null") || checkout ==null)
                        checkout = 0
                    locationStat.add(ViewProfileLocationStatisticsModel(1, no_of_events.toLong(),address,min.toLong(),buyins.toLong(),checkout.toDouble(),0.0))

                }
                if(eventArray.length()==0){
                    txt_event_location.visibility = View.VISIBLE
                }
                for (i in 0 until eventArray.length()){
                    val jsonEventData = eventArray.getJSONObject(i)
                    val address = jsonEventData.getString("address")
                    val date = jsonEventData.getString("date_string")
                    val gameTypeString = jsonEventData.getString("game_type_string")
                    val duration = jsonEventData.getString("duration")

                    val pivote = jsonEventData.getJSONObject("pivot")
                    val checkout = pivote.getDouble("checkout")
                    val buyins = pivote.getLong("buyins")
                    eventStat.add(ViewProfileEventStatisticsModel(1, gameTypeString,address,duration.toLong(),buyins,checkout,0.0,date))

                }

                my_profile_number_games_played.text = totalEvents.toString()
                my_profile_total_time_played.text = min.toString() +" min"
                my_profile_total_buy_ins.text = buyins.toString()
                my_profile_total_cash_out.text = checkout.toString()
                my_profile_total_balance.text = balance.toString()
                my_profile_total_balance_cash_games.text = cashGameBal.toString()
                my_profile_total_balance_tournaments.text = tourGameBal.toString()
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
