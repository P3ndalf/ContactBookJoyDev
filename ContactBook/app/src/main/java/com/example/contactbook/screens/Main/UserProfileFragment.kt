package com.example.contactbook.screens.Main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.contactbook.AuthenticationActivity
import com.example.contactbook.MainActivity
import com.example.contactbook.R
import com.example.contactbook.data.Model.UserModel
import com.example.contactbook.data.viewModels.UserViewModel
import com.example.contactbook.databinding.FragmentUserprofileBinding
import com.example.contactbook.services.AuthorizedUserSharedPreferencesService

class UserProfileFragment : Fragment() {

    private lateinit var mUserViewModel: UserViewModel
    private lateinit var authorizedUserSharedPreferencesService: AuthorizedUserSharedPreferencesService
    private lateinit var user : UserModel

    private var _binding: FragmentUserprofileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserprofileBinding.inflate(inflater, container, false)
        val view = binding.root

        mUserViewModel =  ViewModelProvider(this).get(UserViewModel::class.java)
        authorizedUserSharedPreferencesService = AuthorizedUserSharedPreferencesService(this.requireActivity(),"AuthorizedUser")

        user = authorizedUserSharedPreferencesService.loadCurrentUser()

        fillUserFields(user)

        binding.logOutBtn.setOnClickListener{
            logOut()
        }


        return view
    }

    private fun fillUserFields(user : UserModel) {
        binding.userNameTV.text = user.name
        binding.userLastNameTV.text = user.lastName
    }

    private fun logOut(){
        authorizedUserSharedPreferencesService.deleteCurrentUserData()
        startActivity(Intent(requireActivity(), AuthenticationActivity::class.java))
    }
}