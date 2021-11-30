package com.example.contactbook.data.services

import com.example.contactbook.data.services.abstractions.IHashService
import java.security.MessageDigest
import javax.inject.Inject

class HashService @Inject constructor() : IHashService {

    override fun getSHA256Hash(inputString : String): String{
        val bytes = MessageDigest.getInstance("SHA-256").digest(inputString.toByteArray())
        return toHex(bytes)
    }

    override fun toHex(byteArray : ByteArray): String {
        return byteArray.joinToString (""){ "%02x".format(it) }
    }

}