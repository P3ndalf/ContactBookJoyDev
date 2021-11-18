package com.example.contactbook.ui.views.Authentication.Login

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
import com.example.contactbook.databinding.FragmentLoginBinding
import com.example.contactbook.ui.activities.AuthenticationActivity
import com.example.contactbook.ui.viewModels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment() : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val mLoginViewModel: LoginViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)


        with(binding) {
            registerFragmentButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            login.setOnClickListener {

                lifecycleScope.launch(Main) {
                    val user = mLoginViewModel.getUserByEmail(email.text.toString())

                    if (user?.passwordHash == password.text.toString()) {

                        startActivity(Intent(requireContext(), AuthenticationActivity::class.java))
                    } else {
                        setValidityWrongPassword()
                    }
                }

            }
        }
        return binding.root
    }

    private fun changeLayoutValidity() {
        with(binding) {

        }
    }

    private fun setValidityWrongPassword() {
        with(binding) {
            emailError.isErrorEnabled = true
            emailError.error = "Incorrect password or email"
            passwordError.isErrorEnabled = true
            passwordError.error = "Incorrect password or email"
        }
    }

}