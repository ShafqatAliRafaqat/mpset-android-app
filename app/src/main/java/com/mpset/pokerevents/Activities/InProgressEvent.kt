package com.mpset.pokerevents.Activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.facebook.AccessToken
import com.mpset.pokerevents.Adapters.InProgressHostListAdapter
import com.mpset.pokerevents.Adapters.InProgressListAdapter
import com.mpset.pokerevents.Adapters.SelectionFriendListAdapter
import com.mpset.pokerevents.MainActivity
import com.mpset.pokerevents.Model.InProgressBottom
import com.mpset.pokerevents.Model.InProgressHost
import com.mpset.pokerevents.Model.SelectionFriendsModel
import com.mpset.pokerevents.R

import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import com.mpset.pokerevents.SplashScreen.Companion.userId
import kotlinx.android.synthetic.main.activity_create_event_game_type.*
import kotlinx.android.synthetic.main.activity_dialog_table_rules.*
import kotlinx.android.synthetic.main.activity_in_progress_event.*
import kotlinx.android.synthetic.main.activity_join_event_detail_page.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_snipped_inprogress_add_freind.*
import kotlinx.android.synthetic.main.snipped_table_rules.*
import org.json.JSONObject
import java.util.ArrayList

class InProgressEvent : AppCompatActivity() {
    lateinit var myDialog: Dialog
    val users2 = ArrayList<InProgressHost>()
    val usersPlaced = ArrayList<InProgressHost>()
    lateinit var adapterInProgress: InProgressHostListAdapter
    var id = 0
    var hostId = 0
    var seatNo = 0
    var eventName = ""
    var tableRules = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_progress_event)
        myDialog = Dialog(this)
        id = intent.getIntExtra("event_id", 0)
        eventName = intent.getStringExtra("event_name")
        supportActionBar!!.title = eventName
        adapterInProgress = InProgressHostListAdapter(this, users2, object : InProgressHostListAdapter.MyClickListener {
            override fun onClick(position: Int) {
                addToSeat(seatNo, userAccessToken, users2.get(position).userId, position)
            }

            override fun onLongClick(position: Int) {
            }
        })
        getSpeciedEvent(userAccessToken)

        audit_title.setOnClickListener {
            val intent = Intent(this@InProgressEvent, AuditInProgress::class.java)
            intent.putExtra("event_id", id)
            startActivity(intent)
        }
        text_table.setOnClickListener {

            ShowPopupTableRules(it)

        }

        player1.setOnClickListener {
            if (hostId == userId.toInt()) {
                ShowPopup(it)
                seatNo = 1
            } else
                Toast.makeText(applicationContext, "player", Toast.LENGTH_LONG).show()
        }
        player2.setOnClickListener {
            if (hostId == userId.toInt()) {
                ShowPopup(it)
                seatNo = 2
            }

        }
        player3.setOnClickListener {
            if (hostId == userId.toInt()) {
                ShowPopup(it)
                seatNo = 3
            }
        }
        player4.setOnClickListener {
            if (hostId == userId.toInt()) {
                ShowPopup(it)
                seatNo = 4
            }
        }
        player5.setOnClickListener {
            if (hostId == userId.toInt()) {
                ShowPopup(it)
                seatNo = 5
            }
        }
        player6.setOnClickListener {
            if (hostId == userId.toInt()) {
                ShowPopup(it)
                seatNo = 6
            }
        }
        player7.setOnClickListener {
            if (hostId == userId.toInt()) {
                ShowPopup(it)
                seatNo = 7
            }
        }
        player8.setOnClickListener {
            if (hostId == userId.toInt()) {
                ShowPopup(it)
                seatNo = 8
            }
        }
        player9.setOnClickListener {
            if (hostId == userId.toInt()) {
                ShowPopup(it)
                seatNo = 9
            }
        }
        player10.setOnClickListener {
            if (hostId == userId.toInt()) {
                ShowPopup(it)
                seatNo = 10
            }
        }
        player1_remove.setOnClickListener {
            removeToSeat(0, userAccessToken, findUid(1), findPosition(1))
        }
        player2_remove.setOnClickListener {
            removeToSeat(0, userAccessToken, findUid(2), findPosition(2))
        }
        player3_remove.setOnClickListener {
            removeToSeat(0, userAccessToken, findUid(3), findPosition(3))
        }
        player4_remove.setOnClickListener {
            removeToSeat(0, userAccessToken, findUid(4), findPosition(4))
        }
        player5_remove.setOnClickListener {
            removeToSeat(0, userAccessToken, findUid(5), findPosition(5))
        }
        player6_remove.setOnClickListener {
            removeToSeat(0, userAccessToken, findUid(6), findPosition(6))
        }
        player7_remove.setOnClickListener {
            removeToSeat(0, userAccessToken, findUid(7), findPosition(7))
        }
        player8_remove.setOnClickListener {
            removeToSeat(0, userAccessToken, findUid(8), findPosition(8))
        }
        player9_remove.setOnClickListener {
            removeToSeat(0, userAccessToken, findUid(9), findPosition(9))
        }
        player10_remove.setOnClickListener {
            removeToSeat(0, userAccessToken, findUid(10), findPosition(10))
        }
    }

    fun ShowPopup(v: View) {
        myDialog.setContentView(R.layout.activity_snipped_inprogress_add_freind)
        myDialog.show()
        myDialog.recycler_inprogress_host.layoutManager = LinearLayoutManager(this)
        myDialog.recycler_inprogress_host.adapter = adapterInProgress
        myDialog.btn_add_friend.setOnClickListener {
            myDialog.cancel()
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

    fun getSpeciedEvent(tokenAccess: String) {
        val URL = resources.getString(R.string.base_url) + "/user/event/" + id
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            try {
//                Toast.makeText(context,s,Toast.LENGTH_LONG).show()
                val jsonObject = JSONObject(s)
                val jsonObj = jsonObject.getJSONObject("data")
                var gameDateTime = jsonObj.getString("date_string")
                tableRules = jsonObj.getString("table_rules")
                val stat = jsonObj.getJSONObject("stats")
                val buyings = stat.getString("buyins")
                val homePayment = stat.getString("checkout")
                val gameStatus = jsonObj.getString("status")
                var host = jsonObj.getJSONObject("host")
                hostId = host.getInt("id")
                val address = jsonObj.getString("address")
                val users = jsonObj.getJSONArray("users")
                for (i in 0 until users.length()) {
                    val userData = users.getJSONObject(i)
                    val id = userData.getInt("id")
                    val nickName = userData.getString("nick_name")
                    val avatar = userData.getString("avatar")
                    val pivot = userData.getJSONObject("pivot")
                    val seatNo = pivot.getInt("seat_no")
                    if (seatNo == 0)
                        users2.add(InProgressHost(id, nickName, avatar, seatNo))
                    else
                        usersPlaced.add(InProgressHost(id, nickName, avatar, seatNo))

                    if (adapterInProgress != null)
                        adapterInProgress.notifyDataSetChanged()
                }

                address_inprogress.text = address
                date_inprogresss.text = gameDateTime
                status_inprogress.text = gameStatus
                buyins_inprogress.text = buyings
                home_payment_inprogress.text = homePayment
                placeImageLoop()
            } catch (e: Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            }
        }, Response.ErrorListener { e ->
            //Toast.makeText(applicationContext,"Please fill your information",Toast.LENGTH_LONG).show()
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)

        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }

    fun placeImageLoop() {
        removeAllPic()
        for (i in 0 until usersPlaced.size) {
            placeOnSeat(usersPlaced.get(i).userId, usersPlaced.get(i).userName, usersPlaced.get(i).url, usersPlaced.get(i).seat_no)
            if (adapterInProgress != null)
                adapterInProgress.notifyDataSetChanged()
        }

    }

    private fun placeOnSeat(id: Int, nickName: String?, avatar: String?, seatNo: Int) {
        if (seatNo == 1) {
            Glide.with(applicationContext).load(avatar).into(player1)
            player1_remove.visibility = View.VISIBLE
        }
        if (seatNo == 2) {
            Glide.with(applicationContext).load(avatar).into(player2)
            player2_remove.visibility = View.VISIBLE
        }
        if (seatNo == 3) {
            Glide.with(applicationContext).load(avatar).into(player3)
            player3_remove.visibility = View.VISIBLE
        }
        if (seatNo == 4) {
            Glide.with(applicationContext).load(avatar).into(player4)
            player4_remove.visibility = View.VISIBLE
        }
        if (seatNo == 5) {
            Glide.with(applicationContext).load(avatar).into(player5)
            player5_remove.visibility = View.VISIBLE
        }
        if (seatNo == 6) {
            Glide.with(applicationContext).load(avatar).into(player6)
            player6_remove.visibility = View.VISIBLE
        }
        if (seatNo == 7) {
            Glide.with(applicationContext).load(avatar).into(player7)
            player7_remove.visibility = View.VISIBLE
        }
        if (seatNo == 8) {
            Glide.with(applicationContext).load(avatar).into(player8)
            player8_remove.visibility = View.VISIBLE
        }
        if (seatNo == 9) {
            Glide.with(applicationContext).load(avatar).into(player9)
            player9_remove.visibility = View.VISIBLE
        }
        if (seatNo == 10) {
            Glide.with(applicationContext).load(avatar).into(player1)
            player10_remove.visibility = View.VISIBLE
        }
        adapterInProgress.notifyDataSetChanged()

    }

    fun addToSeat(seatNo: Int, tokenAccess: String, uid: Int, position: Int) {
        val URL = resources.getString(R.string.base_url) + "/user/event/placePlayerOnSeat"
        val params = JSONObject()

        params.put("event_id", id)
        params.put("user_id", uid)
        params.put("seat_no", seatNo)
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : JsonObjectRequest(Request.Method.POST, URL, params, Response.Listener { s ->
            val message = s.getString("message")
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            usersPlaced.add(InProgressHost( users2.get(position).userId, users2.get(position).userName, users2.get(position).url, seatNo))
            users2.removeAt(position)
            placeImageLoop()
            myDialog.cancel()
            if (adapterInProgress != null)
                adapterInProgress.notifyDataSetChanged()
        }, Response.ErrorListener { e ->
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)

        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)
    }
    fun removeToSeat(seatNo: Int, tokenAccess: String, uid: Int, position: Int) {
        val URL = resources.getString(R.string.base_url) + "/user/event/placePlayerOnSeat"
        val params = JSONObject()

        params.put("event_id", id)
        params.put("user_id", uid)
        params.put("seat_no", seatNo)
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : JsonObjectRequest(Request.Method.POST, URL, params, Response.Listener { s ->
            Toast.makeText(applicationContext, "remove successfully", Toast.LENGTH_LONG).show()
            users2.add(InProgressHost(usersPlaced.get(position).userId, usersPlaced.get(position).userName, usersPlaced.get(position).url, seatNo))
            usersPlaced.removeAt(position)
            placeImageLoop()
//            if (adapterInProgress != null)
//                adapterInProgress.notifyDataSetChanged()
        }, Response.ErrorListener { e ->
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)

        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)
    }

    fun findUid(seatNo: Int):Int {
        var uid = 0
        for (i in 0 until usersPlaced.size) {
            if (usersPlaced.get(i).seat_no == seatNo) {
                uid =  usersPlaced.get(i).userId
            }
        }
        return uid

    }
    fun findPosition(seatNo: Int):Int {
        var position = 0
        for (i in 0 until usersPlaced.size) {
            if (usersPlaced.get(i).seat_no == seatNo) {
                position =  i
            }
        }
        return position

    }
    fun removeAllPic(){
        Glide.with(applicationContext).load(R.drawable.adduser).into(player1)
        Glide.with(applicationContext).load(R.drawable.adduser).into(player2)
        Glide.with(applicationContext).load(R.drawable.adduser).into(player3)
        Glide.with(applicationContext).load(R.drawable.adduser).into(player4)
        Glide.with(applicationContext).load(R.drawable.adduser).into(player5)
        Glide.with(applicationContext).load(R.drawable.adduser).into(player6)
        Glide.with(applicationContext).load(R.drawable.adduser).into(player7)
        Glide.with(applicationContext).load(R.drawable.adduser).into(player8)
        Glide.with(applicationContext).load(R.drawable.adduser).into(player9)
        Glide.with(applicationContext).load(R.drawable.adduser).into(player10)
        player1_remove.visibility = View.GONE
        player2_remove.visibility = View.GONE
        player3_remove.visibility = View.GONE
        player4_remove.visibility = View.GONE
        player5_remove.visibility = View.GONE
        player6_remove.visibility = View.GONE
        player7_remove.visibility = View.GONE
        player8_remove.visibility = View.GONE
        player9_remove.visibility = View.GONE
        player10_remove.visibility = View.GONE
    }

}
