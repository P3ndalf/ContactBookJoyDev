package com.example.contactbook.services.abstractions

interface IHashService {

    fun getHash(inputString : String, algorithm : String): String

    fun toHex(byteArray : ByteArray): String
}