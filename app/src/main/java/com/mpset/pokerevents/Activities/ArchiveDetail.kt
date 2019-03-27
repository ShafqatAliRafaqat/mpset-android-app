package com.mpset.pokerevents.Activities

import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mpset.pokerevents.Adapters.ArchiveDetailHostListAdapter
import com.mpset.pokerevents.Adapters.ArchiveStatisticsHostListAdapter
import com.mpset.pokerevents.Adapters.SelectionFriendListAdapter
import com.mpset.pokerevents.Helper.ImageHelperJava
import com.mpset.pokerevents.MainActivity
import com.mpset.pokerevents.Model.ArchiveDetailsHostModel
import com.mpset.pokerevents.Model.ArchiveStatisticsHostModel
import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import com.mpset.pokerevents.SplashScreen.Companion.userId
import kotlinx.android.synthetic.main.activity_archive_detail.*
import kotlinx.android.synthetic.main.activity_new_event_friend_selection.*
import kotlinx.android.synthetic.main.snipped_get_rating.*
import kotlinx.android.synthetic.main.snipped_table_rules.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

class ArchiveDetail : AppCompatActivity() {
    val usersDetails = ArrayList<ArchiveDetailsHostModel>()
    val usersStat = ArrayList<ArchiveStatisticsHostModel>()
    lateinit var adapterDetails : ArchiveDetailHostListAdapter
    lateinit var adapterStat : ArchiveStatisticsHostListAdapter
    var id = ""
    var eventName = ""
    var tableRules = ""
    var hostId = 0
    var locationId = 0
    lateinit var myDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive_detail)
        supportActionBar!!.hide()
        myDialog = Dialog(this)
        id = intent.getStringExtra("event_id")
        eventName = intent.getStringExtra("event_name")
        title_archive_event_name.text = eventName
//        val fragmentAdapter = MyPagerAdapterArchiveDetail(supportFragmentManager)
//        viewpager_archive.adapter = fragmentAdapter
//        tabs_archive.setupWithViewPager(viewpager_archive)

        rcyclerview_plaerlist_archive_host_details.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        rcyclerview_plaerlist_archive_host_statistics.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL,false)

        getHistoryOfEvent(userAccessToken)
         adapterDetails = ArchiveDetailHostListAdapter(this,usersDetails, object : ArchiveDetailHostListAdapter.MyClickListener {
             override fun onRating(position: Int) {
                 ShowPopupGetRating("Player Rating")
             }

             override fun onClick(position: Int) {
                 val intent = Intent(applicationContext,ViewProfile::class.java)
                 intent.putExtra("friend_id", usersDetails[position].id)
                 startActivity(intent)
             }
            override fun onLongClick(position: Int) {}
        })
        adapterStat = ArchiveStatisticsHostListAdapter(this,usersStat, object : ArchiveStatisticsHostListAdapter.MyClickListener {
            override fun onClick(position: Int) {}
            override fun onLongClick(position: Int) {}
        })
        rcyclerview_plaerlist_archive_host_details.adapter = adapterDetails
        rcyclerview_plaerlist_archive_host_statistics.adapter = adapterStat
        ////////////////////////////////////////////////////////////////////////////////
//        viewDetailsArchiveHost.setOnClickListener {
//            host_detail_part.visibility = View.VISIBLE
//            host_statistics_part.visibility = View.GONE
//        }
//        viewStatisticsArchiveHost.setOnClickListener {
//            host_detail_part.visibility = View.GONE
//            host_statistics_part.visibility = View.VISIBLE
//        }

        view_details_archive_host.setOnClickListener {
            linear_rating_button_players.visibility = View.VISIBLE
            host_detail_part2.visibility = View.VISIBLE
            cart_host_rate.visibility = View.VISIBLE
            cart_location_rate.visibility = View.VISIBLE
            rcyclerview_plaerlist_archive_host_details.visibility = View.VISIBLE
            rcyclerview_plaerlist_archive_host_statistics.visibility = View.INVISIBLE
            host_statistics_part.visibility = View.GONE
            view_details_archive_host.background = resources.getDrawable(R.drawable.goldtoggleleft)
            view_statistics_archive_host.background = resources.getDrawable(R.drawable.whitetoggleright)
            view_details_archive_host.setTextColor(Color.parseColor("#ffffff"))
            view_statistics_archive_host.setTextColor(Color.parseColor("#000000"))
            if(userId.toInt()==hostId){
                cart_location_rate.visibility = View.VISIBLE
                cart_host_rate.visibility = View.VISIBLE
                linear_rating_button_players.visibility = View.GONE
                linear_home_payment.visibility = View.VISIBLE
            }else{
                cart_location_rate.visibility = View.GONE
                cart_host_rate.visibility = View.GONE
                linear_rating_button_players.visibility = View.VISIBLE
                linear_home_payment.visibility = View.GONE
            }
        }

        view_statistics_archive_host.setOnClickListener {
            linear_rating_button_players.visibility = View.GONE
            cart_host_rate.visibility = View.GONE
            cart_location_rate.visibility = View.GONE
            host_statistics_part.visibility = View.VISIBLE
            rcyclerview_plaerlist_archive_host_details.visibility = View.INVISIBLE
            host_detail_part2.visibility = View.INVISIBLE
            rcyclerview_plaerlist_archive_host_statistics.visibility = View.VISIBLE
            view_statistics_archive_host.background = resources.getDrawable(R.drawable.goldtoggleright)
            view_details_archive_host.background = resources.getDrawable(R.drawable.whitetoggleleft)
            view_statistics_archive_host.setTextColor(Color.parseColor("#ffffff"))
            view_details_archive_host.setTextColor(Color.parseColor("#000000"))
            if(userId.toInt()==hostId){
//                cart_location_rate.visibility = View.VISIBLE
//                cart_host_rate.visibility = View.VISIBLE
//                linear_rating_button_players.visibility = View.GONE
                linear_home_payment.visibility = View.VISIBLE
            }else{
//                cart_location_rate.visibility = View.GONE
//                cart_host_rate.visibility = View.GONE
//                linear_rating_button_players.visibility = View.VISIBLE
                linear_home_payment.visibility = View.GONE
            }
        }
        image_archive_back.setOnClickListener {
           finish()
        }
        table_rules_archive.setOnClickListener {
            ShowPopupTableRules(it)
        }
        rate_host_history_player.setOnClickListener {
            ShowPopupGetRating("Host Rating")
        }
        rate_location_history_player.setOnClickListener {
            ShowPopupGetRating("Location Rating")
        }
        view_location_rating_history_host.setOnClickListener {
            val intent = Intent(this, ReviewDetailsPage::class.java)
            intent.putExtra("review_id", locationId)
            intent.putExtra("type", "location")
            startActivity(intent)
        }
        view_host_rating_history_host.setOnClickListener {
            val intent = Intent(this, ReviewDetailsPage::class.java)
            intent.putExtra("review_id", hostId)
            intent.putExtra("type", "host")
            startActivity(intent)
        }
    }
    fun ShowPopupTableRules(v: View) {
        myDialog.setContentView(R.layout.snipped_table_rules)
        myDialog.show()
        myDialog.text_table_rules.text = tableRules
        myDialog.btn_table_rules.setOnClickListener {
            myDialog.cancel()
        }
    }
    fun ShowPopupGetRating(title:String) {
        myDialog.setContentView(R.layout.snipped_get_rating)
        myDialog.show()
        myDialog.text_title_get_rating.text = title
        var type =""
        var host = false
        if(myDialog.text_title_get_rating.text.equals("Host Rating")){
            type = "host"
            host = true
        }else if(myDialog.text_title_get_rating.text.equals("Player Rating")){
            type = "host"
            host = false
        }
        else {
            type = "location"
        }
        myDialog.btn_rating.setOnClickListener {
             Toast.makeText(applicationContext,myDialog.get_rating.rating.toString(),Toast.LENGTH_LONG).show()
             Toast.makeText(applicationContext,myDialog.get_comment.text.toString(),Toast.LENGTH_LONG).show()
            serverCallRating(userAccessToken,myDialog.get_rating.rating.toString(),myDialog.get_comment.text.toString(),hostId,type, host)
            myDialog.cancel()
        }
    }
    fun getHistoryOfEvent(tokenAccess: String) {
        val URL = resources.getString(R.string.base_url) + "/user/event/" + id
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            try {
                val jsonObject = JSONObject(s)
                val jsonObj = jsonObject.getJSONObject("data")

//                val gameType = jsonObj.getString("game_type_string")
                var gameDateTime = jsonObj.getString("date_string")
//                var rating = jsonObj.getDouble("rating")
                 tableRules = jsonObj.getString("table_rules")
                 val host  = jsonObj.getJSONObject("host")
                  hostId  = host.getInt("id")
                if(userId.toInt()==hostId){
                    cart_location_rate.visibility = View.VISIBLE
                    cart_host_rate.visibility = View.VISIBLE
                    linear_rating_button_players.visibility = View.GONE
                    linear_home_payment.visibility = View.VISIBLE
                }else{
                    cart_location_rate.visibility = View.GONE
                    cart_host_rate.visibility = View.GONE
                    linear_rating_button_players.visibility = View.VISIBLE
                    linear_home_payment.visibility = View.GONE
                }
                val maxPlayer = jsonObj.getInt("max_players")
                val status = jsonObj.getString("status")
                val minPlayer = jsonObj.getInt("min_players")
                val minBuyins = jsonObj.getInt("min_buyins")
                val maxBuyins = jsonObj.getInt("max_buyins")
                val totalPlayers = jsonObj.getInt("no_of_players")
                val availSeats= jsonObj.getInt("available_seats")
                var address = jsonObj.getString("address")
                var location = jsonObj.getJSONObject("location")
                 locationId = location.getInt("id")
                var duration = jsonObj.getLong("duration")
                var jsonArray = jsonObj.getJSONArray("users")
                for (i in 0 until jsonArray.length()){
                    val jsonObject = jsonArray.getJSONObject(i)
                    val id = jsonObject.getInt("id")
                    val nickName = jsonObject.getString("nick_name")
                    val firstName = jsonObject.getString("first_name")
                    val lastName = jsonObject.getString("last_name")
                    val pivot = jsonObject.getJSONObject("pivot")
                    val buyins = pivot.getLong("buyins")
                    val checkout = pivot.getDouble("checkout")
                    usersDetails.add(ArchiveDetailsHostModel(id,firstName +" "+lastName, nickName))
                    usersStat.add(ArchiveStatisticsHostModel(id,firstName +" "+lastName, nickName,buyins,checkout,0.0))
                    if(usersDetails!=null || usersStat !=null){
                        adapterDetails.notifyDataSetChanged()
                        adapterStat.notifyDataSetChanged()
                    }
                }
                status_archive_host.text = status
                location_archive_host.text = address
                date_archive_host.text = gameDateTime
                seats_archive_host.text = availSeats.toString()+ "/" + totalPlayers.toString()
                min_max_butin_archive_host.text = minBuyins.toString() + "/" + maxBuyins.toString()
                min_max_player_archive_host.text = minPlayer.toString() + "/" + maxPlayer.toString()
                duration_archive_host.text = duration.toString()
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
    fun serverCallRating(tokenAccess:String,rating:String,comment:String,hostId:Int,type:String,host: Boolean){
        val params = JSONObject()
        var URL = ""
        if(type.equals("host")){
            params.put("rating", rating)
            params.put("comment", comment)
            params.put("user_id", hostId)
            params.put("asHost",host)
            URL = resources.getString(R.string.base_url) +"/user/reviews/user"
        }else{
            params.put("rating", rating)
            params.put("comment", comment)
            params.put("location_id", locationId)
         URL = resources.getString(R.string.base_url) +"/user/reviews/location"
        }



        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : JsonObjectRequest(Request.Method.POST, URL, params,Response.Listener { s ->
            val message = s.getString("message")
            Toast.makeText(applicationContext,message.toString(),Toast.LENGTH_LONG).show()
        }, Response.ErrorListener { e ->
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json" , "Authorization" to "Bearer " +tokenAccess)
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)
    }
}
