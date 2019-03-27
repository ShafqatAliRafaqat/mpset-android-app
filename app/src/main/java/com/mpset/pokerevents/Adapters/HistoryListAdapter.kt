package com.mpset.pokerevents.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mpset.pokerevents.Model.AttendingModel
import com.mpset.pokerevents.Model.HistoryModel
import com.mpset.pokerevents.R
import com.mpset.pokerevents.Model.UpcomingModel

class HistoryListAdapter(val userList: ArrayList<HistoryModel>, var myClickListener: MyClickListener) : RecyclerView.Adapter<HistoryListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.snipped_history_page, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.count()
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val user : HistoryModel = userList[p1]
        p0.txtEventName?.text = user.eventName
        p0.txtTime?.text = user.time
        p0.txtAddress?.text = user.address
        p0.txtEventType?.text = user.eventType
        p0.txtStatus?.text = user.eventStatus
        p0.txtRole?.text = user.gameRole
        p0.onClickRow(p1,myClickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtEventName = itemView.findViewById(R.id.event_name_history) as TextView
        val txtTime = itemView.findViewById(R.id.time_date_event_history) as TextView
        val txtAddress = itemView.findViewById(R.id.location_event_history) as TextView
        val txtEventType = itemView.findViewById(R.id.event_type_history) as TextView
        val txtStatus = itemView.findViewById(R.id.event_status_history) as TextView
        val txtRole = itemView.findViewById(R.id.event_role_history) as TextView

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