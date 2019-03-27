package com.mpset.pokerevents.Fragments


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
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
import com.mpset.pokerevents.Activities.AttendingEventDetail
import com.mpset.pokerevents.Activities.InProgressEvent
import com.mpset.pokerevents.Adapters.AttendingListAdapter
import com.mpset.pokerevents.Adapters.UpcomingListAdapter
import com.mpset.pokerevents.Model.AttendingModel
import com.mpset.pokerevents.Model.UpcomingModel
import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import kotlinx.android.synthetic.main.fragment_attending.*
import kotlinx.android.synthetic.main.fragment_attending.view.*
import kotlinx.android.synthetic.main.fragment_up_comming.view.*
import org.json.JSONObject
import java.text.FieldPosition
import java.util.ArrayList


class Attending : Fragment() {

    val usersAttending = ArrayList<AttendingModel>()
    lateinit var adapterAttending: AttendingListAdapter
    var inProgressId: Int = 0
    lateinit var inProgressName: String
    lateinit var inProgressAddress: String
    lateinit var inProgressDate: String
    lateinit var inProgressGameType: String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_attending, container, false)
        val recyclerview = view.findViewById(R.id.recyclerview_attending) as RecyclerView
        val cardInProgress = view.findViewById(R.id.card_in_progress) as CardView
        recyclerview.layoutManager = LinearLayoutManager(context)
        getAttendingEvents(userAccessToken)

        adapterAttending = AttendingListAdapter(usersAttending, object : AttendingListAdapter.MyClickListener {
            override fun onClick(position: Int) {
                Toast.makeText(context, usersAttending[position].id, Toast.LENGTH_LONG).show()
                val intent = Intent(context, AttendingEventDetail::class.java)
                intent.putExtra("event_id", usersAttending[position].id)
                intent.putExtra("event_name", usersAttending[position].eventName)
                startActivity(intent)


            }

            override fun onLongClick(position: Int) {
                Toast.makeText(context, "You got a long click it", Toast.LENGTH_LONG).show()
            }

            override fun onCancel(position: Int) {
                Toast.makeText(context, "cancel", Toast.LENGTH_LONG).show()

                showDialog(usersAttending.get(position).id.toInt(), position)
//                adapterAttending.notifyItemRemoved(position)
//                adapterAttending.notifyDataSetChanged()
            }

        })
        recyclerview.adapter = adapterAttending

        cardInProgress.setOnClickListener {
            val intent = Intent(context, InProgressEvent::class.java)
            intent.putExtra("event_id", inProgressId)
            intent.putExtra("event_name", inProgressName)
            startActivity(intent)

        }
        return view
    }

    fun getAttendingEvents(tokenAccess: String) {
        usersAttending.clear()
        val URL = resources.getString(R.string.base_url) + "/user/event/attending"
        val queue = Volley.newRequestQueue(context)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            // Your success code here
            try {
//                Toast.makeText(context,s,Toast.LENGTH_LONG).show()
                val jsonObject = JSONObject(s)
                val jasonarray = jsonObject.getJSONArray("data")
                val jasonMeta = jsonObject.getJSONObject("meta")
                val jasonarrayInProgress = jasonMeta.getJSONArray("inProgress")
                if (jasonarray.length()==0)
                    view!!.empty_event_attending.visibility = View.VISIBLE
                for (i in 0..jasonarray!!.length() - 1) {

                    val jsonObj = jasonarray.getJSONObject(i)
                    val id: Int = jsonObj.getInt("id")
                    val gameType = jsonObj.getString("game_type_string")
                    var name = jsonObj.getString("name")
                    var gameDateTime = jsonObj.getString("date_string")
                    var publicStatus = jsonObj.getString("PublicStatus")
                    var address = jsonObj.getString("address")

                    usersAttending.add(AttendingModel(id.toString(), name, gameDateTime, address, gameType, publicStatus))
                }
                if (jasonarrayInProgress.length() > 0) {
                    val jsonObjectInprogres = jasonarrayInProgress.getJSONObject(0)
                    inProgressId = jsonObjectInprogres.getInt("id")
                    inProgressName = jsonObjectInprogres.getString("name")
                    inProgressGameType = jsonObjectInprogres.getString("PublicStatus")
                    inProgressDate = jsonObjectInprogres.getString("date_string")
                    inProgressAddress = jsonObjectInprogres.getString("address")
                    event_name_inprogress.text = inProgressName
                    location_event_inprogress.text = inProgressAddress
                    time_date_event_inprogress.text = inProgressDate
                    game_event_type_inprogress.text = inProgressGameType
                } else {
                    event_name_inprogress.text = "Currently You Are Not Playing Any Event."
                    card_in_progress.visibility = View.GONE
                    title_in_progress.visibility = View.GONE

                }


                if (context != null)
                    adapterAttending.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }, Response.ErrorListener { e ->

            //          Toast.makeText(applicationContext,"Please fill your information",Toast.LENGTH_LONG).show()
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)

        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }

    fun cancelAttendingEvent(tokenAccess: String, id: Int, itemPosition: Int) {
        usersAttending.clear()
        val URL = resources.getString(R.string.base_url) + "/user/event/cancel/" + id
        val queue = Volley.newRequestQueue(context)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            // Your success code here
            try {
//                Toast.makeText(context, s, Toast.LENGTH_LONG).show()
                val jsonObject = JSONObject(s)
                val message = jsonObject.getString("message")
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()

            } catch (e: Exception) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }, Response.ErrorListener { e ->

            //          Toast.makeText(applicationContext,"Please fill your information",Toast.LENGTH_LONG).show()
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)

        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }

    private fun showDialog(id: Int, itemPosition: Int) {
        lateinit var dialog: AlertDialog
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Cancel Attending Event")
        builder.setMessage("Do you really want to cancel this event")

        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
//                    Toast.makeText(context, "Positive/Yes button clicked.", Toast.LENGTH_LONG).show()
                    cancelAttendingEvent(userAccessToken, id, itemPosition)
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


