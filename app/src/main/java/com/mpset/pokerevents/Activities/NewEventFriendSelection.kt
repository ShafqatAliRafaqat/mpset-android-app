package com.mpset.pokerevents.Activities

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.mpset.pokerevents.Activities.AddLocation.Companion.locationAddress
import com.mpset.pokerevents.Adapters.AddFriendsAdapter
import com.mpset.pokerevents.Adapters.SelectionFriendListAdapter
import com.mpset.pokerevents.Adapters.SelectionFriendListAdapter.Companion.arrayListUsers
import com.mpset.pokerevents.Helper.ImageHelperJava
import com.mpset.pokerevents.MainActivity
import com.mpset.pokerevents.Model.AddFriendsModel
import com.mpset.pokerevents.Model.SelectionFriendsModel
import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import kotlinx.android.synthetic.main.activity_create_event_game_type.*
import kotlinx.android.synthetic.main.activity_facebook_detail_page.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_new_event_friend_selection.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.ArrayList

class NewEventFriendSelection : AppCompatActivity() {

    val sortUsers = ArrayList<SelectionFriendsModel>()
    lateinit var adapter:SelectionFriendListAdapter
    var imagesList: ArrayList<String> = ArrayList()
    var eventName: String = "null"
    var smallBlind: String = "null"
    var bigBlind: String = "null"
    var minBuyIn: String = "null"
    var maxBuyIn: String = "null"
    var minPlayer: String = "null"
    var maxPlayer: String = "null"
    var gameProfile: String = "null"
    var gameType: String = "null"
    var amount: String = "null"
    var noRebuy: String = "null"
    var tableRules: String = "null"
    var gameDate: String = "null"
    var votingOptions: Boolean = false
    lateinit var startDate: String
    lateinit var endDate: String

    val jsonArrayUsers = JSONArray()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_event_friend_selection)
        supportActionBar!!.title = "Invite your friends"
        val doubleBounce = DoubleBounce()
        spin_kit_selection.setIndeterminateDrawable(doubleBounce)
        imagesList = intent.getStringArrayListExtra("images")
        gameType = intent.getStringExtra("game_type")
        gameDate = intent.getStringExtra("game_date")
        gameProfile = intent.getStringExtra("game_profile")
        eventName = intent.getStringExtra("event_name")
        minPlayer = intent.getStringExtra("min_player")
        maxPlayer = intent.getStringExtra("max_player")
        tableRules = intent.getStringExtra("table_rules")
        supportActionBar!!.title = eventName
            smallBlind = intent.getStringExtra("small_blind")
            bigBlind = intent.getStringExtra("big_blind")
            minBuyIn = intent.getStringExtra("min_buy_in")
            maxBuyIn = intent.getStringExtra("max_buy_in")
            amount = intent.getStringExtra("amount")
            noRebuy = intent.getStringExtra("no_of_rebuy")
            startDate = intent.getStringExtra("start_date")
            endDate = intent.getStringExtra("end_date")
            votingOptions = intent.getBooleanExtra("voting_option",false)

        val recyclerview = findViewById(R.id.recyclerview_selection_friends) as RecyclerView

        recyclerview.layoutManager = LinearLayoutManager(this)

        getUserData(userAccessToken)
         adapter = SelectionFriendListAdapter(this,sortUsers, object : SelectionFriendListAdapter.MyClickListener {
            override fun onClick(position: Int) {

//                jsonArrayUsers.put(users.get(position).friendId)
                Toast.makeText(applicationContext, arrayListUsers.size.toString(), Toast.LENGTH_LONG).show()
//                val intent = Intent(context,EventDetailPlusJoinEvent::class.java)
//                intent.putExtra("event_id", users[position].id)

//                startActivity(intent)



            }

            override fun onLongClick(position: Int) {
//                Toast.makeText(context, "You got a long click it", Toast.LENGTH_LONG).show()
            }

        })
        recyclerview.adapter = adapter
        search_select_friend.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                sortUsers.clear()
                if(s!!.length!=0) {
                    for (i in 0..usersData.size - 1) {
                        var user = usersData.get(i)
                        if (user.friendName.contains(s!!, true)) {
                            sortUsers.add(user)
                        }
                    }
                }else{
                   sortUsers.addAll(usersData)
                }

                adapter.notifyDataSetChanged()


            }


        })
        btn_create_event_friend.setOnClickListener {
//            Toast.makeText(applicationContext, arrayListUsers.get(0), Toast.LENGTH_LONG).show()
            serverCallCheckPrivateEvent("Bearer "+userAccessToken)

        }


    }


    fun getUserData(tokenAccess:String) {
        spin_kit_selection.visibility = View.VISIBLE
        sortUsers.clear()
        usersData.clear()
        val URL = resources.getString(R.string.base_url) +"/user/friends"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            // Your success code here
            try {
                val jsonObject = JSONObject(s)
                val jasonarray = jsonObject.getJSONArray("data")

                for (i in 0..jasonarray!!.length() - 1){
                    val jsonObj = jasonarray.getJSONObject(i)
                    val id:Int = jsonObj.getInt("id")
                    val firstName = jsonObj.getString("first_name")
                    val lastName = jsonObj.getString("last_name")
                    var avatar = jsonObj.get("avatar")
                    usersData.add(SelectionFriendsModel(id,firstName +" "+lastName,avatar, false))
                }
                if (adapter!=null) {
                    adapter.notifyDataSetChanged()
                    sortUsers.addAll(usersData)
                    spin_kit_selection.visibility = View.GONE
                }
            }catch (e:Exception){
                spin_kit_selection.visibility = View.GONE
                Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
            }
        }, Response.ErrorListener { e ->

//          Toast.makeText(applicationContext,"Please fill your information",Toast.LENGTH_LONG).show()
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json" , "Authorization" to "Bearer " +tokenAccess)

        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }
    fun serverCallCheckPrivateEvent(tokenAccess:String) {

        spin_kit_selection.visibility = View.VISIBLE
        val jsonArrayUsers = JSONArray()
//        jsonArrayUsers.put("2")
//        jsonArrayUsers.put("4")
        for(i in 0..arrayListUsers!!.size-1){
            jsonArrayUsers.put(arrayListUsers.get(i))
        }
        val jsonArrayImages = JSONArray()
        for (item in imagesList) {
            val imageStream = getContentResolver().openInputStream(Uri.parse(item))
            val selectedImage = BitmapFactory.decodeStream(imageStream)
            var imageString = ImageHelperJava.encodeImage(selectedImage)
            imageString = imageString.replace("\n", "")
            jsonArrayImages.put(imageString)
        }

        val params = JSONObject()
        val params2 = JSONObject()
        params2.put("lat", AddLocation.location_lat)
        params2.put("lng", AddLocation.location_lon)
        params2.put("address",locationAddress)
        params2.put("images",jsonArrayImages)
//        params2.put("images",array)
        params.put("name", eventName)
        params.put("game_profile", gameProfile)
        params.put("game_type", gameType)
//        params.put("game_type", "both")
        params.put("isPrivate", "1")
        params.put("max_players", maxPlayer)
        params.put("min_players", minPlayer)
        params.put("table_rules", tableRules)
        params.put("game_date", gameDate)
        params.put("purchase_amount", amount)
        params.put("re_buyins", noRebuy)
        params.put("small_blind", smallBlind)
        params.put("big_blind", bigBlind)
        params.put("max_buyins", maxBuyIn)
        params.put("min_buyins", minBuyIn)
        params.put("votingEnabled", votingOptions)
        params.put("valid_start_date", startDate.replace("/","-"))
        params.put("valid_end_date", endDate.replace("/","-"))
        params.put("users", jsonArrayUsers)
        params.put("location", params2)

        val json = params.toString()

        val URL = resources.getString(R.string.base_url) +"/user/event"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : JsonObjectRequest(Request.Method.POST, URL, params,Response.Listener { s ->
            // Your success code here
//            Toast.makeText(applicationContext,s.toString(),Toast.LENGTH_LONG).show()
//            spin_kit.visibility = View.GONEs
//            val json = s.getString("message")
            spin_kit_selection.visibility = View.GONE
            startActivity(Intent(this@NewEventFriendSelection,MainActivity::class.java))
            finish()
            arrayListUsers.clear()

        }, Response.ErrorListener { e ->
            spin_kit_selection.visibility = View.GONE
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json" , "Authorization" to tokenAccess)

        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }
    companion object {
        val usersData = ArrayList<SelectionFriendsModel>()
    }
}
