package com.example.cointrack

import android.content.res.Resources.NotFoundException
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fragmentActivity : FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> { FragmentStatistics() }
            1 -> { FragmentList() }
            else -> { throw NotFoundException("Position not found") }
        }
    }
}