package com.mpset.pokerevents.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.mpset.pokerevents.Adapters.AddFriendsAdapter
import com.mpset.pokerevents.Adapters.MyFriendsAdapter
import com.mpset.pokerevents.Model.AddFriendsModel
import com.mpset.pokerevents.Model.MyFriendsModel
import com.mpset.pokerevents.R
import kotlinx.android.synthetic.main.activity_add_to_friend.*
import java.util.ArrayList
import android.R.attr.name
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.ResolveInfo
import android.content.pm.PackageManager
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import org.json.JSONArray
import org.json.JSONObject
import android.support.v4.content.ContextCompat.startActivity
import android.text.Editable
import android.text.TextWatcher
import com.mpset.pokerevents.Helper.DBHelper
import com.mpset.pokerevents.SplashScreen
import com.mpset.pokerevents.SplashScreen.Companion.userId
import kotlinx.android.synthetic.main.activity_my_friends.*


class AddToFriend : AppCompatActivity() {
    val usersAddFriends = ArrayList<AddFriendsModel>()
    val sortUsersAddFriends = ArrayList<AddFriendsModel>()
    lateinit var adapterFriends: AddFriendsAdapter
    lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_friend)
        dbHelper = DBHelper(this)
        supportActionBar!!.title = "Add Friend"
        recyclerview_add_friends.layoutManager = LinearLayoutManager(this)
        getUserAddFriends(userAccessToken)
        adapterFriends = AddFriendsAdapter(this, sortUsersAddFriends, object : AddFriendsAdapter.MyClickListener {
            override fun onClickChat(position: Int) {
                if (dbHelper.checkIsDataAlreadyInDBorNot(userId.toInt(), sortUsersAddFriends.get(position).friendId)) {
//                    Toast.makeText(applicationContext, "true", Toast.LENGTH_LONG).show()
                    val id = dbHelper.getConversationId(userId.toInt(), sortUsersAddFriends.get(position).friendId)
                    var conId = ""
                    if (id.getCount() > 0) {
                        id.moveToFirst()
                        conId = id.getString(id.getColumnIndex("Id"))
                    }
//                    Toast.makeText(applicationContext, conId, Toast.LENGTH_LONG).show()
                    funCallChat(conId.toInt(),position)
                } else {
                    dbHelper.insertDataConversation(userId.toInt(), sortUsersAddFriends.get(position).friendId, "12/10/2019 2:10")
//                    Toast.makeText(applicationContext, "false", Toast.LENGTH_LONG).show()
                    val id = dbHelper.getConversationId(userId.toInt(), sortUsersAddFriends.get(position).friendId)
                    var conId = ""
                    if (id.getCount() > 0) {
                        id.moveToFirst()
                         conId = id.getString(id.getColumnIndex("Id"))
                    }
//                    Toast.makeText(applicationContext, conId, Toast.LENGTH_LONG).show()
                    funCallChat(conId.toInt(),position)
                }
            }

            override fun onClick(position: Int) {
//                Toast.makeText(, users[position].id, Toast.LENGTH_LONG).show()
                val intent = Intent(applicationContext,ViewProfile::class.java)
                intent.putExtra("friend_id", sortUsersAddFriends[position].friendId)
                startActivity(intent)


            }

            override fun onLongClick(position: Int) {
//                Toast.makeText(context, "You got a long click it", Toast.LENGTH_LONG).show()
            }

            override fun onClickAddFriend(position: Int) {
                addFriend(userAccessToken, sortUsersAddFriends.get(position).friendId, position)
                usersAddFriends.get(position).friendshipStatus = "pending_acceptance"
                sortUsersAddFriends.get(position).friendshipStatus = "pending_acceptance"
                adapterFriends.notifyItemChanged(position)
            }

            override fun onClickDeclineReq(position: Int) {
                declineFriendRequest(userAccessToken, sortUsersAddFriends.get(position).friendId, position)
                usersAddFriends.get(position).friendshipStatus = "not_friend"
                sortUsersAddFriends.get(position).friendshipStatus = "not_friend"
                adapterFriends.notifyItemChanged(position)
            }

            override fun onClickAcceptReq(position: Int) {
                acceptFriendRequest(userAccessToken, sortUsersAddFriends.get(position).friendId, position)
                usersAddFriends.get(position).friendshipStatus = "already_friend"
                sortUsersAddFriends.get(position).friendshipStatus = "already_friend"
                adapterFriends.notifyItemChanged(position)
            }

            override fun onClickCancelReq(position: Int) {
                cancelFriendRequest(userAccessToken, sortUsersAddFriends.get(position).friendId, position)
                usersAddFriends.get(position).friendshipStatus = "not_friend"
                sortUsersAddFriends.get(position).friendshipStatus = "not_friend"
                adapterFriends.notifyItemChanged(position)
            }

            override fun onClickUnfriend(position: Int) {
                showDialogUnfriend(sortUsersAddFriends.get(position).friendId, position)
            }
        })
        recyclerview_add_friends.adapter = adapterFriends

        email_send_invitation.setOnClickListener {
            val intent = Intent(android.content.Intent.ACTION_SEND)
            intent.type = "text/plain"
            val pm = packageManager
            val matches = pm.queryIntentActivities(intent, 0)
            var best: ResolveInfo? = null
            for (info in matches)
                if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                    best = info
            if (best != null)
                intent.setClassName(best.activityInfo.packageName, best.activityInfo.name)
            intent.putExtra(Intent.ACTION_SEND, "support@mpset.co")
            intent.putExtra(Intent.EXTRA_SUBJECT, "Friend Invitation")
            intent.putExtra(Intent.EXTRA_TEXT, "Please join this app \n" +
                    "https://play.google.com/store/apps/details?id=" + packageName)
            startActivity(intent)
        }
        sms_send_invitation.setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_VIEW)
            sendIntent.data = Uri.parse("sms:")
            sendIntent.putExtra("sms_body", "Please join this app \n" +
                    "https://play.google.com/store/apps/details?id=" + packageName)
            startActivity(sendIntent)
        }
        whatsapp_send_invitation.setOnClickListener {
            val whatsappIntent = Intent(Intent.ACTION_SEND)
            whatsappIntent.type = "text/plain"
            whatsappIntent.setPackage("com.whatsapp")
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Please join this app \n" +
                    "https://play.google.com/store/apps/details?id=" + packageName)
            try {
                startActivity(whatsappIntent)
            } catch (ex: android.content.ActivityNotFoundException) {
                Toast.makeText(applicationContext, "Whatsapp have not been installed.", Toast.LENGTH_LONG).show()
            }

        }
        search_add_friend.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                sortUsersAddFriends.clear()
                if (s!!.length != 0) {
                    for (i in 0 until usersAddFriends.size - 1) {
                        var user = usersAddFriends.get(i)
                        if (user.friendName.contains(s!!, true)) {
                            sortUsersAddFriends.add(user)
                        }
                    }
                } else {
                    sortUsersAddFriends.addAll(usersAddFriends)
                }

                adapterFriends.notifyDataSetChanged()


            }


        })
    }

    fun funCallChat(conId:Int,position: Int) {
        if (dbHelper.checkIsUserAlreadyInDBorNot(sortUsersAddFriends[position].friendId))

        else
            dbHelper.insertDataUser(sortUsersAddFriends[position].friendId, sortUsersAddFriends[position].friendName, sortUsersAddFriends[position].url, Chat.getCurrentTime())
        val intent = Intent(applicationContext, Chat::class.java)
        intent.putExtra("friend_id", sortUsersAddFriends[position].friendId)
        intent.putExtra("friend_name", sortUsersAddFriends[position].friendName)
        intent.putExtra("con_id", conId)
        intent.putExtra("friend_avatar", sortUsersAddFriends[position].url)
        startActivity(intent)
    }

    fun getUserAddFriends(tokenAccess: String) {
        val URL = resources.getString(R.string.base_url) + "/user/getUsersWithFriendshipStatus"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            // Your success code here
            try {
                val jasonarray = JSONArray(s)

                for (i in 0..jasonarray!!.length() - 1) {
                    val jsonObj = jasonarray.getJSONObject(i)
                    val id: Int = jsonObj.getInt("id")
                    val firstName = jsonObj.getString("first_name")
                    val lastName = jsonObj.getString("last_name")
                    var avatar = jsonObj.getString("avatar")
                    var friendshipStatus = jsonObj.getString("friendshipStatus")
                    usersAddFriends.add(AddFriendsModel(id, firstName + " " + lastName, avatar, friendshipStatus))
                }
                if (adapterFriends != null) {
                    adapterFriends.notifyDataSetChanged()
                    sortUsersAddFriends.addAll(usersAddFriends)

                }
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

    fun unFriend(tokenAccess: String, id: Int, position: Int) {

        val URL = resources.getString(R.string.base_url) + "/user/friends/" + id
        val queue = Volley.newRequestQueue(applicationContext)

        val stringRequest = object : StringRequest(Request.Method.DELETE, URL, Response.Listener { s ->
            // Your success code here
            try {
                val jsonObject = JSONObject(s)
                val message = jsonObject.getString("message")
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                adapterFriends.notifyDataSetChanged()
                usersAddFriends.removeAt(position)
                sortUsersAddFriends.removeAt(position)
                adapterFriends.notifyItemRemoved(position)
                adapterFriends.notifyItemRangeChanged(position, usersAddFriends.size)
                adapterFriends.notifyItemRangeChanged(position, sortUsersAddFriends.size)


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
        val builder = AlertDialog.Builder(this@AddToFriend)
        builder.setTitle("UnFriend")
        builder.setMessage("Do you really want to unfriend")

        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    unFriend(userAccessToken, id, position)
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

    fun addFriend(tokenAccess: String, id: Int, position: Int) {

        val URL = resources.getString(R.string.base_url) + "/user/friends/" + id
        val queue = Volley.newRequestQueue(applicationContext)

        val stringRequest = object : StringRequest(Request.Method.POST, URL, Response.Listener { s ->
            // Your success code here
            try {
                val jsonObject = JSONObject(s)
                val message = jsonObject.getString("message")
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()


            } catch (e: Exception) {
                Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
                usersAddFriends.get(position).friendshipStatus = "not_friend"
                sortUsersAddFriends.get(position).friendshipStatus = "not_friend"
                adapterFriends.notifyItemChanged(position)
            }
        }, Response.ErrorListener { e ->

            //          Toast.makeText(applicationContext,"Please fill your information",Toast.LENGTH_LONG).show()
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
            usersAddFriends.get(position).friendshipStatus = "not_friend"
            sortUsersAddFriends.get(position).friendshipStatus = "not_friend"
            adapterFriends.notifyItemChanged(position)
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)

        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }

    fun acceptFriendRequest(tokenAccess: String, id: Int, position: Int) {

        val URL = resources.getString(R.string.base_url) + "/user/friends/accept/" + id
        val queue = Volley.newRequestQueue(applicationContext)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            // Your success code here
            try {
                val jsonObject = JSONObject(s)
                val message = jsonObject.getString("message")
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()


            } catch (e: Exception) {
                Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
                usersAddFriends.get(position).friendshipStatus = "requested_friendsip"
                sortUsersAddFriends.get(position).friendshipStatus = "requested_friendsip"
                adapterFriends.notifyItemChanged(position)
            }
        }, Response.ErrorListener { e ->

            //          Toast.makeText(applicationContext,"Please fill your information",Toast.LENGTH_LONG).show()
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
            usersAddFriends.get(position).friendshipStatus = "requested_friendsip"
            sortUsersAddFriends.get(position).friendshipStatus = "requested_friendsip"
            adapterFriends.notifyItemChanged(position)
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)

        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }

    fun cancelFriendRequest(tokenAccess: String, id: Int, position: Int) {

        val URL = resources.getString(R.string.base_url) + "/user/friends/cancel/" + id
        val queue = Volley.newRequestQueue(applicationContext)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            // Your success code here
            try {
                val jsonObject = JSONObject(s)
                val message = jsonObject.getString("message")
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()


            } catch (e: Exception) {
                Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
                usersAddFriends.get(position).friendshipStatus = "pending_acceptance"
                sortUsersAddFriends.get(position).friendshipStatus = "pending_acceptance"
                adapterFriends.notifyItemChanged(position)
            }
        }, Response.ErrorListener { e ->

            //          Toast.makeText(applicationContext,"Please fill your information",Toast.LENGTH_LONG).show()
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
            usersAddFriends.get(position).friendshipStatus = "pending_acceptance"
            sortUsersAddFriends.get(position).friendshipStatus = "pending_acceptance"
            adapterFriends.notifyItemChanged(position)
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)

        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }

    fun declineFriendRequest(tokenAccess: String, id: Int, position: Int) {

        val URL = resources.getString(R.string.base_url) + "/user/friends/decline/" + id
        val queue = Volley.newRequestQueue(applicationContext)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            // Your success code here
            try {
                val jsonObject = JSONObject(s)
                val message = jsonObject.getString("message")
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
                usersAddFriends.get(position).friendshipStatus = "not_friend"
                sortUsersAddFriends.get(position).friendshipStatus = "not_friend"
                adapterFriends.notifyItemChanged(position)
            }
        }, Response.ErrorListener { e ->
            //Toast.makeText(applicationContext,"Please fill your information",Toast.LENGTH_LONG).show()
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
            usersAddFriends.get(position).friendshipStatus = "not_friend"
            sortUsersAddFriends.get(position).friendshipStatus = "not_friend"
            adapterFriends.notifyItemChanged(position)
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)
    }
//    fun getConId(id: Cursor): Int {
//        val conId = null
//        return conId!!.toInt()
//    }
}
