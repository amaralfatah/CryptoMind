package com.uadev.cryptomind.views.program

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.uadev.cryptomind.R
import com.uadev.cryptomind.databinding.FragmentDecryptionBinding
import com.uadev.cryptomind.databinding.FragmentEncryptionBinding
import com.uadev.cryptomind.utils.Utils
import com.uadev.cryptomind.utils.Utils.calculateXorOneString
import com.uadev.cryptomind.utils.Utils.calculateXorString
import com.uadev.cryptomind.utils.Utils.generateIV
import com.uadev.cryptomind.utils.Utils.isBinaryString
import com.uadev.cryptomind.utils.Utils.isValidBinaryLength
import com.uadev.cryptomind.utils.Utils.logData
import com.uadev.cryptomind.utils.Utils.shiftArrayLeftAndAddNew
import com.uadev.cryptomind.utils.Utils.shiftStringLeft
import io.github.muddz.styleabletoast.StyleableToast

class DecryptionFragment : Fragment() {

    private var _binding: FragmentDecryptionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDecryptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDecrypt.setOnClickListener {
            val inCipher = binding.etCipher.text.toString()
            val inKey = binding.etKey.text.toString()

            if (validateInputs(inCipher, inKey)) {
                modeDekripsi(inCipher, inKey)
            }
        }

        binding.lyDecrypted.setEndIconOnClickListener {
            copyTextToClipboard(binding.etPlain.text.toString())
        }
    }

    private fun modeDekripsi(inCipher: String, inKey: String) {
        val ciphertext = processInput(inCipher)
        val key = processInput(inKey)
        val iv = generateIV(key.size)
        var antrian = iv.toMutableList()
        val plainText = mutableListOf<String>()

        // start fungsi dekripsi
        for (i in ciphertext.indices) {
            val hasilXorDekripsi = calculateXorString(antrian, key)
            val hasilGeserDekripsi = shiftStringLeft(hasilXorDekripsi)
            plainText.add(calculateXorOneString(hasilGeserDekripsi[0], ciphertext[i]))
            antrian = shiftArrayLeftAndAddNew(antrian, ciphertext[i])
        }
        // END fungsi dekripsi

        logData(ciphertext, key, iv, antrian)

        binding.etPlain.setText(plainText.joinToString("") { binaryString ->
            binaryString.toInt(2).toChar().toString()
        })

        StyleableToast.makeText(requireContext(), "Dekripsi Berhasil", Toast.LENGTH_SHORT, R.style.toastNormalBot).show()
    }

    private fun processInput(input: String): MutableList<String> {
        return if (isBinaryString(input)) {
            input.chunked(8).toMutableList()
        } else {
            input.map { it.code.toString(2).padStart(8, '0') }.toMutableList()
        }
    }

    private fun validateInputs(inCipher: String, inKey: String): Boolean {
        val isPlainBinary = isBinaryString(inCipher)
        val isKeyBinary = isBinaryString(inKey)
        val isValidPlainLength = isValidBinaryLength(inCipher)
        val isValidKeyLength = isValidBinaryLength(inKey)

        if (inCipher.isEmpty()) {
            binding.etCipher.error = "Input tidak boleh kosong!"
            return false
        }

        if (inKey.isEmpty()) {
            binding.etKey.error = "Input tidak boleh kosong!"
            return false
        }

        if (isPlainBinary && !isValidPlainLength) {
            binding.etCipher.error = "Bilangan biner harus terdiri dari 8 bit atau kelipatan"
            return false
        }

        if (isKeyBinary && !isValidKeyLength) {
            binding.etKey.error = "Bilangan biner harus terdiri dari 8 bit atau kelipatan"
            return false
        }

        return true
    }

    private fun copyTextToClipboard(textToCopy: String) {
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Copied Text", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        StyleableToast.makeText(requireContext(), "Ciphertext berhasil disalin", Toast.LENGTH_SHORT, R.style.toastNormalBot).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
