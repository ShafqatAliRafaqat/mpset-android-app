package com.mpset.pokerevents.Activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
//import com.mpset.pokerevents.MainActivity.Companion.vlaue
import com.mpset.pokerevents.R
import kotlinx.android.synthetic.main.activity_agree_term_condition.*
import kotlinx.android.synthetic.main.activity_dialog_table_rules.*
import kotlinx.android.synthetic.main.activity_in_progress_event.*
import kotlinx.android.synthetic.main.activity_new_event_cash_tour.*

class NewEventCashTour : AppCompatActivity() {
    lateinit var myDialog: Dialog
    lateinit var gameProfile: String
    lateinit var gameType: String
    var tableRules: String = "null"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_event_cash_tour)
        supportActionBar!!.title = "New Event"
        gameProfile = "omaha"
        gameType = "tournament"
        myDialog =  Dialog(this)
        text_texas.setOnClickListener {
            text_omaha.background = resources.getDrawable(R.drawable.selectedbarwhite)
            text_dealer.background = resources.getDrawable(R.drawable.selectedbarwhite)
            text_texas.background = resources.getDrawable(R.drawable.selectedbar)
            text_omaha.setTextColor(Color.parseColor("#000000"))
            text_dealer.setTextColor(Color.parseColor("#000000"))
            text_texas.setTextColor(Color.parseColor("#ffffff"))
            gameProfile = "texas_holdem"
        }
        text_omaha.setOnClickListener {
            text_dealer.background = resources.getDrawable(R.drawable.selectedbarwhite)
            text_texas.background = resources.getDrawable(R.drawable.selectedbarwhite)
            text_omaha.background = resources.getDrawable(R.drawable.selectedbar)
            text_texas.setTextColor(Color.parseColor("#000000"))
            text_dealer.setTextColor(Color.parseColor("#000000"))
            text_omaha.setTextColor(Color.parseColor("#ffffff"))
            gameProfile = "omaha"
        }
        text_dealer.setOnClickListener {
            text_omaha.background = resources.getDrawable(R.drawable.selectedbarwhite)
            text_texas.background = resources.getDrawable(R.drawable.selectedbarwhite)
            text_dealer.background = resources.getDrawable(R.drawable.selectedbar)
            text_omaha.setTextColor(Color.parseColor("#000000"))
            text_texas.setTextColor(Color.parseColor("#000000"))
            text_dealer.setTextColor(Color.parseColor("#ffffff"))
            gameProfile = "dealer_choice"
        }

        text_tournament.setOnClickListener {
            text_cash.background = resources.getDrawable(R.drawable.selectedbarwhite)
            text_both.background = resources.getDrawable(R.drawable.selectedbarwhite)
            text_tournament.background = resources.getDrawable(R.drawable.selectedbar)
            text_cash.setTextColor(Color.parseColor("#000000"))
            text_both.setTextColor(Color.parseColor("#000000"))
            text_tournament.setTextColor(Color.parseColor("#ffffff"))
            linear_tournament_main.visibility = View.VISIBLE
            linear_both.visibility = View.GONE
            gameType = "tournament"
        }
        text_cash.setOnClickListener {
            text_tournament.background = resources.getDrawable(R.drawable.selectedbarwhite)
            text_both.background = resources.getDrawable(R.drawable.selectedbarwhite)
            text_cash.background = resources.getDrawable(R.drawable.selectedbar)
            text_tournament.setTextColor(Color.parseColor("#000000"))
            text_both.setTextColor(Color.parseColor("#000000"))
            text_cash.setTextColor(Color.parseColor("#ffffff"))
            linear_tournament_main.visibility = View.GONE
            linear_both.visibility = View.VISIBLE
            gameType = "cash"
        }
        text_both.setOnClickListener {
            text_tournament.background = resources.getDrawable(R.drawable.selectedbarwhite)
            text_cash.background = resources.getDrawable(R.drawable.selectedbarwhite)
            text_both.background = resources.getDrawable(R.drawable.selectedbar)
            text_cash.setTextColor(Color.parseColor("#000000"))
            text_tournament.setTextColor(Color.parseColor("#000000"))
            text_both.setTextColor(Color.parseColor("#ffffff"))
            linear_tournament_main.visibility = View.VISIBLE
            linear_both.visibility = View.VISIBLE
            gameType = "both"

        }
      linear_table.setOnClickListener {
          ShowPopup(it)
      }

        btn_next_cash.setOnClickListener {
            validation()
//            startActivity(Intent(this@NewEventCashTour,CreateEventGameType::class.java))
        }



        }

    fun ShowPopup(v:View) {

        myDialog.setContentView(R.layout.activity_dialog_table_rules)

        myDialog.show()



        myDialog.btn_submit_table_rules_host.setOnClickListener {
            if(myDialog.table_rules.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please enter table rules",Toast.LENGTH_LONG).show()

            }else {
                Toast.makeText(applicationContext, myDialog.table_rules.text.toString(), Toast.LENGTH_LONG).show()
                tableRules = myDialog.table_rules.text.toString()
                myDialog.cancel()
            }

        }
    }
    fun validation(){
        if(event_name.text.toString().isEmpty()){
            Toast.makeText(applicationContext,"Please enter your event name",Toast.LENGTH_LONG).show()
            return
        }
        if(gameType.equals("tournament")){
            if(small_blind_tour.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please enter small blind",Toast.LENGTH_LONG).show()
                return
            }
            if(big_blind_tour.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please enter big blind",Toast.LENGTH_LONG).show()
                return
            }
            if(min_but_in_tour.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please enter minimum buy in",Toast.LENGTH_LONG).show()
                return
            }
            if(max_but_in_tour.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please enter maximum buy in",Toast.LENGTH_LONG).show()
                return
            }
            if(max_player_per_table.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please enter maximum player per",Toast.LENGTH_LONG).show()
                return
            }
            if(min_player_per_table.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please enter minimum player per",Toast.LENGTH_LONG).show()
                return
            }
            val intent = Intent(this@NewEventCashTour,CreateEventGameType::class.java)
            intent.putExtra("event_name",event_name.text.toString())
            intent.putExtra("small_blind",small_blind_tour.text.toString())
            intent.putExtra("big_blind",big_blind_tour.text.toString())
            intent.putExtra("min_buy_in",min_but_in_tour.text.toString())
            intent.putExtra("max_buy_in",max_but_in_tour.text.toString())
            intent.putExtra("min_player",min_player_per_table.text.toString())
            intent.putExtra("max_player",max_player_per_table.text.toString())
            intent.putExtra("gameProfile",gameProfile)
            intent.putExtra("gameType",gameType)
            intent.putExtra("table_rules",tableRules)
            startActivity(intent)
        }else if(gameType.equals("cash")){

            if(amount_purchase_both.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please enter amount of purchase",Toast.LENGTH_LONG).show()
                return
            }
            if(no_rebuy_both.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please enter no of re-buy",Toast.LENGTH_LONG).show()
                return
            }
            if(max_player_per_table.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please enter maximum player per",Toast.LENGTH_LONG).show()
                return
            }
            if(min_player_per_table.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please enter minimum player per",Toast.LENGTH_LONG).show()
                return
            }
            val intent = Intent(this@NewEventCashTour,CreateEventGameType::class.java)
            intent.putExtra("event_name",event_name.text.toString())
            intent.putExtra("amount",amount_purchase_both.text.toString())
            intent.putExtra("no_of_rebuy",no_rebuy_both.text.toString())
            intent.putExtra("min_player",min_player_per_table.text.toString())
            intent.putExtra("max_player",max_player_per_table.text.toString())
            intent.putExtra("gameProfile",gameProfile)
            intent.putExtra("gameType",gameType)
            intent.putExtra("table_rules",tableRules)
            startActivity(intent)
        }else if(gameType.equals("both")){
            if(small_blind_tour.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please enter small blind",Toast.LENGTH_LONG).show()
                return
            }
            if(big_blind_tour.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please enter big blind",Toast.LENGTH_LONG).show()
                return
            }
            if(min_but_in_tour.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please enter minimum buy in",Toast.LENGTH_LONG).show()
                return
            }
            if(max_but_in_tour.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please enter maximum buy in",Toast.LENGTH_LONG).show()
                return
            }
            if(max_player_per_table.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please enter maximum player per",Toast.LENGTH_LONG).show()
                return
            }
            if(min_player_per_table.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please enter minimum player per",Toast.LENGTH_LONG).show()
                return
            }
            if(amount_purchase_both.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please enter minimum player per",Toast.LENGTH_LONG).show()
                return
            }
            if(no_rebuy_both.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please enter maximum player per",Toast.LENGTH_LONG).show()
                return
            }
            val intent = Intent(this@NewEventCashTour,CreateEventGameType::class.java)
            intent.putExtra("event_name",event_name.text.toString())
            intent.putExtra("amount",amount_purchase_both.text.toString())
            intent.putExtra("no_of_rebuy",no_rebuy_both.text.toString())
            intent.putExtra("small_blind",small_blind_tour.text.toString())
            intent.putExtra("big_blind",big_blind_tour.text.toString())
            intent.putExtra("min_buy_in",min_but_in_tour.text.toString())
            intent.putExtra("max_buy_in",max_but_in_tour.text.toString())
            intent.putExtra("min_player",min_player_per_table.text.toString())
            intent.putExtra("max_player",max_player_per_table.text.toString())
            intent.putExtra("gameProfile",gameProfile)
            intent.putExtra("gameType",gameType)
            intent.putExtra("table_rules",tableRules)
            startActivity(intent)
        }
    }
}
