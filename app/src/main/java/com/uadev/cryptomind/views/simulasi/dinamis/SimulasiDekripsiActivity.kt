package com.uadev.cryptomind.views.simulasi.dinamis

import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.uadev.cryptomind.R
import com.uadev.cryptomind.databinding.ActivitySimulasiDekripsiBinding
import com.uadev.cryptomind.utils.Utils
import android.os.Handler

class SimulasiDekripsiActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySimulasiDekripsiBinding

    private var plaintext: List<String> = emptyList()
    private var key: List<String> = emptyList()
    private var antrian: MutableList<String> = mutableListOf()
    private var ciphertext: MutableList<String> = mutableListOf()

    private val duration = 500L
    private val handler = Handler()
    private var cumulativeDelay = 0L

    private var hasilXorEnkripsi: MutableList<String> = mutableListOf()
    private var hasilGeserEnkripsi: MutableList<String> = mutableListOf()

    private var currentStep = 0
    private var isPlaying = false
    private val stepActions = mutableListOf<Runnable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimulasiDekripsiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Simulasi Dekripsi"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(
            ContextCompat.getColor(this, R.color.primary_color)))

        val datas = intent.getSerializableExtra("DATA_ENKRIPSI") as Array<*>
        plaintext = (datas[0] as List<*>).filterIsInstance<String>()
        key = (datas[1] as List<*>).filterIsInstance<String>()
        antrian.addAll((datas[2] as List<*>).filterIsInstance<String>())

        binding.btnPrev.setOnClickListener { prevStep() }
        binding.btnPlayPause.setOnClickListener { togglePlayPause() }
        binding.btnNext.setOnClickListener { nextStep() }

        binding.btnPrev.visibility = View.GONE // Sembunyikan tombol prev di awal

        buildDashes()
        prepareSimulationSteps()
    }

    private fun prepareSimulationSteps() {
        for (i in plaintext.indices) {
            addSimulationStep {
                resetViews()
                binding.tvTitle.text = "Mencari Ciphertext C${ciphertext.size + 1}"
            }
            addSimulationStep { binding.lyAntrian.visibility = View.VISIBLE; populateLinearLayout(binding.lyAntrian, antrian, true) }
            addSimulationStep { binding.arrowAntrian.visibility = View.VISIBLE }
            addSimulationStep { binding.tvEnkripsi.visibility = View.VISIBLE }
            addSimulationStep { binding.tvK.visibility = View.VISIBLE; binding.arrowK.visibility = View.VISIBLE }
            addSimulationStep { binding.wrapCalcAntrian.visibility = View.VISIBLE; populateLinearLayout(binding.lyCalcAntrian, antrian) }
            addSimulationStep { binding.wrapCalcKey.visibility = View.VISIBLE; populateLinearLayout(binding.lyCalcKey, key) }
            addSimulationStep {
                hasilXorEnkripsi = Utils.calculateXorString(antrian, key)
                binding.garisXor.visibility = View.VISIBLE
                binding.wrapCalcXorEnkripsi.visibility = View.VISIBLE
                populateLinearLayout(binding.lyCalcXorEnkripsi, hasilXorEnkripsi)
            }
            addSimulationStep {
                hasilGeserEnkripsi = Utils.shiftStringLeft(hasilXorEnkripsi)
                binding.garisGeser.visibility = View.VISIBLE
                binding.wrapCalcHasilEnkripsi.visibility = View.VISIBLE
                populateLinearLayout(binding.lyCalcHasilEnkripsi, hasilGeserEnkripsi)
            }
            addSimulationStep { binding.arrowEnkripsi.visibility = View.VISIBLE }
            addSimulationStep { binding.lyHasilEnkripsi.visibility = View.VISIBLE; populateLinearLayout(binding.lyHasilEnkripsi, hasilGeserEnkripsi, true) }
            addSimulationStep { binding.arrowMsb.visibility = View.VISIBLE }
            addSimulationStep { binding.tvXor.visibility = View.VISIBLE }
            addSimulationStep { binding.arrowPlain.visibility = View.VISIBLE; binding.tvPlain.visibility = View.VISIBLE; binding.tvPlain.text = plaintext[i] }
            addSimulationStep { binding.wrapCalcMsb.visibility = View.VISIBLE; binding.tvCalcMsb.text = hasilGeserEnkripsi[0] }
            addSimulationStep { binding.wrapCalcPlain.visibility = View.VISIBLE; binding.tvCalcPlain.text = plaintext[i] }
            addSimulationStep { binding.garisXorPlain.visibility = View.VISIBLE }
            addSimulationStep {
                ciphertext.add(Utils.calculateXorOneString(hasilGeserEnkripsi[0], plaintext[i]))
                binding.wrapCalcCipher.visibility = View.VISIBLE
                binding.tvCalcCipher.text = ciphertext[i]
            }
            addSimulationStep { binding.arrowXor.visibility = View.VISIBLE }
            addSimulationStep { binding.tvCipher.visibility = View.VISIBLE; binding.tvCipher.text = ciphertext[i] }
            addSimulationStep { binding.arrowCipher.visibility = View.VISIBLE }
            addSimulationStep {
                antrian = Utils.shiftArrayLeftAndAddNew(antrian, ciphertext[i])
                populateLinearLayout(binding.lyAntrian, antrian, true)
            }
        }
        addSimulationStep { // Step terakhir untuk tombol selesai
            binding.btnNext.text = "Finish"
            binding.btnNext.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun addSimulationStep(action: () -> Unit) {
        stepActions.add(Runnable { action() })
    }

    private fun executeCurrentStep() {
        if (currentStep < stepActions.size) {
            stepActions[currentStep].run()
            currentStep++
            updateButtonVisibility()
        }
    }

    private fun prevStep() {
        if (currentStep > 1) { // Ubah dari > 0 menjadi > 1 agar menghindari melampaui batas awal
            currentStep--
            resetViews()
            for (i in 0 until currentStep) {
                stepActions[i].run()
            }
            updateButtonVisibility()
        }
    }

    private fun nextStep() {
        if (currentStep < stepActions.size) {
            executeCurrentStep()
        }
    }

    private fun togglePlayPause() {
        if (isPlaying) {
            handler.removeCallbacksAndMessages(null)
            binding.btnPlayPause.text = "Play"
        } else {
            playSimulation()
            binding.btnPlayPause.text = "Pause"
        }
        isPlaying = !isPlaying
    }

    private fun playSimulation() {
        if (currentStep < stepActions.size) {
            executeCurrentStep()
            handler.postDelayed({ playSimulation() }, duration)
        }
    }

    private fun resetViews() {
        binding.arrowAntrian.visibility = View.GONE
        binding.tvEnkripsi.visibility = View.GONE
        binding.tvK.visibility = View.GONE
        binding.arrowK.visibility = View.GONE
        binding.wrapCalcAntrian.visibility = View.GONE
        binding.wrapCalcKey.visibility = View.GONE
        binding.garisXor.visibility = View.GONE
        binding.wrapCalcXorEnkripsi.visibility = View.GONE
        binding.garisGeser.visibility = View.GONE
        binding.wrapCalcHasilEnkripsi.visibility = View.GONE
        binding.arrowEnkripsi.visibility = View.GONE
        binding.lyHasilEnkripsi.visibility = View.GONE
        binding.arrowMsb.visibility = View.GONE
        binding.tvXor.visibility = View.GONE
        binding.arrowPlain.visibility = View.GONE
        binding.tvPlain.visibility = View.GONE
        binding.wrapCalcMsb.visibility = View.GONE
        binding.wrapCalcPlain.visibility = View.GONE
        binding.garisXorPlain.visibility = View.GONE
        binding.wrapCalcCipher.visibility = View.GONE
        binding.arrowXor.visibility = View.GONE
        binding.tvCipher.visibility = View.GONE
        binding.arrowCipher.visibility = View.GONE
    }

    private fun updateButtonVisibility() {
        binding.btnPrev.visibility = if (currentStep > 0) View.VISIBLE else View.GONE
        if (currentStep == stepActions.size) {
            binding.btnNext.text = "Finish"
            binding.btnNext.setOnClickListener {
                onBackPressed()
            }
        } else {
            binding.btnNext.text = "Next"
            binding.btnNext.setOnClickListener {
                nextStep()
            }
        }
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

    private fun populateLinearLayout(linearLayout: LinearLayout, dataList: List<String>, addOutline: Boolean = false) {
        linearLayout.removeAllViews()

        val context = linearLayout.context
        val monospaceTypeface = Typeface.MONOSPACE

        dataList.forEach { data ->
            val textView = TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    if (!addOutline) {
                        setMargins(resources.getDimensionPixelSize(R.dimen.margin_start), 0, 0, 0)
                    }
                }
                background = if (addOutline) ContextCompat.getDrawable(context, R.drawable.outline) else null
                text = data
                typeface = monospaceTypeface
            }
            linearLayout.addView(textView)
        }
    }

    private fun buildDashes(){
        val dashes = StringBuilder()
        for (i in antrian.indices) {
            dashes.append("--------")
        }

        binding.dashGarisXor.text = dashes
        binding.dashGarisGeser.text = dashes
    }
}
