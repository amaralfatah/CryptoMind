package com.uadev.cryptomind.views.program

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.uadev.cryptomind.R
import com.uadev.cryptomind.adapter.ViewPagerAdapter
import com.uadev.cryptomind.databinding.ActivityProgramBinding

class ProgramActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProgramBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgramBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aktifkan action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Program"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.primary_color)))

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Buat adapter untuk ViewPager dan tambahkan fragmen ke dalamnya
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle klik tombol kembali di action bar
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
