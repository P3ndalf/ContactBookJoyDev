package com.example.contactbook.data.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.contactbook.data.ApplicationDatabase
import com.example.contactbook.data.entities.Contact

class ContactRepository (private val application: Application) {

    private  val contactDao = ApplicationDatabase.getDatabase(application).contactDao()

    suspend fun addContact(contact : Contact){
        contactDao.addContact(contact)
    }

    fun getContacts(ownerId : String) : LiveData<List<Contact>> {
        return contactDao.getContacts(ownerId)
    }
}