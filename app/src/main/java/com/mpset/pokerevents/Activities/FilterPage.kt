package com.mpset.pokerevents.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import com.mpset.pokerevents.R
import kotlinx.android.synthetic.main.activity_filter_page.*
import kotlinx.android.synthetic.main.activity_new_event_cash_tour.*

class FilterPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_page)
        supportActionBar!!.title = "Filter"
        val gameProfile = arrayOf("Texas holdem", "Omaha")
        val gameType = arrayOf("Tournament", "Cash")

        //Adapter for spinner
        spinner_game_profile_filter.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, gameProfile) as SpinnerAdapter?
        spinner_game_type_filter.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, gameType)

        //item selected listener for spinner
        spinner_game_profile_filter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                Toast.makeText(this@NewEventCashTour, myStrings[p2], LENGTH_LONG).show()

                if (gameProfile[p2].equals("Holdem")) {
//                    Toast.makeText(this@NewEventCashTour, "Holdem", Toast.LENGTH_LONG).show()
                } else {
//                    Toast.makeText(this@NewEventCashTour, "Omaha", Toast.LENGTH_LONG).show()

                }
            }
        }
        spinner_game_type_filter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                Toast.makeText(this@NewEventCashTour, myStrings[p2], LENGTH_LONG).show()
                if (gameType[p2].equals("Tournament")) {
//                    Toast.makeText(this@NewEventCashTour, "Tournament", Toast.LENGTH_LONG).show()


                } else {
//                    Toast.makeText(this@NewEventCashTour, "Cash", Toast.LENGTH_LONG).show()


                }

            }
        }
    }
}
