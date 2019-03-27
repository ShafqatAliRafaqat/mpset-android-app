package com.mpset.pokerevents.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mpset.pokerevents.R
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        supportActionBar!!.title = "Verification"
        btn_verify.setOnClickListener {
            startActivity(Intent(this@ForgotPassword,SmsVerification::class.java))
        }
    }
}
