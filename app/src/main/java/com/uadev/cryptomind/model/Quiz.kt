package com.uadev.cryptomind.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Quiz(
    val question: String = "",
    val options: List<String> = emptyList(),
    val correctOptionIndex: Int = 0
)
