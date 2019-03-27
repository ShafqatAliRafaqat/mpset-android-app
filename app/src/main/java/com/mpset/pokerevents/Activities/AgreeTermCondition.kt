package com.mpset.pokerevents.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mpset.pokerevents.R
import kotlinx.android.synthetic.main.activity_agree_term_condition.*

class AgreeTermCondition : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agree_term_condition)
        supportActionBar!!.title = "Terms & Conditions"
        btn_agreed.setOnClickListener {
            startActivity(Intent(applicationContext,SmsVerification::class.java))
        }

    }
}
