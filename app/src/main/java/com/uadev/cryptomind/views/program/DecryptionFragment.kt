package com.uadev.cryptomind.views.program

import android.util.Log
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.uadev.cryptomind.databinding.FragmentDecryptionBinding

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
                showToast("Field tidak boleh kosong")
                return@setOnClickListener
            }

            if (inIV.length != inKey.length) {
                binding.inIV.error = "IV harus sama dengan Key"
                return@setOnClickListener
            }

            val cipherText: List<Int>
            try {
                cipherText = if (isBinaryString(inCipher)) {
                    stringToIntList(inCipher)
                } else {
                    inCipher.map { it.code }
                }
                Log.d("Dekripsi", cipherText.toString())
            } catch (e: Exception) {
                showToast("Input tidak valid")
                return@setOnClickListener
            }

            val key: List<Int>
            try {
                key = inKey.map { charToBinary(it).toInt(2) }
                Log.d("Dekripsi", key.toString())
            } catch (e: Exception) {
                showToast("Key tidak valid")
                return@setOnClickListener
            }

            val initialVector: List<Int>
            try {
                initialVector = inIV.map { charToBinary(it).toInt(2) }
            } catch (e: Exception) {
                showToast("IV tidak valid")
                return@setOnClickListener
            }

            var register: MutableList<Int> = mutableListOf()
            register.addAll(initialVector)
            Log.d("Dekripsi", register.toString())

            val plainText = StringBuilder()

            try {
                for (i in cipherText.indices) {
                    val temp = mutableListOf<Int>()
                    for (y in key.indices) {
                        temp.add(decrypt(register[y], key[y]))
                    }
                    val decryptedValue = temp[0] xor cipherText[i]
                    plainText.append(decryptedValue.toChar())
                    register = inputDataRegister(register, cipherText[i])
                }
            } catch (e: Exception) {
                showToast("Proses dekripsi gagal")
                return@setOnClickListener
            }

            // Set the decrypted text to the output view
            binding.outDecrypted.setText(plainText.toString())
            showToast("Proses Dekripsi Berhasil")
        }

        binding.layoutOutDecrypted.setEndIconOnClickListener {
            copyTextToClipboard(binding.outDecrypted.text.toString())
        }
    }

    private fun decrypt(cipherText: Int, key: Int): Int {
        var temp = cipherText xor key
        temp = (temp shl 1) or (temp ushr 7)
        temp = temp and 0xFF
        return temp
    }

    private fun copyTextToClipboard(textToCopy: String) {
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Copied Text", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        showToast("Text successfully copied to clipboard")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun inputDataRegister(register: MutableList<Int>, cipherText: Int): MutableList<Int> {
        for (i in 1 until register.size) {
            register[i - 1] = register[i]
        }
        register[register.size - 1] = cipherText
        return register
    }

    private fun logEncryptionDetails(index: Int, cipherText: List<Int>, register: MutableList<Int>) {
        with(register) {
            Log.d("ISI REGISTER", "CipherText setelah diisi index $index:")
            Log.d("ISI REGISTER", cipherText.joinToString(" ") { it.toString(2).padStart(8, '0') })
            Log.d("ISI REGISTER", "Register after input data for index $index:")
            Log.d("ISI REGISTER", joinToString(" ") { it.toString(2).padStart(8, '0') })
            Log.d("ISI REGISTER", "\n")
        }
    }

    fun stringToIntList(binStr: String): List<Int> {
        val intList = mutableListOf<Int>()
        try {
            for (i in binStr.indices step 8) {
                val byteStr = binStr.substring(i, i + 8)
                intList.add(byteStr.toInt(2))
            }
        } catch (e: Exception) {
            showToast("Format string biner tidak valid")
            throw e
        }
        return intList
    }

    private fun charToBinary(char: Char): String {
        return char.code.toString(2).padStart(8, '0')
    }

    private fun isBinaryString(input: String): Boolean {
        return input.all { it == '0' || it == '1' }
    }
}
