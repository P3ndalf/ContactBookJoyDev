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
import com.example.contactbook.services.HashService
import com.example.contactbook.services.abstractions.IAuthorizedUserSharedPreferencesService
import com.example.contactbook.services.abstractions.IHashService
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class RegisterFragment : Fragment() {
    private lateinit var mUserViewModel : UserViewModel
    private lateinit var authorizedUserSharedPreferencesService : IAuthorizedUserSharedPreferencesService
    private lateinit var hashService : IHashService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_register,container,false)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        hashService = HashService()

        view.findViewById<Button>(R.id.login_fragment_button).setOnClickListener{
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

        val inputValidationService : InputValidationService = InputValidationService(
                                                                                            this.requireContext()
                                                                                          )

        if(inputValidationService.registerInputValidation(name, lastName, email, password, confirmedPassword)){
            val user = User(userId, name, lastName, email, hashService.getHash(password,"SHA-256"))
            authorizedUserSharedPreferencesService = AuthorizedUserSharedPreferencesService(this.requireActivity())
            authorizedUserSharedPreferencesService.saveCurrentUserData(UserModel(userId, name, lastName,email))

            mUserViewModel.addUser(user)

            startActivity(Intent(requireActivity(), MainActivity::class.java))
        }
    }
}