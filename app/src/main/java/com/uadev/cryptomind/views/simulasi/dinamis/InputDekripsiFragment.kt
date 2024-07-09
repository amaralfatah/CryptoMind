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
import com.uadev.cryptomind.databinding.FragmentInputDekripsiBinding
import com.uadev.cryptomind.utils.Utils
import com.uadev.cryptomind.utils.Utils.generateIV
import com.uadev.cryptomind.utils.Utils.logData
import io.github.muddz.styleabletoast.StyleableToast

class InputDekripsiFragment : Fragment() {

    private var _binding: FragmentInputDekripsiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputDekripsiBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnSimulasi.setOnClickListener {
            val inCipher = binding.etPlaintext.text.toString()
            val inKey = binding.etKey.text.toString()

            if (inCipher.isEmpty() || inKey.isEmpty()) {
                showErrorMessage("Form tidak boleh kosong")
                return@setOnClickListener
            }

            val cipher = processInput(inCipher)
            val key = processInput(inKey)
            val iv = generateIV(key.size)
            val antrian = iv.toMutableList()

            val data = arrayOf<Any?>(
                cipher,
                key,
                antrian
            )

            startEncryptionActivity(data)

            logData(cipher, key, iv, antrian)
        }

        return view
    }

    private fun processInput(input: String): MutableList<String> {
        return if (Utils.isBinaryString(input)) {
            input.chunked(8).map { it }.toMutableList()  // Jika input sudah dalam format binary string, langsung pecah per 8 karakter
        } else {
            input.map { it.code.toString(2).padStart(8, '0') }.toMutableList()  // Jika input dalam bentuk teks biasa, konversikan ke binary string
        }
    }

    private fun showErrorMessage(message: String) {
        StyleableToast.makeText(requireContext(), message, Toast.LENGTH_SHORT, R.style.toastNormalBot).show()
    }

    private fun startEncryptionActivity(data: Array<Any?>) {
        val intent = Intent(activity, SimulasiDekripsiActivity::class.java)
        intent.putExtra("DATA_ENKRIPSI", data)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
