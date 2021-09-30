package com.example.contactbook.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.contactbook.R

class LoginFragment : Fragment(R.layout.fragment_login) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle? ){
        super.onViewCreated(view, savedInstanceState)
        view.postDelayed({
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }, 3000)
    }
}