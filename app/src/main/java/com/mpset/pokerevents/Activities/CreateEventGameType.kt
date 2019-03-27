package com.mpset.pokerevents.Activities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.google.android.gms.common.ConnectionResult
import com.mpset.pokerevents.R
import kotlinx.android.synthetic.main.activity_create_event_game_type.*
import kotlinx.android.synthetic.main.activity_new_event_cash_tour.*
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.gms.common.api.GoogleApiClient
import com.mpset.pokerevents.Activities.AddLocation.Companion.locationAddress
import com.mpset.pokerevents.Activities.AddLocation.Companion.location_lat
import com.mpset.pokerevents.Activities.AddLocation.Companion.location_lon
import com.mpset.pokerevents.Helper.ImageHelperJava
import com.mpset.pokerevents.MainActivity
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import kotlinx.android.synthetic.main.activity_facebook_detail_page.*
import org.json.JSONArray
import org.json.JSONObject
import java.sql.Array
//import com.mpset.pokerevents.Activities.AddLocation.Companion.locationAddress
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class CreateEventGameType : AppCompatActivity(),GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(applicationContext,p0.toString(),Toast.LENGTH_LONG).show()
    }

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
    var gameTime: String = "null"
    val PICK_IMAGE_REQUEST = 101
    private var filePath: Uri? = null
    val imagesList: ArrayList<String> = ArrayList()
    var timeInMilliseconds:Long = 0
    var votingOptions:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event_game_type)
        supportActionBar!!.title = "New Event"
        val doubleBounce = DoubleBounce()
        spin_kit.setIndeterminateDrawable(doubleBounce)
        gameType = intent.getStringExtra("gameType")
        gameProfile = intent.getStringExtra("gameProfile")
        eventName = intent.getStringExtra("event_name")
        minPlayer = intent.getStringExtra("min_player")
        maxPlayer = intent.getStringExtra("max_player")
        tableRules = intent.getStringExtra("table_rules")
        supportActionBar!!.title = eventName
        if (gameType.equals("tournament")) {
            smallBlind = intent.getStringExtra("small_blind")
            bigBlind = intent.getStringExtra("big_blind")
            minBuyIn = intent.getStringExtra("min_buy_in")
            maxBuyIn = intent.getStringExtra("max_buy_in")
        }
        if (gameType.equals("cash")) {
            amount = intent.getStringExtra("amount")
            noRebuy = intent.getStringExtra("no_of_rebuy")
        }
        if (gameType.equals("both")) {
            smallBlind = intent.getStringExtra("small_blind")
            bigBlind = intent.getStringExtra("big_blind")
            minBuyIn = intent.getStringExtra("min_buy_in")
            maxBuyIn = intent.getStringExtra("max_buy_in")
            amount = intent.getStringExtra("amount")
            noRebuy = intent.getStringExtra("no_of_rebuy")
        }



        text_public.setOnClickListener {
            text_private.background = resources.getDrawable(R.drawable.selectedbarwhite)
            text_public.background = resources.getDrawable(R.drawable.selectedbar)
            text_private.setTextColor(Color.parseColor("#000000"))
            text_public.setTextColor(Color.parseColor("#ffffff"))
            private_event.visibility = View.GONE
            btn_next_friend.visibility = View.GONE
            linear_public.visibility = View.VISIBLE
        }
        text_private.setOnClickListener {
            text_public.background = resources.getDrawable(R.drawable.selectedbarwhite)
            text_private.background = resources.getDrawable(R.drawable.selectedbar)
            text_public.setTextColor(Color.parseColor("#000000"))
            text_private.setTextColor(Color.parseColor("#ffffff"))
            private_event.visibility = View.VISIBLE
            btn_next_friend.visibility = View.VISIBLE
            linear_public.visibility = View.GONE
        }
    image_picker_create_event.setOnClickListener {
        fileChoser()
    }

    location_event.setOnClickListener {
     startActivity(Intent(this@CreateEventGameType,AddLocation::class.java))
//        val PLACE_AUTOCOMPLETE_REQUEST_CODE = 1
//
//
//            val intent =
//             PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
//                    .build(this)
//            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE)

    }
        check_voting_enable.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                private_event.visibility = View.VISIBLE
            }else{
                private_event.visibility = View.GONE
            }
            votingOptions = isChecked
        }
        date_public.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                date_public.setText("" + dayOfMonth + "/" + (monthOfYear.toInt() + 1).toString() + "/" + year)
            }, year, month, day)
            dpd.show()
        }
        time_public.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                time_public.setText(SimpleDateFormat("H:mm").format(cal.time))
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
        start_date_friends.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                start_date_friends.setText("" + dayOfMonth + "/" + (monthOfYear.toInt() + 1).toString() + "/" + year)
            }, year, month, day)
            dpd.show()
        }
        end_date_friends.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                end_date_friends.setText("" + dayOfMonth + "/" + (monthOfYear + 1).toString() + "/" + year)
            }, year, month, day)
            dpd.show()
        }
        btn_create_public.setOnClickListener {

//            if (event_name.text.toString().isEmpty()) {
//                Toast.makeText(applicationContext, "Please enter your location", Toast.LENGTH_LONG).show()
//            }
            spin_kit.visibility = View.VISIBLE
            serverCallCheckPublicEvent("Bearer "+userAccessToken)
//            Toast.makeText(this@CreateEventGameType, "Created", Toast.LENGTH_LONG).show()
        }
        btn_next_friend.setOnClickListener {
            if (eventName.isNullOrEmpty()) {
                Toast.makeText(applicationContext, "Please enter your location", Toast.LENGTH_LONG).show()
            }
            timeInMilliseconds = changeTimeMile()
            val intent = Intent(this@CreateEventGameType, NewEventFriendSelection::class.java)
            intent.putExtra("event_name",eventName)
            intent.putExtra("amount",amount)
            intent.putExtra("no_of_rebuy",noRebuy)
            intent.putExtra("small_blind",smallBlind)
            intent.putExtra("big_blind",bigBlind)
            intent.putExtra("min_buy_in",minBuyIn)
            intent.putExtra("max_buy_in",maxBuyIn)
            intent.putExtra("min_player",minPlayer)
            intent.putExtra("max_player",maxPlayer)
            intent.putExtra("game_profile",gameProfile)
            intent.putExtra("game_type",gameType)
            intent.putExtra("table_rules",tableRules)
            intent.putExtra("images",imagesList)
            intent.putExtra("voting_option",votingOptions)
            intent.putExtra("start_date",start_date_friends.text.toString())
            intent.putExtra("end_date",end_date_friends.text.toString())
            intent.putExtra("game_date","@"+timeInMilliseconds.toString())
            startActivity(intent)
        }

        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())
        date_public.text = currentDate
        start_date_friends.text = currentDate
        end_date_friends.text = currentDate
    }
    private fun fileChoser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.data != null) {
            filePath = data.data

            if(image1_create_event.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.imageholder).getConstantState()){
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image1_create_event!!.setImageBitmap(bitmap)
                imagesList.add(filePath.toString())
                return
            }
            else if(image2_create_event.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.imageholder).getConstantState()){
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image2_create_event!!.setImageBitmap(bitmap)
                imagesList.add(filePath.toString())
                return
            }
            else if(image3_create_event.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.imageholder).getConstantState()){
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image3_create_event!!.setImageBitmap(bitmap)
                imagesList.add(filePath.toString())
                return
            }
            else if(image4_create_event.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.imageholder).getConstantState()){
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image4_create_event!!.setImageBitmap(bitmap)
                imagesList.add(filePath.toString())
                return
            }else if(image5_create_event.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.imageholder).getConstantState()){
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image5_create_event!!.setImageBitmap(bitmap)
                imagesList.add(filePath.toString())
                return
            }
            Toast.makeText(applicationContext,"You already select your maximum images" +imagesList.size.toString(),Toast.LENGTH_LONG).show()

//            try {
//                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
////                var lastBitmap = bitmap
//
////                bitmap =
////                imageString = bitmap
//                image1_create_event!!.setImageBitmap(bitmap)
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
        }

}

    override fun onResume() {
        super.onResume()
        if (!locationAddress.equals(""))
            location_event.setText(locationAddress, TextView.BufferType.EDITABLE)
    }
    fun serverCallCheckPublicEvent(tokenAccess:String) {

         timeInMilliseconds = changeTimeMile()

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
        params2.put("lat",location_lat)
        params2.put("lng",location_lon)
        params2.put("address",location_event.text.toString())
        params2.put("images",jsonArrayImages)
        params.put("name", eventName)
        params.put("game_profile", gameProfile)
        params.put("game_type", gameType)
        params.put("isPrivate", "0")
        params.put("max_players", maxPlayer)
        params.put("min_players", minPlayer)
        params.put("table_rules", tableRules)
        params.put("game_date", "@"+timeInMilliseconds.toString())
        params.put("purchase_amount", amount)
        params.put("re_buyins", noRebuy)
        params.put("small_blind", smallBlind)
        params.put("big_blind", bigBlind)
        params.put("max_buyins", maxBuyIn)
        params.put("min_buyins", minBuyIn)
        params.put("location", params2)

        val URL = resources.getString(R.string.base_url) +"/user/event"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : JsonObjectRequest(Request.Method.POST, URL, params,Response.Listener { s ->
            // Your success code here
//            Toast.makeText(applicationContext,s.toString(),Toast.LENGTH_LONG).show()
            spin_kit.visibility = View.GONE
            val json = s.getString("message")
            startActivity(Intent(this@CreateEventGameType,MainActivity::class.java))
            finish()


        }, Response.ErrorListener { e ->
            spin_kit.visibility = View.GONE
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json" , "Authorization" to tokenAccess)

        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }
    fun changeTimeMile():Long{
        val timeStamp = date_public.text.toString() +" "+ time_public.text.toString()
        val sdf =  SimpleDateFormat("dd/M/yyyy hh:mm")
        val mDate = sdf.parse(timeStamp)
        timeInMilliseconds = (mDate.time)/1000
        return  timeInMilliseconds
    }
}
