package com.example.contactbook.services.abstractions

interface IInputValidationService {

    fun loginInputValidation(
        email: String, password: String,
    ): Boolean

    fun addContactInputValidation(
        name : String, instagram : String, phoneNumber : String
    ): Boolean

    fun registerInputValidation(
        name: String, lastName: String,
        email: String, password: String,
        confirmedPassword : String
    ): Boolean
}