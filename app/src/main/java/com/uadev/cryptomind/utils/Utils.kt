package com.uadev.cryptomind.utils

import android.util.Log

object Utils {

    fun isBinaryString(input: String): Boolean {
        return input.all { it == '0' || it == '1' }
    }

    fun isValidBinaryLength(input: String): Boolean {
        return input.length % 8 == 0
    }

    fun convertIntListToBinaryStringList(intList: List<Int>?): List<String> {
        return intList!!.map { it.toString(2).padStart(8, '0') }
//        val plainStr = Utils.convertIntListToBinaryStringList(plaintext)
//        val stringUtuh = plainStr.joinToString(" ")
    }

    fun convertMutableIntListToMutableBinaryStringList(mutableIntList: MutableList<Int>): MutableList<String> {
        return mutableIntList.map { it.toString(2).padStart(8, '0') }.toMutableList()
    }

    fun calculateXorString(listA: MutableList<String>, listB: List<String>): MutableList<String> {
        require(listA.size == listB.size) { "Ukuran listA dan listB harus sama" }

        return listA.zip(listB) { a, b ->
            require(a.length == b.length) { "Panjang string dalam list harus sama" }
            a.zip(b) { charA, charB ->
                if (charA == charB) '0' else '1'
            }.joinToString("")
        }.toMutableList() // Mengubah hasil ke MutableList
    }

    fun calculateXorOneString(nilaiA: String, nilaiB: String): String {
        require(nilaiA.length == nilaiB.length) { "Panjang string harus sama" }

        return nilaiA.zip(nilaiB) { charA, charB ->
            if (charA == charB) '0' else '1'
        }.joinToString("")
    }



    fun shiftStringLeft(input: MutableList<String>): MutableList<String> {
        // Menggabungkan semua elemen input menjadi satu string
        val gabungInput = input.joinToString("")

        // Menggeser string satu karakter ke kiri
        val geserKiri = gabungInput.substring(1) + gabungInput[0]

        // Memecah string yang digeser menjadi potongan-potongan berukuran yang sama
        val chunkSize = gabungInput.length / input.size
        val chunks = geserKiri.chunked(chunkSize)

        // Mengembalikan hasil sebagai MutableList<String>
        return chunks.toMutableList()
    }



    fun enkripsi(antrian: MutableList<String>, key: List<String>): List<String> {
        val hasilXor = calculateXorString(antrian, key)
        return shiftStringLeft(hasilXor)
    }

    fun shiftArrayLeftAndAddNew(input: MutableList<String>, newValue: String): MutableList<String> {
        // Mengecek apakah input kosong
        if (input.isEmpty()) return mutableListOf(newValue)

        // Menggeser semua elemen ke kiri
        for (i in 0 until input.size - 1) {
            input[i] = input[i + 1]
        }

        // Memasukkan nilai baru di akhir
        input[input.size - 1] = newValue

        return input
    }
}
