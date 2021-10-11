package com.example.contactbook.screens.Authentication.Login

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.contactbook.R
import com.example.contactbook.data.entities.User
import com.example.contactbook.data.services.AuthenticationInputValidationService
import com.example.contactbook.data.services.SharedPreferencesService
import com.example.contactbook.data.viewModels.UserViewModel
import com.example.contactbook.screens.UserObserver
import com.google.android.material.textfield.TextInputEditText

class LoginFragment() : Fragment() {
    private lateinit var mUserViewModel : UserViewModel
    private lateinit var sharedPreferencesService : SharedPreferencesService
    private lateinit var  authenticationInputValidationService: AuthenticationInputValidationService
    private lateinit var userObserver : UserObserver

    override fun  onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) : View? {

        val view = inflater.inflate(R.layout.fragment_login,container,false)
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userObserver = UserObserver()
        view.findViewById<Button>(R.id.register_fragment).setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        view.findViewById<Button>(R.id.login).setOnClickListener{
            authenticateUser()
        }
        return view;
    }

    private fun authenticateUser() {
        val email = view?.findViewById<TextInputEditText>(R.id.email)?.text.toString()
        val password = view?.findViewById<TextInputEditText>(R.id.password)?.text.toString()

        authenticationInputValidationService = AuthenticationInputValidationService(this)

        if (authenticationInputValidationService.inputValidation(email, password)){
            mUserViewModel.authenticateUser(email, password).observe(viewLifecycleOwner, Observer{
                    currentUser -> userObserver.setData(currentUser)
            })
            if(userObserver == null ){
                Toast.makeText(requireContext(), "Wrong email or password", Toast.LENGTH_LONG).show()
            }
            else{
                sharedPreferencesService = SharedPreferencesService(this, "AuthorizedUser")
                sharedPreferencesService.saveCurrentUserData(userObserver.user)
                Toast.makeText(requireContext(), "Successful", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            }
        }
    }

}