package com.example.contactbook.ui.views.mainscreens.contacts.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.contactbook.R
import com.example.contactbook.data.entities.Contact
import com.example.contactbook.databinding.FragmentEditContactBinding

import com.example.contactbook.ui.viewModels.ContactViewModel
import com.example.contactbook.ui.views.mainscreens.contacts.detail.ContactDetailFragmentArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditContactFragment : Fragment() {
    private lateinit var binding: FragmentEditContactBinding
    private val args: ContactDetailFragmentArgs by navArgs()

    private val mContactViewModel: ContactViewModel by viewModels()

    private var inputValidationFlags: Array<Boolean> = Array(2) { true }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditContactBinding.inflate(inflater, container, false)

        lifecycleScope.launch(Dispatchers.Main) {
            val contact = mContactViewModel.getContact(args.transferContactId)
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

    private suspend fun getGender(): String {
        var gender = when (binding.genderRG.checkedRadioButtonId) {
            binding.maleRB.id -> {
                getString(R.string.male)
            }
            binding.femaleRB.id -> {
                getString(R.string.female)
            }
            else -> "Others"
        }
        return gender
    }

    private fun changeLayoutValidity() {
        with(binding) {
            if (!inputValidationFlags[0]) {
                nameError.isErrorEnabled = true
                nameError.error = getString(R.string.contactNameError)
            } else {
                nameError.isErrorEnabled = false
                nameError.error = null
            }
            if (!inputValidationFlags[1]) {
                phoneNumberError.isErrorEnabled = true
                phoneNumberError.error = getString(R.string.contactPhoneNumberError)
            } else {
                phoneNumberError.isErrorEnabled = false
                phoneNumberError.error = null
            }
        }
    }

    private fun editContact() {
        with(binding) {
            inputValidationFlags = mContactViewModel.checkInputValidation(
                contactNameTV.text.toString(),
                phoneNumberTV.text.toString()
            )

            lifecycleScope.launch(Dispatchers.Main) {
                val contact = mContactViewModel.getContact(args.transferContactId)
                    contact.contactName = contactNameTV.text.toString()
                    contact.instagram = instagramTV.text.toString()
                    contact.phoneNumber = phoneNumberTV.text.toString()
                    contact.gender = getGender()
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
}