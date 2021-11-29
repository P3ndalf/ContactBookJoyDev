package com.example.contactbook.ui.views.Main.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.contactbook.data.entities.Contact
import com.example.contactbook.databinding.FragmentEditContactBinding
<<<<<<< Updated upstream:ContactBook/app/src/main/java/com/example/contactbook/ui/views/Main/contacts/EditContactFragment.kt
import dagger.hilt.android.AndroidEntryPoint
=======
import com.example.contactbook.ui.viewModels.ContactViewModel
import com.example.contactbook.ui.views.mainscreens.contacts.detail.ContactDetailFragmentArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
>>>>>>> Stashed changes:ContactBook/app/src/main/java/com/example/contactbook/ui/views/mainscreens/contacts/edit/EditContactFragment.kt

@AndroidEntryPoint
class EditContactFragment : Fragment() {
    private lateinit var binding: FragmentEditContactBinding
    private val args: ContactDetailFragmentArgs by navArgs()

    private val mContactViewModel: ContactViewModel by viewModels()

    private var inputValidationFlags: Array<Boolean> = Array(3) { true }
    private lateinit var contact: Contact
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditContactBinding.inflate(inflater, container, false)
        var gender = "Others"
        when (binding.genderRG.checkedRadioButtonId) {
            binding.maleRB.id -> {
                gender = "Male"
            }
            binding.femaleRB.id -> {
                gender = "Female"
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            contact = mContactViewModel.getContact(args.transferContactId)
            with(binding) {
                contactNameTV.setText(contact.contactName)
                phoneNumberTV.setText(contact.phoneNumber)
                instagramTV.setText(contact.instagram)
            }
        }

        binding.editBtn.setOnClickListener {
            editContact()
        }



        binding.cancelBtn.setOnClickListener {
            val action =
                EditContactFragmentDirections.actionEditContactFragmentToContactDetailFragment(args.transferContactId)
            findNavController().navigate(action)
        }
        return binding.root
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

    private fun editContact() {
        with(binding) {
            inputValidationFlags = mContactViewModel.checkInputValidation(
                contactNameTV.text.toString(),
                instagramTV.text.toString(),
                phoneNumberTV.text.toString()
            )
            var gender = "Others"
            when (genderRG.checkedRadioButtonId) {
                maleRB.id -> {
                    gender = "Male"
                }
                femaleRB.id -> {
                    gender = "Female"
                }
            }

            contact.contactName = contactNameTV.text.toString()
            contact.instagram = instagramTV.text.toString()
            contact.phoneNumber = phoneNumberTV.text.toString()
            if (!inputValidationFlags.contains(false)) {
                mContactViewModel.editContact(
                    contact
                )
                val action =
                    EditContactFragmentDirections.actionEditContactFragmentToContactDetailFragment(
                        args.transferContactId
                    )
                findNavController().navigate(action)
            } else {
                changeLayoutValidity()
            }
        }
    }
}