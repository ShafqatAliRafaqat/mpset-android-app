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
import com.mpset.pokerevents.Model.PendingHostPublicModel
import com.mpset.pokerevents.R
import kotlinx.android.synthetic.main.snipped_pending_host_public.view.*

class PendingHostPublicListAdapter(val context:Context, val userList: ArrayList<PendingHostPublicModel>, var myClickListener: MyClickListener) : RecyclerView.Adapter<PendingHostPublicListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.snipped_pending_host_public, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.count()
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val user : PendingHostPublicModel = userList[p1]
        p0.txtPlayerName?.text = user.playerName
        p0.txtPlayerStatus?.text = user.playerStatus
        if(user.isApprove==0){
            p0.accept.visibility = View.VISIBLE
        }else{
            p0.accept.visibility = View.GONE
        }
        p0.onClickRow(p1,myClickListener)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtPlayerName = itemView.findViewById(R.id.player_name_pending_host_public) as TextView
        val txtPlayerStatus = itemView.findViewById(R.id.player_status_pending_host_public) as TextView
        val accept = itemView.accept_icon
        val reject = itemView.reject_request
        val viewProfile = itemView.view_profile

        fun  onClickRow(position: Int,myClickListener: MyClickListener){
            itemView.setOnClickListener {
                myClickListener.onClick(position)
            }
            itemView.setOnLongClickListener {
                myClickListener.onLongClick(position)
                return@setOnLongClickListener  true
            }
            accept.setOnClickListener {
             myClickListener.onClickAcceptRequest(position)
            }
            reject.setOnClickListener {
                myClickListener.onClickRejectRequest(position)
            }
            viewProfile.setOnClickListener {
                myClickListener.onClickViewProfile(position)
            }
        }
    }

    interface MyClickListener {
        fun onClick(position: Int)
        fun onLongClick(position: Int)
        fun onClickAcceptRequest(position: Int)
        fun onClickRejectRequest(position: Int)
        fun onClickViewProfile(position: Int)
    }
}