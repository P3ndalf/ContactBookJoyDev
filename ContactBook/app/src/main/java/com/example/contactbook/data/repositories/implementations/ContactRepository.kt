package com.example.contactbook.data.repositories.implementations

import androidx.lifecycle.LiveData
import com.example.contactbook.data.daos.ContactDao
import com.example.contactbook.data.entities.Contact
import java.util.*

class ContactRepository(private val contactDao: ContactDao) {

    suspend fun addContact(
        contactName: String,
        phoneNumber: String,
        birthday: Long,
        gender: String,
        instagram: String,
        ownerId: String
    ): Boolean {
        if (getContact(contactName, phoneNumber, ownerId) != null) {
            return false
        }
        else{
            val id = UUID.randomUUID().toString()
            contactDao.addContact(
                Contact(
                    id,
                    contactName,
                    phoneNumber,
                    birthday,
                    gender,
                    instagram,
                    ownerId
                )
            )
            return true
        }

    }

    private fun getContact(name: String, phoneNumber: String, ownerId: String): Contact? {
        return contactDao.getContact(name, phoneNumber, ownerId)
    }

    fun getContacts(ownerId: String): LiveData<List<Contact>> {
        return contactDao.getContacts(ownerId)
    }

    fun getContact(id: String): Contact? {
        return contactDao.getContact(id)
    }

    fun deleteContacts(ownerId: String) {
        contactDao.deleteContacts(ownerId)
    }

    fun deleteContact(id: String) {
        contactDao.deleteContact(id)
    }

    suspend fun editContact(contact: Contact) {
        contactDao.editContact(contact)
    }
}