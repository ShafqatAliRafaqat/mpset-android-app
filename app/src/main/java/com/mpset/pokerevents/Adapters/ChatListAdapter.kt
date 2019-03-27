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
import kotlinx.android.synthetic.main.activity_chat.view.*
import kotlinx.android.synthetic.main.snipped_chat_left_side.view.*
//import kotlinx.android.synthetic.main.snipped_chat_right_side.view.*

class ChatListAdapter(val context:Context, val userList: ArrayList<ChatModel>, var myClickListener: MyClickListener) : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {
    lateinit var v:View
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        if (p1 == 1)
         v = LayoutInflater.from(p0.context).inflate(R.layout.snipped_chat_left_side, p0, false)
        else
         v = LayoutInflater.from(p0.context).inflate(R.layout.snipped_chat_right_side, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.count()
    }

    override fun getItemViewType(position: Int): Int {
        var type:Int=-1
        if (userList.get(position).userType == 1){
            type = 1
    }else if(userList.get(position).userType==0) {
            type = 0
        }
        return  type
    }
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val user : ChatModel = userList[p1]
        p0.time?.text = user.time
        p0.lastMessage?.text = user.message
        Glide.with(context)
                .load(user.url)
                .into(p0.imageUser)
        p0.onClickRow(p1,myClickListener)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time = itemView.time_chat
        val lastMessage = itemView.message_chat
        val imageUser = itemView.image_chat

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