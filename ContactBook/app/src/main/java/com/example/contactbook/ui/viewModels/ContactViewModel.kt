package com.example.contactbook.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactbook.data.entities.Contact
import com.example.contactbook.data.repositories.implementations.ContactRepository
import com.example.contactbook.data.services.InputValidationService
import com.example.contactbook.data.services.abstractions.IInputValidationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val inputValidationService: InputValidationService
) : ViewModel() {

    fun addContact(
        name: String,
        phoneNumber: String,
        birthday: Long,
        gender: String,
        instagram: String,
        ownerId: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            contactRepository.addContact(name, phoneNumber, birthday, gender, instagram, ownerId)
        }
    }

    fun getContacts(ownerId: String): LiveData<List<Contact>> {
        return contactRepository.getContacts(ownerId)
    }

    fun checkInputValidation(name: String, instagram: String, phone: String): Array<Boolean> {
        return inputValidationService.addContactInputValidation(name, instagram, phone)
    }
}