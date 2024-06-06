package com.uadev.cryptomind.views.simulasi

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.uadev.cryptomind.R
import com.uadev.cryptomind.databinding.ActivityOptionSimulasiBinding
import com.uadev.cryptomind.views.simulasi.decrypt.Decrypt2BitActivity
import com.uadev.cryptomind.views.simulasi.decrypt.Decrypt8BitActivity
import com.uadev.cryptomind.views.simulasi.encrypt.Encrypt2BitActivity
import com.uadev.cryptomind.views.simulasi.encrypt.Encrypt4BitActivity
import com.uadev.cryptomind.views.simulasi.encrypt.Encrypt8BitActivity

class OptionSimulasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOptionSimulasiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionSimulasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aktifkan action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Simulasi"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.primary_color)))

//        enableEdgeToEdge()
//        setContentView(R.layout.activity_option_simulasi)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        // Enkripsi
        binding.btnEn2Bit.setOnClickListener {
            val intent = Intent(this, Encrypt2BitActivity::class.java)
            startActivity(intent)
        }
        binding.btnEn4Bit.setOnClickListener {
            val intent = Intent(this, Encrypt4BitActivity::class.java)
            startActivity(intent)
        }
        binding.btnEn8Bit.setOnClickListener {
            val intent = Intent(this, Encrypt8BitActivity::class.java)
            startActivity(intent)
        }
        
        // Dekripsi
        binding.btnDek2Bit.setOnClickListener {
            val intent = Intent(this, Decrypt2BitActivity::class.java)
            startActivity(intent)
        }
//        binding.btnDek4Bit.setOnClickListener {
//            val intent = Intent(this, Encrypt2BitActivity::class.java)
//            startActivity(intent)
//        }
        binding.btnDek8Bit.setOnClickListener {
            val intent = Intent(this, Decrypt8BitActivity::class.java)
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