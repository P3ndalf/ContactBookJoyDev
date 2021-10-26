package com.example.contactbook.services

import android.content.Context
import android.text.TextUtils
import android.widget.Toast

class AuthenticationInputValidationService(currentContext: Context) {

    private lateinit var currentContext: Context

    init {
        this.currentContext = currentContext
    }

    public fun inputValidation(
        email: String, password: String,
    ): Boolean {
        if(!email.contains("@", false)){
            Toast.makeText(currentContext, "Fill correct email", Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(currentContext, "Fill email field", Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(currentContext, "Fill password field", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    public fun inputValidation(
        name: String, lastName: String,
        email: String, password: String,
        confirmedPassword : String
    ): Boolean {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(currentContext, "Fill name field",Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(currentContext, "Fill lastName field",Toast.LENGTH_LONG).show()
            return false
        }

        if (!((name.length in 2..21) || (lastName.length in 2..21))){
            Toast.makeText(currentContext, "Name and LastName should be shorter than 20 symbs and longer than 2",Toast.LENGTH_LONG).show()
            return false
        }

        if(!email.contains("@", false)){
            Toast.makeText(currentContext, "Fill correct email",Toast.LENGTH_LONG).show()
            return false
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(currentContext, "Fill email field",Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(currentContext, "Fill password field",Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(confirmedPassword)) {
            Toast.makeText(currentContext, "Fill confirmedPassword field",Toast.LENGTH_LONG).show()
            return false
        }
        if (confirmedPassword != password) {
            Toast.makeText(currentContext, "Please confirm the password",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}