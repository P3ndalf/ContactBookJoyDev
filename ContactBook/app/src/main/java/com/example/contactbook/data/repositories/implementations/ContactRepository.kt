package com.example.contactbook.data.repositories.implementations

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.contactbook.data.ApplicationDatabase
import com.example.contactbook.data.daos.ContactDao
import com.example.contactbook.data.entities.Contact
import java.util.*

class ContactRepository(private val contactDao: ContactDao) {

    suspend fun addContact(
        name: String,
        phoneNumber: String,
        birthday: Long,
        gender: String,
        instagram: String,
        ownerId: String
    ) {
        var id = UUID.randomUUID().toString()

        contactDao.addContact(Contact(id, name, phoneNumber, birthday, gender, instagram, ownerId))
    }

    fun getContacts(ownerId: String): LiveData<List<Contact>> {
        return contactDao.getContacts(ownerId)
    }

    fun getContact(id: String): Contact {
        return contactDao.getContact(id)
    }

    suspend fun deleteContacts(ownerId : String){
        contactDao.deleteContacts(ownerId)
    }

    suspend fun deleteContact(id: String){
        contactDao.deleteContact(id)
    }
}