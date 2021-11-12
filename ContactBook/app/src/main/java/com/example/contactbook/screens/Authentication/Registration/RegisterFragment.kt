package com.example.contactbook.screens.Authentication.Registration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.example.contactbook.databinding.FragmentLoginBinding
import com.example.contactbook.databinding.FragmentRegisterBinding
import com.example.contactbook.services.HashService
import com.example.contactbook.services.abstractions.IAuthorizedUserSharedPreferencesService
import com.example.contactbook.services.abstractions.IHashService
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class RegisterFragment : Fragment() {
    private lateinit var mUserViewModel : UserViewModel
    private lateinit var authorizedUserSharedPreferencesService : IAuthorizedUserSharedPreferencesService
    private lateinit var hashService : IHashService

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var name : TextInputEditText
    private lateinit var lastName : TextInputEditText
    private lateinit var email :TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var passwordConfirm : TextInputEditText
    private lateinit var nameError : TextInputLayout
    private lateinit var lastNameError : TextInputLayout
    private lateinit var emailError : TextInputLayout
    private lateinit var passwordConfirmError : TextInputLayout
    private lateinit var passwordError : TextInputLayout


    private var inputValidationFlags : Array<Boolean> = Array(5) { true}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        hashService = HashService()

        name = binding.name
        lastName = binding.lastname
        email = binding.email
        password = binding.password
        passwordConfirm = binding.confirmPassword
        nameError = binding.nameError
        lastNameError = binding.lastnameError
        emailError = binding.emailError
        passwordConfirmError = binding.confirmPasswordError
        passwordError = binding.passwordError

        val inputValidationService : InputValidationService = InputValidationService(
            this.requireContext()
        )

        binding.loginFragmentButton.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.register.setOnClickListener{
            inputValidationFlags = inputValidationService.registerInputValidation(name.text.toString()
                , lastName.text.toString(), email.text.toString()
                , password.text.toString(), passwordConfirm.text.toString()
            )
            if(!inputValidationFlags.contains(false)) {
                insertDataToDataBase()
            }
            changeLayoutValidity()
        }
        return binding.root
    }

    private fun insertDataToDataBase(){
        val userId = UUID.randomUUID().toString()
        val user = User(userId, name.text.toString()
            , lastName.text.toString(), email.text.toString()
            , hashService.getHash(password.text.toString(),"SHA-256")
        )

        authorizedUserSharedPreferencesService = AuthorizedUserSharedPreferencesService(this.requireActivity())

        authorizedUserSharedPreferencesService.saveCurrentUserData(
            UserModel(userId, name.text.toString()
                ,lastName.text.toString(), email.text.toString()
            )
        )

        mUserViewModel.addUser(user)

        startActivity(Intent(requireActivity(), MainActivity::class.java))
    }

    private fun changeLayoutValidity() {
        if (!inputValidationFlags[0]){
            nameError.isErrorEnabled = true
            nameError.error = "Enter correct name"
        } else {
            nameError.isErrorEnabled = false
            nameError.error = null
        }
        if (!inputValidationFlags[1]){
            lastNameError.isErrorEnabled = true
            lastNameError.error = "Enter correct last name"
        } else {
            lastNameError.isErrorEnabled = false
            lastNameError.error = null
        }
        if(!inputValidationFlags[2]){
            emailError.isErrorEnabled = true
            emailError.error = "Enter correct email"
        } else {
            emailError.isErrorEnabled = false
            emailError.error = null
        }
        if(!inputValidationFlags[3]){
            passwordError.isErrorEnabled = true
            passwordError.error = "Fill password field"
        } else {
            passwordError.isErrorEnabled = false
            passwordError.error = null
        }
        if (!inputValidationFlags[4]){
            passwordConfirmError.isErrorEnabled = true
            passwordConfirmError.error = "Repeat your password"
        } else {
            passwordConfirmError.isErrorEnabled = false
            passwordConfirmError.error = null
        }
    }
}