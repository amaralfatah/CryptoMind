package com.uadev.cryptomind.views

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.uadev.cryptomind.databinding.FragmentEncryptionBinding
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

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

            var initialVector = listOf<Int>()
            val key = mutableListOf<Int>()
            var register = mutableListOf<Int>()
            var cipherText = mutableListOf<Int>()
            val plainText: List<Int>

            val inText = binding.textInputEditTextInput.text.toString()
            val inKey = binding.textInputEditTextKey.text.toString()
            val inIv = binding.textInputEditTextIV.text.toString()

            println("Bismillah")
            plainText = inText.map { charToBinary(it).toInt(2) }
            key.addAll(inKey.map { charToBinary(it).toInt(2) })
            initialVector = inIv.map { charToBinary(it).toInt(2) }
            register.addAll(initialVector)


            for (i in plainText.indices) {
                // Enkripsi IV menggunakan key yang sesuai
                val temp = encrypt(register[i], key[i])

                // XOR hasil enkripsi dengan PlainText
                val encryptedValue = temp xor plainText[i]

                // Tambahkan hasil XOR sebagai CipherText
                cipherText.add(encryptedValue)

                // Masukkan CipherText ke LSB (Least Significant Bit) Register
                register = inputDataRegister(register, cipherText.last())

                // Cetak hasil
                println("CipherText setelah diisi index $i:")
                println(cipherText.joinToString(" ") { it.toString(2).padStart(8, '0') })
                println("Register after input data for index $i:")
                println(register.joinToString(" ") { it.toString(2).padStart(8, '0') })
                println()
            }

            binding.textInputEditTextEncrypted.setText(cipherText.joinToString(" ") { it.toString(2).padStart(8, '0') })
            // Show toast message
            showToast("Text successfully encrypted")
        }

        binding.textInputLayoutEncrypted.setEndIconOnClickListener {
            copyTextToClipboard(binding.textInputEditTextEncrypted.text.toString())
        }
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

    fun encrypt(cipherText: Int, key: Int): Int {
        // Enkripsi menggunakan operasi XOR antara cipherText dan key
        var temp = cipherText xor key

        // Geser bit hasil XOR ke kiri sebanyak 1 kali
        temp = (temp shl 1) or (temp ushr 7)

        // Pastikan hanya 8 bit yang tersisa dengan memotong nilai yang lebih besar dari 255
        temp = temp and 0xFF

        return temp
    }

    fun inputDataRegister(register: MutableList<Int>, cipherText: Int): MutableList<Int> {
        // Geser nilai-nilai dalam register ke kiri
        for (i in 1 until register.size) {
            register[i - 1] = register[i]
        }
        // Atur nilai terakhir dari register menjadi nilai dari cipherText
        register[register.size - 1] = cipherText
        return register
    }


    fun charToBinary(char: Char): String {
        return char.toInt().toString(2).padStart(8, '0')
    }
}
