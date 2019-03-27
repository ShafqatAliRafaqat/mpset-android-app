package com.mpset.pokerevents.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mpset.pokerevents.Model.HeaderModel
import com.mpset.pokerevents.Model.NotificationModel
import com.mpset.pokerevents.Model.PendingHostPrivateModel
import com.mpset.pokerevents.Model.PendingHostPublicModel
import com.mpset.pokerevents.R

class PendingHostPrivateListAdapter(val context:Context, val userList: ArrayList<PendingHostPrivateModel>, var myClickListener: MyClickListener) : RecyclerView.Adapter<PendingHostPrivateListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.snipped_pending_host_private, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.count()
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val user : PendingHostPrivateModel = userList[p1]
        p0.txtPlayerName?.text = user.playerName
        p0.txtPlayerDate1?.text = user.playerDate1
        p0.txtPlayerDate2?.text = user.playerDate2
        p0.onClickRow(p1,myClickListener)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtPlayerName = itemView.findViewById(R.id.player_name_pending_host_private) as TextView
        val txtPlayerDate1 = itemView.findViewById(R.id.player_status_pending_host_private_date1) as TextView
        val txtPlayerDate2 = itemView.findViewById(R.id.player_status_pending_host_private_date2) as TextView

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