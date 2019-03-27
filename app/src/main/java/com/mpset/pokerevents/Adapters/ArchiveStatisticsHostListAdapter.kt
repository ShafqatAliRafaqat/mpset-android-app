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
import com.mpset.pokerevents.Model.ArchiveStatisticsHostModel
import com.mpset.pokerevents.Model.HeaderModel
import com.mpset.pokerevents.Model.NotificationModel
import com.mpset.pokerevents.R

class ArchiveStatisticsHostListAdapter(val context:Context?, val userList: ArrayList<ArchiveStatisticsHostModel>, var myClickListener: MyClickListener) : RecyclerView.Adapter<ArchiveStatisticsHostListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.snipped_host_archive_statistics, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.count()
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val user : ArchiveStatisticsHostModel = userList[p1]
        p0.txtName?.text = user.userName
        p0.txtNickname?.text = user.userNickname
        p0.txtBuyIntxtBuyIn?.text = user.buyIn.toString()
        p0.txtCashOut?.text = user.cashOut.toString()
        p0.txtBalance?.text = user.balance.toString()
        p0.onClickRow(p1,myClickListener)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName = itemView.findViewById(R.id.archive_statistics_host_name) as TextView
        val txtNickname = itemView.findViewById(R.id.archive_statistics_host_nickname) as TextView
        val txtBuyIntxtBuyIn = itemView.findViewById(R.id.txt_buyin_host_statistics) as TextView
        val txtCashOut = itemView.findViewById(R.id.txt_cashout_host_statistics) as TextView
        val txtBalance = itemView.findViewById(R.id.txt_balance_host_statistics) as TextView

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