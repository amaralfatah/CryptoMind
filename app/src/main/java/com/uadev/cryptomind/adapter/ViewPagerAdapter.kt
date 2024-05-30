package com.uadev.cryptomind.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.uadev.cryptomind.views.program.DecryptionFragment
import com.uadev.cryptomind.views.program.EncryptionFragment

class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> EncryptionFragment()
            1 -> DecryptionFragment()
            else -> throw IndexOutOfBoundsException()
        }
    }
}
