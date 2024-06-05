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
import com.uadev.cryptomind.databinding.FragmentEncryptionBinding

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
            val inIV = binding.textInputEditTextIV.text.toString()

            if (inPlain.isEmpty() || inKey.isEmpty() || inIV.isEmpty()) {
                showToast("Form tidak boleh kosong")
                return@setOnClickListener
            }

            validateBinaryInput(inPlain, binding.textInputEditTextPlaintext)
            validateBinaryInput(inKey, binding.textInputEditTextKey)
            validateBinaryInput(inIV, binding.textInputEditTextIV)

            if (inIV.length != inKey.length) {
                binding.textInputEditTextIV.error = "IV harus sama dengan Key"
                return@setOnClickListener
            }

            val plainText = processInput(inPlain)
            val key = processInput(inKey)
            val initialVector = processInput(inIV)
            var register = initialVector!!.toMutableList()
            val cipherText = mutableListOf<Int>()

            // START Encrypt
            for (i in plainText!!.indices) {

                val temp = mutableListOf<Int>()

                for (y in key!!.indices) {
                    temp.add(encrypt(register[y], key[y]))
                }

                val encryptedValue = temp[0] xor plainText[i]
                cipherText.add(encryptedValue)
                register = inputDataRegister(register, cipherText.last())

                logEncryptionDetails(i, cipherText, register)
            }
            // END Encrypt

            binding.textInputEditTextEncrypted.setText(cipherText.joinToString("") { it.toString(2).padStart(8, '0') })
            showToast("Proses Enkripsi Berhasil")
        }

        binding.textInputLayoutEncrypted.setEndIconOnClickListener {
            copyTextToClipboard(binding.textInputEditTextEncrypted.text.toString())
        }
    }

    private fun processInput(input: String): List<Int>? {
        return try {
            if (isBinaryString(input)) {
                stringToIntList(input)
            } else {
                input.map { it.code }
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun validateBinaryInput(input: String, editText: EditText) {
        if (isBinaryString(input) && !isValidBinaryLength(input)) {
            editText.error = "Bilangan biner harus terdiri dari 8 bit atau kelipatan"
        }
    }

    private fun isBinaryString(input: String): Boolean {
        return input.all { it == '0' || it == '1' }
    }

    private fun isValidBinaryLength(input: String): Boolean {
        return input.length % 8 == 0
    }

    private fun stringToIntList(binStr: String): List<Int> {
        return try {
            val binList = binStr.chunked(8)
            binList.map { binaryString -> binaryString.toInt(2) }
        } catch (e: Exception) {
            showToast("Format string biner tidak valid")
            throw e
        }
    }

    private fun encrypt(cipherText: Int, key: Int): Int {

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

    private fun logEncryptionDetails(index: Int, cipherText: List<Int>, register: MutableList<Int>) {
        with(register) {
            Log.d("ISI REGISTER", "CipherText setelah diisi index $index :")
            Log.d("ISI REGISTER", cipherText.joinToString(" ") { it.toString(2).padStart(8, '0') })
            Log.d("ISI REGISTER", "Register setelah diisi index $index :")
            Log.d("ISI REGISTER", register.joinToString(" ") { it.toString(2).padStart(8, '0') })
            Log.d("ISI REGISTER", "\n")
        }
    }

    private fun copyTextToClipboard(textToCopy: String) {
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Copied Text", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        showToast("Ciphertext berhasil disalin")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
