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
                binding.inIV.error = "IV dan Key harus memiliki panjang karakter yang sama"
                return@setOnClickListener
            }

            val cipherText = stringToIntList(inCipher)
            Log.d("Dekripsi", cipherText.toString())
            val key = inKey.map { charToBinary(it).toInt(2) }
            Log.d("Dekripsi", key.toString())
            val initialVector = inIV.map { charToBinary(it).toInt(2) }
            var register: MutableList<Int> = mutableListOf()
            var plainText = mutableListOf<Char>() // Menggunakan List<Char> untuk menyimpan karakter

            register.addAll(initialVector)
            Log.d("Dekripsi", register.toString())

            for (i in cipherText.indices) {
                val temp = mutableListOf<Int>()
                for (y in key.indices) {
                    temp.add(decrypt(register[y], key[y]))
                }
                val decryptedValue = temp[0] xor cipherText[i]
                plainText.add(decryptedValue.toChar())
                register = inputDataRegister(register, cipherText[i])
            }

            // Mengubah List<Char> menjadi String sebelum menampilkannya
            binding.outDecrypted.setText(plainText.joinToString(""))
            showToast("Proses Dekripsi Berhasil")

//            01111001 01100001 01111111 01101011 01001010
        }
        binding.layoutOutDecrypted.setEndIconOnClickListener {
            copyTextToClipboard(binding.outDecrypted.text.toString())
        }
    }


    private fun decrypt(cipherText: Int, key: Int): Int {
        // Enkripsi menggunakan operasi XOR antara cipherText dan key
        var temp = cipherText xor key

        // Geser bit hasil XOR ke kiri sebanyak 1 kali
        temp = (temp shl 1) or (temp ushr 7)

        // Pastikan hanya 8 bit yang tersisa dengan memotong nilai yang lebih besar dari 255
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
        // Geser nilai-nilai dalam register ke kiri
        for (i in 1 until register.size) {
            register[i - 1] = register[i]
        }
        // Atur nilai terakhir dari register menjadi nilai dari cipherText
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
        // Memisahkan string pada setiap spasi
        val binList = binStr.split(" ").map { it }

        // Mengonversi setiap string biner menjadi integer dan menyimpan dalam list
        val intList = binList.map { binaryString ->
            binaryString.toInt(2) // Mengonversi string biner menjadi integer
        }

        return intList
    }

    private fun charToBinary(char: Char): String {
        return char.toInt().toString(2).padStart(8, '0')
    }
}
