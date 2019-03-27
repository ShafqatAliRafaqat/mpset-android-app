package com.mpset.pokerevents.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.mpset.pokerevents.Fragments.*

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                UpComming()
            }
            1 -> Attending()
            2 -> Pending()
            3 -> Hosting()
            else -> {
                return History()
            }
        }
    }

    override fun getCount(): Int {
        return 5
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Upcoming"
            1 -> "Attending"
            2 -> "Pending"
            3 -> "Hosting"
            else -> {
                return "Archive"
            }
        }
    }

}