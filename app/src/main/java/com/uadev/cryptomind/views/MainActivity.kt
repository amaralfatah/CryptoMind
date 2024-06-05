package com.uadev.cryptomind.views

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uadev.cryptomind.R
import com.uadev.cryptomind.databinding.ActivityMainBinding
import com.uadev.cryptomind.views.info.InfoActivity
import com.uadev.cryptomind.views.kuis.QuizActivity
import com.uadev.cryptomind.views.materi.OptionMateriActivity
import com.uadev.cryptomind.views.bantuan.BantuanActivity
import com.uadev.cryptomind.views.program.ProgramActivity
import com.uadev.cryptomind.views.simulasi.OptionSimulasiActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Menambahkan event listener untuk button
        binding.btnKuis.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }

        binding.btnMateri.setOnClickListener {
            val intent = Intent(this, OptionMateriActivity::class.java)
            startActivity(intent)
        }

        binding.btnProgram.setOnClickListener {
            val intent = Intent(this, ProgramActivity::class.java)
            startActivity(intent)
        }

        binding.btnSimulasi.setOnClickListener {
            val intent = Intent(this, OptionSimulasiActivity::class.java)
            startActivity(intent)
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_petunjuk -> {
                val intent = Intent(this, BantuanActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_info -> {
                val intent = Intent(this, InfoActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
