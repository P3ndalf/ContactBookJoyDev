package com.example.contactbook.screens.Main.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.contactbook.R
import com.example.contactbook.data.entities.Contact
import com.example.contactbook.data.viewModels.ContactViewModel
import com.example.contactbook.databinding.FragmentAddContactBinding
import com.example.contactbook.services.InputValidationService
import com.example.contactbook.services.AuthorizedUserSharedPreferencesService
import java.util.*

class AddContactFragment : Fragment() {
    private var _binding: FragmentAddContactBinding? = null
    private val binding get() = _binding!!
    private lateinit var authorizedUserSharedPreferencesService: AuthorizedUserSharedPreferencesService
    private lateinit var inputValidationService: InputValidationService
    private lateinit var mContactViewModel : ContactViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddContactBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.cancelBtn.setOnClickListener{
            findNavController().navigate(R.id.action_addContactFragment_to_contactsFragment)
        }

        mContactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        inputValidationService = InputValidationService(this.requireContext())

        binding.addBtn.setOnClickListener{
            addContact()
        }

        return view
    }

    private fun addContact() {
        var id = UUID.randomUUID().toString()
        var nameET = binding.nameET.text.toString()
        var instagramET = binding.instagramET.text.toString()
        var phoneET = binding.phoneNumberTV.text.toString()

        if(inputValidationService.addContactInputValidation(nameET, instagramET, phoneET)){
            mContactViewModel.addContact(Contact(id, nameET,phoneET, true, instagramET, authorizedUserSharedPreferencesService.loadCurrentUser().id))
            findNavController().navigate(R.id.action_addContactFragment_to_contactsFragment)
        }
        else{

        }
    }
}