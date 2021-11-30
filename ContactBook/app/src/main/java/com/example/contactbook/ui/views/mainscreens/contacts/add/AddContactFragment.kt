package com.example.contactbook.ui.views.mainscreens.contacts.add

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.contactbook.R
import com.example.contactbook.data.services.AuthorisedSharedPreferencesService
import com.example.contactbook.data.services.abstractions.IAuthorisedSharedPreferencesService
import com.example.contactbook.databinding.FragmentAddContactBinding
import com.example.contactbook.ui.viewModels.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class AddContactFragment : Fragment() {
    private lateinit var binding: FragmentAddContactBinding
    private lateinit var authorisedSharedPreferencesService: IAuthorisedSharedPreferencesService

    private val mContactViewModel: ContactViewModel by viewModels()

    private var inputValidationFlags: Array<Boolean> = Array(3) { true }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddContactBinding.inflate(inflater, container, false)

        with(binding) {
            cancelBtn.setOnClickListener {
                findNavController().navigate(R.id.action_addContactFragment_to_contactsFragment)
            }

            authorisedSharedPreferencesService = AuthorisedSharedPreferencesService(
                requireActivity().getSharedPreferences(
                    "AuthorizedUser",
                    Context.MODE_PRIVATE
                )
            )

            addBtn.setOnClickListener {
                addContact()
            }
        }

        return binding.root
    }

    private fun addContact() {
        var ownerId = authorisedSharedPreferencesService.loadCurrentUser().id

        with(binding) {
            inputValidationFlags = mContactViewModel.checkInputValidation(
                nameET.text.toString(), instagramET.text.toString(), phoneNumberET.text.toString()
            )
            var gender = "other"
            genderRG.setOnCheckedChangeListener { radioGroup, checkedId ->
                maleRB.apply {
                    gender = text.toString()
                }
                femaleRB.apply {
                    gender = text.toString()
                }
                othersRB.apply {
                    gender = text.toString()
                }
            }
            if (!inputValidationFlags.contains(false)) {
                lifecycleScope.launch(Dispatchers.Main){
                    if(mContactViewModel.addContact(
                            nameET.text.toString(),
                            phoneNumberET.text.toString(),
                            Calendar.getInstance().timeInMillis,
                            gender,
                            instagramET.text.toString(),
                            ownerId
                        )){
                        findNavController().navigate(R.id.action_addContactFragment_to_contactsFragment)
                    }
                    else{
                        inputValidationFlags = Array(inputValidationFlags.size){false}
                        Toast.makeText(requireContext(), getString(R.string.ContactExistsToast), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                changeLayoutValidity()
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