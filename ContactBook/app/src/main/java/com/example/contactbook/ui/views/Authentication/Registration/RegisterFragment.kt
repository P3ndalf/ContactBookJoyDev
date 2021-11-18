package com.example.contactbook.ui.views.Authentication.Registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.contactbook.R
import com.example.contactbook.data.entities.User
import com.example.contactbook.data.services.AuthorizedUserSharedPreferencesService
import com.example.contactbook.data.services.abstractions.IAuthorizedUserSharedPreferencesService
import com.example.contactbook.databinding.FragmentRegisterBinding
import com.example.contactbook.ui.activities.MainActivity
import com.example.contactbook.ui.viewModels.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    private val mRegistrationViewModel: RegistrationViewModel by viewModels()

    lateinit var authorizedUserSharedPreferencesService: IAuthorizedUserSharedPreferencesService

    var inputValidationFlags: Array<Boolean> = Array(5) { true }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        authorizedUserSharedPreferencesService = AuthorizedUserSharedPreferencesService(
            requireActivity().getSharedPreferences(
                "AuthorizedUser",
                Context.MODE_PRIVATE
            )
        )

        binding.loginFragmentButton.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.register.setOnClickListener {
            with(binding) {
                inputValidationFlags = mRegistrationViewModel.checkInputValidation(
                    name.text.toString(),
                    lastname.text.toString(),
                    email.text.toString(),
                    password.text.toString(),
                    confirmPassword.text.toString()
                )

                if (!inputValidationFlags.contains(false)
                ) {
                    lifecycleScope.launch(Dispatchers.Main) {

                        mRegistrationViewModel.addUser(
                            name.text.toString(),
                            lastname.text.toString(),
                            email.text.toString(),
                            password.text.toString()
                        )
                        authorizedUserSharedPreferencesService.saveCurrentUserData(
                            User(
                                "",
                                name.text.toString(),
                                lastname.text.toString(),
                                email.text.toString(),
                                ""
                            )
                        )
                        startActivity(Intent(requireActivity(), MainActivity::class.java))
                    }
                } else {
                    changeLayoutValidity()
                }
            }
        }
        return binding.root
    }

    private fun changeLayoutValidity() {
        with(binding) {
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