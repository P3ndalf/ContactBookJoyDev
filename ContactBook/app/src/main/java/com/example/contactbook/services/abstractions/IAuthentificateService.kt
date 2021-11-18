package com.example.contactbook.services.abstractions

interface IAuthentificateService {
    fun isUserExists(email : String, password : String) : Boolean

    fun loginUser(email : String, password: String)
}