package com.mpset.pokerevents.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mpset.pokerevents.R
import com.mpset.pokerevents.Model.UpcomingModel

class UpcomingListAdapter(val userList: ArrayList<UpcomingModel>, var myClickListener: MyClickListener) : RecyclerView.Adapter<UpcomingListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.snipped_upcoming_page, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.count()
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val user : UpcomingModel = userList[p1]
        p0.txtEventName?.text = user.eventName
        p0.txtTime?.text = user.time
        p0.txtAddress?.text = user.address
        p0.txtHostName?.text = user.hostName
        p0.txtGameType?.text = user.gameType
        p0.txtRating?.text = user.rating.toString()
        p0.onClickRow(p1,myClickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtEventName = itemView.findViewById(R.id.event_name) as TextView
        val txtTime = itemView.findViewById(R.id.time_date_event) as TextView
        val txtAddress = itemView.findViewById(R.id.location_event) as TextView
        val txtHostName = itemView.findViewById(R.id.host_name_event) as TextView
        val txtGameType = itemView.findViewById(R.id.game_event_type) as TextView
        val txtRating = itemView.findViewById(R.id.host_rating_event) as TextView

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