package com.avanzz.phonebook.model

data class CallLogItem(
    val number: String,
    val date: Long,
    val duration: Int,
    val name: String
)