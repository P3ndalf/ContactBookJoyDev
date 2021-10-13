package com.example.contactbook.screens.Main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.FileObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.contactbook.R
import com.example.contactbook.data.entities.User
import com.example.contactbook.data.services.SharedPreferencesService
import com.example.contactbook.data.viewModels.UserViewModel
import com.example.contactbook.databinding.FragmentListBinding
import com.example.contactbook.databinding.FragmentMainBinding
import com.example.contactbook.screens.UserObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

class MainFragment : Fragment() {

    private lateinit var mUserViewModel: UserViewModel
    private lateinit var sharedPreferencesService: SharedPreferencesService
    private lateinit var userObserver: UserObserver

    private var _binding:FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root


        mUserViewModel =  ViewModelProvider(this).get(com.example.contactbook.data.viewModels.UserViewModel::class.java)
        sharedPreferencesService = SharedPreferencesService(this,"AuthorizedUser")
        val currentUserId = sharedPreferencesService.loadCurrentUserId()

        return view
    }

    private fun fillUserFields(currentId : String) {

       mUserViewModel.getUserById(currentId).observe(viewLifecycleOwner, Observer {
               currentUser -> userObserver.setData(currentUser)
       })


    }



}