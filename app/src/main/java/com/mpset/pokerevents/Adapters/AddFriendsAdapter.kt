package com.mpset.pokerevents.Adapters

import android.content.Context
import android.opengl.Visibility
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mpset.pokerevents.Model.AddFriendsModel
import com.mpset.pokerevents.Model.HeaderModel
import com.mpset.pokerevents.Model.MyFriendsModel
import com.mpset.pokerevents.Model.NotificationModel
import com.mpset.pokerevents.R
import kotlinx.android.synthetic.main.snipped_add_friends_list.view.*

class AddFriendsAdapter(val context:Context, val userList: ArrayList<AddFriendsModel>, var myClickListener: MyClickListener) : RecyclerView.Adapter<AddFriendsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.snipped_add_friends_list, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.count()
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val user : AddFriendsModel = userList[p1]
        p0.txtName?.text = user.friendName
        Glide.with(context)
                .load(user.url)
                .into(p0.myPic)
        if(user.friendshipStatus.equals("already_friend")){
            p0.imageAddFriend.visibility = View.GONE
            p0.imageDeclineReq.visibility = View.GONE
            p0.imageAcceptReq.visibility = View.GONE
            p0.imageCancelReq.visibility = View.GONE
            p0.imageUnfriend.visibility = View.VISIBLE

        }else if(user.friendshipStatus.equals("not_friend")){
            p0.imageAddFriend.visibility = View.VISIBLE
            p0.imageDeclineReq.visibility = View.GONE
            p0.imageAcceptReq.visibility = View.GONE
            p0.imageCancelReq.visibility = View.GONE
            p0.imageUnfriend.visibility = View.GONE

        }else if(user.friendshipStatus.equals("pending_acceptance")){
            p0.imageAddFriend.visibility = View.GONE
            p0.imageDeclineReq.visibility = View.GONE
            p0.imageAcceptReq.visibility = View.GONE
            p0.imageCancelReq.visibility = View.VISIBLE
            p0.imageUnfriend.visibility = View.GONE

        }else if(user.friendshipStatus.equals("requested_friendsip")){
            p0.imageAddFriend.visibility = View.GONE
            p0.imageDeclineReq.visibility = View.VISIBLE
            p0.imageAcceptReq.visibility = View.VISIBLE
            p0.imageCancelReq.visibility = View.GONE
            p0.imageUnfriend.visibility = View.GONE
        }
        p0.onClickRow(p1,myClickListener)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName = itemView.add_friend_name
        val  imageAddFriend = itemView.add_friend
        val  imageDeclineReq = itemView.decline_request_friend
        val  imageAcceptReq = itemView.accept_request_friend
        val  imageCancelReq = itemView.cancel_request_friend
        val  imageUnfriend = itemView.unfiriend_friend
        val  myPic = itemView.add_friend_profile_url
        val  chat = itemView.chat_add_friend

        fun  onClickRow(position: Int,myClickListener: MyClickListener){
            itemView.setOnClickListener {
                myClickListener.onClick(position)
            }
            itemView.setOnLongClickListener {
                myClickListener.onLongClick(position)
                return@setOnLongClickListener  true
            }
            imageAddFriend.setOnClickListener {
                myClickListener.onClickAddFriend(position)
            }
            imageDeclineReq.setOnClickListener {
                myClickListener.onClickDeclineReq(position)
            }
            imageAcceptReq.setOnClickListener {
                myClickListener.onClickAcceptReq(position)
            }
            imageCancelReq.setOnClickListener {
                myClickListener.onClickCancelReq(position)
            }
            imageUnfriend.setOnClickListener {
                myClickListener.onClickUnfriend(position)
            }
            chat.setOnClickListener {
                myClickListener.onClickChat(position)
            }
        }
    }

    interface MyClickListener {
        fun onClick(position: Int)
        fun onLongClick(position: Int)
        fun onClickAddFriend(position: Int)
        fun onClickDeclineReq(position: Int)
        fun onClickAcceptReq(position: Int)
        fun onClickCancelReq(position: Int)
        fun onClickUnfriend(position: Int)
        fun onClickChat(position: Int)
    }
}