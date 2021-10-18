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
import com.example.contactbook.services.AuthenticationInputValidationService
import com.example.contactbook.services.AuthorizedUserSharedPreferencesService
import com.example.contactbook.data.viewModels.UserViewModel
import com.example.contactbook.screens.UserObserver
import com.example.contactbook.services.HashService
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

class LoginFragment() : Fragment() {

    private lateinit var mUserViewModel : UserViewModel
    private lateinit var authorizedUserSharedPreferencesService : AuthorizedUserSharedPreferencesService
    private lateinit var authenticationInputValidationService: AuthenticationInputValidationService
    private lateinit var hashService : HashService
    private var userObserver : UserObserver = UserObserver()

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mUserViewModel = ViewModelProvider(this).get(com.example.contactbook.data.viewModels.UserViewModel::class.java)
    }

    override fun  onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) : View? {
        val view = inflater.inflate(R.layout.fragment_login,container,false)
        authorizedUserSharedPreferencesService = AuthorizedUserSharedPreferencesService(this.requireActivity(), "AuthorizedUser")
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
                authenticationInputValidationService = AuthenticationInputValidationService(this.requireContext())
                if (authenticationInputValidationService.inputValidation(email, password)){
                        authenticateUser(email,hashService.getHash(password, "SHA-256"))
                }
            }
        }
        return view;
    }

    private fun authenticateUser(email : String, password : String) {
        mUserViewModel.authenticateUser(email, password).observe(viewLifecycleOwner, Observer { currentUser ->
            lifecycleScope.launch(Main){
                userObserver.setData(currentUser)
                if(userObserver.user == null ){
                    Toast.makeText(requireContext(), "Wrong email or password", Toast.LENGTH_LONG).show()
                }
                else{
                    authorizedUserSharedPreferencesService.saveCurrentUserData(userObserver.user)
                    Toast.makeText(requireActivity(), "Successful", Toast.LENGTH_LONG).show()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                }
            }
        })
    }
}