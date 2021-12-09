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
        picturePath: String?,
        ownerId: String
    ): Boolean {
        if (findContact(contactName, phoneNumber, ownerId) != null) {
            return false
        }
        val id = UUID.randomUUID().toString()
        contactDao.addContact(
            Contact(
                id,
                contactName,
                phoneNumber,
                birthday,
                gender,
                instagram,
                picturePath,
                ownerId
            )
        )
        return true
    }

    private fun findContact(name: String, phoneNumber: String, ownerId: String): Contact? {
        return contactDao.findContact(name, phoneNumber, ownerId)
    }

    fun getContacts(ownerId: String): LiveData<List<Contact>> {
        return contactDao.getContacts(ownerId)
    }

    fun findContact(id: String): Contact? {
        return contactDao.findContact(id)
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