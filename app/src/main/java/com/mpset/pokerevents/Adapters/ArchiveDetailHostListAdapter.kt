package com.mpset.pokerevents.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mpset.pokerevents.Model.ArchiveDetailsHostModel
import com.mpset.pokerevents.Model.HeaderModel
import com.mpset.pokerevents.Model.NotificationModel
import com.mpset.pokerevents.R
import kotlinx.android.synthetic.main.snipped_host_archive_detail.view.*

class ArchiveDetailHostListAdapter(val context:Context?, val userList: ArrayList<ArchiveDetailsHostModel>, var myClickListener: MyClickListener) : RecyclerView.Adapter<ArchiveDetailHostListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.snipped_host_archive_detail, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.count()
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val user : ArchiveDetailsHostModel = userList[p1]
        p0.txtName?.text = user.userName
        p0.txtNickname?.text = user.userNickname
        p0.onClickRow(p1,myClickListener)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName = itemView.findViewById(R.id.archive_detail_host_name) as TextView
        val txtNickname = itemView.findViewById(R.id.archive_detail_host_nickname) as TextView
        val rating = itemView.archive_detail_host_rate_review

        fun  onClickRow(position: Int,myClickListener: MyClickListener){
            itemView.setOnClickListener {
                myClickListener.onClick(position)
            }
            itemView.setOnLongClickListener {
                myClickListener.onLongClick(position)
                return@setOnLongClickListener  true
            }
            rating.setOnClickListener {
                myClickListener.onRating(position)
            }
        }
    }

    interface MyClickListener {
        fun onClick(position: Int)
        fun onLongClick(position: Int)
        fun onRating(position: Int)
    }
}