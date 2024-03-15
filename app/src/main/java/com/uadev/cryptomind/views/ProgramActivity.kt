package com.uadev.cryptomind.views

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.uadev.cryptomind.R
import com.uadev.cryptomind.databinding.ActivityProgramBinding

class ProgramActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProgramBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgramBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Buat adapter untuk ViewPager dan tambahkan fragmen ke dalamnya
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = ViewPagerAdapter(this)

        // Hubungkan ViewPager dengan TabLayout
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.enkripsi)
                1 -> getString(R.string.dekripsi)
                else -> ""
            }
        }.attach()
    }
}
