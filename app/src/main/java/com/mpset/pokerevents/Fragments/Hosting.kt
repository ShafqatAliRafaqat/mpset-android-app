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
import com.mpset.pokerevents.Activities.HostPublicEventDetail
import com.mpset.pokerevents.Adapters.AttendingListAdapter
import com.mpset.pokerevents.Adapters.HostingListAdapter
import com.mpset.pokerevents.Model.AttendingModel
import com.mpset.pokerevents.Model.UpcomingModel

import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import kotlinx.android.synthetic.main.fragment_hosting.view.*
import org.json.JSONObject
import java.util.ArrayList


class Hosting : Fragment() {
     var users= ArrayList<AttendingModel>()
    lateinit var adapter:HostingListAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_hosting, container, false)
        val recyclerview = view.findViewById(R.id.recyclerview_hosting) as RecyclerView

        recyclerview.layoutManager = LinearLayoutManager(context)

        getHostingEvents(userAccessToken)


         adapter = HostingListAdapter(users, object : HostingListAdapter.MyClickListener {
            override fun onClick(position: Int) {
                Toast.makeText(context, users[position].id, Toast.LENGTH_LONG).show()
                val intent = Intent(context,HostPublicEventDetail::class.java)
                intent.putExtra("event_id", users[position].id)
                intent.putExtra("event_name", users[position].eventName)
                intent.putExtra("event_type", users[position].eventStatus)
                startActivity(intent)


            }

            override fun onLongClick(position: Int) {
                Toast.makeText(context, "You got a long click it", Toast.LENGTH_LONG).show()
            }

        })
        recyclerview.adapter = adapter
        return  view
    }
    fun getHostingEvents(tokenAccess:String) {
   users.clear()
        val URL = resources.getString(R.string.base_url) +"/user/event/hosting"
        val queue = Volley.newRequestQueue(context)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            // Your success code here
            try {
//                Toast.makeText(context,s,Toast.LENGTH_LONG).show()
                val jsonObject = JSONObject(s)
                val jasonarray = jsonObject.getJSONArray("data")
                if (jasonarray.length()==0)
                    view!!.empty_event_hosting.visibility = View.VISIBLE
                for (i in 0..jasonarray!!.length() - 1){

                    val jsonObj = jasonarray.getJSONObject(i)
                    val id:Int = jsonObj.getInt("id")
                    val gameType = jsonObj.getString("game_type_string")
                    var name = jsonObj.getString("name")
                    var gameDateTime = jsonObj.getString("date_string")
                    var publicStatus = jsonObj.getString("PublicStatus")
                    var address = jsonObj.getString("address")

                    users.add(AttendingModel(id.toString(), name,gameDateTime, address,gameType,publicStatus))
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
