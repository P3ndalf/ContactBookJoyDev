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
import com.example.contactbook.services.abstractions.IAuthorizedUserSharedPreferencesService
import com.example.contactbook.services.abstractions.IInputValidationService
import java.util.*

class AddContactFragment : Fragment() {
    private var _binding: FragmentAddContactBinding? = null
    private val binding get() = _binding!!
    private lateinit var authorizedUserSharedPreferencesService: IAuthorizedUserSharedPreferencesService
    private lateinit var inputValidationService: IInputValidationService
    private lateinit var mContactViewModel : ContactViewModel

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
        var nameET = binding.nameET.text.toString()
        var phoneET = binding.phoneNumberTV.text.toString()
        var birthday = Calendar.getInstance().timeInMillis
        var gender = true
        var instagramET = binding.instagramET.text.toString()
        var ownerId = authorizedUserSharedPreferencesService.loadCurrentUser().id

        if(inputValidationService.addContactInputValidation(nameET, instagramET, phoneET)){
            mContactViewModel.addContact(Contact(id, nameET,phoneET, birthday, gender, instagramET, ownerId))
            findNavController().navigate(R.id.action_addContactFragment_to_contactsFragment)
        }
        else{

        }
    }
}