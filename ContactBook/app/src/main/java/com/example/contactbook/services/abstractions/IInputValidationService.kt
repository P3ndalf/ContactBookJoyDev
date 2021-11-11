package com.example.contactbook.services.abstractions

import com.google.android.material.textfield.TextInputEditText

interface IInputValidationService {

    fun loginInputValidation(
        email: String, password: String,
    ): Array<Boolean>

    fun addContactInputValidation(
        name : String, instagram : String, phoneNumber : String
    ): Array<Boolean>

    fun registerInputValidation(
        name: String, lastName: String,
        email: String, password: String,
        confirmedPassword : String
    ): Array<Boolean>
}