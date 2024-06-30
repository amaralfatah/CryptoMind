package com.uadev.cryptomind.views.simulasi.dinamis

import android.graphics.Typeface
import android.os.Bundle
import android.view.View.generateViewId
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.uadev.cryptomind.R
import com.uadev.cryptomind.databinding.ActivitySimulasiEnkripsiBinding

class SimulasiEnkripsiActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySimulasiEnkripsiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimulasiEnkripsiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val datas = intent.getSerializableExtra("DATA_ENKRIPSI") as? Array<*>

        binding.lyAntrian.removeAllViews()
        datas?.let {
            if (it.size >= 3 && it[2] is List<*>) {
                val antrian = it[2] as List<*>
                populateLinearLayout(binding.lyAntrian, antrian)
            }
        }

        binding.lyCalcKey.removeAllViews()
        datas?.let {
            if (it.size >= 2 && it[1] is List<*>) {
                val key = it[1] as List<*>
                populateNoOutlineLinearLayout(binding.lyCalcKey, key)
            }
        }

        binding.lyCalcAntrian.removeAllViews()
        datas?.let {
            if (it.size >= 3 && it[2] is List<*>) {
                val antrian = it[2] as List<*>
                populateNoOutlineLinearLayout(binding.lyCalcAntrian, antrian)
            }
        }
    }

    private fun populateLinearLayout(linearLayout: LinearLayout, dataList: List<*>) {
        dataList.forEach { data ->
            if (data is Int) {
                val textView = TextView(this).apply {
                    id = generateViewId()
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    background = ContextCompat.getDrawable(context, R.drawable.outline)
                    text = data.toString(2).padStart(8, '0')
                    typeface = Typeface.MONOSPACE
                }
                linearLayout.addView(textView)
            }
        }
    }
    private fun populateNoOutlineLinearLayout(linearLayout: LinearLayout, dataList: List<*>) {
        dataList.forEach { data ->
            if (data is Int) {
                val textView = TextView(this).apply {
                    id = generateViewId()
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        marginStart = resources.getDimensionPixelSize(R.dimen.margin_start)
                    }
                    text = data.toString(2).padStart(8, '0')
                    typeface = Typeface.MONOSPACE
                }
                linearLayout.addView(textView)
            }
        }
    }
}
