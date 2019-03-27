package com.mpset.pokerevents.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.mpset.pokerevents.Adapters.MyPagerAdapterJoinEvent
import com.mpset.pokerevents.R
import kotlinx.android.synthetic.main.activity_join_event.*

class JoinEvent : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_event)
        supportActionBar!!.title = "Join Event"
        val fragmentAdapter = MyPagerAdapterJoinEvent(supportFragmentManager)
        viewpager_join_event.adapter = fragmentAdapter

        tabs_join_event.setupWithViewPager(viewpager_join_event)
        setupTabIcons()
        /////////////
    }
    fun setupTabIcons() {
        tabs_join_event.getTabAt(0)!!.setIcon(R.drawable.title)
        tabs_join_event.getTabAt(1)!!.setIcon(R.drawable.location)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.filtermenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_filter ->{
                startActivity(Intent(this@JoinEvent,FilterPage::class.java))
                return true}
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
