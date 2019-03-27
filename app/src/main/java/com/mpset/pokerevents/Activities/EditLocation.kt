package com.mpset.pokerevents.Activities

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
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
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mpset.pokerevents.Helper.ImageHelperJava
import com.mpset.pokerevents.MainActivity
import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import kotlinx.android.synthetic.main.activity_create_event_game_type.*
import kotlinx.android.synthetic.main.activity_edit_location.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class EditLocation : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    lateinit var geocoder:Geocoder
    lateinit var addresses:List<Address>
     var lat:Double = 0.0
     var long:Double = 0.0
    var  address:String = ""
    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(p0: LatLng?) {
                mMap.clear()
                val latLng = LatLng(p0!!.latitude, p0!!.longitude)
                mMap.addMarker(MarkerOptions().position(latLng).title("Marker in Sydney"))
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10f)
                mMap.animateCamera(cameraUpdate)
                lat = p0!!.latitude
                long = p0!!.longitude
                geocoder =  Geocoder (this@EditLocation, Locale.getDefault())

                addresses = geocoder.getFromLocation(p0!!.latitude, p0!!.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                 address = addresses.get (0).getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                String city = addresses . get (0).getLocality();
//                String state = addresses . get (0).getAdminArea();
//                String country = addresses . get (0).getCountryName();
//                String postalCode = addresses . get (0).getPostalCode();
//                String knownName = addresses . get (0).getFeatureName();
                location_event_edit_location.setText(address, TextView.BufferType.EDITABLE)
            }
        })
    }

    val PICK_IMAGE_REQUEST = 101
    private var filePath: Uri? = null
    lateinit var whichFunction: String
    lateinit var locationId: String
    var request: Int = -1
    var URL = ""
    val imagesList: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_location)
        supportActionBar!!.title = "Add Location"
        whichFunction = intent.getStringExtra("function")
        locationId = intent.getStringExtra("location_id")
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        location_event_edit_location.setOnClickListener {
//            startActivity(Intent(this@EditLocation, AddLocation::class.java))
        }
        image_picker_edit_location.setOnClickListener {
            fileChoser()
        }
        image1_remove.setOnClickListener {
            Toast.makeText(applicationContext, "click", Toast.LENGTH_LONG).show()
            image1_create_event_edit_location.setImageDrawable(resources.getDrawable(R.drawable.default_image_picker))
            imagesList.set(0, "null")
            image1_remove.visibility = View.INVISIBLE
        }
        image2_remove.setOnClickListener {
            image2_create_event_edit_location.setImageDrawable(resources.getDrawable(R.drawable.default_image_picker))
            imagesList.set(1, "null")
            image2_remove.visibility = View.INVISIBLE
        }
        image3_remove.setOnClickListener {
            image3_create_event_edit_location.setImageDrawable(resources.getDrawable(R.drawable.default_image_picker))
            imagesList.set(2, "null")
            image3_remove.visibility = View.INVISIBLE
        }
        image4_remove.setOnClickListener {
            image4_create_event_edit_location.setImageDrawable(resources.getDrawable(R.drawable.default_image_picker))
            imagesList.set(3, "null")
            image4_remove.visibility = View.INVISIBLE
        }
        image5_remove.setOnClickListener {
            image5_create_event_edit_location.setImageDrawable(resources.getDrawable(R.drawable.default_image_picker))
            imagesList.set(4, "null")
            image5_remove.visibility = View.INVISIBLE
        }
        btn_save_edit_location.setOnClickListener {
            updateLocation(userAccessToken)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!AddLocation.locationAddress.equals(""))
            location_event_edit_location.setText(AddLocation.locationAddress, TextView.BufferType.EDITABLE)
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

            if (image1_create_event_edit_location.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.imageholder).getConstantState() || imagesList[0].equals("null")) {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image1_create_event_edit_location!!.setImageBitmap(bitmap)
                if (imagesList.size >= 1)
                    imagesList.set(0, (convertToBase64(filePath.toString())))
                else
                    imagesList.add(0, (convertToBase64(filePath.toString())))
                image1_remove.visibility = View.VISIBLE
                return
            } else if (image2_create_event_edit_location.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.imageholder).getConstantState() || imagesList[1].equals("null")) {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image2_create_event_edit_location!!.setImageBitmap(bitmap)
                if (imagesList.size >= 2)
                    imagesList.set(1, (convertToBase64(filePath.toString())))
                else
                    imagesList.add(1, (convertToBase64(filePath.toString())))
                image2_remove.visibility = View.VISIBLE

                return
            } else if (image3_create_event_edit_location.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.imageholder).getConstantState() || imagesList[2].equals("null")) {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image3_create_event_edit_location!!.setImageBitmap(bitmap)
                if (imagesList.size >= 3)
                    imagesList.set(2, (convertToBase64(filePath.toString())))
                else
                    imagesList.add(2, (convertToBase64(filePath.toString())))
                image3_remove.visibility = View.VISIBLE

                return
            } else if (image4_create_event_edit_location.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.imageholder).getConstantState() || imagesList[3].equals("null")) {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image4_create_event_edit_location!!.setImageBitmap(bitmap)
                if (imagesList.size >= 4)
                    imagesList.set(3, (convertToBase64(filePath.toString())))
                else
                    imagesList.add(3, (convertToBase64(filePath.toString())))
                image4_remove.visibility = View.VISIBLE

                return
            } else if (image5_create_event_edit_location.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.imageholder).getConstantState() || imagesList[4].equals("null")) {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image5_create_event_edit_location!!.setImageBitmap(bitmap)
                if (imagesList.size >= 5)
                    imagesList.set(4, (convertToBase64(filePath.toString())))
                else
                    imagesList.add(4, (convertToBase64(filePath.toString())))
                image5_remove.visibility = View.VISIBLE
                return
            }
            Toast.makeText(applicationContext, "You already select your maximum images" + imagesList.size.toString(), Toast.LENGTH_LONG).show()

        }

    }

    fun convertToBase64(item: String): String {
        val imageStream = getContentResolver().openInputStream(Uri.parse(item))
        val selectedImage = BitmapFactory.decodeStream(imageStream)
        var imageString = ImageHelperJava.encodeImage(selectedImage)
        return imageString.replace("\n", "")
    }

    fun updateLocation(tokenAccess: String) {
        val jsonArrayImages = JSONArray()
        for (item in imagesList) {
            if (item.equals("null"))
                continue
            jsonArrayImages.put(item)
        }

        val params = JSONObject()
        params.put("lat", lat)
        params.put("lng", long)
        params.put("address", location_event_edit_location.text.toString())
        params.put("images", jsonArrayImages)
        if (whichFunction.equals("create")) {
            request = Request.Method.POST
            URL = resources.getString(R.string.base_url) + "/user/location"
        } else if (whichFunction.equals("edit")) {
            request = Request.Method.PATCH
            URL = resources.getString(R.string.base_url) + "/user/location/" + locationId
        }
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : JsonObjectRequest(request, URL, params, Response.Listener { s ->
            //            val json = s.getString("message")
            Toast.makeText(applicationContext, s.toString(), Toast.LENGTH_LONG).show()
            startActivity(Intent(this@EditLocation, MyProfile::class.java))
        }, Response.ErrorListener { e ->
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)
    }
}
