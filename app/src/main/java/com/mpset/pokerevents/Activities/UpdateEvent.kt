package com.mpset.pokerevents.Activities

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.TextView
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.gms.common.GooglePlayServicesUtilLight
import com.google.firebase.database.core.view.View
import com.mpset.pokerevents.Activities.AddLocation.Companion.locationAddress
import com.mpset.pokerevents.Activities.AddLocation.Companion.location_lat
import com.mpset.pokerevents.Activities.AddLocation.Companion.location_lon
import com.mpset.pokerevents.Helper.ImageHelperJava
import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen
import kotlinx.android.synthetic.main.activity_create_event_game_type.*
import kotlinx.android.synthetic.main.activity_edit_location.*
import kotlinx.android.synthetic.main.activity_update_event.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

class UpdateEvent : AppCompatActivity() {
     var id= ""
     var eventName= ""
     var address= ""
     var locationId= ""
    val PICK_IMAGE_REQUEST = 101
    private var filePath: Uri? = null
    var imageslist: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_event)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        id = intent.getStringExtra("event_id")
        eventName = intent.getStringExtra("event_name")
         imageslist = intent.getStringArrayListExtra("images")
        address = intent.getStringExtra("address")
        locationId = intent.getStringExtra("location_id")
        supportActionBar!!.title = eventName

        location_event_edit_location_update_host.setText(address, TextView.BufferType.EDITABLE)
        location_event_edit_location_update_host.setOnClickListener {
            startActivity(Intent(this@UpdateEvent,AddLocation::class.java))
        }
        placeImages()
        image_picker_update_location.setOnClickListener {
            fileChoser()
        }
        image1_remove_update.setOnClickListener {
            image1_create_event_update_location.setImageDrawable(resources.getDrawable(R.drawable.default_image_picker))
            imageslist.set(0,"null")
            image1_remove_update.visibility = android.view.View.INVISIBLE
        }
        image2_remove_update.setOnClickListener {
            image2_create_event_update_location.setImageDrawable(resources.getDrawable(R.drawable.default_image_picker))
            imageslist.set(1,"null")
            image2_remove_update.visibility = android.view.View.INVISIBLE
        }
        image3_remove_update.setOnClickListener {
            image3_create_event_update_location.setImageDrawable(resources.getDrawable(R.drawable.default_image_picker))
            imageslist.set(2,"null")
            image3_remove_update.visibility = android.view.View.INVISIBLE
        }
        image4_remove_update.setOnClickListener {
            image4_create_event_update_location.setImageDrawable(resources.getDrawable(R.drawable.default_image_picker))
            imageslist.set(3,"null")
            image4_remove_update.visibility = android.view.View.INVISIBLE
        }
        image5_remove_update.setOnClickListener {
            image5_create_event_update_location.setImageDrawable(resources.getDrawable(R.drawable.default_image_picker))
            imageslist.set(4,"null")
            image5_remove_update.visibility = android.view.View.INVISIBLE
        }
        btn_save_update_location.setOnClickListener {
//            updateLocation(SplashScreen.userAccessToken)
        }

    }

    private fun placeImages() {
        for (i in 0 until imageslist.size){
            if (i==0) {
                Glide.with(applicationContext).load(imageslist.get(i)).into(image1_create_event_update_location)
                image1_remove_update.visibility =  android.view.View.VISIBLE
            }
            else if (i==1) {
                Glide.with(applicationContext).load(imageslist.get(i)).into(image2_create_event_update_location)
                image2_remove_update.visibility =  android.view.View.VISIBLE
            }
            else if (i==2) {
                Glide.with(applicationContext).load(imageslist.get(i)).into(image3_create_event_update_location)
                image3_remove_update.visibility =  android.view.View.VISIBLE
            }
            else if (i==3) {
                Glide.with(applicationContext).load(imageslist.get(i)).into(image4_create_event_update_location)
                image4_remove_update.visibility =  android.view.View.VISIBLE
            }
            else if (i==4) {
                Glide.with(applicationContext).load(imageslist.get(i)).into(image5_create_event_update_location)
                image5_remove_update.visibility =  android.view.View.VISIBLE
            }
        }

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

            if(image1_create_event_update_location.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.imageholder).getConstantState() || imageslist[0].equals("null")){
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image1_create_event_update_location!!.setImageBitmap(bitmap)
                if (imageslist.size>=1)
                    imageslist.set(0,(convertToBase64(filePath.toString())))
                else
                    imageslist.add(0,(convertToBase64(filePath.toString())))
                image1_remove_update.visibility = android.view.View.VISIBLE
                return
            }
            else if(image2_create_event_update_location.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.imageholder).getConstantState() || imageslist[1].equals("null")){
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image2_create_event_update_location!!.setImageBitmap(bitmap)
                if (imageslist.size>=2)
                    imageslist.set(1,(convertToBase64(filePath.toString())))
                else
                    imageslist.add(1,(convertToBase64(filePath.toString())))
                image2_remove_update.visibility = android.view.View.VISIBLE

                return
            }
            else if(image3_create_event_update_location.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.imageholder).getConstantState() || imageslist[2].equals("null")){
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image3_create_event_update_location!!.setImageBitmap(bitmap)
                if (imageslist.size>=3)
                    imageslist.set(2,(convertToBase64(filePath.toString())))
                else
                    imageslist.add(2,(convertToBase64(filePath.toString())))
                image3_remove_update.visibility = android.view.View.VISIBLE

                return
            }
            else if(image4_create_event_update_location.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.imageholder).getConstantState() || imageslist[3].equals("null")){
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image4_create_event_update_location!!.setImageBitmap(bitmap)
                if (imageslist.size>=4)
                    imageslist.set(3,(convertToBase64(filePath.toString())))
                else
                    imageslist.add(3,(convertToBase64(filePath.toString())))
                image4_remove_update.visibility = android.view.View.VISIBLE

                return
            }else if(image5_create_event_update_location.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.imageholder).getConstantState() || imageslist[4].equals("null")){
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image5_create_event_update_location!!.setImageBitmap(bitmap)
                if (imageslist.size>=5)
                    imageslist.set(4,(convertToBase64(filePath.toString())))
                else
                    imageslist.add(4,(convertToBase64(filePath.toString())))
                image5_remove_update.visibility = android.view.View.VISIBLE
                return
            }
            Toast.makeText(applicationContext,"You already select your maximum images" + imageslist.size.toString(), Toast.LENGTH_LONG).show()

        }

    }
    fun convertToBase64(item :String):String{
        val imageStream = getContentResolver().openInputStream(Uri.parse(item))
        val selectedImage = BitmapFactory.decodeStream(imageStream)
        var imageString = ImageHelperJava.encodeImage(selectedImage)
        return imageString.replace("\n", "")
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    override fun onResume() {
        super.onResume()
        if (!AddLocation.locationAddress.equals(""))
            location_event_edit_location_update_host.setText(locationAddress, TextView.BufferType.EDITABLE)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        locationAddress = ""
    }
//    fun updateLocation(tokenAccess:String) {
//        val jsonArrayImages = JSONArray()
//        for (item in imageslist) {
//            if(item.equals("null"))
//                continue
//            jsonArrayImages.put(item)
//        }
//        val params = JSONObject()
//        params.put("lat", location_lat)
//        params.put("lng", location_lon)
//        params.put("address",location_event_edit_location_update_host.text.toString())
//        params.put("images",jsonArrayImages)
//
//        val URL = resources.getString(R.string.base_url) +"/user/location/" + locationId
//        val queue = Volley.newRequestQueue(this)
//        val stringRequest = object : JsonObjectRequest(Request.Method.PATCH, URL, params, Response.Listener { s ->
////                        val json = s.getString("message")
//            Toast.makeText(applicationContext,s.toString(),Toast.LENGTH_LONG).show()
//        }, Response.ErrorListener { e ->
//            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
//        }) {
//            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json" , "Authorization" to "Bearer " +tokenAccess)
//        }
//        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
//        queue.add(stringRequest)
//    }
}
