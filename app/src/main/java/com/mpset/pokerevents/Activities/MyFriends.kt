package com.mpset.pokerevents.Activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mpset.pokerevents.Adapters.MyFriendsAdapter
import com.mpset.pokerevents.Adapters.UpcomingListAdapter
import com.mpset.pokerevents.Helper.DBHelper
import com.mpset.pokerevents.Model.MyFriendsModel
import com.mpset.pokerevents.Model.SelectionFriendsModel
import com.mpset.pokerevents.Model.UpcomingModel
import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import com.mpset.pokerevents.SplashScreen.Companion.userId
import kotlinx.android.synthetic.main.activity_my_friends.*
import kotlinx.android.synthetic.main.activity_new_event_friend_selection.*
import org.json.JSONObject
import java.text.FieldPosition
import java.util.ArrayList

class MyFriends : AppCompatActivity() {
    val sortFriends = ArrayList<MyFriendsModel>()
    val usersFriends = ArrayList<MyFriendsModel>()
    lateinit var adapterFriends:MyFriendsAdapter
    lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_friends)
        supportActionBar!!.title = "My Friends"
        dbHelper = DBHelper(this)
       val recyclerView = findViewById<RecyclerView>(R.id.recyclerview_my_friends)
        recyclerView.layoutManager = LinearLayoutManager(this)
        getUserFriends(userAccessToken)

        adapterFriends = MyFriendsAdapter(this,sortFriends, object : MyFriendsAdapter.MyClickListener {
            override fun onClick(position: Int) {
//                Toast.makeText(, users[position].id, Toast.LENGTH_LONG).show()
                val intent = Intent(applicationContext,ViewProfile::class.java)
                intent.putExtra("friend_id", sortFriends[position].friendId)
                startActivity(intent)


            }

            override fun onLongClick(position: Int) {
//                Toast.makeText(context, "You got a long click it", Toast.LENGTH_LONG).show()
            }

            override fun onItemClickViewProfile(position: Int) {
                val intent = Intent(applicationContext,ViewProfile::class.java)
                intent.putExtra("friend_id", sortFriends[position].friendId)
                startActivity(intent)
            }

            override fun onItemClickChat(position: Int) {
                if (dbHelper.checkIsDataAlreadyInDBorNot(userId.toInt(), sortFriends.get(position).friendId)) {
//                    Toast.makeText(applicationContext, "true", Toast.LENGTH_LONG).show()
                    val id = dbHelper.getConversationId(userId.toInt(), sortFriends.get(position).friendId)
                    var conId = ""
                    if (id.getCount() > 0) {
                        id.moveToFirst()
                        conId = id.getString(id.getColumnIndex("Id"))
                    }
//                    Toast.makeText(applicationContext, conId, Toast.LENGTH_LONG).show()
                    funCallChat(conId.toInt(),position)
                } else {
                    dbHelper.insertDataConversation(userId.toInt(), sortFriends.get(position).friendId, "12/10/2019 2:10")
//                    Toast.makeText(applicationContext, "false", Toast.LENGTH_LONG).show()
                    val id = dbHelper.getConversationId(userId.toInt(), sortFriends.get(position).friendId)
                    var conId = ""
                    if (id.getCount() > 0) {
                        id.moveToFirst()
                        conId = id.getString(id.getColumnIndex("Id"))
                    }
//                    Toast.makeText(applicationContext, conId, Toast.LENGTH_LONG).show()
                    funCallChat(conId.toInt(),position)

                }

            }
            override fun onItemClickUnfriend(position: Int) {
                showDialogUnfriend(sortFriends.get(position).friendId,position)
            }

        })
        recyclerView.adapter = adapterFriends
        search_myfriends.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                sortFriends.clear()
                if(s!!.length!=0) {
                    for (i in 0 until usersFriends.size - 1) {
                        var user = usersFriends.get(i)
                        if (user.friendName.contains(s!!, true)) {
                            sortFriends.add(user)
                        }
                    }
                }else{
                    sortFriends.addAll(usersFriends)
                }

                adapterFriends.notifyDataSetChanged()


            }


        })
    }
    fun funCallChat(conId:Int,position: Int) {
        val intent = Intent(applicationContext, Chat::class.java)
        intent.putExtra("friend_id", sortFriends[position].friendId)
        intent.putExtra("friend_name", sortFriends[position].friendName)
        intent.putExtra("con_id", conId)
        intent.putExtra("friend_avatar", sortFriends[position].url)
        startActivity(intent)
    }
    fun getUserFriends(tokenAccess:String) {
        val URL = resources.getString(R.string.base_url) +"/user/friends"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            // Your success code here
            try {
                val jsonObject = JSONObject(s)
                val jasonarray = jsonObject.getJSONArray("data")
                if(jasonarray.length()==0){
                    empty_myfriends.visibility = View.VISIBLE
                }
                for (i in 0..jasonarray!!.length() - 1){
                    val jsonObj = jasonarray.getJSONObject(i)
                    val id:Int = jsonObj.getInt("id")
                    val firstName = jsonObj.getString("first_name")
                    val lastName = jsonObj.getString("last_name")
                    var avatar = jsonObj.getString("avatar")
                    usersFriends.add(MyFriendsModel(id,firstName +" "+lastName,avatar))
                }
                sortFriends.addAll(usersFriends)
                if (adapterFriends!=null) {
                    adapterFriends.notifyDataSetChanged()
                }
            }catch (e:Exception){
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
        override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_add_friend -> {
                startActivity(Intent(this,AddToFriend::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    fun unFriend(tokenAccess: String, id: Int,position: Int) {

        val URL = resources.getString(R.string.base_url) + "/user/friends/" + id
        val queue = Volley.newRequestQueue(applicationContext)

        val stringRequest = object : StringRequest(Request.Method.DELETE, URL, Response.Listener { s ->
            // Your success code here
            try {
                val jsonObject = JSONObject(s)
                val message = jsonObject.getString("message")
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                adapterFriends.notifyDataSetChanged()
                usersFriends.removeAt(position)
                sortFriends.removeAt(position)
                adapterFriends.notifyItemRemoved(position)
                adapterFriends.notifyItemRangeChanged(position,usersFriends.size)
                adapterFriends.notifyItemRangeChanged(position,sortFriends.size)


            } catch (e: Exception) {
                Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
            }
        }, Response.ErrorListener { e ->

            //          Toast.makeText(applicationContext,"Please fill your information",Toast.LENGTH_LONG).show()
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)

        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }
     fun showDialogUnfriend(id: Int, position: Int) {
        lateinit var dialog: AlertDialog
        val builder = AlertDialog.Builder(this@MyFriends)
        builder.setTitle("UnFriend")
        builder.setMessage("Do you really want to unfriend")

        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    unFriend(userAccessToken, id,position)
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

}
