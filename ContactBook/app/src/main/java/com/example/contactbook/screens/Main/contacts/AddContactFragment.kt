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
import com.example.contactbook.data.services.InputValidationService
import com.example.contactbook.data.viewModels.ContactViewModel
import com.example.contactbook.databinding.FragmentAddContactBinding
import com.example.contactbook.services.AuthorizedUserSharedPreferencesService
import com.example.contactbook.services.abstractions.IAuthorizedUserSharedPreferencesService
import com.example.contactbook.services.abstractions.IInputValidationService
import java.util.*

class AddContactFragment : Fragment() {
    private var _binding: FragmentAddContactBinding? = null
    private val binding get() = _binding!!
    private lateinit var authorizedUserSharedPreferencesService: IAuthorizedUserSharedPreferencesService
    private lateinit var inputValidationService: IInputValidationService
    private lateinit var mContactViewModel : ContactViewModel


    private var nameET = binding.nameET
    private var nameError = binding.nameError
    private var phoneET = binding.phoneNumberTV
    private var phoneError = binding.phoneNumberError
    private var birthday = Calendar.getInstance().timeInMillis
    private var gender = true
    private var instagramET = binding.instagramET
    private var instagramError = binding.instagramError

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddContactBinding.inflate(inflater, container, false)

        binding.cancelBtn.setOnClickListener{
            findNavController().navigate(R.id.action_addContactFragment_to_contactsFragment)
        }

        mContactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        inputValidationService = InputValidationService(this.requireContext())
        authorizedUserSharedPreferencesService = AuthorizedUserSharedPreferencesService(this.requireActivity())

        binding.addBtn.setOnClickListener{
            addContact()
        }

        return binding.root
    }

    private fun addContact() {
        var id = UUID.randomUUID().toString()
        var ownerId = authorizedUserSharedPreferencesService.loadCurrentUser().id

        var inputValidationFlags = inputValidationService.addContactInputValidation(nameET.text.toString()
                                    , instagramET.text.toString()
                                    , phoneET.text.toString())
        if(inputValidationFlags.all{ true}){
            mContactViewModel.addContact(Contact(id, nameET.text.toString()
                                                , phoneET.text.toString(), birthday, gender
                                                , instagramET.text.toString(), ownerId))
            findNavController().navigate(R.id.action_addContactFragment_to_contactsFragment)
        }
        if(!inputValidationFlags[0]) {
            nameError.isErrorEnabled = true
            nameError.error = "Enter correct name"
        }
        else{
            nameError.isErrorEnabled = false
            nameError.error = null
        }
        if(!inputValidationFlags[1]) {
            phoneError.isErrorEnabled = true
            phoneError.error = "Enter correct phone number"
        }
        else{
            phoneError.isErrorEnabled = false
            phoneError.error = null
        }
        if(!inputValidationFlags[2]){
            instagramError.isErrorEnabled = true
            instagramError.error = "Enter correct instagram id  "
        }
        else{
            instagramError.isErrorEnabled = false
            instagramError.error = null
        }
    }
}