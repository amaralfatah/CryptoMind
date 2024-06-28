package com.uadev.cryptomind.utils

object Utils {

    fun isBinaryString(input: String): Boolean {
        return input.all { it == '0' || it == '1' }
    }

    fun isValidBinaryLength(input: String): Boolean {
        return input.length % 8 == 0
    }

}
