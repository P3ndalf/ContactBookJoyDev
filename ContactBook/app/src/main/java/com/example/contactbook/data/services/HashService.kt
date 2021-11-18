package com.example.contactbook.data.services

import com.example.contactbook.data.services.abstractions.IHashService
import java.security.MessageDigest

class HashService : IHashService {

    override fun getHash(inputString : String, algorithm : String): String{
        val bytes = MessageDigest.getInstance(algorithm).digest(inputString.toByteArray())
        return toHex(bytes)
    }

    override fun toHex(byteArray : ByteArray): String {
        return byteArray.joinToString (""){ "%02x".format(it) }
    }

}