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
import com.mpset.pokerevents.Activities.ArchiveDetail
import com.mpset.pokerevents.Adapters.AttendingListAdapter
import com.mpset.pokerevents.Adapters.HistoryListAdapter
import com.mpset.pokerevents.Model.AttendingModel
import com.mpset.pokerevents.Model.HistoryModel
import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import kotlinx.android.synthetic.main.activity_pending_player.*
import kotlinx.android.synthetic.main.fragment_attending.view.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import org.json.JSONObject
import java.util.ArrayList

class History : Fragment() {
    val usersHistory = ArrayList<HistoryModel>()
    lateinit var adapterHistory:HistoryListAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        val recyclerview = view.findViewById(R.id.recyclerview_history) as RecyclerView

        recyclerview.layoutManager = LinearLayoutManager(context)

        getHistoryEvents(userAccessToken)
//        usersHistory.add(HistoryModel("1", "Best Bestival Event in Lahore", "April 17, 2018 9:30 AM", "Hafiz center lahore", "Private","In Progress","Player"))
//        usersHistory.add(HistoryModel("1", "Best Bestival Event ", "April 17, 2018 9:30 AM", "Jeef Tower lahore", "Private","In Progress","Player"))
//        usersHistory.add(HistoryModel("1", "Favorite Bestival Event in Lahore", "April 17, 2018 9:30 AM", "Hafeez hightes lahore", "Private","In Progress","Player"))
//        usersHistory.add(HistoryModel("1", "Festival Event in Lahore", "April 17, 2018 9:30 AM", "Vivo Tower lahore", "Private","In Progress","Player"))
//        usersHistory.add(HistoryModel("1", "Poker Event in Lahore", "April 17, 2018 9:30 AM", "Bon Fire lahore", "Private","In Progress","Player"))
//        usersHistory.add(HistoryModel("1", "Best Bestival Event in Lahore", "April 17, 2018 9:30 AM", "Siddiq Center lahore", "Private","In Progress","Player"))
//        usersHistory.add(HistoryModel("1", "Best Bestival Event in Lahore", "April 17, 2018 9:30 AM", "Mega 3 lahore", "Private","In Progress","Player"))
//        usersHistory.add(HistoryModel("1", "Best Bestival Event in Lahore", "April 17, 2018 9:30 AM", "Iqbal Town lahore", "Private","In Progress","Player"))
//        usersHistory.add(HistoryModel("1", "Best Bestival Event in Lahore", "April 17, 2018 9:30 AM", "Packages Mall lahore", "Private","In Progress","Player"))
//        usersHistory.add(HistoryModel("1", "Best Bestival Event in Lahore", "April 17, 2018 9:30 AM", "Honda Point lahore", "Private","In Progress","Player"))
//        usersHistory.add(HistoryModel("1", "Best Bestival Event in Lahore", "April 17, 2018 9:30 AM", "PU Campus lahore", "Private","In Progress","Host"))
//        usersHistory.add(HistoryModel("1", "Best Bestival Event in Lahore", "April 17, 2018 9:30 AM", "GC University lahore", "Private","In Progress","Both"))





        adapterHistory = HistoryListAdapter(usersHistory, object : HistoryListAdapter.MyClickListener {
            override fun onClick(position: Int) {
//                Toast.makeText(context, usersHistory[position].id, Toast.LENGTH_LONG).show()
                val intent = Intent(context,ArchiveDetail::class.java)
                intent.putExtra("event_id", usersHistory[position].id)
                intent.putExtra("event_name", usersHistory[position].eventName)
                startActivity(intent)


            }

            override fun onLongClick(position: Int) {
                Toast.makeText(context, "You got a long click it", Toast.LENGTH_LONG).show()
            }

        })
        recyclerview.adapter = adapterHistory
        return  view
    }
    fun getHistoryEvents(tokenAccess:String) {
        usersHistory.clear()
        val URL = resources.getString(R.string.base_url) +"/user/event/history"
        val queue = Volley.newRequestQueue(context)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            // Your success code here
            try {
//                Toast.makeText(context,s,Toast.LENGTH_LONG).show()
                val jsonObject = JSONObject(s)
                val jasonarray = jsonObject.getJSONArray("data")
                if (jasonarray.length()==0)
                    view!!.empty_event_history.visibility = View.VISIBLE
                for (i in 0..jasonarray!!.length() - 1){

                    val jsonObj = jasonarray.getJSONObject(i)
                    val id:Int = jsonObj.getInt("id")
                    val gameType = jsonObj.getString("game_type_string")
                    var name = jsonObj.getString("name")
                    var gameDateTime = jsonObj.getString("date_string")
//                    var publicStatus = jsonObj.getString("PublicStatus")
                    var playerStatus = jsonObj.getString("PlayerStatus")
                    var gameStatus = jsonObj.getString("status")
                    var address = jsonObj.getString("address")

                    usersHistory.add(HistoryModel(id.toString(), name ,gameDateTime, address, gameType,gameStatus,playerStatus))
                }
                if (context!=null)
                    adapterHistory.notifyDataSetChanged()
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
