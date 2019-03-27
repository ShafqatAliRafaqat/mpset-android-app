package com.mpset.pokerevents.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.mpset.pokerevents.Fragments.Attending
import com.mpset.pokerevents.Fragments.JoinEvent
import com.mpset.pokerevents.Fragments.JoinMaps
import com.mpset.pokerevents.Fragments.UpComming

class MyPagerAdapterJoinEvent(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                JoinEvent()
            }
            else -> {
                return JoinMaps()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

//    override fun getPageTitle(position: Int): CharSequence {
//        return when (position) {
//            0 -> "Upcoming"
//            else -> {
//                return "Archive"
//            }
//        }

//    }

}