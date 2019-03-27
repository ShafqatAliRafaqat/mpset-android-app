package com.mpset.pokerevents.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mpset.pokerevents.Adapters.InProgressListAdapter
import com.mpset.pokerevents.Model.InProgressBottom
import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import kotlinx.android.synthetic.main.activity_audit_in_progress.*
import org.json.JSONObject
import java.util.ArrayList

class AuditInProgress : AppCompatActivity() {
    val audit = ArrayList<InProgressBottom>()
    var id = 0;
    lateinit var adapterInProgress :InProgressListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R .layout.activity_audit_in_progress)
        supportActionBar!!.title = "Audit"
        id = intent.getIntExtra("event_id",0)
//        val recyclerview = findViewById(R.id.recycler_inprogress) as RecyclerView
        recycler_inprogress.layoutManager = LinearLayoutManager(this)

        getEventAudit(userAccessToken)

        adapterInProgress = InProgressListAdapter(this,audit, object : InProgressListAdapter.MyClickListener {
            override fun onClick(position: Int) {
            }
            override fun onLongClick(position: Int) {
            }
        })
        recycler_inprogress.adapter = adapterInProgress

    }
    fun getEventAudit(tokenAccess: String) {

        val URL = resources.getString(R.string.base_url) + "/user/eventAudits?event_id=" + id
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.GET, URL, Response.Listener { s ->
            try {
//                Toast.makeText(context,s,Toast.LENGTH_LONG).show()
                val jsonObject = JSONObject(s)
                val jsonArray = jsonObject.getJSONArray("data")
                for (i in 0 until jsonArray.length()) {
                    val jsonObj = jsonArray.getJSONObject(i)
                    var id = jsonObj.getInt("id")
                    var details = jsonObj.getString("details")
                    audit.add(InProgressBottom(id, details))
                    if (adapterInProgress!=null)
                        adapterInProgress.notifyDataSetChanged()
                }
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
