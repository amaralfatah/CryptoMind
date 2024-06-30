package com.uadev.cryptomind.views.simulasi.dinamis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.uadev.cryptomind.R
import com.uadev.cryptomind.databinding.FragmentInputEnkripsiBinding
import com.uadev.cryptomind.utils.Utils
import io.github.muddz.styleabletoast.StyleableToast

class InputEnkripsiFragment : Fragment() {

    private var _binding: FragmentInputEnkripsiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputEnkripsiBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnSimulasi.setOnClickListener {
            val inPlain = binding.etPlaintext.text.toString()
            val inKey = binding.etKey.text.toString()

            if (inPlain.isEmpty() || inKey.isEmpty()) {
                showErrorMessage("Form tidak boleh kosong")
                return@setOnClickListener
            }

            val plain = processInput(inPlain)
            val key = processInput(inKey)

            if (key == null) {
                showErrorMessage("Key tidak valid")
                return@setOnClickListener
            }

            val iv = MutableList(key.size) { 0 }

            val antrian = iv.toMutableList()
            val cipherText = mutableListOf<Int>()

            val data = arrayOf<Any?>(
                plain,
                key,
                antrian,
                cipherText
            )

            startEncryptionActivity(data)

            logData(plain, key, iv, antrian)
        }

        return view
    }

    private fun processInput(input: String): List<Int>? {
        return try {
            if (Utils.isBinaryString(input)) {
                stringToIntList(input)
            } else {
                input.map { it.code }
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun stringToIntList(binStr: String): List<Int> {
        val binList = binStr.chunked(8)
        return binList.map { it.toInt(2) }
    }

    private fun showErrorMessage(message: String) {
        StyleableToast.makeText(requireContext(), message, Toast.LENGTH_SHORT, R.style.toastNormalBot).show()
    }

    private fun startEncryptionActivity(data: Array<Any?>) {
        val intent = Intent(activity, SimulasiEnkripsiActivity::class.java)
        intent.putExtra("DATA_ENKRIPSI", data)
        startActivity(intent)
    }

    private fun logData(plain: List<Int>?, key: List<Int>?, iv: List<Int>, antrian: MutableList<Int>) {
        val logMessage = buildString {
            plain?.let { appendLine("Plain: $it") }
            key?.let { appendLine("Key: $it") }
            appendLine("initialCCC: $iv")
            appendLine("antrian: $antrian")
        }
        Log.d("coco", logMessage)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
