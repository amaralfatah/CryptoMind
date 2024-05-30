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

            val inText = binding.textInputEditTextInput.text.toString()
            val inKey = binding.textInputEditTextKey.text.toString()
            val inIv = binding.textInputEditTextIV.text.toString()

            if (inText.isEmpty() || inKey.isEmpty() || inIv.isEmpty()) {
                showToast("Field tidak boleh kosong")
                return@setOnClickListener
            }

            if (inIv.length != inKey.length) {
                binding.textInputEditTextIV.error = "IV dan Key harus memiliki panjang karakter yang sama"
                return@setOnClickListener
            }

            var initialVector: List<Int>
            var key: List<Int>
            var register = mutableListOf<Int>()
            var cipherText = mutableListOf<Int>()
            val plainText: List<Int>

            plainText = inText.map { charToBinary(it).toInt(2) }
            key = inKey.map { charToBinary(it).toInt(2) }
            initialVector = inIv.map { charToBinary(it).toInt(2) }
            register.addAll(initialVector)

            Log.d("ISI REGISTER", register.toString())
            for (i in plainText.indices) {

                val temp = mutableListOf<Int>()

                for (y in key.indices){
                    // Enkripsi IV menggunakan key yang sesuai
                    temp.add(encrypt(register[y], key[y]))
                }

                // XOR hasil enkripsi dengan PlainText
                val encryptedValue = temp[0] xor plainText[i]

                // Tambahkan hasil XOR sebagai CipherText
                cipherText.add(encryptedValue)

                // Masukkan CipherText ke LSB (Least Significant Bit) Register
                register = inputDataRegister(register, cipherText.last())

                // Cetak hasil
                logEncryptionDetails(i, cipherText, register)
            }

            binding.textInputEditTextEncrypted.setText(cipherText.joinToString(" ") { it.toString(2).padStart(8, '0') })
            // Show toast message
            showToast("Proses Enkripsi Berhasil")
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

    private fun encrypt(cipherText: Int, key: Int): Int {
        // Enkripsi menggunakan operasi XOR antara cipherText dan key
        var temp = cipherText xor key

        // Geser bit hasil XOR ke kiri sebanyak 1 kali
        temp = (temp shl 1) or (temp ushr 7)

        // Pastikan hanya 8 bit yang tersisa dengan memotong nilai yang lebih besar dari 255
        temp = temp and 0xFF

        return temp
    }

    private fun inputDataRegister(register: MutableList<Int>, cipherText: Int): MutableList<Int> {
        // Geser nilai-nilai dalam register ke kiri
        for (i in 1 until register.size) {
            register[i - 1] = register[i]
        }
        // Atur nilai terakhir dari register menjadi nilai dari cipherText
        register[register.size - 1] = cipherText
        return register
    }


    private fun charToBinary(char: Char): String {
        return char.toInt().toString(2).padStart(8, '0')
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


}
