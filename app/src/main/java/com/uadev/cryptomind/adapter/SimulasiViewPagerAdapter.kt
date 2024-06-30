package com.uadev.cryptomind.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.uadev.cryptomind.views.simulasi.dinamis.InputDekripsiFragment
import com.uadev.cryptomind.views.simulasi.dinamis.InputEnkripsiFragment

class SimulasiViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> InputEnkripsiFragment()
            1 -> InputDekripsiFragment()
            else -> throw IndexOutOfBoundsException()
        }
    }
}
