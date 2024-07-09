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
import android.os.Looper
import com.uadev.cryptomind.databinding.ActivitySimulasiEnkripsiBinding

class SimulasiDekripsiActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySimulasiDekripsiBinding

    private lateinit var datas: Array<*>
    private var ciphertext: List<String> = emptyList()
    private var key: List<String> = emptyList()
    private var antrian: MutableList<String> = mutableListOf()
    private var plaintext: MutableList<String> = mutableListOf()

    private lateinit var initialCiphertext: List<String>
    private lateinit var initialKey: List<String>
    private lateinit var initialAntrian: MutableList<String>

    private val duration = 500L
    private val handler = Handler(Looper.getMainLooper())

    private var hasilXorEnkripsi: MutableList<String> = mutableListOf()
    private var hasilGeserEnkripsi: MutableList<String> = mutableListOf()

    private var currentStep = 0
    private var isPlaying = false
    private val stepActions = mutableListOf<Runnable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimulasiDekripsiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Enable action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Simulasi Dekripsi"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.primary_color)))

        // Retrieve data from intent
        datas = intent.getSerializableExtra("DATA_ENKRIPSI") as Array<*>
        ciphertext = (datas[0] as List<*>).filterIsInstance<String>()
        key = (datas[1] as List<*>).filterIsInstance<String>()
        antrian.addAll((datas[2] as List<*>).filterIsInstance<String>())

        // Simpan nilai awal
        initialCiphertext = ciphertext
        initialKey = key
        initialAntrian = antrian.toMutableList()

        // Initialize buttons
        binding.btnPlayPause.visibility = View.VISIBLE
        binding.btnRepeat.visibility = View.GONE

        binding.btnPrev.setOnClickListener { prevStep() }
        binding.btnPlayPause.setOnClickListener { togglePlayPause() }
        binding.btnRepeat.setOnClickListener { repeatSimulation() }
        binding.btnNext.setOnClickListener { nextStep() }

        binding.btnPrev.isEnabled = false

        populateLinearLayout(binding.lyDescPlain, plaintext)
        populateLinearLayout(binding.lyDescCipher, ciphertext)

        buildDashes()
        prepareSimulationSteps()
    }

    private fun addSimulationStep(action: () -> Unit) {
        stepActions.add(Runnable { action() })
    }

    private fun prepareSimulationSteps() {
        for (i in ciphertext.indices) {
            addSimulationStep {
                resetViews()
                binding.tvTitle.text = "Mencari Plaintext P${plaintext.size + 1}"

                populateLinearLayout(binding.lyDescPlain, plaintext)
                populateLinearLayout(binding.lyDescCipher, ciphertext)
            }
            addSimulationStep {
                binding.lyAntrian.visibility = View.VISIBLE
                populateLinearLayout(binding.lyAntrian, antrian, true)
            }
            addSimulationStep {
                binding.arrowAntrian.visibility = View.VISIBLE
            }
            addSimulationStep {
                binding.tvEnkripsi.visibility = View.VISIBLE
            }
            addSimulationStep {
                binding.tvK.visibility = View.VISIBLE
                binding.arrowK.visibility = View.VISIBLE
            }
            addSimulationStep {
                binding.wrapCalcAntrian.visibility = View.VISIBLE
                binding.tvWrapXN.text = "X${i+1}  ="
                populateLinearLayout(binding.lyCalcAntrian, antrian)
            }
            addSimulationStep {
                binding.wrapCalcKey.visibility = View.VISIBLE
                populateLinearLayout(binding.lyCalcKey, key)
            }
            addSimulationStep {
                // lOGIC 1
                hasilXorEnkripsi = Utils.calculateXorString(antrian, key)
                binding.garisXor.visibility = View.VISIBLE
                binding.wrapCalcXorEnkripsi.visibility = View.VISIBLE
                populateLinearLayout(binding.lyCalcXorEnkripsi, hasilXorEnkripsi)
            }
            addSimulationStep {
                // LOGIC 2
                hasilGeserEnkripsi = Utils.shiftStringLeft(hasilXorEnkripsi)
                binding.garisGeser.visibility = View.VISIBLE
                binding.wrapCalcHasilEnkripsi.visibility = View.VISIBLE
                populateLinearLayout(binding.lyCalcHasilEnkripsi, hasilGeserEnkripsi)
            }
            addSimulationStep {
                binding.arrowEnkripsi.visibility = View.VISIBLE
            }
            addSimulationStep {
                binding.lyHasilEnkripsi.visibility = View.VISIBLE
                populateLinearLayout(binding.lyHasilEnkripsi, hasilGeserEnkripsi, true)
            }
            addSimulationStep {
                binding.arrowMsb.visibility = View.VISIBLE
                binding.tvMsb.visibility = View.VISIBLE
            }
            addSimulationStep {
                binding.tvXor.visibility = View.VISIBLE
            }
            addSimulationStep {
                binding.arrowPlain.visibility = View.VISIBLE
                binding.tvCipher.visibility = View.VISIBLE
                binding.tvCipher.text = ciphertext[i]
            }
            addSimulationStep {
                binding.wrapCalcMsb.visibility = View.VISIBLE
                binding.tvCalcMsb.text = hasilGeserEnkripsi[0]
            }
            addSimulationStep {
                // LOGIC 3
                plaintext.add(Utils.calculateXorOneString(hasilGeserEnkripsi[0], ciphertext[i]))
                binding.wrapCalcCipher.visibility = View.VISIBLE
                binding.tvWrapCN.text = "C${i+1}  ="
                binding.tvCalcCipher.text = ciphertext[i]
            }
            addSimulationStep {
                binding.garisXorCipher.visibility = View.VISIBLE
            }
            addSimulationStep {
                binding.wrapCalcPlain.visibility = View.VISIBLE
                binding.tvWrapPN.text = "P${i+1}  ="
                binding.tvCalcPlain.text = plaintext[i]
            }
            addSimulationStep {
                binding.tvMsb.visibility = View.VISIBLE
                binding.arrowXor.visibility = View.VISIBLE
            }
            addSimulationStep {
                binding.tvPlain.visibility = View.VISIBLE
                binding.tvPlain.text = plaintext[i]
                populateLinearLayout(binding.lyDescPlain, plaintext)
            }
            if (i < ciphertext.size - 1) {
                addSimulationStep {
                    binding.arrowCipher.visibility = View.VISIBLE
                }
                addSimulationStep {
                    antrian = Utils.shiftArrayLeftAndAddNew(antrian, ciphertext[i])
                    populateLinearLayout(binding.lyAntrian, antrian, true)
                }
            }
        }
    }

    private fun executeCurrentStep() {
        if (currentStep < stepActions.size) {
            stepActions[currentStep].run()
            currentStep++
            updateButtonVisibility()
        }
    }

    private fun prevStep() {
        if (currentStep > 1) {
            currentStep--
            resetViews()
            resetData()

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
        isPlaying = !isPlaying
        if (isPlaying) {
            binding.btnPlayPause.text = getString(R.string.jeda)
            // Show pause icon and hide repeat icon
            binding.btnPlayPause.visibility = View.VISIBLE
            binding.btnRepeat.visibility = View.GONE
            playSimulation()
        } else {
            binding.btnPlayPause.text = getString(R.string.putar)
            // Show play icon and show repeat icon
            binding.btnPlayPause.visibility = View.VISIBLE
            binding.btnRepeat.visibility = View.VISIBLE
            pauseSimulation()
        }
    }


    private fun playSimulation() {
        handler.postDelayed({
            nextStep()
            if (isPlaying && currentStep < stepActions.size) {
                playSimulation()
            }
        }, duration)
    }

    private fun pauseSimulation() {
        handler.removeCallbacksAndMessages(null)
    }

    private fun resetData() {
        ciphertext = initialCiphertext
        key = initialKey
        antrian = initialAntrian.toMutableList()
        plaintext.clear()
    }

    private fun resetViews() {
        binding.tvTitle.text = ""
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
        binding.tvCipher.visibility = View.GONE
        binding.wrapCalcMsb.visibility = View.GONE
        binding.wrapCalcPlain.visibility = View.GONE
        binding.garisXorCipher.visibility = View.GONE
        binding.wrapCalcCipher.visibility = View.GONE
        binding.arrowXor.visibility = View.GONE
        binding.tvPlain.visibility = View.GONE
        binding.arrowCipher.visibility = View.GONE
        binding.tvMsb.visibility = View.GONE
    }

    private fun resetSimulation() {
        currentStep = 0
        resetData()
        resetViews()
        prepareSimulationSteps()
        updateButtonVisibility()
        stepActions[currentStep].run()
    }

    private fun updateButtonVisibility() {
        binding.btnPrev.isEnabled = currentStep > 1
        if (currentStep == stepActions.size) {
            binding.btnNext.text = getString(R.string.selesai)
            binding.btnNext.setOnClickListener {
                resetSimulation()
                onBackPressed()
            }
            // Show repeat button and hide play/pause button
            binding.btnRepeat.visibility = View.VISIBLE
            binding.btnPlayPause.visibility = View.GONE
        } else {
            binding.btnNext.text = getString(R.string.maju)
            binding.btnNext.setOnClickListener {
                nextStep()
            }
            // Show play/pause button and hide repeat button
            binding.btnPlayPause.visibility = View.VISIBLE
            binding.btnRepeat.visibility = View.GONE
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

    private fun repeatSimulation() {
        currentStep = 0
        resetData()
        resetViews()
        prepareSimulationSteps()
        updateButtonVisibility()
        stepActions[currentStep].run()
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

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

}
