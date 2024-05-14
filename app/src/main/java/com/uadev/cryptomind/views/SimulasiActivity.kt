package com.uadev.cryptomind.views

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.uadev.cryptomind.R
import com.uadev.cryptomind.databinding.ActivitySimulasiBinding
import android.util.Log
import android.widget.Toast

class SimulasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySimulasiBinding

    private var currentStep = 1
    private val totalSteps = 15

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

        // Muat konten pertama dari Firebase Storage
        updateTutorialStep(currentStep)
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
        val contentPath = when (step) {
            1 -> "cfb2/cfb2_intro.png"
            2 -> "cfb2/cfb2_step1.mp4"
            3 -> "cfb2/cfb2_step2.mp4"
            4 -> "cfb2/cfb2_step3.mp4"
            5 -> "cfb2/cfb2_step4.mp4"
            6 -> "cfb2/cfb2_step5.mp4"
            7 -> "cfb2/cfb2_step6.mp4"
            8 -> "cfb2/cfb2_step7.mp4"
            9 -> "cfb2/cfb2_step8.mp4"
            10 -> "cfb2/cfb2_step9.mp4"
            11 -> "cfb2/cfb2_step10.mp4"
            12 -> "cfb2/cfb2_step11.mp4"
            13 -> "cfb2/cfb2_step12.mp4"
            14 -> "cfb2/cfb2_step13.mp4"
            15 -> "cfb2/cfb2_outro.png"
            else -> "cfb2/cfb2_intro.png"
        }

        loadContent(contentPath, binding.imageView, binding.videoView)

        when (step) {
            1 -> binding.prevButton.visibility = View.INVISIBLE
            else -> binding.prevButton.visibility = View.VISIBLE
        }

        when (step) {
            totalSteps -> binding.nextButton.text = "Finish"
            else -> binding.nextButton.text = "Next"
        }
    }

    private fun loadContent(contentPath: String, imageView: ImageView, videoView: VideoView) {
        // Referensi ke Firebase Storage
        val storageReference = FirebaseStorage.getInstance().reference.child(contentPath)

        // Dapatkan URL download dan muat konten
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            if (contentPath.endsWith(".png") || contentPath.endsWith(".jpg") || contentPath.endsWith(".jpeg")) {
                // Jika file adalah gambar, muat dengan Glide
                imageView.visibility = View.VISIBLE
                videoView.visibility = View.GONE
                Glide.with(this)
                    .load(uri)
                    .into(imageView)
            } else if (contentPath.endsWith(".mp4")) {
                // Jika file adalah video, muat dengan VideoView
                imageView.visibility = View.GONE
                videoView.visibility = View.VISIBLE
                videoView.setVideoURI(uri)
                videoView.setOnPreparedListener { it.isLooping = true }
                videoView.start()
            }
        }.addOnFailureListener { exception ->
            // Tangani kegagalan
            Log.e("SimulasiActivity", "Error getting download URL", exception)
            Toast.makeText(this, "Failed to load content", Toast.LENGTH_SHORT).show()
        }
    }
}
