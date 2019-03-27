package com.mpset.pokerevents.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.mpset.pokerevents.Fragments.*

class MyPagerAdapterViewProfile(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
               ViewProfileDetails()
            }
            1 -> {
                ViewProfileRating()
            }
            else -> {
                return ViewProfileStatistics()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Details"
            1 -> "Rate & Reviews"
            else -> {
                return "Statistics"
            }
        }

    }

}