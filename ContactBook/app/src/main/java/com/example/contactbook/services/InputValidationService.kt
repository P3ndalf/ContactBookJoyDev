package com.example.contactbook.data.services

import android.content.Context
import android.text.TextUtils
import android.widget.Toast

import com.example.contactbook.services.abstractions.IInputValidationService
import com.google.android.material.textfield.TextInputEditText


class InputValidationService(private var currentContext: Context) : IInputValidationService {

    override fun loginInputValidation(
        email: String, password: String,
    ): Array<Boolean> {
        var flags : Array<Boolean> = arrayOf(true, true)
        flags[0] = !(!email.contains("@", false) || TextUtils.isEmpty(email))
        flags[1] = !TextUtils.isEmpty(password)
        return flags
    }

    override fun addContactInputValidation(
        name : String, instagram : String, phoneNumber : String
    ): Array<Boolean> {
        var flags : Array<Boolean> = arrayOf(true, true, true)
        if (TextUtils.isEmpty(name)){
            flags[0] = false
        }
        if (TextUtils.isEmpty(instagram)){
            flags[1] = false
        }
        if (TextUtils.isEmpty(phoneNumber)){
            flags[2] = false
        }
        return flags
    }

    override fun registerInputValidation(
        name: String, lastName: String,
        email: String, password: String,
        confirmedPassword : String
    ): Array<Boolean> {
        var flags = Array(5) { true}
        if (TextUtils.isEmpty(name) || !(name.length in 2..21)) {
            flags[0] = false
        }
        if (TextUtils.isEmpty(lastName) || !(lastName.length in 2..21)) {
            flags[1] = false
        }

        if(!email.contains("@", false) || TextUtils.isEmpty(email)){
            flags[2] = false
        }

        if (TextUtils.isEmpty(password)) {
            flags[3] = false
        }
        if (TextUtils.isEmpty(confirmedPassword)) {
            flags[4] = false
        }
        if (confirmedPassword != password) {
            flags[4] = false
        }
        return flags
    }
}