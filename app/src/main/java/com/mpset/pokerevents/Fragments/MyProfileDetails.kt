package com.mpset.pokerevents.Fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.mpset.pokerevents.Activities.EditLocation
import com.mpset.pokerevents.Activities.EditProfile
import com.mpset.pokerevents.Activities.FilterPage
import com.mpset.pokerevents.MainActivity
import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import com.mpset.pokerevents.SplashScreen.Companion.userAddress
import com.mpset.pokerevents.SplashScreen.Companion.userAvatar
import com.mpset.pokerevents.SplashScreen.Companion.userEmail
import com.mpset.pokerevents.SplashScreen.Companion.userFirstName
import com.mpset.pokerevents.SplashScreen.Companion.userId
import com.mpset.pokerevents.SplashScreen.Companion.userLastName
import com.mpset.pokerevents.SplashScreen.Companion.userNickName
import com.mpset.pokerevents.SplashScreen.Companion.userPhone
import com.smarteist.autoimageslider.SliderLayout
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_join_event_detail_page.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.fragment_my_profile_details.*
import kotlinx.android.synthetic.main.fragment_my_profile_details.view.*
import kotlinx.android.synthetic.main.fragment_my_profile_ratings.view.*
import org.json.JSONObject
import java.util.ArrayList

class MyProfileDetails : Fragment() {
   var  firstName = ""
   var  lastName = ""
   var  nickName = ""
   var  email = ""
   var  avatar = ""
   var  phone = ""
   var  address = ""
    var imageslist: ArrayList<String> = ArrayList()
     var idLocation = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_profile_details, container, false)

         view.imageSliderMyprofile!!.setIndicatorAnimation(SliderLayout.Animations.FILL)
        view.imageSliderMyprofile!!.scrollTimeInSec = 1

        view.linear_profile_edit.setOnClickListener {
            val i = Intent(context,EditProfile::class.java)
            i.putExtra("nick_name",nickName)
            i.putExtra("first_name",firstName)
            i.putExtra("last_name",lastName)
            i.putExtra("avatar",avatar)
            i.putExtra("email",email)
            i.putExtra("phone",phone)
            i.putExtra("address",address)
            startActivity(i)
        }
        getViewProfileInformation(userAccessToken, userId.toInt())
        view.edit_location_pic_my_profile.setOnClickListener {
            openEditLocation("edit")
        }

        view.btn_create_location.setOnClickListener {
            openEditLocation("create")
        }
        return view
    }
    fun openEditLocation(function:String){
        val i = Intent(context,EditLocation::class.java)
        i.putExtra("function",function )
        i.putExtra("location_id",idLocation)
        startActivity(i)
    }

    fun getViewProfileInformation(tokenAccess: String, id: Int) {

        val URL = resources.getString(R.string.base_url) + "/user/friends/" + id
        val queue = Volley.newRequestQueue(context)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            // Your success code here
            try {
                val jsonObject = JSONObject(s)
                val data = jsonObject.getJSONObject("data")
                nickName = data.getString("nick_name")
                firstName = data.getString("first_name")
                lastName = data.getString("last_name")
                val fullName = firstName + " " + nickName
                email = data.getString("email")
                avatar = data.getString("avatar")
                phone = data.getString("phone")
                address = data.getString("address")
//                getLocationImages(userAccessToken)
                val location = data.getString("location")
//
                if(!location.equals("null")) {
                    val location = data.getJSONObject("location")
                    idLocation = location.getString("id")
                    val jsonArrayImages = location.getJSONArray("images")
                    for (i in 0 until jsonArrayImages.length()) {
                        val pathObject = jsonArrayImages.getJSONObject(i)
                        val path = pathObject.getString("path")
                        imageslist.add(path)
                    }
                    setSliderViews()
                    view!!.btn_create_location.visibility = View.GONE
                }else {
                    view!!.card_location_slider.visibility = View.GONE
                    view!!.btn_create_location.visibility = View.VISIBLE
                }
                Glide.with(this@MyProfileDetails).load(avatar).into(view!!.user_my_profile)
                view!!.my_profile_fullname.text = fullName
                view!!.nickname_my_profile.text = " (" + nickName + ")"
                view!!.address_my_profile.text = address
                view!!.email_my_profile.text = email
                view!!.phone_number_my_profile.text = phone
                userAvatar =  avatar
                userNickName = nickName
                userFirstName = firstName
                userLastName = lastName
                userEmail = email
                userPhone = phone
                userAvatar = avatar
                userAddress = address
                setShareValues()
            } catch (e: Exception) {
//                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }, Response.ErrorListener { e ->
//            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }
//    fun getLocationImages(tokenAccess: String) {
//
//        val URL = resources.getString(R.string.base_url) + "/user/location/" + userId.toInt()
//        val queue = Volley.newRequestQueue(context)
//
//        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
//            // Your success code here
//            try {
//                val jsonObject = JSONObject(s)
////                val data = jsonObject.getJSONObject("data")
////                val location = data.getJSONObject("location")
//                val jsonArrayImages = jsonObject.getJSONArray("images")
//                for (i in 0 until jsonArrayImages.length()) {
//                    val pathObject = jsonArrayImages.getJSONObject(i)
//                    val path = pathObject.getString("path")
//                    imageslist.add(path)
//                }
//                setSliderViews()
//
//            } catch (e: Exception) {
//                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
//            }
//        }, Response.ErrorListener { e ->
//            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
//        }) {
//            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)
//        }
//        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
//        queue.add(stringRequest)
//
//    }
fun setShareValues(){
    val sharedPreference =  this.getActivity()!!.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
    var editor = sharedPreference.edit()
    editor.putString("nick_name",nickName)
    editor.putString("first_name",firstName)
    editor.putString("last_name",lastName)
    editor.putString("avatar",avatar)
    editor.putString("email",email)
    editor.putString("phone",phone)
    editor.commit()
}
    private fun setSliderViews() {

        for (i in 0 until imageslist.size) {
            val sliderView = SliderView(context)
            when (i) {
                i -> sliderView.imageUrl = imageslist.get(i)
            }
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
//            sliderView.setOnSliderClickListener { Toast.makeText(this@JoinEventDetailPage, "This is slider " + (i + 1), Toast.LENGTH_SHORT).show() }
            //at last add this view in your layout :
            view!!.imageSliderMyprofile!!.addSliderView(sliderView)
        }
    }
}
