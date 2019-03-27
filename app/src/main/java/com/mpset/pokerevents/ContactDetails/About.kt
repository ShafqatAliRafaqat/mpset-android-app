package com.mpset.pokerevents.ContactDetails

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mpset.pokerevents.R

class About : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        supportActionBar!!.title = "About Us"
    }
}
