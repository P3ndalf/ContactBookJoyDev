package com.example.contactbook.screens.Registration

import android.content.Context
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
import com.example.contactbook.data.viewModels.UserViewModel
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class RegisterFragment : Fragment() {
    private lateinit var mUserViewModel : UserViewModel

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


        if(inputValidation(name, lastName, email, password, confirmedPassword)){
            val user = User(userId, name, lastName, email, password)

            saveCurrentUserData(user)

            mUserViewModel.addUser(user)
            Toast.makeText(this.requireContext(), "New User were registered.", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }
        else{
            Toast.makeText(requireContext(), "Fill all input fields.", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputValidation(
        name: String, lastName: String,
        email: String, password: String,
        confirmedPassword : String
    ): Boolean {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(requireContext(), "Fill name field",Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(requireContext(), "Fill lastName field",Toast.LENGTH_LONG).show()
            return false
        }

        if (!((name.length in 2..21) || (lastName.length in 2..21))){
            Toast.makeText(requireContext(), "Name and LastName should be shorter than 20 symbs and longer than 2",Toast.LENGTH_LONG).show()
            return false
        }

        if(!email.contains("@", false)){
            Toast.makeText(requireContext(), "Fill correct email",Toast.LENGTH_LONG).show()
            return false
        }


        if (TextUtils.isEmpty(email)) {
            Toast.makeText(requireContext(), "Fill email field",Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(requireContext(), "Fill password field",Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(confirmedPassword)) {
            Toast.makeText(requireContext(), "Fill confirmedPassword field",Toast.LENGTH_LONG).show()
            return false
        }
        if (confirmedPassword != password) {
            Toast.makeText(requireContext(), "Please confirm the password",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun saveCurrentUserData(user : User){
        var sharedPreferences = this.requireActivity().getSharedPreferences("AuthorizedUser", Context.MODE_PRIVATE)

        var editor = sharedPreferences.edit()

        editor.apply{
            putString("InsertedName", user.firstName)
            putString("InsertedLastName", user.lastName)
        }.apply()

        Toast.makeText(requireContext(), "GOVNO", Toast.LENGTH_LONG).show()
    }
}