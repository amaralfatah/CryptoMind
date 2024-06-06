package com.uadev.cryptomind.views.simulasi.encrypt

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.uadev.cryptomind.R
import com.uadev.cryptomind.databinding.ActivityEncrypt4BitBinding
import com.uadev.cryptomind.views.simulasi.OptionSimulasiActivity

class Encrypt4BitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEncrypt4BitBinding
    private var currentStep = 1
    private val totalSteps = 14
    private var isPlaying = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEncrypt4BitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aktifkan action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Simulasi Enkripsi"
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

        binding.playPauseButton.setOnClickListener {
            if (isPlaying) {
                binding.videoView.pause()
                binding.playPauseButton.text = getString(R.string.play)
            } else {
                binding.videoView.start()
                binding.playPauseButton.text = getString(R.string.pause)
            }
            isPlaying = !isPlaying
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
            1 -> "cfb4/cfb4_intro.png"
            2 -> "cfb4/cfb4_step1.mp4"
            3 -> "cfb4/cfb4_step2.mp4"
            4 -> "cfb4/cfb4_step3.mp4"
            5 -> "cfb4/cfb4_step4.mp4"
            6 -> "cfb4/cfb4_step5.mp4"
            7 -> "cfb4/cfb4_step6.mp4"
            8 -> "cfb4/cfb4_step7.mp4"
            9 -> "cfb4/cfb4_step8.mp4"
            10 -> "cfb4/cfb4_step9.mp4"
            11 -> "cfb4/cfb4_step10.mp4"
            12 -> "cfb4/cfb4_step11.mp4"
            13 -> "cfb4/cfb4_step12.mp4"
            14 -> "cfb4/cfb4_outro.png"
            else -> "cfb4/cfb4_intro.png"
        }

        loadContent(contentPath, binding.imageView, binding.videoView, binding.loadingProgressBar)

        when (step) {
            1 -> binding.prevButton.visibility = View.INVISIBLE
            else -> binding.prevButton.visibility = View.VISIBLE
        }

        when (step) {
            totalSteps -> {
                binding.nextButton.text = getString(R.string.finish)
                binding.nextButton.setOnClickListener {
                    val intent = Intent(this, OptionSimulasiActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
            }
            else -> {
                binding.nextButton.text = getString(R.string.next)
                binding.nextButton.setOnClickListener {
                    if (currentStep < totalSteps) {
                        currentStep++
                        updateTutorialStep(currentStep)
                    }
                }
            }
        }
    }

    private fun loadContent(contentPath: String, imageView: ImageView, videoView: VideoView, progressBar: ProgressBar) {
        // Tampilkan progress bar saat memuat konten
        progressBar.visibility = View.VISIBLE

        // Referensi ke Firebase Storage
        val storageReference = FirebaseStorage.getInstance().reference.child(contentPath)

        // Dapatkan URL download dan muat konten
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            if (contentPath.endsWith(".png") || contentPath.endsWith(".jpg") || contentPath.endsWith(".jpeg")) {
                // Jika file adalah gambar, muat dengan Glide
                imageView.visibility = View.VISIBLE
                videoView.visibility = View.GONE
                binding.playPauseButton.visibility = View.GONE // Sembunyikan tombol play/pause
                Glide.with(this)
                    .load(uri)
                    .into(imageView)
                progressBar.visibility = View.GONE // Sembunyikan progress bar setelah gambar dimuat
            } else if (contentPath.endsWith(".mp4")) {
                // Jika file adalah video, muat dengan VideoView
                imageView.visibility = View.GONE
                videoView.visibility = View.VISIBLE
                binding.playPauseButton.visibility = View.VISIBLE // Tampilkan tombol play/pause

                videoView.setVideoURI(uri)
                videoView.setOnPreparedListener {
//                    it.isLooping = true
                    progressBar.visibility = View.GONE // Sembunyikan progress bar setelah video dimuat
                    videoView.start()
                    isPlaying = true
                    binding.playPauseButton.text = getString(R.string.pause)
                }

                videoView.setOnCompletionListener {
                    // Di sini Anda dapat menangani kejadian saat video selesai diputar
                    // Misalnya, mengubah teks tombol menjadi "Play" atau melakukan tindakan lain yang diinginkan.
                    isPlaying = false
                    binding.playPauseButton.text = getString(R.string.play)
//                    binding.playPauseButton.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_color))
//                    binding.playPauseButton.setTextColor(ContextCompat.getColor(this, R.color.white))
                }

            }
        }.addOnFailureListener { exception ->
            // Tangani kegagalan
            Log.e("Encrypt2BitActivity", "Error getting download URL", exception)
            Toast.makeText(this, "Gagal Mendapatkan Konten", Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE // Sembunyikan progress bar jika terjadi kegagalan
        }
    }
}