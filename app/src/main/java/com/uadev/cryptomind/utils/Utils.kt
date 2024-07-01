package com.uadev.cryptomind.utils

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

    fun calculateXorString(listA: List<String>, listB: List<String>): List<String> {
        // Pastikan kedua daftar memiliki panjang yang sama
        if (listA.size != listB.size) {
            throw IllegalArgumentException("Ukuran listA dan listB harus sama")
        }

        return listA.zip(listB) { a, b ->
            if (a.length != b.length) {
                throw IllegalArgumentException("Panjang string dalam list harus sama")
            }
            a.zip(b) { charA, charB ->
                if (charA == charB) '0' else '1'
            }.joinToString("")
        }
    }


    fun shiftStringLeft(input: String): String {
        return input.substring(1) + input[0]
    }

    fun enkripsi(antrian: List<String>, key: List<String>): List<String> {
        // Lakukan operasi XOR pada antrian dan key
        val hasilXor = calculateXorString(antrian, key)

        // Gabungkan hasil XOR menjadi satu string
        val gabungHasilXor = hasilXor.joinToString("")

        // Geser string hasil XOR ke kiri
        val hasilGeser = shiftStringLeft(gabungHasilXor)

        // Pecah hasil geser menjadi list string
        val pecahHasilGeser = hasilGeser.chunked(8)

        // Kembalikan hasil yang sudah dipecah
        return pecahHasilGeser
    }
}
