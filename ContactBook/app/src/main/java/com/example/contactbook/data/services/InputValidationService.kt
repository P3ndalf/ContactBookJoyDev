package com.example.contactbook.data.services

import android.text.TextUtils

import com.example.contactbook.data.services.abstractions.IInputValidationService
import javax.inject.Inject


class InputValidationService @Inject constructor(): IInputValidationService {

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
        if (name.isBlank() || !(name.length in 2..21)) {
            flags[0] = false
        }
        if (lastName.isBlank() || !(lastName.length in 2..21)) {
            flags[1] = false
        }

        if(!email.contains("@", false) || email.isBlank()){
            flags[2] = false
        }

        if (password.isBlank()) {
            flags[3] = false
        }
        if (confirmedPassword.isBlank()) {
            flags[4] = false
        }
        if (confirmedPassword != password) {
            flags[4] = false
        }
        return flags
    }
}