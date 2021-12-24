package com.example.contactbook.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.contactbook.data.entities.Contact
import com.example.contactbook.data.repositories.implementations.ContactRepository
import com.example.contactbook.data.services.InputValidationService
import com.example.contactbook.ui.views.mainscreens.contacts.edit.EditContactFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class EditContactVM @Inject constructor(
    private val contactRepository: ContactRepository,
    private val inputValidationService: InputValidationService
) : ViewModel() {

    val name = MutableLiveData("")

    val phoneNumber = MutableLiveData("")

    val birthday = MutableLiveData("")

    val gender = MutableLiveData("")

    val instagram = MutableLiveData("")

    val picturePath = MutableLiveData("")

    val ownerId = MutableLiveData("")

    fun setData(id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val contact = contactRepository.findContact(id) ?: throw Exception("Contact null exception")
            name.value = contact.contactName
            phoneNumber.value = contact.phoneNumber
            birthday.value = contact.birthday.toString()
            gender.value = contact.gender
            instagram.value = contact.instagram
            picturePath.value = contact.picturePath
            ownerId.value = contact.ownerId
        }
    }

    fun checkInputValidation(name: String, phone: String): Array<Boolean> {
        return inputValidationService.addContactInputValidation(name, phone)
    }

    fun editContact(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val contact = Contact(
                id,
                name.value.toString(),
                phoneNumber.value.toString(),
                birthday.value!!.toLong(),
                gender.value.toString(),
                instagram.value.toString(),
                picturePath.value.toString(),
                ownerId.value.toString()
            )
            contactRepository.editContact(contact)
        }
    }


}