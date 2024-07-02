package com.uadev.cryptomind.views.simulasi.dinamis

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.uadev.cryptomind.R
import com.uadev.cryptomind.databinding.ActivitySimulasiEnkripsiBinding
import com.uadev.cryptomind.utils.Utils
import android.os.Handler

class SimulasiEnkripsiActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySimulasiEnkripsiBinding

    private var plaintext: List<String> = emptyList()
    private var key: List<String> = emptyList()
    private var antrian: MutableList<String> = mutableListOf()
    private var ciphertext: MutableList<String> = mutableListOf()

//    apakah ada cara yang lebih baik untuk membuatan semua komponen ditampilkan  secara berurutan seperti video animasi , dilengkapi dengan tombol prev, play/pause dan next:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimulasiEnkripsiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val datas = intent.getSerializableExtra("DATA_ENKRIPSI") as Array<*>


        plaintext = (datas[0] as List<*>).filterIsInstance<String>()
        key = (datas[1] as List<*>).filterIsInstance<String>()
        antrian.addAll((datas[2] as List<*>).filterIsInstance<String>())

        // LIKE VIDEO ANIMASI SIMULASI --------------------------------------
        val duration = 500L
        val handler = Handler()
        var cumulativeDelay = 0L

        buildDashes()

        handler.postDelayed({
            binding.lyAntrian.visibility = View.VISIBLE
            populateLinearLayout(binding.lyAntrian, antrian, true)
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.arrowAntrian.visibility = View.VISIBLE
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.tvEnkripsi.visibility = View.VISIBLE
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.tvK.visibility = View.VISIBLE
            binding.arrowK.visibility = View.VISIBLE
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.wrapCalcAntrian.visibility = View.VISIBLE
            populateLinearLayout(binding.lyCalcAntrian, antrian)
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.wrapCalcKey.visibility = View.VISIBLE
            populateLinearLayout(binding.lyCalcKey, key)
        }, cumulativeDelay)
        cumulativeDelay += duration

        var hasilXorEnkripsiX1: MutableList<String> = mutableListOf()
        handler.postDelayed({
            hasilXorEnkripsiX1 = Utils.calculateXorString(antrian, key)
            binding.garisXor.visibility = View.VISIBLE
            binding.wrapCalcXorEnkripsi.visibility = View.VISIBLE
            populateLinearLayout(binding.lyCalcXorEnkripsi, hasilXorEnkripsiX1)
        }, cumulativeDelay)
        cumulativeDelay += duration

        var hasilGeserEnkripsiX1: MutableList<String> = mutableListOf()
        handler.postDelayed({
            hasilGeserEnkripsiX1 = Utils.shiftStringLeft(hasilXorEnkripsiX1)
            binding.garisGeser.visibility = View.VISIBLE
            binding.wrapCalcHasilEnkripsi.visibility = View.VISIBLE
            populateLinearLayout(binding.lyCalcHasilEnkripsi, hasilGeserEnkripsiX1)
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.arrowEnkripsi.visibility = View.VISIBLE
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.lyHasilEnkripsi.visibility = View.VISIBLE
            populateLinearLayout(binding.lyHasilEnkripsi, hasilGeserEnkripsiX1, true)
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.arrowMsb.visibility = View.VISIBLE
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.tvXor.visibility = View.VISIBLE
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.arrowPlain.visibility = View.VISIBLE
            binding.tvPlain.visibility = View.VISIBLE
            binding.tvPlain.text = plaintext[0]
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.wrapCalcMsb.visibility = View.VISIBLE
            binding.tvCalcMsb.text = hasilGeserEnkripsiX1[0]
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.wrapCalcPlain.visibility = View.VISIBLE
            binding.tvCalcPlain.text = plaintext[0]
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.garisXorPlain.visibility = View.VISIBLE
        }, cumulativeDelay)
        cumulativeDelay += duration


        handler.postDelayed({
            Log.d("CMIND-msb", hasilGeserEnkripsiX1.joinToString(" "))
            Log.d("CMIND-plain", plaintext.joinToString(" "))
            ciphertext.add(Utils.calculateXorOneString(hasilGeserEnkripsiX1[0], plaintext[0]))
            binding.wrapCalcCipher.visibility = View.VISIBLE
            binding.tvCalcCipher.text = ciphertext[0]
            Log.d("CMIND-cipher", ciphertext.joinToString(" "))
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.arrowXor.visibility = View.VISIBLE
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.tvCipher.visibility = View.VISIBLE
            binding.tvCipher.text = ciphertext[0]
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.arrowCipher.visibility = View.VISIBLE
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.arrowCipher.visibility = View.VISIBLE
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            antrian = Utils.shiftArrayLeftAndAddNew(antrian, ciphertext[0])
            populateLinearLayout(binding.lyAntrian, antrian, true)
        }, cumulativeDelay)
        cumulativeDelay += duration
    }

    private fun populateLinearLayout(linearLayout: LinearLayout, dataList: List<String>, addOutline: Boolean = false) {
        linearLayout.removeAllViews() // Clear existing views

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
