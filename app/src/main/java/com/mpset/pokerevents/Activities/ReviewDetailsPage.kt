package com.mpset.pokerevents.Activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.mpset.pokerevents.Adapters.NotificationListAdapter
import com.mpset.pokerevents.Adapters.ReviewsDetailsListAdapter
import com.mpset.pokerevents.Model.NotificationModel
import com.mpset.pokerevents.Model.ReviewsDetails
import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import kotlinx.android.synthetic.main.activity_review_details_page.*
import kotlinx.android.synthetic.main.activity_view_profile.*
import kotlinx.android.synthetic.main.fragment_view_profile_details.*
import org.json.JSONObject
import java.util.ArrayList

class ReviewDetailsPage : AppCompatActivity() {
    val usersReviews = ArrayList<ReviewsDetails>()
    lateinit var adapterReviews:ReviewsDetailsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_details_page)
        val id:Int = intent.getIntExtra("review_id",0)
        val typeReview:String = intent.getStringExtra("type")
        supportActionBar!!.title = "Reviews"
        Toast.makeText(applicationContext,typeReview,Toast.LENGTH_LONG).show()
        var url:String = resources.getString(R.string.base_url)
        if(typeReview.equals("player")){
         url = url + "/user/reviews/playerReviews/" +id
            getViewProfileReviewInformation(userAccessToken,url)
        }else if (typeReview.equals("location")){
            url = url + "/user/reviews/locationReviews/" +id
            getViewProfileReviewInformation(userAccessToken,url)
        }else if (typeReview.equals("host")){
            url = url + "/user/reviews/hostReviews/" +id
            getViewProfileReviewInformation(userAccessToken,url)
        }
        recycler_view_reviews_details.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        adapterReviews = ReviewsDetailsListAdapter(applicationContext,usersReviews, object : ReviewsDetailsListAdapter.MyClickListener {
            override fun onClick(position: Int) {
                Toast.makeText(applicationContext,"ssss", Toast.LENGTH_LONG).show()



            }

            override fun onLongClick(position: Int) {
                Toast.makeText(applicationContext, "You got a long click it", Toast.LENGTH_LONG).show()
            }

        })
        recycler_view_reviews_details.adapter = adapterReviews
    }
    fun getViewProfileReviewInformation(tokenAccess: String, url:String) {

        val URL = url
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            // Your success code here
            try {
                val jsonObject = JSONObject(s)
                val data = jsonObject.getJSONArray("data")
                for (i in 0 until data.length()){
                    val objectData = data.getJSONObject(i)
                    val time = objectData.getString("created_at")
                    val rating = objectData.getDouble("rating")
                    val comment = objectData.getString("comment")
                    val objectReviwer = objectData.getJSONObject("reviewer")
                    val name = objectReviwer.getString("nick_name")
                    val avatar = objectReviwer.getString("avatar")
        usersReviews.add(ReviewsDetails(1,name,avatar,time,rating.toFloat(),comment))


                }
                if(adapterReviews!=null)
                    adapterReviews.notifyDataSetChanged()
//                val meta = jsonObject.getJSONObject("meta")
//                val id = data.getString("id")
//                val avatar = data.getString("avatar")
//                val firstName = data.getString("first_name")
//                val lastName = data.getString("last_name")
//                val nickName = data.getString("nick_name")
//                val fullName = firstName +" "+ lastName
//                val email = data.getString("email")
//                val address = data.getString("address")
//                val phone = data.getString("phone")
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
}
