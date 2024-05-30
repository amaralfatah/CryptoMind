package com.uadev.cryptomind.views.kuis

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uadev.cryptomind.R
import com.uadev.cryptomind.databinding.ActivityResultBinding
import com.uadev.cryptomind.views.MainActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val score = intent.getIntExtra("SCORE", 0)
        binding.scoreTextView.text = getString(R.string.score_format, score)

        binding.retryButton.setOnClickListener {
            startMainActivity()
        }

        binding.dashboardButton.setOnClickListener {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
