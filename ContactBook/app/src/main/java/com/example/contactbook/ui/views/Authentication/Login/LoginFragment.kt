package com.example.contactbook.ui.views.Authentication.Login

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
import com.example.contactbook.data.services.AuthorisedSharedPreferencesService
import com.example.contactbook.data.services.abstractions.IAuthorisedSharedPreferencesService
import com.example.contactbook.databinding.FragmentLoginBinding
import com.example.contactbook.ui.activities.MainActivity
import com.example.contactbook.ui.viewModels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(){
    private lateinit var binding: FragmentLoginBinding

    private val mLoginViewModel: LoginViewModel by viewModels()

    lateinit var authorizedUserSharedPreferencesService: IAuthorisedSharedPreferencesService

    var inputValidationFlags: Array<Boolean> = Array(2) { true }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        authorizedUserSharedPreferencesService = AuthorisedSharedPreferencesService(
            requireActivity().getSharedPreferences(
                "AuthorizedUser",
                Context.MODE_PRIVATE
            )
        )

        if (authorizedUserSharedPreferencesService.isAuthorized()) {
            startActivity(
                Intent(
                    requireContext(),
                    MainActivity::class.java
                )
            )
        }

        with(binding) {
            registerFragmentButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            login.setOnClickListener {
                inputValidationFlags = mLoginViewModel.checkInputValidation(
                    email.text.toString(),
                    password.text.toString()
                )
                if (!inputValidationFlags.contains(false)) {
                    lifecycleScope.launch(Dispatchers.Main) {
                        var user = mLoginViewModel.authenticateUser(
                            email.text.toString(),
                            password.text.toString()
                        )
                        if (user != null) {
                            authorizedUserSharedPreferencesService.saveCurrentUserData(
                                user
                            )
                            startActivity(
                                Intent(
                                    requireContext(),
                                    MainActivity::class.java
                                )
                            )
                        } else {
                            setValidityWrongPassword()
                        }
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
                emailError.isErrorEnabled = true
                emailError.error = getString(R.string.emailError)
            } else {
                emailError.isErrorEnabled = false
                emailError.error = null
            }
            if (!inputValidationFlags[1]) {
                passwordError.isErrorEnabled = true
                passwordError.error = getString(R.string.passwordError)
            } else {
                passwordError.isErrorEnabled = false
                passwordError.error = null
            }
        }
    }

    private fun setValidityWrongPassword() {
        with(binding) {
            emailError.isErrorEnabled = true
            emailError.error = getString(R.string.nouser)
            passwordError.isErrorEnabled = true
            passwordError.error = getString(R.string.nouser)
        }
    }
}