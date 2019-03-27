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
import com.mpset.pokerevents.Model.MyFriendsModel
import com.mpset.pokerevents.Model.NotificationModel
import com.mpset.pokerevents.R
import kotlinx.android.synthetic.main.snipped_my_friends_list.view.*

class MyFriendsAdapter(val context:Context, val userList: ArrayList<MyFriendsModel>, var myClickListener: MyClickListener) : RecyclerView.Adapter<MyFriendsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.snipped_my_friends_list, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.count()
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val user : MyFriendsModel = userList[p1]
        p0.txtNotificationName?.text = user.friendName
        Glide.with(context)
                .load(user.url)
                .into(p0.myPic)
        p0.onClickRow(p1,myClickListener)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNotificationName = itemView.findViewById(R.id.my_friend_name) as TextView
        val myPic = itemView.my_friend_pic
        val viewProfile = itemView.view_profile_my_friend
        val chat = itemView.chat_my_friend
        val unFriend = itemView.unfriend_my_friend

        fun  onClickRow(position: Int,myClickListener: MyClickListener){
            itemView.setOnClickListener {
                myClickListener.onClick(position)
            }
            itemView.setOnLongClickListener {
                myClickListener.onLongClick(position)
                return@setOnLongClickListener  true
            }
            viewProfile.setOnClickListener {
                myClickListener.onItemClickViewProfile(position)
            }
            chat.setOnClickListener {
                myClickListener.onItemClickChat(position)
            }
            unFriend.setOnClickListener {
                myClickListener.onItemClickUnfriend(position)
            }
        }
    }

    interface MyClickListener {
        fun onClick(position: Int)
        fun onLongClick(position: Int)
        fun onItemClickViewProfile(position: Int)
        fun onItemClickChat(position: Int)
        fun onItemClickUnfriend(position: Int)

    }
}