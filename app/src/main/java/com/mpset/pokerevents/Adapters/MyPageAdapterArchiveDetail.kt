package com.mpset.pokerevents.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.mpset.pokerevents.Fragments.*

class MyPagerAdapterArchiveDetail(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                ArchivePlayerDetail()
            }
            else -> {
                return ArchiveHostDetail()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Player"
            else -> {
                return "Host"
            }
        }

    }

}