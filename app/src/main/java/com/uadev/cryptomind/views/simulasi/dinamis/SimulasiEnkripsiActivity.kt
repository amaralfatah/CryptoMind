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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimulasiEnkripsiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val datas = intent.getSerializableExtra("DATA_ENKRIPSI") as Array<*>


        plaintext = (datas[0] as List<*>).filterIsInstance<String>()
        key = (datas[1] as List<*>).filterIsInstance<String>()
        antrian.addAll((datas[2] as List<*>).filterIsInstance<String>())

        // SIMULASI --------------------------------------
        val duration = 2000L
        val handler = Handler()
        var cumulativeDelay = 0L

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
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
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

        handler.postDelayed({
            val hasilXorEnkripsi = Utils.calculateXorString(antrian, key)
            binding.garisXor.visibility = View.VISIBLE
            binding.wrapCalcXorEnkripsi.visibility = View.VISIBLE
            populateLinearLayout(binding.lyCalcXorEnkripsi, hasilXorEnkripsi)
        }, cumulativeDelay)
        cumulativeDelay += duration

        val hasilEnkripsi = Utils.enkripsi(antrian, key)
        handler.postDelayed({
            binding.garisGeser.visibility = View.VISIBLE
            binding.wrapCalcHasilEnkripsi.visibility = View.VISIBLE
            populateLinearLayout(binding.lyCalcHasilEnkripsi, hasilEnkripsi)
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.arrowEnkripsi.visibility = View.VISIBLE
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.lyHasilEnkripsi.visibility = View.VISIBLE
            populateLinearLayout(binding.lyHasilEnkripsi, hasilEnkripsi, true)
        }, cumulativeDelay)
        cumulativeDelay += duration

        plaintext.firstOrNull()?.let { plain ->
            binding.tvPlain.text = plain.padStart(8, '0')
        }


        Log.d("CMIND", hasilEnkripsi.joinToString(" "))

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

}
