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
import kotlinx.android.synthetic.main.snipped_reviews_details.view.*

class ReviewsDetailsListAdapter(val context:Context, val userList: ArrayList<ReviewsDetails>, var myClickListener: MyClickListener) : RecyclerView.Adapter<ReviewsDetailsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.snipped_reviews_details, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.count()
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val user : ReviewsDetails = userList[p1]
        p0.userName?.text = user.userName
        p0.time?.text = user.time
        p0.review?.text = user.review
        p0.rating?.rating = user.rating
        Glide.with(context)
                .load(user.url)
                .into(p0.imageUser)
        p0.onClickRow(p1,myClickListener)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName = itemView.findViewById(R.id.name_review_details) as TextView
        val imageUser = itemView.findViewById(R.id.image_review_details) as ImageView
        val time = itemView.time__review_details
        val rating = itemView.rating_review_details
        val review = itemView.review_review_details

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