package com.example.contactbook.screens.Login

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
import com.example.contactbook.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText

class LoginFragment : Fragment() {
    private lateinit var mUserViewModel : UserViewModel

    override fun  onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) : View? {

        val view = inflater.inflate(R.layout.fragment_login,container,false)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

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
        if (inputValidation(email, password)){
            var user = mUserViewModel.authenticateUser(email, password)
            if(user == null ){
                Toast.makeText(requireContext(), "Wrong email or password", Toast.LENGTH_LONG).show()
            }
            else{
                saveCurrentUserData(user)
                Toast.makeText(requireContext(), "Successful", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            }
        }
    }

    private fun inputValidation(
        email: String, password: String,
    ): Boolean {
        if(!email.contains("@", false)){
            Toast.makeText(requireContext(), "Fill correct email", Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(requireContext(), "Fill email field", Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(requireContext(), "Fill password field", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun saveCurrentUserData(user : User?){
        var sharedPreferences = this.requireActivity().getSharedPreferences("AuthorizedUser", Context.MODE_PRIVATE)

        var editor = sharedPreferences.edit()

        editor.apply{
            putString("InsertedName", user?.firstName)
            putString("InsertedLastName", user?.lastName)
        }.apply()

        Toast.makeText(requireContext(), "GOVNO", Toast.LENGTH_LONG).show()
    }
}