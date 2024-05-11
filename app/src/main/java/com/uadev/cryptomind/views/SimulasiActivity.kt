package com.uadev.cryptomind.views

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.uadev.cryptomind.R
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
                Glide.with(this)
                    .load(R.drawable.cfb4_intro)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(binding.imageView)
//                binding.textView.text = getString(R.string.cfb_materi)
                binding.prevButton.visibility = View.INVISIBLE
            }
            2 -> {
                Glide.with(this)
                    .load(R.drawable.cfb4_langkah_1)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(binding.imageView)
//                binding.textView.text = getString(R.string.intro2)
                binding.prevButton.visibility = View.VISIBLE
            }
            3 -> {
                Glide.with(this)
                    .load(R.drawable.cfb4_langkah_2)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(binding.imageView)
//                binding.textView.text = getString(R.string.app_name)
                binding.nextButton.text = "Next"
            }
            4 -> {
                Glide.with(this)
                    .load(R.drawable.cfb4_langkah_3)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(binding.imageView)
//                binding.textView.text = getString(R.string.dekripsi)
                binding.nextButton.text = "Finish"
            }
        }
    }

}
