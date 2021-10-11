package com.example.contactbook.screens.Main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.navigation.fragment.findNavController
import com.example.contactbook.R
import com.example.contactbook.data.entities.User

class MainFragment : Fragment() {

    private lateinit var currentUser : User
    private lateinit var currentUserName : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main,container,false)
        currentUserName = view?.findViewById<TextView>(R.id.name)!!

        loadCurrentUser()

        return view
    }

    private fun loadCurrentUser(){
        var sharedPreferences = this.requireActivity().getSharedPreferences("AuthorizedUser", Context.MODE_PRIVATE)
        var savedString = sharedPreferences.getString("InsertedName", null)

        currentUserName.text = savedString
        Toast.makeText(requireContext(), savedString, Toast.LENGTH_LONG).show()
    }

}