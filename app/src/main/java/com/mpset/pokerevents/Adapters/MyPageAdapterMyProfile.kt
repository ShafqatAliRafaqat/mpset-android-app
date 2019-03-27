package com.mpset.pokerevents.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.mpset.pokerevents.Fragments.*

class MyPagerAdapterMyProfile(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                MyProfileDetails()
            }
            1 -> {
                MyProfileRatings()
            }
            else -> {
                return MyProfileStatistics()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "My Profile"
            1 -> "My Ratings"
            else -> {
                return "My Statistics"
            }
        }

    }

}