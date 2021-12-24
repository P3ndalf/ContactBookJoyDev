package com.example.contactbook.ui.views.mainscreens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.contactbook.R
import com.example.contactbook.data.services.AuthorisedSharedPreferencesService
import com.example.contactbook.data.services.abstractions.IAuthorisedSharedPreferencesService
import com.example.contactbook.databinding.UserProfileFragmentBinding
import com.example.contactbook.ui.activities.AuthenticationActivity
import com.example.contactbook.ui.viewModels.UserProfileVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment(R.layout.fragment_userprofile) {
    private lateinit var binding: UserProfileFragmentBinding
    private lateinit var authorisedSharedPreferencesService: IAuthorisedSharedPreferencesService

    private val viewModel: UserProfileVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_userprofile, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        authorisedSharedPreferencesService = AuthorisedSharedPreferencesService(
            requireActivity().getSharedPreferences(
                "AuthorizedUser",
                Context.MODE_PRIVATE
            )
        )

        val user = authorisedSharedPreferencesService.loadCurrentUser()

        viewModel.setData(user.firstName, user.lastName, user.email)

        binding.logOutBtn.setOnClickListener {
            logOut()
        }
    }


    private fun logOut() {
        authorisedSharedPreferencesService.deleteCurrentUserData()
        startActivity(Intent(requireActivity(), AuthenticationActivity::class.java))
    }
}