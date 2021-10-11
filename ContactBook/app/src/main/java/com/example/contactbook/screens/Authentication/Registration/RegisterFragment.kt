package com.example.contactbook.screens.Authentication.Registration

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.contactbook.R
import com.example.contactbook.data.entities.User
import com.example.contactbook.data.services.AuthenticationInputValidationService
import com.example.contactbook.data.services.SharedPreferencesService
import com.example.contactbook.data.viewModels.UserViewModel
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class RegisterFragment : Fragment() {
    private lateinit var mUserViewModel : UserViewModel
    private lateinit var sharedPreferencesService : SharedPreferencesService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_register,container,false)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        view.findViewById<Button>(R.id.login_fragment).setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        view.findViewById<Button>(R.id.register).setOnClickListener{
            insertDataToDataBase()
        }

        return view
    }

    private fun insertDataToDataBase(){
        val userId = UUID.randomUUID().toString()
        val name = view?.findViewById<TextInputEditText>(R.id.name)?.text.toString()
        val lastName = view?.findViewById<TextInputEditText>(R.id.lastname)?.text.toString()
        val email = view?.findViewById<TextInputEditText>(R.id.email)?.text.toString()
        val password = view?.findViewById<TextInputEditText>(R.id.password)?.text.toString()
        val confirmedPassword = view?.findViewById<TextInputEditText>(R.id.confirmPassword)?.text.toString()

        val authenticationInputValidationService : AuthenticationInputValidationService = AuthenticationInputValidationService(this)

        if(authenticationInputValidationService.inputValidation(name, lastName, email, password, confirmedPassword)){
            val user = User(userId, name, lastName, email, password)

            sharedPreferencesService = SharedPreferencesService(this, "AuthorizedUser")
            sharedPreferencesService.saveCurrentUserData(user)

            mUserViewModel.addUser(user)

            Toast.makeText(this.requireContext(), "New User were registered.", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }
        else{
            Toast.makeText(requireContext(), "Fill all input fields.", Toast.LENGTH_LONG).show()
        }
    }



}