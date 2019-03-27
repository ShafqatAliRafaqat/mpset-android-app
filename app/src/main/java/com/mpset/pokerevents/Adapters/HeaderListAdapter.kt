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
import com.mpset.pokerevents.R

class HeaderListAdapter(val context:Context ,val userList: ArrayList<HeaderModel>, var myClickListener: MyClickListener) : RecyclerView.Adapter<HeaderListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.snipped_header_page, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.count()
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val user : HeaderModel = userList[p1]
//        p0.txtEventName?.text = user.eventName
        Glide.with(context)
                .load(user.url)
                .into(p0.imageEvent)
        p0.onClickRow(p1,myClickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val txtEventName = itemView.findViewById(R.id.name_header_type) as TextView
        val imageEvent = itemView.findViewById(R.id.image_header_type) as ImageView

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