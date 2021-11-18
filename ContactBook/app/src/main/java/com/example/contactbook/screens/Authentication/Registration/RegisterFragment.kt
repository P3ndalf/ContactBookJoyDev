package com.example.contactbook.screens.Authentication.Registration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.contactbook.MainActivity
import com.example.contactbook.R
import com.example.contactbook.data.Model.UserModel
import com.example.contactbook.data.entities.User
import com.example.contactbook.data.services.InputValidationService
import com.example.contactbook.services.AuthorizedUserSharedPreferencesService
import com.example.contactbook.data.viewModels.UserViewModel
import com.example.contactbook.databinding.FragmentRegisterBinding
import com.example.contactbook.services.HashService
import com.example.contactbook.services.abstractions.IAuthorizedUserSharedPreferencesService
import com.example.contactbook.services.abstractions.IHashService
import java.util.*

class RegisterFragment : Fragment() {
    private lateinit var mUserViewModel: UserViewModel
    private lateinit var authorizedUserSharedPreferencesService: IAuthorizedUserSharedPreferencesService
    private lateinit var hashService: IHashService

    private lateinit var binding: FragmentRegisterBinding


    private var inputValidationFlags: Array<Boolean> = Array(5) { true }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        hashService = HashService()

        val inputValidationService: InputValidationService = InputValidationService()

        binding.loginFragmentButton.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        with(binding){
            binding.register.setOnClickListener {
                inputValidationFlags = inputValidationService.registerInputValidation(
                    name.text.toString(),
                    lastname.text.toString(),
                    email.text.toString(),
                    password.text.toString(),
                    confirmPassword.text.toString()
                )
                if (!inputValidationFlags.contains(false)) {
                    insertDataToDataBase()
                }
                changeLayoutValidity()
            }
        }

        return binding.root
    }

    private fun insertDataToDataBase() {
        val userId = UUID.randomUUID().toString()
        val user : User
        authorizedUserSharedPreferencesService =
            AuthorizedUserSharedPreferencesService(this.requireActivity())
        with(binding){
            user = User(
                userId,
                name.text.toString(),
                lastname.text.toString(),
                email.text.toString(),
                hashService.getHash(password.text.toString(), "SHA-256")
            )
            authorizedUserSharedPreferencesService.saveCurrentUserData(
                UserModel(
                    userId, name.text.toString(), lastname.text.toString(), email.text.toString()
                )
            )
        }

        mUserViewModel.addUser(user)

        startActivity(Intent(requireActivity(), MainActivity::class.java))
    }

    private fun changeLayoutValidity() {
        with(binding){
            if (!inputValidationFlags[0]) {
                nameError.isErrorEnabled = true
                nameError.error = "Enter correct name"
            } else {
                nameError.isErrorEnabled = false
                nameError.error = null
            }
            if (!inputValidationFlags[1]) {
                lastnameError.isErrorEnabled = true
                lastnameError.error = "Enter correct last name"
            } else {
                lastnameError.isErrorEnabled = false
                lastnameError.error = null
            }
            if (!inputValidationFlags[2]) {
                emailError.isErrorEnabled = true
                emailError.error = "Enter correct email"
            } else {
                emailError.isErrorEnabled = false
                emailError.error = null
            }
            if (!inputValidationFlags[3]) {
                passwordError.isErrorEnabled = true
                passwordError.error = "Fill password field"
            } else {
                passwordError.isErrorEnabled = false
                passwordError.error = null
            }
            if (!inputValidationFlags[4]) {
                confirmPasswordError.isErrorEnabled = true
                confirmPasswordError.error = "Repeat your password"
            } else {
                confirmPasswordError.isErrorEnabled = false
                confirmPasswordError.error = null
            }
        }
    }
}