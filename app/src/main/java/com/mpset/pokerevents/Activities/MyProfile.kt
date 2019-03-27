package com.mpset.pokerevents.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.mpset.pokerevents.Adapters.MyPagerAdapterMyProfile
import com.mpset.pokerevents.MainActivity
import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen
import com.mpset.pokerevents.SplashScreen.Companion.userAvatar
import com.mpset.pokerevents.SplashScreen.Companion.userNickName
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        supportActionBar!!.hide()
        image_archive_back_myprofile.setOnClickListener {
            startActivity(Intent(this@MyProfile , MainActivity::class.java))
        }
        val fragmentAdapter = MyPagerAdapterMyProfile(supportFragmentManager)
        viewpager_my_profile.adapter = fragmentAdapter

        tabs_my_profile.setupWithViewPager(viewpager_my_profile)
        filter.setOnClickListener {
            startActivity(Intent(this@MyProfile,FilterPage::class.java))

        }
        myprofile_name.text = userNickName
        Glide.with(applicationContext).load(userAvatar).into(image_archive_myprofile_toolbar)

    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.filtermenu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        when (item.itemId) {
//            R.id.action_filter ->{
//                startActivity(Intent(this@MyProfile,FilterPage::class.java))
//                return true}
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }
}
