package com.mpset.pokerevents.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mpset.pokerevents.Model.*
import com.mpset.pokerevents.R

class ViewProfileLocationStatisticsListAdapter(val context:Context?, val userList: ArrayList<ViewProfileLocationStatisticsModel>, var myClickListener: MyClickListener) : RecyclerView.Adapter<ViewProfileLocationStatisticsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.snipped_view_profile_location_statistics, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.count()
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val user : ViewProfileLocationStatisticsModel = userList[p1]
        p0.txtNumGames?.text = user.numOfEvents.toString()
        p0.txtLocation?.text = user.location
        p0.txtDurations?.text = user.totalDuration.toString()
        p0.txtBuyIn?.text = user.totalBuyin.toString()
        p0.txtCashOut?.text = user.totalCashout.toString()
        p0.txtBalance?.text = user.totalBalance.toString()
        p0.onClickRow(p1,myClickListener)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNumGames = itemView.findViewById(R.id.view_profile_location_number_of_event_sta) as TextView
        val txtLocation = itemView.findViewById(R.id.view_profile_location_location_sta) as TextView
        val txtDurations = itemView.findViewById(R.id.view_profile_location_location_duration_sta) as TextView
        val txtBuyIn = itemView.findViewById(R.id.view_profile_location_total_buyin_sta) as TextView
        val txtCashOut = itemView.findViewById(R.id.view_profile_loaction_total_cashout_sta) as TextView
        val txtBalance = itemView.findViewById(R.id.view_profile_location_total_balance_sta) as TextView


        fun  onClickRow(position: Int,myClickListener: MyClickListener){
            itemView.setOnClickListener {
                myClickListener.onClick(position)
            }
            itemView.setOnLongClickListener {
                myClickListener.onLongClick(position)
                return@setOnLongClickListener  true
            }
        }
    }

    interface MyClickListener {
        fun onClick(position: Int)
        fun onLongClick(position: Int)
    }
}