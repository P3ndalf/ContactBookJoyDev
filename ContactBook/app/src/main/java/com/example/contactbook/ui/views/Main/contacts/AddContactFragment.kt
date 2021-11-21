package com.example.contactbook.ui.views.Main.contacts

import android.content.Context
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
import com.example.contactbook.ui.viewModels.ContactViewModel
import com.example.contactbook.databinding.FragmentAddContactBinding
import com.example.contactbook.data.services.AuthorisedSharedPreferencesService
import com.example.contactbook.data.services.abstractions.IAuthorisedSharedPreferencesService
import com.example.contactbook.data.services.abstractions.IInputValidationService
import java.util.*

class AddContactFragment : Fragment() {
    private lateinit var binding: FragmentAddContactBinding
    private lateinit var authorisedSharedPreferencesService: IAuthorisedSharedPreferencesService
    private lateinit var inputValidationService: IInputValidationService
    private lateinit var mContactViewModel: ContactViewModel

    private var inputValidationFlags: Array<Boolean> = Array(3) { true }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddContactBinding.inflate(inflater, container, false)

        binding.cancelBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addContactFragment_to_contactsFragment)
        }

        mContactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        inputValidationService = InputValidationService()
        authorisedSharedPreferencesService = AuthorisedSharedPreferencesService(
            requireActivity().getSharedPreferences(
                "AuthorizedUser",
                Context.MODE_PRIVATE
            )
        )

        binding.addBtn.setOnClickListener {
            addContact()
        }

        return binding.root
    }

    private fun addContact() {
        var id = UUID.randomUUID().toString()
        var ownerId = authorisedSharedPreferencesService.loadCurrentUser().id
        var birthday = Calendar.getInstance().timeInMillis
        var gender = true
        with(binding) {
            inputValidationFlags = inputValidationService.addContactInputValidation(
                nameET.text.toString(), instagramET.text.toString(), phoneNumberET.text.toString()
            )
            if (!inputValidationFlags.contains(false)) {
                mContactViewModel.addContact(
                    Contact(
                        id,
                        nameET.text.toString(),
                        phoneNumberET.text.toString(),
                        birthday,
                        gender,
                        instagramET.text.toString(),
                        ownerId
                    )
                )
                findNavController().navigate(R.id.action_addContactFragment_to_contactsFragment)
            }
        }

    }

    private fun changeLayoutValidity() {
        with(binding) {
            if (!inputValidationFlags[0]) {
                nameError.isErrorEnabled = true
                nameError.error = "Enter correct name"
            } else {
                nameError.isErrorEnabled = false
                nameError.error = null
            }
            if (!inputValidationFlags[1]) {
                phoneNumberError.isErrorEnabled = true
                phoneNumberError.error = "Enter correct phone number"
            } else {
                phoneNumberError.isErrorEnabled = false
                phoneNumberError.error = null
            }
            if (!inputValidationFlags[2]) {
                instagramError.isErrorEnabled = true
                instagramError.error = "Enter correct instagram id  "
            } else {
                instagramError.isErrorEnabled = false
                instagramError.error = null
            }
        }
    }
}