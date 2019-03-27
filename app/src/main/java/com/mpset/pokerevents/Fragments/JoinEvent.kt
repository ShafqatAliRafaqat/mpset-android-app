package com.mpset.pokerevents.Fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mpset.pokerevents.Activities.JoinEventDetailPage
import com.mpset.pokerevents.Adapters.UpcomingListAdapter
import com.mpset.pokerevents.Model.UpcomingModel

import com.mpset.pokerevents.R
import java.util.ArrayList


class JoinEvent : Fragment() {
    val imageslist: ArrayList<String> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_join_event, container, false)
        val recyclerview = view.findViewById(R.id.recyclerview_join_event) as RecyclerView

        recyclerview.layoutManager = LinearLayoutManager(context)
        imageslist.add("uploads/locationImages/2019/January/1546493247_ujy84fgphNVIEyl.jpeg")
        imageslist.add("uploads/locationImages/2019/January/1546493247_ujy84fgphNVIEyl.jpeg")
        imageslist.add("uploads/locationImages/2019/January/1546493247_ujy84fgphNVIEyl.jpeg")
        val users = ArrayList<UpcomingModel>()
        users.add(UpcomingModel("1", "CardsChat Hurricane Harvey Charity Event", "April 17, 2018 9:30 AM", "Host Name", "Defence Phase 2 lahore","Game Type",2.2))
        users.add(UpcomingModel("1", "CardsChat Hurricane Harvey Charity Event", "April 17, 2018 9:30 AM", "Host Name", "Gulberg || lahore","Tournament",2.2))
        users.add(UpcomingModel("1", "CardsChat Hurricane Harvey Charity Event", "April 17, 2018 9:30 AM", "Host Name", "Liberty lahore","Cash",2.2))
        users.add(UpcomingModel("1", "CardsChat Hurricane Harvey Charity Event", "April 17, 2018 9:30 AM", "Host Name", "Edden Tower lahore","Tournament",2.2))
        users.add(UpcomingModel("1", "CardsChat Hurricane Harvey Charity Event", "April 17, 2018 9:30 AM", "Host Name", "Vivo Tower lahore","Cash",2.2))
        users.add(UpcomingModel("1", "CardsChat Hurricane Harvey Charity Event", "April 17, 2018 9:30 AM", "Host Name", "Hafeez Center lahore","Tournament",2.2))
        users.add(UpcomingModel("1", "CardsChat Hurricane Harvey Charity Event", "April 17, 2018 9:30 AM", "Host Name", "Gazi Chowk lahore","Cash",2.2))
        users.add(UpcomingModel("1", "CardsChat Hurricane Harvey Charity Event", "April 17, 2018 9:30 AM", "Host Name", "Canal Mor lahore","Tournament",2.2))
        users.add(UpcomingModel("1", "CardsChat Hurricane Harvey Charity Event", "April 17, 2018 9:30 AM", "Host Name", "Nazmabad lahore","Cash",2.2))
        users.add(UpcomingModel("1", "CardsChat Hurricane Harvey Charity Event", "April 17, 2018 9:30 AM", "Host Name", "Ali View Garden lahore","Tournament",2.2))


        val adapter = UpcomingListAdapter(users, object : UpcomingListAdapter.MyClickListener {
            override fun onClick(position: Int) {
                Toast.makeText(context, users[position].id, Toast.LENGTH_LONG).show()
                val intent = Intent(context, JoinEventDetailPage::class.java)
//                intent.putExtra("event_id", users[position].id)
                intent.putExtra("event_name", users[position].eventName)
                startActivity(intent)


            }

            override fun onLongClick(position: Int) {
                Toast.makeText(context, "You got a long click it", Toast.LENGTH_LONG).show()
            }

        })
        recyclerview.adapter = adapter
        return  view
    }


}
