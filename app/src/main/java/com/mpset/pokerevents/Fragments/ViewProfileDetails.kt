package com.mpset.pokerevents.Fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide

import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import kotlinx.android.synthetic.main.activity_view_profile.*
import kotlinx.android.synthetic.main.fragment_view_profile_details.*
import org.json.JSONObject


class ViewProfileDetails : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_view_profile_details, container, false)
        val id:Int = activity!!.intent.getIntExtra("friend_id",0)
        getViewProfileInformation(userAccessToken,id,requireContext())
        return  view
    }
    fun getViewProfileInformation(tokenAccess: String, id: Int,context: Context) {

        val URL = resources.getString(R.string.base_url) + "/user/friends/" + id
        val queue = Volley.newRequestQueue(context)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            // Your success code here
            try {
                val jsonObject = JSONObject(s)
                val data = jsonObject.getJSONObject("data")
                val meta = jsonObject.getJSONObject("meta")
                val id = data.getString("id")
                val avatar = data.getString("avatar")
                val firstName = data.getString("first_name")
                val lastName = data.getString("last_name")
                val nickName = data.getString("nick_name")
                val fullName = firstName +" "+ lastName
                val email = data.getString("email")
                val address = data.getString("address")
                val phone = data.getString("phone")
                Glide.with(context).load(avatar).into(image_view_profile)
                full_name_view_profile.text = fullName
                view_profile_nick_name.text = "("+nickName+")"
                view_profile_email.text = email
                view_profile_address.text = address
                view_profile_phone_number.text = phone

                Glide.with(context).load(avatar).into(activity!!.image_archive_view_profile)
                activity!!.image_archive_view_profile_name.text = fullName


            } catch (e: Exception) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }, Response.ErrorListener { e ->
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)

        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }



}
