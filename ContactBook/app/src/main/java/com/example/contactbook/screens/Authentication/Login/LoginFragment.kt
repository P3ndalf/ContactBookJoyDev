package com.example.contactbook.screens.Authentication.Login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import com.example.contactbook.services.InputValidationService
import com.example.contactbook.services.AuthorizedUserSharedPreferencesService
import com.example.contactbook.data.viewModels.UserViewModel
import com.example.contactbook.services.HashService
import com.example.contactbook.services.abstractions.IAuthorizedUserSharedPreferencesService
import com.example.contactbook.services.abstractions.IHashService
import com.example.contactbook.services.abstractions.IInputValidationService
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

class LoginFragment() : Fragment() {

    private lateinit var mUserViewModel : UserViewModel
    private lateinit var authorizedUserSharedPreferencesService : IAuthorizedUserSharedPreferencesService
    private lateinit var inputValidationService: IInputValidationService
    private lateinit var hashService : IHashService
    private lateinit var userModel : UserModel

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mUserViewModel = ViewModelProvider(this).get(com.example.contactbook.data.viewModels.UserViewModel::class.java)
    }

    override fun  onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) : View? {
        val view = inflater.inflate(R.layout.fragment_login,container,false)
        authorizedUserSharedPreferencesService = AuthorizedUserSharedPreferencesService(this.requireActivity())
        hashService = HashService()

        if (authorizedUserSharedPreferencesService.isAuthorized()) {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
        }
        else{
            view.findViewById<Button>(R.id.register_fragment_button).setOnClickListener{
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            view.findViewById<Button>(R.id.login).setOnClickListener{
                val email = view?.findViewById<TextInputEditText>(R.id.email)?.text.toString()
                val password = view?.findViewById<TextInputEditText>(R.id.password)?.text.toString()
                inputValidationService = InputValidationService(this.requireContext())
                if (inputValidationService.loginInputValidation(email, password)){
                        authenticateUser(email,hashService.getHash(password, "SHA-256"))
                }
            }
        }
        return view;
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
}