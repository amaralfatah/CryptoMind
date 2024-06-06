package com.uadev.cryptomind.views.materi

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.uadev.cryptomind.R
import com.uadev.cryptomind.databinding.ActivityOptionMateriBinding
import com.uadev.cryptomind.views.materi.latihan.LatihanEnkripsiActivity
import com.uadev.cryptomind.views.materi.latihan.OptionLatihanActivity

class OptionMateriActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOptionMateriBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionMateriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aktifkan action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Materi"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.primary_color)))

        binding.btnMateri.setOnClickListener(){
            val intent = Intent(this, MateriActivity::class.java)
            startActivity(intent)
        }

        binding.btnLatihan.setOnClickListener(){
            val intent = Intent(this, OptionLatihanActivity::class.java)
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