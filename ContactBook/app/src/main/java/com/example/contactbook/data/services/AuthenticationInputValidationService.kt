package com.example.contactbook.data.services

import android.text.TextUtils
import android.widget.Toast
import androidx.fragment.app.Fragment

class AuthenticationInputValidationService(fragment: Fragment) {

    private lateinit var fragment: Fragment

    init {
        this.fragment = fragment
    }

    public fun inputValidation(
        email: String, password: String,
    ): Boolean {
        if(!email.contains("@", false)){
            Toast.makeText(fragment.requireContext(), "Fill correct email", Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(fragment.requireContext(), "Fill email field", Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(fragment.requireContext(), "Fill password field", Toast.LENGTH_LONG).show()
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
            Toast.makeText(fragment.requireContext(), "Fill name field",Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(fragment.requireContext(), "Fill lastName field",Toast.LENGTH_LONG).show()
            return false
        }

        if (!((name.length in 2..21) || (lastName.length in 2..21))){
            Toast.makeText(fragment.requireContext(), "Name and LastName should be shorter than 20 symbs and longer than 2",Toast.LENGTH_LONG).show()
            return false
        }

        if(!email.contains("@", false)){
            Toast.makeText(fragment.requireContext(), "Fill correct email",Toast.LENGTH_LONG).show()
            return false
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(fragment.requireContext(), "Fill email field",Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(fragment.requireContext(), "Fill password field",Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(confirmedPassword)) {
            Toast.makeText(fragment.requireContext(), "Fill confirmedPassword field",Toast.LENGTH_LONG).show()
            return false
        }
        if (confirmedPassword != password) {
            Toast.makeText(fragment.requireContext(), "Please confirm the password",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}