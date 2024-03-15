package com.uadev.cryptomind.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.uadev.cryptomind.R

class ResultActivity : AppCompatActivity() {

    private lateinit var scoreTextView: TextView
    private lateinit var retryButton: Button
    private lateinit var dashboardButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        initializeViews()

        val score = intent.getIntExtra("SCORE", 0)
        scoreTextView.text = getString(R.string.score_format, score)

        retryButton.setOnClickListener {
            startMainActivity()
        }

        dashboardButton.setOnClickListener {
            startMainActivity()
        }
    }

    private fun initializeViews() {
        scoreTextView = findViewById(R.id.scoreTextView)
        retryButton = findViewById(R.id.retryButton)
        dashboardButton = findViewById(R.id.dashboardButton)
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
