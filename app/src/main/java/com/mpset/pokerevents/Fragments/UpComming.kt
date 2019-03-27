package com.mpset.pokerevents.Fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mpset.pokerevents.Activities.JoinEventDetailPage
import com.mpset.pokerevents.Adapters.UpcomingListAdapter
import com.mpset.pokerevents.Model.SelectionFriendsModel
import com.mpset.pokerevents.R
import com.mpset.pokerevents.Model.UpcomingModel
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import kotlinx.android.synthetic.main.fragment_up_comming.view.*
import org.json.JSONObject
import java.util.ArrayList
import kotlin.math.min


class UpComming : Fragment() {
    lateinit var adapter :UpcomingListAdapter
    val users = ArrayList<UpcomingModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_up_comming, container, false)
        val recyclerview = view.findViewById(R.id.recyclerview_upcoming) as RecyclerView

        recyclerview.layoutManager = LinearLayoutManager(context)


        getUpcomingEvents(userAccessToken)

//        users.add(UpcomingModel("1", "CardsChat Hurricane Harvey Charity Event", "April 17, 2018 9:30 AM", "Host Name", "Defence Phase 2 lahore","Game Type"))
//        users.add(UpcomingModel("1", "CardsChat Hurricane Harvey Charity Event", "April 17, 2018 9:30 AM", "Host Name", "Gulberg || lahore","Tournament"))
//        users.add(UpcomingModel("1", "CardsChat Hurricane Harvey Charity Event", "April 17, 2018 9:30 AM", "Host Name", "Liberty lahore","Cash"))
//        users.add(UpcomingModel("1", "CardsChat Hurricane Harvey Charity Event", "April 17, 2018 9:30 AM", "Host Name", "Edden Tower lahore","Tournament"))
//        users.add(UpcomingModel("1", "CardsChat Hurricane Harvey Charity Event", "April 17, 2018 9:30 AM", "Host Name", "Vivo Tower lahore","Cash"))
//        users.add(UpcomingModel("1", "CardsChat Hurricane Harvey Charity Event", "April 17, 2018 9:30 AM", "Host Name", "Hafeez Center lahore","Tournament"))
//        users.add(UpcomingModel("1", "CardsChat Hurricane Harvey Charity Event", "April 17, 2018 9:30 AM", "Host Name", "Gazi Chowk lahore","Cash"))
//        users.add(UpcomingModel("1", "CardsChat Hurricane Harvey Charity Event", "April 17, 2018 9:30 AM", "Host Name", "Canal Mor lahore","Tournament"))
//        users.add(UpcomingModel("1", "CardsChat Hurricane Harvey Charity Event", "April 17, 2018 9:30 AM", "Host Name", "Nazmabad lahore","Cash"))
//        users.add(UpcomingModel("1", "CardsChat Hurricane Harvey Charity Event", "April 17, 2018 9:30 AM", "Host Name", "Ali View Garden lahore","Tournament"))


         adapter = UpcomingListAdapter(users, object : UpcomingListAdapter.MyClickListener {
            override fun onClick(position: Int) {
                Toast.makeText(context, users[position].id, Toast.LENGTH_LONG).show()
                val intent = Intent(context,JoinEventDetailPage::class.java)
                intent.putExtra("event_name", users[position].eventName)
//                intent.putExtra("images", users[position].imagesList)
                intent.putExtra("id", users[position].id)
//                intent.putExtra("game_date", users[position].time)
//                intent.putExtra("game_type", users[position].gameType)
//                intent.putExtra("max_players", users[position].maxPlayers)
//                intent.putExtra("min_players", users[position].minPlayers)
//                intent.putExtra("table_rules", users[position].tableRules)
//                intent.putExtra("min_buyins", users[position].minBuyins)
//                intent.putExtra("max_buyins", users[position].maxBuyins)
//                intent.putExtra("no_of_players", users[position].totalSeats)
//                intent.putExtra("avail_seat", users[position].totalSeats)
//                intent.putExtra("address", users[position].address)
                startActivity(intent)


            }

            override fun onLongClick(position: Int) {
                Toast.makeText(context, "You got a long click it", Toast.LENGTH_LONG).show()
            }

        })
        recyclerview.adapter = adapter

        return  view
    }
    fun getUpcomingEvents(tokenAccess:String) {
        users.clear()
        val URL = resources.getString(R.string.base_url) +"/user/event/upcoming"
        val queue = Volley.newRequestQueue(context)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            // Your success code here
            try {
//                Toast.makeText(context,s,Toast.LENGTH_LONG).show()
                val jsonObject = JSONObject(s)
                val jasonarray = jsonObject.getJSONArray("data")
                if (jasonarray.length()==0)
                    view!!.empty_event.visibility = View.VISIBLE
                for (i in 0..jasonarray!!.length() - 1){

                    val jsonObj = jasonarray.getJSONObject(i)
                    val id:Int = jsonObj.getInt("id")
                    val gameType = jsonObj.getString("game_type_string")
                    var name = jsonObj.getString("name")
                    var gameDateTime = jsonObj.getString("date_string")
                    var rating = jsonObj.getDouble("rating")
                    var hostName = jsonObj.getString("host_name")
                    var address = jsonObj.getString("address")

                    users.add(UpcomingModel(id.toString(), name,gameDateTime,hostName, address,gameType,rating))
                }
                if (context!=null)
                    adapter.notifyDataSetChanged()
            }catch (e:Exception){
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }, Response.ErrorListener { e ->

            //          Toast.makeText(applicationContext,"Please fill your information",Toast.LENGTH_LONG).show()
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json" , "Authorization" to "Bearer " +tokenAccess)

        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }


}
