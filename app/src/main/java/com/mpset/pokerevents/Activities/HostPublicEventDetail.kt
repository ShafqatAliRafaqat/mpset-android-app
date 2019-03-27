package com.mpset.pokerevents.Activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mpset.pokerevents.Adapters.PendingHostPrivateListAdapter
import com.mpset.pokerevents.Adapters.PendingHostPublicListAdapter
import com.mpset.pokerevents.Helper.ImageHelperJava
import com.mpset.pokerevents.MainActivity
import com.mpset.pokerevents.Model.PendingHostPrivateModel
import com.mpset.pokerevents.Model.PendingHostPublicModel
import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import com.smarteist.autoimageslider.SliderLayout
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_create_event_game_type.*
import kotlinx.android.synthetic.main.activity_dialog_table_rules.*
import kotlinx.android.synthetic.main.activity_host_public_event_detail.*
import kotlinx.android.synthetic.main.snipped_update_date_time.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.FieldPosition
import java.text.SimpleDateFormat
import java.util.*


class HostPublicEventDetail : AppCompatActivity() {
    var imageslist: ArrayList<String> = ArrayList()
    lateinit var id: String
    lateinit var address: String
    lateinit var eventName: String
    lateinit var locationId: String
    lateinit var eventType: String
    lateinit var myDialog: Dialog
    val usersPublic = ArrayList<PendingHostPublicModel>()
    val usersPrivate = ArrayList<PendingHostPrivateModel>()
    lateinit var adapterPrivate: PendingHostPrivateListAdapter
    lateinit var adapterPublic: PendingHostPublicListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host_public_event_detail)
        supportActionBar!!.hide()
        eventName = intent.getStringExtra("event_name")
        id = intent.getStringExtra("event_id")
        eventType = intent.getStringExtra("event_type")
        title_hosting.text = eventName
        myDialog = Dialog(this)
        imageSlider_hosting!!.setIndicatorAnimation(SliderLayout.Animations.FILL)
        imageSlider_hosting!!.setScrollTimeInSec(1)
        recycler_hosting_public.layoutManager = LinearLayoutManager(this)
        recycler_hosting_private.layoutManager = LinearLayoutManager(this)
        getSpecifiedHostEvent(userAccessToken)



        adapterPrivate = PendingHostPrivateListAdapter(this, usersPrivate, object : PendingHostPrivateListAdapter.MyClickListener {
            override fun onClick(position: Int) {
//                Toast.makeText(, users[position].id, Toast.LENGTH_LONG).show()
//                val intent = Intent(context,EventDetailPlusJoinEvent::class.java)
//                intent.putExtra("event_id", users[position].id)
//                startActivity(intent)
            }

            override fun onLongClick(position: Int) {}

        })
        adapterPublic = PendingHostPublicListAdapter(this, usersPublic, object : PendingHostPublicListAdapter.MyClickListener {
            override fun onClickAcceptRequest(position: Int) {
                acceptRequest(userAccessToken, usersPublic.get(position).id,position,"accept")
            }

            override fun onClickRejectRequest(position: Int) {
                acceptRequest(userAccessToken, usersPublic.get(position).id,position,"reject")
            }

            override fun onClickViewProfile(position: Int) {
                val intent = Intent(applicationContext, ViewProfile::class.java)
                intent.putExtra("friend_id", usersPublic[position].id)
                startActivity(intent)
            }

            override fun onClick(position: Int) {
//                Toast.makeText(, users[position].id, Toast.LENGTH_LONG).show()
//                val intent = Intent(context,EventDetailPlusJoinEvent::class.java)
//                intent.putExtra("event_id", users[position].id)
//                startActivity(intent)
            }

            override fun onLongClick(position: Int) {
            }

        })
        recycler_hosting_public.adapter = adapterPublic
        recycler_hosting_private.adapter = adapterPrivate
        image_host_back.setOnClickListener {
            finish()
        }
        linear_cancel_event_hosting.setOnClickListener {
            showDialogCancelRequest(id.toInt())
        }
        linear_refresh.setOnClickListener {
            val intent = Intent(this, HostPublicEventDetail::class.java)
            intent.putExtra("event_id", id)
            intent.putExtra("event_name", eventName)
            intent.putExtra("event_type", eventType)
            startActivity(intent)
        }
        linear_start_end.setOnClickListener {
            if (text_start_hosting.text.equals("Start")) {
                text_start_hosting.text = "End"
                serverCallStartEvent(userAccessToken, "start", "", 122222)
            } else if (text_start_hosting.text.equals("End")) {
                serverCallStartEvent(userAccessToken, "end", "", 122222)
            }
        }
        update_table_rules_hosting.setOnClickListener {
            ShowPopup(it)
        }
        update_date_time.setOnClickListener {
            ShowPopupDateTime(it)
        }
        linear_update_event.setOnClickListener {
            val intent = Intent(this, UpdateEvent::class.java)
            intent.putExtra("event_id", id)
            intent.putExtra("event_name", eventName)
            intent.putExtra("images", imageslist)
            intent.putExtra("address", address)
            intent.putExtra("location_id", locationId)
            startActivity(intent)
        }
    }

    fun getSpecifiedHostEvent(tokenAccess: String) {
        val URL = resources.getString(R.string.base_url) + "/user/event/" + id
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            try {
//                Toast.makeText(context,s,Toast.LENGTH_LONG).show()
                val jsonObject = JSONObject(s)
                val jsonObj = jsonObject.getJSONObject("data")

//                val gameType = jsonObj.getString("game_type_string")
                var gameDateTime = jsonObj.getString("date_string")
//                var rating = jsonObj.getDouble("rating")
                var tableRules = jsonObj.getString("table_rules")
                val maxPlayer = jsonObj.getString("max_players")
                val minPlayer = jsonObj.getString("min_players")
                val publicStatus = jsonObj.getString("PublicStatus")
                val minBuyins = jsonObj.getString("min_buyins")
                val maxBuyins = jsonObj.getString("max_buyins")
                val totalPlayers = jsonObj.getString("no_of_players")
                val jsonObjLocation = jsonObj.getJSONObject("location")
                address = jsonObjLocation.getString("address")
                locationId = jsonObjLocation.getString("id")
                val jsonArrayImages = jsonObjLocation.getJSONArray("images")
                for (i in 0 until jsonArrayImages.length()) {
                    val pathObject = jsonArrayImages.getJSONObject(i)
                    val path = pathObject.getString("path")
                    imageslist.add(path)
                }
                var jsonArray = jsonObj.getJSONArray("users")
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val id = jsonObject.getInt("id")
                    val nickName = jsonObject.getString("nick_name")
                    val firstName = jsonObject.getString("first_name")
                    val lastName = jsonObject.getString("last_name")
                    val pivot = jsonObject.getJSONObject("pivot")
                    val isApproved = pivot.getInt("isApproved")
                    var startDate = pivot.getString("start_date")
                    var endDate = pivot.getString("end_date")
                    var isApprovedStatus: String
                    if (isApproved == 0)
                        isApprovedStatus = "pending"
                    else
                        isApprovedStatus = "voted"
                    if (startDate.equals("null") || endDate.equals("null")) {
                        startDate = ""
                        endDate = ""
                    }
                    usersPublic.add(PendingHostPublicModel(id, firstName + " " + lastName, isApprovedStatus, isApproved))
                    usersPrivate.add(PendingHostPrivateModel(id, firstName + " " + lastName, isApprovedStatus, id, startDate, endDate))
//                    usersDetails.add(ArchiveDetailsHostModel(id,firstName +" "+lastName, nickName))
//                    usersStat.add(ArchiveStatisticsHostModel(id,firstName +" "+lastName, nickName,buyins,checkout,0.0))
                    if (usersPublic != null || usersPrivate != null) {
                        adapterPrivate.notifyDataSetChanged()
                        adapterPublic.notifyDataSetChanged()
                    }
                }
                if (publicStatus.equals("Private")) {
                    eye_public_private_hosting_text.text = "Private"
                    eye_public_private_hosting_icon.setImageDrawable(resources.getDrawable(R.drawable.eyeprivate))
                    recycler_hosting_private.visibility = View.VISIBLE
                    recycler_hosting_public.visibility = View.GONE
                } else {
                    eye_public_private_hosting_icon.setImageDrawable(resources.getDrawable(R.drawable.eyewhite))
                    eye_public_private_hosting_text.text = "Public"
                    recycler_hosting_public.visibility = View.VISIBLE
                    recycler_hosting_private.visibility = View.GONE
                }
                location_hosting_detail.text = address
                date_hosting_detail.text = gameDateTime
                no_seats_hosting_detail.text = totalPlayers.toString()
                avail_seats_hosting_detail.text = totalPlayers.toString()
                min_max_buyin_hosting_detail.text = minBuyins.toString() + "/" + maxBuyins.toString()
                min_max_player_hosting_detail.text = minPlayer.toString() + "/" + maxPlayer.toString()
                table_rules_hosting_detail.text = tableRules
                setSliderViews()


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

    private fun setSliderViews() {

        for (i in 0 until imageslist.size) {
            val sliderView = SliderView(this)
            when (i) {
                i -> sliderView.imageUrl = imageslist.get(i)
            }
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
            imageSlider_hosting!!.addSliderView(sliderView)
        }
    }

    private fun showDialogCancelRequest(id: Int) {
        lateinit var dialog: AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cancel Hosting Event")
        builder.setMessage("Do you really want to cancel this event")

        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
//                    Toast.makeText(context, "Positive/Yes button clicked.", Toast.LENGTH_LONG).show()
                    cancelAttendingEvent(userAccessToken)
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

    fun cancelAttendingEvent(tokenAccess: String) {
        val params = JSONObject()
        params.put("event_id", id)
        params.put("status", "canceled")
        val URL = resources.getString(R.string.base_url) + "/user/event/update"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : JsonObjectRequest(Request.Method.POST, URL, params, Response.Listener { s ->
            val message = s.getString("message")
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            startActivity(Intent(this@HostPublicEventDetail, MainActivity::class.java))
            finish()

        }, Response.ErrorListener { e ->
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }

    fun serverCallStartEvent(tokenAccess: String, status: String, tableRules: String, date: Long) {
        val params = JSONObject()
        params.put("event_id", id)
        if (status.equals("start")) {
            params.put("status", "started")
        } else if (status.equals("end")) {
            params.put("status", "completed")
        } else if (status.equals("tableRules")) {
            params.put("table_rules", tableRules)
        } else {
            params.put("table_rules", date)
        }
        val URL = resources.getString(R.string.base_url) + "/user/event/update"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : JsonObjectRequest(Request.Method.POST, URL, params, Response.Listener { s ->
            val message = s.getString("message")
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            if (status.equals("end")) {
                startActivity(Intent(this@HostPublicEventDetail, MainActivity::class.java))
                finish()
            }
        }, Response.ErrorListener { e ->
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }

    fun ShowPopup(v: View) {
        var myDialog: Dialog = Dialog(this)
        myDialog.setContentView(R.layout.activity_dialog_table_rules)
        myDialog.show()
        myDialog.btn_submit_table_rules_host.setOnClickListener {
            if (myDialog.table_rules.text.toString().isEmpty()) {
                Toast.makeText(applicationContext, "Please enter table rules", Toast.LENGTH_LONG).show()
            } else {
                serverCallStartEvent(userAccessToken, "tableRules", myDialog.table_rules.text.toString(), 123333)
//                Toast.makeText(applicationContext, myDialog.table_rules.text.toString(), Toast.LENGTH_LONG).show()
                myDialog.cancel()
            }

        }
    }

    fun ShowPopupDateTime(v: View) {

        myDialog.setContentView(R.layout.snipped_update_date_time)
        myDialog.show()
        myDialog.date_update_host.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                myDialog.date_update_host.setText("" + dayOfMonth + "/" + (monthOfYear.toInt() + 1).toString() + "/" + year)
            }, year, month, day)
            dpd.show()
        }
        myDialog.time_update_host.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                myDialog.time_update_host.setText(SimpleDateFormat("H:mm").format(cal.time))
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
        myDialog.btn_submit_update_date_time.setOnClickListener {
            serverCallStartEvent(userAccessToken, "date", "", changeTimeMile())
            myDialog.cancel()
        }

    }

    fun changeTimeMile(): Long {
        val timeStamp = myDialog.date_update_host.text.toString() + " " + myDialog.time_update_host.text.toString()
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
        val mDate = sdf.parse(timeStamp)
        return (mDate.time) / 1000

    }

    fun acceptRequest(tokenAccess: String, uid: Int,position: Int,type:String) {
        var URL = ""
        val params = JSONObject()
        params.put("user_id", uid)
        if(type.equals("accept"))
         URL = resources.getString(R.string.base_url) + "/user/event/acceptPlayer/" + id
        else
        URL = resources.getString(R.string.base_url) + "/user/event/removePlayer/" + id
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : JsonObjectRequest(Request.Method.POST, URL, params, Response.Listener { s ->
            val json = s.getString("message")
            if (type.equals("accept")) {
                usersPublic.get(position).isApprove = 1
                adapterPublic.notifyItemChanged(position)
            }
            else {
                usersPublic.get(position).isApprove = 0
                adapterPublic.notifyItemRemoved(position)
                usersPublic.removeAt(position)
            }
            Toast.makeText(applicationContext, json, Toast.LENGTH_LONG).show()
        }, Response.ErrorListener { e ->
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)
    }
}
