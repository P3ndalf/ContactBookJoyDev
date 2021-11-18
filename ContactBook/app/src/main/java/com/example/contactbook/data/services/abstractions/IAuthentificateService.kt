package com.example.contactbook.data.services.abstractions

interface IAuthentificateService {
    fun isUserExists(email : String, password : String) : Boolean

    fun loginUser(email : String, password: String)
}