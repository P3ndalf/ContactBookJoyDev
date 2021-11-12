package com.example.contactbook.screens.Authentication.Login

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.contactbook.MainActivity
import com.example.contactbook.R
import com.example.contactbook.data.Model.UserModel
import com.example.contactbook.data.services.InputValidationService
import com.example.contactbook.services.AuthorizedUserSharedPreferencesService
import com.example.contactbook.data.viewModels.UserViewModel
import com.example.contactbook.databinding.FragmentLoginBinding
import com.example.contactbook.databinding.FragmentUserprofileBinding
import com.example.contactbook.services.HashService
import com.example.contactbook.services.abstractions.IAuthorizedUserSharedPreferencesService
import com.example.contactbook.services.abstractions.IHashService
import com.example.contactbook.services.abstractions.IInputValidationService
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

class LoginFragment() : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var mUserViewModel : UserViewModel
    private lateinit var authorizedUserSharedPreferencesService : IAuthorizedUserSharedPreferencesService
    private lateinit var inputValidationService: IInputValidationService
    private lateinit var hashService : IHashService
    private lateinit var userModel : UserModel

    private lateinit var email : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var emailError : TextInputLayout
    private lateinit var passwordError : TextInputLayout

    private var inputValidationFlags : Array<Boolean>  = Array(2) {true}
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mUserViewModel = ViewModelProvider(this).get(com.example.contactbook.data.viewModels.UserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        authorizedUserSharedPreferencesService = AuthorizedUserSharedPreferencesService(this.requireActivity())
        hashService = HashService()

        if (authorizedUserSharedPreferencesService.isAuthorized()) {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
        }
        else{
            email = binding.email
            password = binding.password
            emailError = binding.emailError
            passwordError = binding.passwordError

            inputValidationService = InputValidationService(this.requireContext())


            binding.registerFragmentButton.setOnClickListener{
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            binding.login.setOnClickListener{
                inputValidationFlags = inputValidationService.loginInputValidation(email.text.toString(), password.text.toString())
                if (!inputValidationFlags.contains(false)) {
                    authenticateUser(email.text.toString(),hashService.getHash(password.text.toString(), "SHA-256"))
                }
                changeLayoutValidity()
            }

        }
        return binding.root
    }

    private fun authenticateUser(email : String, password : String) {
        mUserViewModel.authenticateUser(email, password).observe(viewLifecycleOwner, Observer { currentUser ->
            lifecycleScope.launch(Main){
                if(currentUser == null){
                    Toast.makeText(requireContext(), "Wrong email or password", Toast.LENGTH_LONG).show()
                }
                else{
                    authorizedUserSharedPreferencesService.saveCurrentUserData(UserModel(currentUser.id,currentUser.firstName,currentUser.lastName,currentUser.email))
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                }
            }
        })
    }

    private fun changeLayoutValidity(){
        if(!inputValidationFlags[0]){
            emailError.isErrorEnabled = true
            emailError.error = "Fill password field"
        } else {
            emailError.isErrorEnabled = false
            emailError.error = null
        }
        if (!inputValidationFlags[1]) {
            passwordError.isErrorEnabled = true
            passwordError.error = "Fill password field"

        } else {
            passwordError.isErrorEnabled = false
            passwordError.error = null
        }
    }
}