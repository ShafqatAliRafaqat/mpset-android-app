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
import com.mpset.pokerevents.Model.ArchiveDetailsHostModel
import com.mpset.pokerevents.Model.ArchiveStatisticsHostModel
import com.mpset.pokerevents.R
import kotlinx.android.synthetic.main.fragment_archive_host_detail.*
import kotlinx.android.synthetic.main.fragment_archive_player_detail.*
import java.util.ArrayList


class ArchivePlayerDetail : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_archive_player_detail, container, false)
//        val viewDetailsArchivePlayer = view.findViewById<TextView>(R.id.view_details_archive_player)
//        val viewStatisticsArchivePlayer = view.findViewById<TextView>(R.id.view_statistics_archive_player)
//        //////////////////////////////////////////////
//        val recyclerview = view.findViewById(R.id.rcyclerview_plaerlist_archive_player_details) as RecyclerView
//        val recyclerview2 = view.findViewById(R.id.rcyclerview_plaerlist_archive_player_statistics) as RecyclerView
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
//        users2.add(ArchiveStatisticsHostModel(1,"Mr Albert Jhon", "Jhon ge",8,12999.0,255557.6))
//        users2.add(ArchiveStatisticsHostModel(1,"Mr Albert Jhon", "Jhon ge",8,12999.0,255557.6))
//        users2.add(ArchiveStatisticsHostModel(1,"Mr Albert Jhon", "Jhon ge",8,12999.0,255557.6))
//        users2.add(ArchiveStatisticsHostModel(1,"Mr Albert Jhon", "Jhon ge",8,12999.0,255557.6))
//        users2.add(ArchiveStatisticsHostModel(1,"Mr Albert Jhon", "Jhon ge",8,12999.0,255557.6))
//        users2.add(ArchiveStatisticsHostModel(1,"Mr Albert Jhon", "Jhon ge",8,12999.0,255557.6))
//        users2.add(ArchiveStatisticsHostModel(1,"Mr Albert Jhon", "Jhon ge",8,12999.0,255557.6))
//        users2.add(ArchiveStatisticsHostModel(1,"Mr Albert Jhon", "Jhon ge",8,12999.0,255557.6))
//        users2.add(ArchiveStatisticsHostModel(1,"Mr Albert Jhon", "Jhon ge",8,12999.0,255557.6))
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
//        ///////////////////////////////////////////////
//
//        viewDetailsArchivePlayer.setOnClickListener {
//            player_detail.visibility = View.VISIBLE
//            player_sub.visibility = View.VISIBLE
//            recyclerview.visibility = View.VISIBLE
//            recyclerview2.visibility = View.INVISIBLE
//            player_statistics_part.visibility = View.GONE
//            viewDetailsArchivePlayer.background = resources.getDrawable(R.drawable.goldtoggleleft)
//            viewStatisticsArchivePlayer.background = resources.getDrawable(R.drawable.whitetoggleright)
//            viewDetailsArchivePlayer.setTextColor(Color.parseColor("#ffffff"))
//            viewStatisticsArchivePlayer.setTextColor(Color.parseColor("#000000"))
//        }
//
//        viewStatisticsArchivePlayer.setOnClickListener {
//            player_detail.visibility = View.GONE
//            player_statistics_part.visibility = View.VISIBLE
//            recyclerview.visibility = View.INVISIBLE
//            player_sub.visibility = View.INVISIBLE
//            recyclerview2.visibility = View.VISIBLE
//            viewStatisticsArchivePlayer.background = resources.getDrawable(R.drawable.goldtoggleright)
//            viewDetailsArchivePlayer.background = resources.getDrawable(R.drawable.whitetoggleleft)
//            viewStatisticsArchivePlayer.setTextColor(Color.parseColor("#ffffff"))
//            viewDetailsArchivePlayer.setTextColor(Color.parseColor("#000000"))
//        }
        return view
    }


}
