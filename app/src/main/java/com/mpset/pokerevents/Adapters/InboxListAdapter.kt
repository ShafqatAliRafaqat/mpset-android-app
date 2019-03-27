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
import com.mpset.pokerevents.Model.InboxModel
import com.mpset.pokerevents.Model.NotificationModel
import com.mpset.pokerevents.Model.PendingHostPublicModel
import com.mpset.pokerevents.R

class InboxListAdapter(val context:Context, val userList: ArrayList<InboxModel>, var myClickListener: MyClickListener) : RecyclerView.Adapter<InboxListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.snipped_inbox, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.count()
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val user : InboxModel = userList[p1]
        p0.userName?.text = user.userName
        p0.lastMessage?.text = user.lastMessage
        p0.lastMessageTime?.text = user.lastMessageTime
        Glide.with(context)
                .load(user.url)
                .into(p0.imageUser)
        p0.onClickRow(p1,myClickListener)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName = itemView.findViewById(R.id.inbox_user_name) as TextView
        val lastMessage = itemView.findViewById(R.id.inbox_last_message) as TextView
        val lastMessageTime = itemView.findViewById(R.id.inbox_time) as TextView
        val imageUser = itemView.findViewById(R.id.image_inbox) as ImageView

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