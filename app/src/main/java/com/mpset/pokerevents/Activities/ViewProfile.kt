package com.mpset.pokerevents.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.mpset.pokerevents.Adapters.MyPagerAdapterViewProfile
import com.mpset.pokerevents.MainActivity
import com.mpset.pokerevents.R
import kotlinx.android.synthetic.main.activity_join_event.*
import kotlinx.android.synthetic.main.activity_view_profile.*

class ViewProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)
        supportActionBar!!.hide()
        val fragmentAdapter = MyPagerAdapterViewProfile(supportFragmentManager)
        viewpager_view_profile.adapter = fragmentAdapter

        tabs_view_profile.setupWithViewPager(viewpager_view_profile)
        image_archive_back_view_profile.setOnClickListener {
            startActivity(Intent(this@ViewProfile , MainActivity::class.java))

        }


    }
}
