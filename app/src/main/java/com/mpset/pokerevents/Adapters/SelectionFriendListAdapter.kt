package com.mpset.pokerevents.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mpset.pokerevents.Activities.NewEventFriendSelection.Companion.usersData
import com.mpset.pokerevents.Model.HeaderModel
import com.mpset.pokerevents.Model.SelectionFriendsModel
import com.mpset.pokerevents.R
import org.json.JSONArray

class SelectionFriendListAdapter(val context:Context, val userList: ArrayList<SelectionFriendsModel>, var myClickListener: MyClickListener) : RecyclerView.Adapter<SelectionFriendListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.snipped_friend_selection_page, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.count()
    }


    fun updateUserCheckedStatus(user:SelectionFriendsModel, value:Boolean){
        for(i in 0..usersData!!.size-1){
            var dataUser = usersData.get(i)
            if(user.friendId.equals(dataUser.friendId)){
                dataUser.ischeck = value
            }
        }
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val user  = userList[p1]
        p0.txtName?.text = user.friendName
        Glide.with(context)
                .load(user.url)
                .into(p0.imageFriend)
        p0.onClickRow(p1,myClickListener)
        p0.checkBox.isChecked = user.ischeck
        p0.checkBox.setOnClickListener {
            if(p0.checkBox.isChecked){
                arrayListUsers.add(user.friendId.toString())
//                p0.checkBox.isChecked = true
                updateUserCheckedStatus(user,true)
                p0.checkBox.isChecked = true
            }else{
                arrayListUsers.remove(user.friendId.toString())
                updateUserCheckedStatus(user,false)
                p0.checkBox.isChecked = false
//                user.ischeck = false
            }
        }
//        p0.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
//            if(isChecked){
//
//            }else{
////                for (i in 0..arrayList!!.size - 1){
////                    if(arrayList.get(i).toString().equals(user.friendId)){
//
////                    }
////
////                }
//            }
//        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName = itemView.findViewById(R.id.friend_select_name) as TextView
        val imageFriend = itemView.findViewById(R.id.friend_select_url) as ImageView
        val checkBox = itemView.findViewById(R.id.check_selection_friend_page) as CheckBox

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
    companion object {
        val arrayListUsers = ArrayList<String>()
    }

}