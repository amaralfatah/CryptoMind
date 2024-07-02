package com.uadev.cryptomind.views.simulasi.dinamis

import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
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
import android.view.MenuItem

class SimulasiEnkripsiActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySimulasiEnkripsiBinding

    private var plaintext: List<String> = emptyList()
    private var key: List<String> = emptyList()
    private var antrian: MutableList<String> = mutableListOf()
    private var ciphertext: MutableList<String> = mutableListOf()

    private val duration = 500L
    private val handler = Handler()
    private var cumulativeDelay = 0L

    private var hasilXorEnkripsi: MutableList<String> = mutableListOf()
    private var hasilGeserEnkripsi: MutableList<String> = mutableListOf()

//    apakah ada cara yang lebih baik untuk membuatan semua komponen ditampilkan  secara berurutan seperti video animasi , dilengkapi dengan tombol prev, play/pause dan next:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimulasiEnkripsiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aktifkan action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Simulasi Enkripsi"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.primary_color)))

        val datas = intent.getSerializableExtra("DATA_ENKRIPSI") as Array<*>

        plaintext = (datas[0] as List<*>).filterIsInstance<String>()
        key = (datas[1] as List<*>).filterIsInstance<String>()
        antrian.addAll((datas[2] as List<*>).filterIsInstance<String>())

        // LIKE VIDEO ANIMASI SIMULASI --------------------------------------

        buildDashes()
        for (i in plaintext.indices) {
//            Log.d("CMIND", "Iterasi ke-$i")
            handler.postDelayed({
                simulasi(i)
                Log.d("CMIND", "Iterasi ke-$i == ${plaintext.size}")
            }, cumulativeDelay)
            cumulativeDelay += duration
        }

        binding.btnSelesai.setOnClickListener(){
            onBackPressed()
        }

    Log.d("CMIND","${plaintext.size}")

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

    private fun simulasi(i: Int){

        handler.postDelayed({
            resetViews()
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.tvTitle.text = "Mencari Ciphertext C${ciphertext.size+1}"
        }, cumulativeDelay)
        cumulativeDelay += duration

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

        handler.postDelayed({
            hasilXorEnkripsi = Utils.calculateXorString(antrian, key)

            binding.garisXor.visibility = View.VISIBLE
            binding.wrapCalcXorEnkripsi.visibility = View.VISIBLE
            populateLinearLayout(binding.lyCalcXorEnkripsi, hasilXorEnkripsi)
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            hasilGeserEnkripsi = Utils.shiftStringLeft(hasilXorEnkripsi)

            binding.garisGeser.visibility = View.VISIBLE
            binding.wrapCalcHasilEnkripsi.visibility = View.VISIBLE
            populateLinearLayout(binding.lyCalcHasilEnkripsi, hasilGeserEnkripsi)
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.arrowEnkripsi.visibility = View.VISIBLE
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.lyHasilEnkripsi.visibility = View.VISIBLE
            populateLinearLayout(binding.lyHasilEnkripsi, hasilGeserEnkripsi, true)
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
            binding.tvPlain.text = plaintext[i]
//            Log.d("CMIND", "plaintext simulasi ke-$i")
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.wrapCalcMsb.visibility = View.VISIBLE
            binding.tvCalcMsb.text = hasilGeserEnkripsi[0]
//            Log.d("CMIND", "msb calc ke-$i")
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.wrapCalcPlain.visibility = View.VISIBLE
            binding.tvCalcPlain.text = plaintext[i]
//            Log.d("CMIND", "plaintext calc ke-$i")
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.garisXorPlain.visibility = View.VISIBLE
//            Log.d("CMIND", "garis xor ke-$i")
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            ciphertext.add(Utils.calculateXorOneString(hasilGeserEnkripsi[0], plaintext[i]))

            binding.wrapCalcCipher.visibility = View.VISIBLE
            binding.tvCalcCipher.text = ciphertext[i]
//            Log.d("CMIND", "MSB xor Plaintext ke-$i")
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.arrowXor.visibility = View.VISIBLE
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            binding.tvCipher.visibility = View.VISIBLE
            binding.tvCipher.text = ciphertext[i]
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
            antrian = Utils.shiftArrayLeftAndAddNew(antrian, ciphertext[i])

            populateLinearLayout(binding.lyAntrian, antrian, true)
        }, cumulativeDelay)
        cumulativeDelay += duration

        handler.postDelayed({
            if (i == plaintext.size - 1){
                binding.btnSelesai.visibility = View.VISIBLE
            }
        }, cumulativeDelay)
        cumulativeDelay += duration
    }
    private fun resetViews() {
//        binding.lyAntrian.visibility = View.GONE
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
