package com.example.contactbook.screens.Main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.contactbook.R
import com.example.contactbook.services.SharedPreferencesService
import com.example.contactbook.data.viewModels.UserViewModel
import com.example.contactbook.databinding.FragmentMainBinding
import com.example.contactbook.screens.UserObserver
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private lateinit var mUserViewModel: UserViewModel
    private lateinit var sharedPreferencesService: SharedPreferencesService
    private lateinit var userObserver: UserObserver

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root

        userObserver = UserObserver()
        mUserViewModel =  ViewModelProvider(this).get(com.example.contactbook.data.viewModels.UserViewModel::class.java)
        sharedPreferencesService = SharedPreferencesService(this.requireActivity(),"AuthorizedUser")
        var currentUserId = sharedPreferencesService.loadCurrentUserId()

        fillUserFields(currentUserId)

        binding.logOutBtn.setOnClickListener{
            logOut()
        }

        return view
    }

    private fun fillUserFields(currentId : String) {
        mUserViewModel.getUserById(currentId).observe(viewLifecycleOwner, Observer {
                currentUser ->
                    lifecycleScope.launch(Main){
                        userObserver.setData(currentUser)
                        binding.userNameTV.text = userObserver.user?.firstName
                        binding.userLastNameTV.text = userObserver.user?.lastName
                    }
        })
    }

    private fun logOut(){
        sharedPreferencesService.deleteCurrentUserData()
        findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
    }
}