package com.uadev.cryptomind.views

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.uadev.cryptomind.R
import com.uadev.cryptomind.databinding.ActivityProgramBinding
import com.uadev.cryptomind.databinding.ActivitySimulasiBinding

class SimulasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySimulasiBinding

    private var currentStep = 1
    private val totalSteps = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimulasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aktifkan action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Simulasi"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.primary_color)))

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        updateTutorialStep(currentStep)

        binding.nextButton.setOnClickListener {
            if (currentStep < totalSteps) {
                currentStep++
                updateTutorialStep(currentStep)
            }
        }

        binding.prevButton.setOnClickListener {
            if (currentStep > 1) {
                currentStep--
                updateTutorialStep(currentStep)
            }
        }

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

    private fun updateTutorialStep(step: Int) {
        when (step) {
            1 -> {
                // Mengatur gambar dan teks untuk langkah 1
                binding.imageView.setImageResource(R.drawable.ic_simulasi)
                binding.textView.text = getString(R.string.next)
                binding.prevButton.visibility = View.INVISIBLE // Tombol prev tidak terlihat pada langkah pertama
            }
            2 -> {
                // Mengatur gambar dan teks untuk langkah 2
                binding.imageView.setImageResource(R.drawable.ic_kuis)
                binding.textView.text = getString(R.string.intro2)
                binding.prevButton.visibility = View.VISIBLE // Tombol prev terlihat pada langkah kedua
            }
            3 -> {
                // Mengatur gambar dan teks untuk langkah 3
                binding.imageView.setImageResource(R.drawable.ic_program)
                binding.textView.text = getString(R.string.app_name)
            }
            4 -> {
                // Mengatur gambar dan teks untuk langkah 4
                binding.imageView.setImageResource(R.drawable.ic_materi)
                binding.textView.text = getString(R.string.dekripsi)
                binding.nextButton.text = "Finish" // Mengubah teks tombol next menjadi "Finish" pada langkah terakhir
            }
        }
    }
}
