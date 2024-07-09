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

class EncryptionFragment : Fragment() {

    private var _binding: FragmentEncryptionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEncryptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonEncrypt.setOnClickListener {
            val inPlain = binding.textInputEditTextPlaintext.text.toString()
            val inKey = binding.textInputEditTextKey.text.toString()

            if (validateInputs(inPlain, inKey)) {
                modeEnkripsi(inPlain, inKey)
            }
        }

        binding.textInputLayoutEncrypted.setEndIconOnClickListener {
            copyTextToClipboard(binding.textInputEditTextEncrypted.text.toString())
        }
    }

    private fun modeEnkripsi(inPlain: String, inKey: String) {
        val plainText = processInput(inPlain)
        val key = processInput(inKey)
        val iv = generateIV(key.size)
        var antrian = iv.toMutableList()
        val cipherText = mutableListOf<String>()

        // start fungsi enkripsi
        for (i in plainText.indices) {
            val hasilXorEnkripsi = calculateXorString(antrian, key)
            val hasilGeserEnkripsi = shiftStringLeft(hasilXorEnkripsi)
            cipherText.add(calculateXorOneString(hasilGeserEnkripsi[0], plainText[i]))
            antrian = shiftArrayLeftAndAddNew(antrian, cipherText[i])
        }
        // END fungsi enkripsi

        logData(plainText, key, iv, antrian)

        binding.textInputEditTextEncrypted.setText(cipherText.joinToString(""))
        StyleableToast.makeText(requireContext(), "Enkripsi Berhasil", Toast.LENGTH_SHORT, R.style.toastNormalBot).show()
    }

    private fun processInput(input: String): MutableList<String> {
        return if (isBinaryString(input)) {
            input.chunked(8).toMutableList()
        } else {
            input.map { it.code.toString(2).padStart(8, '0') }.toMutableList()
        }
    }

    private fun validateInputs(inPlain: String, inKey: String): Boolean {
        val isPlainBinary = isBinaryString(inPlain)
        val isKeyBinary = isBinaryString(inKey)
        val isValidPlainLength = isValidBinaryLength(inPlain)
        val isValidKeyLength = isValidBinaryLength(inKey)

        if (inPlain.isEmpty()) {
            binding.textInputEditTextPlaintext.error = "Input tidak boleh kosong!"
            return false
        }

        if (inKey.isEmpty()) {
            binding.textInputEditTextKey.error = "Input tidak boleh kosong!"
            return false
        }

        if (isPlainBinary && !isValidPlainLength) {
            binding.textInputEditTextPlaintext.error = "Bilangan biner harus terdiri dari 8 bit atau kelipatan"
            return false
        }

        if (isKeyBinary && !isValidKeyLength) {
            binding.textInputEditTextKey.error = "Bilangan biner harus terdiri dari 8 bit atau kelipatan"
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
