package com.uadev.cryptomind.views.tentang

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.uadev.cryptomind.R
import com.uadev.cryptomind.databinding.ActivityTentangBinding

class TentangActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTentangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTentangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aktifkan action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Tentang Kami"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.primary_color)))

        setupDeveloperLink()
    }

    private fun setupDeveloperLink() {
        val developerText = binding.tvDev.text.toString()
        val developerSpannableString = SpannableString(developerText)

        val githubSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/amaralfatah/CryptoMind"))
                startActivity(intent)
            }
        }

        val developerStart = developerText.indexOf("Amar Al Fatah")
        val developerEnd = developerStart + "Amar Al Fatah".length
        developerSpannableString.setSpan(
            githubSpan,
            developerStart,
            developerEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvDev.text = developerSpannableString
        binding.tvDev.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
