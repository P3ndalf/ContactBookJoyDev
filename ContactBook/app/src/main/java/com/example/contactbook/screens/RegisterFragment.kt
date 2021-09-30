package com.example.contactbook.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.contactbook.R

class RegisterFragment : Fragment(R.layout.fragment_register) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle? ){
        super.onViewCreated(view, savedInstanceState)
        view.postDelayed({
            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }, 3000)
    }
}