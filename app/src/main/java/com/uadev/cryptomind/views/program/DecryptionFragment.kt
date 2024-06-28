package com.uadev.cryptomind.views.program

import android.util.Log
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.uadev.cryptomind.R
import com.uadev.cryptomind.databinding.FragmentDecryptionBinding
import com.uadev.cryptomind.utils.Utils
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

        binding.buttonDecrypt.setOnClickListener {

            val inCipher = binding.inCipherText.text.toString()
            val inKey = binding.inKey.text.toString()
            val inIV = binding.inIV.text.toString()

            if (inCipher.isEmpty() || inKey.isEmpty() || inIV.isEmpty()) {
                StyleableToast.makeText(requireContext(), "Form tidak boleh kosong", Toast.LENGTH_SHORT, R.style.toastNormalBot).show();
                return@setOnClickListener
            }

            if (!validateInputs(inKey, inIV)) {
                return@setOnClickListener
            }

            if (inIV.length != inKey.length) {
                binding.inIV.error = "IV harus sama dengan Key"
                return@setOnClickListener
            }

            val cipherText = processInput(inCipher)
            val key = processInput(inKey)
            val initialVector = processInput(inIV)

            if (cipherText == null || key == null || initialVector == null) {
                StyleableToast.makeText(requireContext(), "Input tidak valid", Toast.LENGTH_SHORT, R.style.toastNormalBot).show()
                return@setOnClickListener
            }

            var register = initialVector.toMutableList()
            val plainText = StringBuilder()

            // START Decrypt
            for (i in cipherText.indices) {

                val temp = mutableListOf<Int>()

                for (y in key.indices) {
                    temp.add(decrypt(register[y], key[y]))
                }

                val decryptedValue = temp[0] xor cipherText[i]
                plainText.append(decryptedValue.toChar())
                register = inputDataRegister(register, cipherText[i])

                logEncryptionDetails(i, plainText, register)
            }
            // END Decrypt

            binding.outDecrypted.setText(plainText.toString())
            StyleableToast.makeText(requireContext(), "Dekripsi Berhasil", Toast.LENGTH_SHORT, R.style.toastNormalBot).show();
        }

        binding.layoutOutDecrypted.setEndIconOnClickListener {
            copyTextToClipboard(binding.outDecrypted.text.toString())
        }
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

    private fun validateInputs(inKey: String, inIV: String): Boolean {
        return when {
            Utils.isBinaryString(inKey) && !Utils.isValidBinaryLength(inKey) -> {
                binding.inKey.error = "Bilangan biner harus terdiri dari 8 bit atau kelipatan"
                false
            }
            Utils.isBinaryString(inIV) && !Utils.isValidBinaryLength(inIV) -> {
                binding.inIV.error = "Bilangan biner harus terdiri dari 8 bit atau kelipatan"
                false
            }
            Utils.isBinaryString(inKey) != Utils.isBinaryString(inIV) -> {
                StyleableToast.makeText(requireContext(), "Key dan IV harus memiliki tipe yang sama", Toast.LENGTH_SHORT, R.style.toastNormalBot).show()
                false
            }
            else -> true
        }
    }

    private fun stringToIntList(binStr: String): List<Int> {
        return try {
            val binList = binStr.chunked(8)
            binList.map { binaryString -> binaryString.toInt(2) }
        } catch (e: Exception) {
            StyleableToast.makeText(requireContext(), "Bilangan biner tidak valid", Toast.LENGTH_SHORT, R.style.toastNormalBot).show();
            throw e
        }
    }

    private fun decrypt(cipherText: Int, key: Int): Int {

        var temp = cipherText xor key

        // Geser bit hasil XOR ke kiri sebanyak 1 kali
        temp = (temp shl 1) or (temp ushr 7)

        // Pastikan hanya 8 bit yang tersisa dengan memotong nilai yang lebih besar dari 255
        temp = temp and 0xFF

        return temp
    }

    private fun inputDataRegister(register: MutableList<Int>, cipherText: Int): MutableList<Int> {

        for (i in 0 until register.size - 1) {
            register[i] = register[i + 1]
        }

        register[register.size - 1] = cipherText

        return register
    }

    private fun logEncryptionDetails(index: Int, plaintext: StringBuilder, register: MutableList<Int>) {
        with(register) {
            Log.d("ISI REGISTER", "Plaintext setelah diisi index $index:")
            Log.d("ISI REGISTER", plaintext.toString())
            Log.d("ISI REGISTER", "Register after input data for index $index:")
            Log.d("ISI REGISTER", register.joinToString(" ") { it.toString(2).padStart(8, '0') })
            Log.d("ISI REGISTER", "\n")
        }
    }

    private fun copyTextToClipboard(textToCopy: String) {
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Copied Text", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        StyleableToast.makeText(requireContext(), "Plaintext berhasil disalin", Toast.LENGTH_SHORT, R.style.toastNormalBot).show();
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
