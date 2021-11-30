package com.example.contactbook.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactbook.data.entities.Contact
import com.example.contactbook.data.repositories.implementations.ContactRepository
import com.example.contactbook.data.services.InputValidationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val inputValidationService: InputValidationService
) : ViewModel() {

    suspend fun addContact(
        name: String,
        phoneNumber: String,
        birthday: Long,
        gender: String,
        instagram: String,
        ownerId: String
    ): Boolean = withContext(Dispatchers.IO){
        contactRepository.addContact(name, phoneNumber, birthday, gender, instagram, ownerId)
    }

    suspend fun getContact(id: String): Contact = withContext(Dispatchers.IO) {
        val contact = contactRepository.getContact(id)
        if(contact == null){
            throw Exception("Contact null exception")
        }
        else{
            return@withContext contact
        }
    }

    fun checkInputValidation(name: String, phone: String): Array<Boolean> {
        return inputValidationService.addContactInputValidation(name, phone)
    }

    fun editContact(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            contactRepository.editContact(contact)
        }
    }

    fun deleteContact(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            contactRepository.deleteContact(id)
        }
    }
}