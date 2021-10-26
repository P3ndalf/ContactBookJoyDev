package com.example.contactbook.screens.Main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.contactbook.AuthenticationActivity
import com.example.contactbook.data.Model.UserModel
import com.example.contactbook.data.viewModels.UserViewModel
import com.example.contactbook.databinding.FragmentUserprofileBinding
import com.example.contactbook.services.AuthorizedUserSharedPreferencesService
import com.example.contactbook.services.abstractions.IAuthorizedUserSharedPreferencesService

class UserProfileFragment : Fragment() {

    private lateinit var mUserViewModel: UserViewModel
    private lateinit var authorizedUserSharedPreferencesService: IAuthorizedUserSharedPreferencesService
    private lateinit var user : UserModel

    private var _binding: FragmentUserprofileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserprofileBinding.inflate(inflater, container, false)

        mUserViewModel =  ViewModelProvider(this).get(UserViewModel::class.java)
        authorizedUserSharedPreferencesService = AuthorizedUserSharedPreferencesService(this.requireActivity())

        user = authorizedUserSharedPreferencesService.loadCurrentUser()

        fillUserFields(user)

        binding.logOutBtn.setOnClickListener{
            logOut()
        }


        return binding.root
    }

    private fun fillUserFields(user : UserModel) {
        binding.userNameTV.text = user.firstName
        binding.userLastNameTV.text = user.lastName
        binding.emailTV.text = user.email
    }

    private fun logOut(){
        authorizedUserSharedPreferencesService.deleteCurrentUserData()
        startActivity(Intent(requireActivity(), AuthenticationActivity::class.java))
    }
}