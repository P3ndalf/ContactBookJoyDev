package com.example.contactbook.ui.views.mainscreens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.contactbook.data.entities.User
import com.example.contactbook.data.services.AuthorisedSharedPreferencesService
import com.example.contactbook.data.services.abstractions.IAuthorisedSharedPreferencesService
import com.example.contactbook.databinding.FragmentUserprofileBinding
import com.example.contactbook.ui.activities.AuthenticationActivity
import com.example.contactbook.ui.viewModels.UserProfileModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserprofileBinding
    private val mUserProfileModel: UserProfileModel by viewModels()
    private lateinit var authorisedSharedPreferencesService: IAuthorisedSharedPreferencesService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserprofileBinding.inflate(inflater, container, false)

        authorisedSharedPreferencesService = AuthorisedSharedPreferencesService(
            requireActivity().getSharedPreferences(
                "AuthorizedUser",
                Context.MODE_PRIVATE
            )
        )

        val user = authorisedSharedPreferencesService.loadCurrentUser()

        fillUserFields(user)

        binding.logOutBtn.setOnClickListener{
            logOut()
        }
        return binding.root
    }

    private fun fillUserFields(user : User) {
        with(binding){
            userNameTV.text = user.firstName
            userLastNameTV.text = user.lastName
            emailTV.text = user.email
        }
    }

    private fun logOut(){
        authorisedSharedPreferencesService.deleteCurrentUserData()
        startActivity(Intent(requireActivity(), AuthenticationActivity::class.java))
    }
}