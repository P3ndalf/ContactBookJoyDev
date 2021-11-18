package com.example.contactbook.data.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.contactbook.data.entities.Contact
import com.example.contactbook.data.repositories.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactViewModel(application : Application) : AndroidViewModel(application) {

    private val repository : ContactRepository

    init {
        repository = ContactRepository(application)
    }

    fun addContact(contact : Contact){
        viewModelScope.launch(Dispatchers.IO){
            repository.addContact(contact)
        }
    }

    fun getContacts(ownerId : String) : LiveData<List<Contact>> {
            return repository.getContacts(ownerId)
    }
}