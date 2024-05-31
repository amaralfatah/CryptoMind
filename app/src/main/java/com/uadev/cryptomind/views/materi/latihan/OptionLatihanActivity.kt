package com.uadev.cryptomind.views.materi.latihan

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.uadev.cryptomind.R
import com.uadev.cryptomind.databinding.ActivityOptionLatihanBinding
import com.uadev.cryptomind.views.materi.MateriActivity

@Suppress("DEPRECATION")
class OptionLatihanActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOptionLatihanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionLatihanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aktifkan action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Latihan"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.primary_color)))

        binding.btnLatihanEnkripsi.setOnClickListener {
            val intent = Intent(this, LatihanEnkripsiActivity::class.java)
            startActivity(intent)
        }

        binding.btnLatihanDekripsi.setOnClickListener {
            val intent = Intent(this, LatihanDekripsiActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle klik tombol kembali di action bar
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}