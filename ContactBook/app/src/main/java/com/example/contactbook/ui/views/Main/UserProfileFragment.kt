package com.example.contactbook.ui.views.Main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.contactbook.data.entities.User
import com.example.contactbook.data.services.AuthorizedUserSharedPreferencesService
import com.example.contactbook.data.services.abstractions.IAuthorizedUserSharedPreferencesService
import com.example.contactbook.databinding.FragmentUserprofileBinding
import com.example.contactbook.ui.activities.AuthenticationActivity
import com.example.contactbook.ui.viewModels.UserProfileModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserprofileBinding
    private val mUserProfileModel: UserProfileModel by viewModels()
    private lateinit var authorizedUserSharedPreferencesService: IAuthorizedUserSharedPreferencesService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserprofileBinding.inflate(inflater, container, false)

        authorizedUserSharedPreferencesService = AuthorizedUserSharedPreferencesService(
            requireActivity().getSharedPreferences(
                "AuthorizedUser",
                Context.MODE_PRIVATE
            )
        )

        val user = authorizedUserSharedPreferencesService.loadCurrentUser()

        fillUserFields(user)

        binding.logOutBtn.setOnClickListener{
            logOut()
        }
        return binding.root
    }

    private fun fillUserFields(user : User) {
        binding.userNameTV.text = user.firstName
        binding.userLastNameTV.text = user.lastName
        binding.emailTV.text = user.email
    }

    private fun logOut(){
        authorizedUserSharedPreferencesService.deleteCurrentUserData()
        startActivity(Intent(requireActivity(), AuthenticationActivity::class.java))
    }
}