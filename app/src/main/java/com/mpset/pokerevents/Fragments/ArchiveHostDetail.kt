package com.mpset.pokerevents.Fragments


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.mpset.pokerevents.Adapters.ArchiveDetailHostListAdapter
import com.mpset.pokerevents.Adapters.ArchiveStatisticsHostListAdapter
import com.mpset.pokerevents.Adapters.NotificationListAdapter
import com.mpset.pokerevents.Model.ArchiveDetailsHostModel
import com.mpset.pokerevents.Model.ArchiveStatisticsHostModel
import com.mpset.pokerevents.Model.NotificationModel

import com.mpset.pokerevents.R
import kotlinx.android.synthetic.main.fragment_archive_host_detail.*
import kotlinx.android.synthetic.main.fragment_archive_player_detail.*
import java.util.ArrayList


class ArchiveHostDetail : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_archive_host_detail, container, false)
        ///////////////////////////////////////////////////
//        val recyclerview = view.findViewById(R.id.rcyclerview_plaerlist_archive_host_details) as RecyclerView
//        val recyclerview2 = view.findViewById(R.id.rcyclerview_plaerlist_archive_host_statistics) as RecyclerView
//        val viewDetailsArchiveHost = view.findViewById<TextView>(R.id.view_details_archive_host)
//        val viewStatisticsArchiveHost = view.findViewById<TextView>(R.id.view_statistics_archive_host)
//        recyclerview.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL,false)
//        recyclerview2.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL,false)
//
//        val users = ArrayList<ArchiveDetailsHostModel>()
//        users.add(ArchiveDetailsHostModel(1,"Mr Albert Jhon", "Jhon ge"))
//        users.add(ArchiveDetailsHostModel(1,"Mr Albert Jhon", "Jhon ge"))
//        users.add(ArchiveDetailsHostModel(1,"Mr Albert Jhon", "Jhon ge"))
//        users.add(ArchiveDetailsHostModel(1,"Mr Albert Jhon", "Jhon ge"))
//        users.add(ArchiveDetailsHostModel(1,"Mr Albert Jhon", "Jhon ge"))
//        users.add(ArchiveDetailsHostModel(1,"Mr Albert Jhon", "Jhon ge"))
//        users.add(ArchiveDetailsHostModel(1,"Mr Albert Jhon", "Jhon ge"))
//        users.add(ArchiveDetailsHostModel(1,"Mr Albert Jhon", "Jhon ge"))
//        users.add(ArchiveDetailsHostModel(1,"Mr Albert Jhon", "Jhon ge"))
//
//        val users2 = ArrayList<ArchiveStatisticsHostModel>()
//          users2.add(ArchiveStatisticsHostModel(1,"Mr Albert Jhon", "Jhon ge",8,12999.0,255557.6))
//          users2.add(ArchiveStatisticsHostModel(1,"Mr Albert Jhon", "Jhon ge",8,12999.0,255557.6))
//          users2.add(ArchiveStatisticsHostModel(1,"Mr Albert Jhon", "Jhon ge",8,12999.0,255557.6))
//          users2.add(ArchiveStatisticsHostModel(1,"Mr Albert Jhon", "Jhon ge",8,12999.0,255557.6))
//          users2.add(ArchiveStatisticsHostModel(1,"Mr Albert Jhon", "Jhon ge",8,12999.0,255557.6))
//          users2.add(ArchiveStatisticsHostModel(1,"Mr Albert Jhon", "Jhon ge",8,12999.0,255557.6))
//          users2.add(ArchiveStatisticsHostModel(1,"Mr Albert Jhon", "Jhon ge",8,12999.0,255557.6))
//          users2.add(ArchiveStatisticsHostModel(1,"Mr Albert Jhon", "Jhon ge",8,12999.0,255557.6))
//          users2.add(ArchiveStatisticsHostModel(1,"Mr Albert Jhon", "Jhon ge",8,12999.0,255557.6))
//
//
//
//        val adapter = ArchiveDetailHostListAdapter(context,users, object : ArchiveDetailHostListAdapter.MyClickListener {
//            override fun onClick(position: Int) {
//                Toast.makeText(context,"ssss", Toast.LENGTH_LONG).show()
//
//
//
//            }
//
//            override fun onLongClick(position: Int) {
//                Toast.makeText(context, "You got a long click it", Toast.LENGTH_LONG).show()
//            }
//
//        })
//        val adapter2 = ArchiveStatisticsHostListAdapter(context,users2, object : ArchiveStatisticsHostListAdapter.MyClickListener {
//            override fun onClick(position: Int) {
//                Toast.makeText(context,"ssss2", Toast.LENGTH_LONG).show()
//
//
//
//            }
//
//            override fun onLongClick(position: Int) {
//                Toast.makeText(context, "You got a long click it", Toast.LENGTH_LONG).show()
//            }
//
//        })
//        recyclerview.adapter = adapter
//        recyclerview2.adapter = adapter2
//        ////////////////////////////////////////////////////////////////////////////////
////        viewDetailsArchiveHost.setOnClickListener {
////            host_detail_part.visibility = View.VISIBLE
////            host_statistics_part.visibility = View.GONE
////        }
////        viewStatisticsArchiveHost.setOnClickListener {
////            host_detail_part.visibility = View.GONE
////            host_statistics_part.visibility = View.VISIBLE
////        }
//
//        viewDetailsArchiveHost.setOnClickListener {
//            host_detail_part.visibility = View.VISIBLE
//            host_detail_part2.visibility = View.VISIBLE
//            recyclerview.visibility = View.VISIBLE
//            recyclerview2.visibility = View.INVISIBLE
//            host_statistics_part.visibility = View.GONE
//            viewDetailsArchiveHost.background = resources.getDrawable(R.drawable.goldtoggleleft)
//            viewStatisticsArchiveHost.background = resources.getDrawable(R.drawable.whitetoggleright)
//            viewDetailsArchiveHost.setTextColor(Color.parseColor("#ffffff"))
//            viewStatisticsArchiveHost.setTextColor(Color.parseColor("#000000"))
//        }
//
//        viewStatisticsArchiveHost.setOnClickListener {
//            host_detail_part.visibility = View.GONE
//            host_statistics_part.visibility = View.VISIBLE
//            recyclerview.visibility = View.INVISIBLE
//            host_detail_part2.visibility = View.INVISIBLE
//            recyclerview2.visibility = View.VISIBLE
//            viewStatisticsArchiveHost.background = resources.getDrawable(R.drawable.goldtoggleright)
//            viewDetailsArchiveHost.background = resources.getDrawable(R.drawable.whitetoggleleft)
//            viewStatisticsArchiveHost.setTextColor(Color.parseColor("#ffffff"))
//            viewDetailsArchiveHost.setTextColor(Color.parseColor("#000000"))
//        }
        return view
    }


}
